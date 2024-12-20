package com.rose.procurement.purchaseOrder.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/purchase-order")
@Slf4j
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final ContractRepository contractRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, ContractRepository contractRepository, SupplierRepository supplierRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderService = purchaseOrderService;
        this.contractRepository = contractRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority({'ADMIN'})")
    public PurchaseOrderDto createPurchaseOrder(@RequestBody @Valid PurchaseOrderDto purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }

    @GetMapping("/latest")
    public Page<PurchaseOrder> getLatestOrders() {
        // Create a pageable request for the first page with 5 orders
        PageRequest pageRequest = PageRequest.of(0, 5);

        // Retrieve the 5 latest created orders using the custom method
        return purchaseOrderRepository.findTop5ByOrderByCreatedAtDesc(pageRequest);
    }

    @PostMapping("/create-from-contract/{contractId}")
    public ResponseEntity<PurchaseOrderDto> createPurchaseOrderFromContract(@PathVariable String contractId, @RequestBody PurchaseOrderDto purchaseOrderRequest) {
        try {
            // Retrieve the contract from the database
            Optional<Contract> contract = contractRepository.findById(contractId);
            if (contract.isPresent()) {
                // Create a new purchase order from the contract
                PurchaseOrderDto purchaseOrderDto = purchaseOrderService.createPurchaseOrderFromContract(contract.get(), purchaseOrderRequest);
                return new ResponseEntity<>(purchaseOrderDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle exceptions as needed
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/filtered")
    public Page<PurchaseOrder> getFilteredPurchaseOrdersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) ApprovalStatus approvalStatus
    ) {
        return purchaseOrderService.findFilteredPurchaseOrders(page, size, supplierId, ApprovalStatus.valueOf(String.valueOf(approvalStatus)));
    }
    @PostMapping("/clone-order/{id}")
    public Optional<PurchaseOrder> cloneOrder(@PathVariable("id") Long purchaseOrderId){
        return purchaseOrderService.cloneOrder(purchaseOrderId);
    }
    @GetMapping("/search-by-date-range")
    public Page<PurchaseOrder> searchPurchaseOrdersByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return purchaseOrderService.searchPurchaseOrdersByDateRange(startDate, endDate);
    }

    @GetMapping("/{id}")
    public Optional<PurchaseOrder> getPurchaseOrderById(@PathVariable("id") Long purchaseOrderId) {
        return purchaseOrderService.findPurchaseOrderById(purchaseOrderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchaseOrder(@PathVariable("id") Long purchaseOrderId) {
        return ResponseEntity.ok(purchaseOrderService.deletePurchaseOrder(purchaseOrderId));
    }


    @GetMapping
    public List<PurchaseOrder> getAllPO() {
        return purchaseOrderService.getAllOrders();
    }

    @GetMapping("/get/order-items/{id}")
    public Optional<PurchaseOrderDto> getPurchaseOrderWithItems(@PathVariable("id") Long purchaseOrderId) {
        return purchaseOrderService.getPurchaseOrderWithItems(purchaseOrderId);
    }

//    @GetMapping("/paginate")
//    public ApiResponse<PurchaseOrder> findAllPurchaseOrders(@RequestParam(name = "offSet") int offSet,
//                                                            @RequestParam(name = "pageSize") int pageSize) {
//        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.findPurchaseOrderWithPagination(offSet, pageSize);
//        return new ApiResponse<>(purchaseOrders);
//    }

    @GetMapping("/paginations")
    public Page<PurchaseOrder> findAllPurchaseOrders1(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) ApprovalStatus approvalStatus,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String purchaseOrderTitle,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<PurchaseOrder> filteredOrders = null;
        if (purchaseOrderTitle != null && !purchaseOrderTitle.isEmpty() && startDate != null && endDate != null) {
            // Search by name and createdAt date range with pagination and sorting
            filteredOrders = purchaseOrderRepository.findByPurchaseOrderTitleContainingAndCreatedAtBetween(
                    purchaseOrderTitle, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (purchaseOrderTitle != null && !purchaseOrderTitle.isEmpty()) {
            // Search by name with pagination and sorting
            filteredOrders = purchaseOrderRepository.findByPurchaseOrderTitleContaining(purchaseOrderTitle, pageable);
        } else if (startDate != null && endDate != null) {
            // Search by createdAt date range with pagination and sorting
            filteredOrders = purchaseOrderRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (approvalStatus != null && supplierId != null) {
            // Filter by both approval status and supplier with pagination and sorting
            filteredOrders = purchaseOrderRepository.findByApprovalStatusAndSupplier_VendorId(approvalStatus, supplierId, pageable);
        } else if (approvalStatus != null) {
            // Filter only by approval status with pagination and sorting
            filteredOrders = purchaseOrderRepository.findByApprovalStatus(approvalStatus, pageable);
        } else if (supplierId != null) {
            // Filter only by supplier with pagination and sorting
            filteredOrders = purchaseOrderRepository.findBySupplier_VendorId(supplierId, pageable);
        } else {
            // No filters applied, return all orders with pagination and sorting
            filteredOrders = purchaseOrderRepository.findAll(pageable);
        }
        return filteredOrders;
    }

    @GetMapping("/paginate")
    public Page<PurchaseOrder> searchOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

     return purchaseOrderRepository.findAll(pageable);
    }

    @GetMapping("/{purchaseOrderId}/items")
    public ResponseEntity<Set<Item>> getItemsForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        Set<Item> items = purchaseOrderService.getItemsForPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("purchase-details/{purchaseOrderId}")
    public List<Object[]> getPurchaseDetailsByPurchaseOrderId(@PathVariable("purchaseOrderId") Long purchaseOrderId) {
        return purchaseOrderService.findPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId);
    }

    //   @GetMapping("/paginate-sorting")
//    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrderWithSorting(@RequestParam(name = "offSet") int offSet,
//                                                                            @RequestParam(name = "pageSize") int pageSize,
//                                                                            @RequestParam(name = "field") String field){
//        Page<PurchaseOrder> purchaseOrders=
//                purchaseOrderService.findAllPurchaseOrderWithPaginationAndSorting(offSet,pageSize,field);
//        return new ApiResponse<>(purchaseOrders.getSize(),purchaseOrders);
//    }
    @GetMapping("/title")
    public PurchaseOrder getPurchaseOrderByPurchaseOrderTitle(@RequestParam("title") String purchaseOrderTitle) {
        return purchaseOrderService.getPurchaseOrderByPurchaseOrderTitle(purchaseOrderTitle);

    }

    @GetMapping("/p-orders/{month}")
    public List<PurchaseOrder> findPurchaseOrdersByMonth(@PathVariable("month") int month) {
        return purchaseOrderService.findPurchaseOrdersByMonth(month);
    }

    @PutMapping("/{id}")
    public PurchaseOrder updatePO(@PathVariable("id") Long purchaseOrderId, @RequestBody PurchaseOrderDto purchaseOrderDto) {
        return purchaseOrderService.updatePurchaseOrder(purchaseOrderId, purchaseOrderDto);
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> exportReport(@PathVariable("id") Long purchaseOrderId) {
        try {
            // Call the service method to generate and export the report
            purchaseOrderService.generateAndExportReport1(purchaseOrderId);

            // Get the generated file and set response headers
            File file = new File("output.pdf");
            FileInputStream fileInputStream = new FileInputStream(file);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "purchase_order.pdf");

            // Convert the file to byte array
            byte[] fileContent = new byte[(int) file.length()];
            fileInputStream.read(fileContent);
            fileInputStream.close();

            // Return the response entity with byte array and headers
            return ResponseEntity.ok().headers(headers).body(fileContent);
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/send-order-to-supplier/{id}")
    public ResponseEntity<String> sendContractToSupplier(@PathVariable("id") Long purchaseOrderId) throws ProcureException {
        log.info("start sending order email to supplier...");

        try {
            return ResponseEntity.ok(purchaseOrderService.sendApprovalEmailToSupplier(purchaseOrderId));

        } catch (ProcureException | Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Step 3: Edit Approval Status by Supplier
    @PatchMapping("/approve/{id}")
    public String editContractApprovalStatus(
            @PathVariable("id") Long purchaseOrderId,
            @RequestParam String approvalStatus) {
        // Implement logic to update the contract approval status
        try {
            ResponseEntity.ok(purchaseOrderService.updateApprovalStatus(purchaseOrderId, ApprovalStatus.valueOf(approvalStatus)));
            return "approved!!";

        } catch (ProcureException | Exception e) {
            ResponseEntity.badRequest().body(e.getMessage());
            return "an error occurred";

        }
    }

    @GetMapping("/purchase-orders/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrder>> getOrdersForSupplierById(
            @PathVariable String supplierId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getOrdersForSupplierById(supplierId);
        return new ResponseEntity<>(purchaseOrders, HttpStatus.OK);
    }

    @GetMapping("/purchase-orders/status/{status}")
    public ResponseEntity<List<PurchaseOrder>> getOrdersByStatus(
            @PathVariable String status) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getOrdersByStatus(status);
        return new ResponseEntity<>(purchaseOrders, HttpStatus.OK);
    }

}
