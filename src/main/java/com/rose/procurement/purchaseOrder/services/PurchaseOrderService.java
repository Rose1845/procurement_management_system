package com.rose.procurement.purchaseOrder.services;

import com.rose.procurement.contract.service.ContractService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
public class PurchaseOrderService {

private final PurchaseOrderRepository purchaseOrderRepository;
private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ContractService contractService;
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                SupplierRepository supplierRepository, PurchaseOrderMapper purchaseOrderMapper, ContractService contractService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.contractService = contractService;
    }

    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderRequest) {
        Optional<Supplier> supplier = supplierRepository.findById(purchaseOrderRequest.getVendorId());
        PurchaseOrder purchaseOrder = PurchaseOrderMapper.MAPPER.toEntity(purchaseOrderRequest);
       purchaseOrder.setPurchaseOrderTitle(purchaseOrderRequest.getPurchaseOrderTitle());
        supplier.ifPresent(purchaseOrder::setSupplier);
       purchaseOrder.setTermsAndConditions(purchaseOrderRequest.getTermsAndConditions());
       purchaseOrder.setApprovalStatus(ApprovalStatus.PENDING);
       purchaseOrder.setPaymentType(purchaseOrderRequest.getPaymentType());
        Set<Item> items = new HashSet<>(purchaseOrder.getItems());
        purchaseOrder.setItems(new HashSet<>(items));
        purchaseOrder.setDeliveryDate(purchaseOrderRequest.getDeliveryDate());
        purchaseOrder.setUpdatedAt(LocalDate.now().atStartOfDay());
        purchaseOrder.setCreatedAt(LocalDate.now().atStartOfDay());
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toDto(savedPurchaseOrder);

    }
   public PurchaseOrder updatePurchaseOrder(Long purchaseOrderId, PurchaseOrderDto purchaseOrderDto){
        Optional<PurchaseOrder> purchaseOrder= purchaseOrderRepository.findById(purchaseOrderId);
        if(purchaseOrder.isPresent()){
            purchaseOrder.get().setPurchaseOrderTitle(purchaseOrderDto.getPurchaseOrderTitle());
            purchaseOrder.get().setApprovalStatus(purchaseOrderDto.getApprovalStatus());
            purchaseOrder.get().setPaymentType(purchaseOrderDto.getPaymentType());
            purchaseOrder.get().setTermsAndConditions(purchaseOrderDto.getTermsAndConditions());
            purchaseOrder.get().setDeliveryDate(purchaseOrderDto.getDeliveryDate());
            purchaseOrder.get().setItems(purchaseOrderDto.getItems());
            Supplier supplier = supplierRepository.findById(purchaseOrderDto.getVendorId()).orElseThrow(()->new RuntimeException("no supplier with id"+ purchaseOrderDto.getVendorId()));
            purchaseOrder.get().setSupplier(supplier);
            purchaseOrder.get().setUpdatedAt(LocalDate.now().atStartOfDay());
        }
        else {
            throw new RuntimeException("An error occurred");
        }
        return purchaseOrderRepository.save(purchaseOrder.get());
   }
    public List<PurchaseOrder> getAllOrders(){
        return new ArrayList<>(purchaseOrderRepository.findAll());
    }

    public Page<PurchaseOrder> findPurchaseOrderWithPagination(int offSet,int pageSize){
        return purchaseOrderRepository.findAll(PageRequest.of(offSet,pageSize));
    }

    public Page<PurchaseOrder> findAllPurchaseOrderWithPaginationAndSorting(int offSet,int pageSize,String field){
        return purchaseOrderRepository.findAll(PageRequest.of(offSet,pageSize).withSort(Sort.by(field)));
    }
    public Set<Item> getItemsForPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found with id: " + purchaseOrderId));

        return purchaseOrder.getItems();
    }
    public Optional<PurchaseOrderDto> getPurchaseOrderWithItems( Long purchaseOrderId) {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findByIdWithItems(purchaseOrderId);
        return purchaseOrder.map(purchaseOrderMapper::toDto);
    }

    public List<Object[]> findPurchaseOrderDetailsByPurchaseOrderId(Long purchaseOrderId){
        return purchaseOrderRepository.findPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId);
    }
    public List<PurchaseOrder> findPurchaseOrdersByMonth(int month){
        return purchaseOrderRepository.findPurchaseOrdersByMonth(month);
    }
    public PurchaseOrder getPurchaseOrderByPurchaseOrderTitle(String purchaseOrderTitle){
        return purchaseOrderRepository.findByPurchaseOrderTitle(purchaseOrderTitle);
    }

    public Optional<PurchaseOrder> findPurchaseOrderById(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }


public String exportReport(Long purchaseOrderId) throws FileNotFoundException, JRException {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);
    File fIle = ResourceUtils.getFile("purchase_order.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(fIle.getAbsolutePath());
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(purchaseOrder));
    Map<String,Object> parameters = new HashMap<>();
    parameters.put("purchaseOrderId",purchaseOrderId);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
    JasperExportManager.exportReportToPdf(jasperPrint);
    return "generated";
}
}
