package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
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

}
