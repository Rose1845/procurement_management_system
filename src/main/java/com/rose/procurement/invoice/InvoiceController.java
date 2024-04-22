package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.ContractStatus;
import com.rose.procurement.enums.InvoiceStatus;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }
    @PostMapping("/add")
    public InvoiceDto createInvoice(@RequestBody @Valid InvoiceDto invoiceDto) throws ProcureException {
        return invoiceService.createInvoice(invoiceDto);
    }

    @GetMapping
    public List<Invoice> getAll(){
        return invoiceService.getAllInvoices();
    }
    @GetMapping("/invoice-details/{invoiceId}")
    public List<?> getInvoiceWithDetails(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceWithDetails(invoiceId);
    }
    @GetMapping("{id}")
    public Optional<Invoice> getInvoice(@PathVariable("id") String invoiceId){
        return invoiceService.getInvoice(invoiceId);
    }
    @PatchMapping("/edit-invoice/{invoiceId}")
    public ResponseEntity<Invoice> editContractApprovalStatus(
            @PathVariable String invoiceId,
            @RequestParam String invoiceStatus) throws ProcureException {
        // Implement logic to update the contract approval status
        Invoice updatedInvoice = invoiceService.updateInvoiceStatus(invoiceId, InvoiceStatus.valueOf(invoiceStatus));
        return ResponseEntity.ok(updatedInvoice);
    }
@GetMapping("/{id}/report")
public ResponseEntity<byte[]> exportReport(@PathVariable("id") String invoiceId) {
    try {
        // Call the service method to generate and export the report
        invoiceService.generateAndExportReport1(invoiceId);

        // Get the generated file and set response headers
        File file = new File("output.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice.pdf");

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

    @GetMapping("/all")
    public Page<Invoice> findAllPurchaseOrders1(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepository.findAll(pageable);
    }

    @GetMapping("/paginations")
    public Page<Invoice> findAllInvoices(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) ApprovalStatus approvalStatus,
            @RequestParam(required = false) InvoiceStatus invoiceStatus,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String searchField,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Invoice> filteredInvoices = null;
        if (searchField != null && !searchField.isEmpty() && startDate != null && endDate != null) {
            // Search by name and createdAt date range with pagination and sorting
            filteredInvoices = invoiceRepository.findByInvoiceNumberContainingAndCreatedAtBetween(
                    searchField, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (searchField != null && !searchField.isEmpty()) {
            // Search by name with pagination and sorting
            filteredInvoices = invoiceRepository.findByInvoiceNumberContaining(searchField, pageable);
        } else if (startDate != null && endDate != null) {
            // Search by createdAt date range with pagination and sorting
            filteredInvoices = invoiceRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (approvalStatus != null && invoiceStatus != null && supplierId != null) {
            // Filter by approval status, invoice status, and supplier with pagination and sorting
            filteredInvoices = invoiceRepository.findByPurchaseOrderApprovalStatusAndInvoiceStatusAndPurchaseOrderSupplierVendorId(approvalStatus, invoiceStatus, supplierId, pageable);
        } else if (approvalStatus != null && invoiceStatus != null) {
            // Filter by approval status and invoice status with pagination and sorting
            filteredInvoices = invoiceRepository.findByPurchaseOrderApprovalStatusAndInvoiceStatus(approvalStatus, invoiceStatus, pageable);
        } else if (approvalStatus != null && supplierId != null) {
            // Filter by approval status and supplier with pagination and sorting
            filteredInvoices = invoiceRepository.findByPurchaseOrderApprovalStatusAndPurchaseOrderSupplierVendorId(approvalStatus, supplierId, pageable);
        } else if (approvalStatus != null) {
            // Filter only by approval status with pagination and sorting
            filteredInvoices = invoiceRepository.findByPurchaseOrderApprovalStatus(approvalStatus, pageable);
        } else if (invoiceStatus != null && supplierId != null) {
            // Filter by invoice status and supplier with pagination and sorting
            filteredInvoices = invoiceRepository.findByInvoiceStatusAndPurchaseOrder_SupplierVendorId(invoiceStatus, supplierId, pageable);
        } else if (invoiceStatus != null) {
            // Filter only by invoice status with pagination and sorting
            filteredInvoices = invoiceRepository.findByInvoiceStatus(invoiceStatus, pageable);
        } else if (supplierId != null) {
            // Filter only by supplier with pagination and sorting
            filteredInvoices = invoiceRepository.findByPurchaseOrderSupplierVendorId(supplierId, pageable);
        } else {
            // No filters applied, return all invoices with pagination and sorting
            filteredInvoices = invoiceRepository.findAll(pageable);
        }
        return filteredInvoices;
    }



}
