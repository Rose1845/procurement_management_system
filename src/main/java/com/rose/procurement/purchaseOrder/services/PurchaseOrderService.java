package com.rose.procurement.purchaseOrder.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.service.ContractService;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.webjars.NotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class PurchaseOrderService {

private final PurchaseOrderRepository purchaseOrderRepository;
private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ContractService contractService;
    private final EmailService emailService;
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                SupplierRepository supplierRepository, PurchaseOrderMapper purchaseOrderMapper, ContractService contractService, EmailService emailService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.contractService = contractService;
        this.emailService = emailService;
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
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Item item : items) {
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        purchaseOrder.setTotalAmount(totalAmount);
        purchaseOrder.setItems(items);
        purchaseOrder.setDeliveryDate(purchaseOrderRequest.getDeliveryDate());
        purchaseOrder.setUpdatedAt(LocalDate.now().atStartOfDay());
        purchaseOrder.setCreatedAt(LocalDate.now().atStartOfDay());
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }
    public PurchaseOrderDto createPurchaseOrderFromContract(Contract contract,PurchaseOrderDto purchaseOrderRequest) {
        // Assuming you have the necessary information in the Contract entity
        Optional<Supplier> supplier = Optional.ofNullable(contract.getSupplier());

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderTitle(purchaseOrderRequest.getPurchaseOrderTitle()); // Set your title here
        supplier.ifPresent(purchaseOrder::setSupplier);
        purchaseOrder.setTermsAndConditions(purchaseOrderRequest.getTermsAndConditions());
        purchaseOrder.setApprovalStatus(ApprovalStatus.PENDING);
        // Set other properties as needed
        // Copy items from the contract to the purchase order
        Set<Item> items = new HashSet<>(contract.getItems());
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Item item : items) {
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        purchaseOrder.setTotalAmount(totalAmount);
        purchaseOrder.setItems(new HashSet<>(items));
        purchaseOrder.setDeliveryDate(purchaseOrderRequest.getDeliveryDate()); // Set your delivery date logic here
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setPaymentType(purchaseOrderRequest.getPaymentType());
        // Associate the contract with the purchase order
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return PurchaseOrderMapper.MAPPER.toDto(savedPurchaseOrder);
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
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Item item : purchaseOrder.get().getItems()) {
                totalAmount = totalAmount.add(item.getTotalPrice());
            }
            purchaseOrderDto.setTotalAmount(totalAmount);
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
    public List<PurchaseOrder> getOrdersForSupplierById(String supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
        return purchaseOrderRepository.findBySupplier(supplier);
    }

    public List<PurchaseOrder> getOrdersByStatus(String status) {
        PurchaseOrder exampleOrder = new PurchaseOrder();
        exampleOrder.setApprovalStatus(ApprovalStatus.valueOf(status)); // Set status

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT) // Match string exactly
                .withIgnoreNullValues(); // Ignore null values in the example

        Example<PurchaseOrder> example = Example.of(exampleOrder, matcher);

        return purchaseOrderRepository.findAll(example);
    }


    public Page<PurchaseOrder> findPurchaseOrderWithPagination(int offSet, int pageSize){
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
    public Optional<PurchaseOrderDto> getPurchaseOrderWithItems(Long purchaseOrderId) {
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
    public String deletePurchaseOrder(Long purchaseOrderId){
        purchaseOrderRepository.deleteById(purchaseOrderId);
        return "deleted succesffully";
    }

    public PurchaseOrderDto sendPurchaseOrderToSupplier(Long purchaseOrderId) {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder purchaseOrder = optionalPurchaseOrder.get();

            // Assuming you have a method to send an email to the supplier

            // Update the purchase order status or perform any other necessary actions
            purchaseOrder.setApprovalStatus(ApprovalStatus.PENDING); // You can set the appropriate status

            // Save the updated purchase order
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

            // Convert and return the updated purchase order as a DTO
            return purchaseOrderMapper.toDto(savedPurchaseOrder);
        } else {
            // Handle the case where the purchase order with the given ID is not found
            throw new EntityNotFoundException("Purchase Order not found with ID: " + purchaseOrderId);
        }
    }

    public String sendApprovalEmailToSupplier(Long purchaseOrderId) throws ProcureException {
        log.info("sending order....", purchaseOrderId);

        if (purchaseOrderId == null) {
            throw ProcureException.builder().message("order id not found").metadata("id").build();
        }
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder purchaseOrder = optionalPurchaseOrder.get();
            String approvalLink = "http://localhost:8081/api/v1/purchase-order/approve/" + purchaseOrder.getPurchaseOrderId();

            // Assuming you have a method to send an email to the supplier

            String subject = "Purchase Order Approval Request";
            String text = "Dear Supplier,\n\n"
                    + "A purchase order requires your approval. Please review and take appropriate action.\n\n"
                    + "Purchase Order Title: " + purchaseOrder.getPurchaseOrderTitle() + "\n"
                    + "Delivery Date: " + purchaseOrder.getDeliveryDate() + "\n"
                    + "To approve or reject, click the following link: " + approvalLink + "\n"
                    + "\n\nBest Regards,\nProcureSwift Company";


            log.info("sending email template....", purchaseOrderId);


            // Send the email
            emailService.sendEmail(purchaseOrder.getSupplier().getEmail(), subject, text);
            log.info("done sending");


            // Update the purchase order status or perform any other necessary actions
            purchaseOrder.setApprovalStatus(ApprovalStatus.ISSUED); // You can set the appropriate status

            // Save the updated purchase order
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

            return "order sent!!";

            // Convert and return the updated purchase order as a DTO
        } else {
            // Handle the case where the purchase order with the given ID is not found
            log.info("didn't send becoz that order not found ");
            ProcureException.builder().message("an error occured").metadata("email sending").build();
            return "email not sent becoz order not found by request id";
        }

    }
    public PurchaseOrder updateApprovalStatus(Long purchaseOrderId, ApprovalStatus approvalStatus) throws ProcureException {
        log.info("approving...");
        // Retrieve the existing contract from the database
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(purchaseOrderId).orElseThrow(()->new ProcureException("id already exists"));
//        existingOrder.checkAndSetExpiredStatus();
        // Update the approval status
        try{
            existingOrder.setApprovalStatus(approvalStatus);
            // Save the updated contract in the databasecharacters
            return purchaseOrderRepository.save(existingOrder);
        }catch(Exception e){
           throw  ProcureException.builder().metadata("error").message(e.getMessage()).build();
        }
    }
    public void generateAndExportReport(Long purchaseOrderId) throws JRException, FileNotFoundException {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);
        File file = ResourceUtils.getFile("purchase_order.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Your SQL query and other logic for report generation go here

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(purchaseOrder.get()));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("purchaseOrderId", purchaseOrderId);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to a file
        JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
    }
    public void generateAndExportReport1(Long purchaseOrderId) throws JRException, FileNotFoundException {
        // Assuming you have a JDBC URL, username, and password for your database
        String jdbcUrl = "jdbc:mysql://localhost:3306/procure";
        String username = "rose";
        String password = "Atieno18_";

        // Obtain the .jrxml file
        File file = ResourceUtils.getFile("classpath:Order.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Parameters to be passed to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("purchaseOrderId", purchaseOrderId);

        // Establishing a database connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Filling the report with data from the database, parameters, and the connection
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export the report to a file
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
        } catch (Exception e) {
            // Handle exceptions such as SQLExceptions
            e.printStackTrace();
            throw new RuntimeException("Error while generating report", e);
        }
    }

}
