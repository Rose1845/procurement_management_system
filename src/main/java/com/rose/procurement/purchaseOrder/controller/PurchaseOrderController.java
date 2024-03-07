package com.rose.procurement.purchaseOrder.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import com.rose.procurement.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/purchase-order")
@Slf4j
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final ContractRepository contractRepository;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, ContractRepository contractRepository) {
        this.purchaseOrderService = purchaseOrderService;
        this.contractRepository = contractRepository;
    }

    @PostMapping
    public PurchaseOrderDto createPurchaseOrder(@RequestBody @Valid PurchaseOrderDto purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }
    @PostMapping("/create-from-contract/{contractId}")
    public ResponseEntity<PurchaseOrderDto> createPurchaseOrderFromContract(@PathVariable String contractId, @RequestBody PurchaseOrderDto purchaseOrderRequest) {
        try {
            // Retrieve the contract from the database
            Optional<Contract> contract = contractRepository.findById(contractId);
            if (contract.isPresent()) {
                // Create a new purchase order from the contract
                PurchaseOrderDto purchaseOrderDto = purchaseOrderService.createPurchaseOrderFromContract(contract.get(),purchaseOrderRequest);
                return new ResponseEntity<>(purchaseOrderDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle exceptions as needed
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public Optional<PurchaseOrder> getPurchaseOrderById(@PathVariable("id") Long purchaseOrderId){
        return purchaseOrderService.findPurchaseOrderById(purchaseOrderId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchaseOrder(@PathVariable("id") Long purchaseOrderId){
        return ResponseEntity.ok(purchaseOrderService.deletePurchaseOrder(purchaseOrderId));
    }

    @GetMapping
    public List<PurchaseOrder> getAllPO(){
        return purchaseOrderService.getAllOrders();
    }
    @GetMapping("/get/order-items/{id}")
    public Optional<PurchaseOrderDto> getPurchaseOrderWithItems(@PathVariable("id") Long purchaseOrderId) {
        return purchaseOrderService.getPurchaseOrderWithItems(purchaseOrderId);
    }
    @GetMapping("/paginate")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrders( @RequestParam(name = "offSet") int offSet,
                                                                  @RequestParam(name = "pageSize") int pageSize){
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.findPurchaseOrderWithPagination(offSet,pageSize);
        return new ApiResponse<>(purchaseOrders.getTotalPages(),purchaseOrders);
    }
    @GetMapping("/{purchaseOrderId}/items")
    public ResponseEntity<Set<Item>> getItemsForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        Set<Item> items = purchaseOrderService.getItemsForPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(items);
    }
    @GetMapping("purchase-details/{purchaseOrderId}")
    public List<Object[]> getPurchaseDetailsByPurchaseOrderId(@PathVariable("purchaseOrderId") Long purchaseOrderId){
        return purchaseOrderService.findPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId);
    }
   @GetMapping("/paginate-sorting")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrderWithSorting(@RequestParam(name = "offSet") int offSet,
                                                                            @RequestParam(name = "pageSize") int pageSize,
                                                                            @RequestParam(name = "field") String field){
        Page<PurchaseOrder> purchaseOrders=
                purchaseOrderService.findAllPurchaseOrderWithPaginationAndSorting(offSet,pageSize,field);
        return new ApiResponse<>(purchaseOrders.getSize(),purchaseOrders);
    }
    @GetMapping("/title")
    public PurchaseOrder getPurchaseOrderByPurchaseOrderTitle(@RequestParam("title") String purchaseOrderTitle){
        return purchaseOrderService.getPurchaseOrderByPurchaseOrderTitle(purchaseOrderTitle);

    }
    @GetMapping("p-orders/{month}")
    public List<PurchaseOrder> findPurchaseOrdersByMonth(@PathVariable("month") int month){
        return  purchaseOrderService.findPurchaseOrdersByMonth(month);
    }
    @PutMapping("{id}")
    public PurchaseOrder updatePO(@PathVariable("id") Long purchaseOrderId, @RequestBody PurchaseOrderDto purchaseOrderDto){
        return purchaseOrderService.updatePurchaseOrder(purchaseOrderId,purchaseOrderDto);
    }
    @GetMapping("{id}/report")
    public String generateReport(@PathVariable("id") Long purchaseOrderId) throws JRException, FileNotFoundException {
        return purchaseOrderService.exportReport(purchaseOrderId);
    }
//    @PostMapping("/{purchaseOrderId}/send-to-supplier")
//    public ResponseEntity<PurchaseOrderDto> sendPurchaseOrderToSupplier(@PathVariable Long purchaseOrderId) {
//        PurchaseOrderDto updatedPurchaseOrder = purchaseOrderService.sendPurchaseOrderToSupplier(purchaseOrderId);
//        return ResponseEntity.ok(updatedPurchaseOrder);
//    }

//    @GetMapping("/approve/{purchaseOrderId}")
//    public String approvePurchaseOrder(@PathVariable Long purchaseOrderId) {
//        // Logic to handle the supplier's approval
//        // You might want to update the approval status in the database
//
//        return "Purchase Order approved successfully";
//    }
    @PostMapping("/send-order-to-supplier/{id}")
    public ResponseEntity<String> sendContractToSupplier(@PathVariable("id") Long purchaseOrderId) throws ProcureException {
        log.info("start sending order email to supplier...");

        try{
            return ResponseEntity.ok(purchaseOrderService.sendApprovalEmailToSupplier(purchaseOrderId));

        }catch (ProcureException | Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Step 3: Edit Approval Status by Supplier
    @PatchMapping("/approve/{id}")
    public String editContractApprovalStatus(
            @PathVariable("id") Long purchaseOrderId,

            @RequestParam String approvalStatus)  {
        // Implement logic to update the contract approval status
        try{
            ResponseEntity.ok(purchaseOrderService.updateApprovalStatus(purchaseOrderId, ApprovalStatus.valueOf(approvalStatus)));
            return "approved!!";

        }catch (ProcureException | Exception e){
            ResponseEntity.badRequest().body(e.getMessage());
            return "an error occured";

        }
    }

}
