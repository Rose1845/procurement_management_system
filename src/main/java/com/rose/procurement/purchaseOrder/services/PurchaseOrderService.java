package com.rose.procurement.purchaseOrder.services;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

private final PurchaseOrderRepository purchaseOrderRepository;
private final ContractRepository contractRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, ContractRepository contractRepository,
                                PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.contractRepository = contractRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderRequest) {
        Optional<Contract> contract = contractRepository.findById(purchaseOrderRequest.getContract().getContractId());
       if(contract.isEmpty()){
           throw new IllegalStateException();
        }


        // Creating the purchase order with the selected suppliers and items
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderTitle(purchaseOrderRequest.getPurchaseOrderTitle())
                .deliveryDate(purchaseOrderRequest.getDeliveryDate())
                .termsAndConditions(purchaseOrderRequest.getTermsAndConditions())
                .approvalStatus(ApprovalStatus.valueOf(purchaseOrderRequest.getApprovalStatus().name()))
                .paymentType(PaymentType.valueOf(purchaseOrderRequest.getPaymentType().name()))
                .contract(contract.get())
                .build();
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }
   public PurchaseOrder updatePurchaseOrder(Long purchaseOrderId, PurchaseOrderDto purchaseOrderDto){
        Optional<PurchaseOrder> purchaseOrder= purchaseOrderRepository.findById(purchaseOrderId);
        if(purchaseOrder.isPresent()){
            purchaseOrder.get().setPurchaseOrderTitle(purchaseOrderDto.getPurchaseOrderTitle());
            purchaseOrder.get().setContract(purchaseOrderDto.getContract());
            purchaseOrder.get().setApprovalStatus(purchaseOrderDto.getApprovalStatus());
            purchaseOrder.get().setPaymentType(purchaseOrderDto.getPaymentType());
            purchaseOrder.get().setTermsAndConditions(purchaseOrderDto.getTermsAndConditions());
            purchaseOrder.get().setDeliveryDate(purchaseOrderDto.getDeliveryDate());
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


}
