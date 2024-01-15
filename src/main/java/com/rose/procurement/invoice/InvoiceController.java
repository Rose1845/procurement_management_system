package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @PostMapping
    public InvoiceDto createInvoice(@RequestBody @Valid InvoiceDto invoiceDto) throws ProcureException {
        return invoiceService.createInvoice(invoiceDto);
    }
    @GetMapping("/byInvoiceId/{invoiceId}")
    public Optional<Invoice> getInvoicesByInvoiceId(@PathVariable String invoiceId) {
        return invoiceService.getInvoicesByInvoiceId(invoiceId);
    }
    @GetMapping
    public List<Invoice> getAll(){
        return invoiceService.getAllInvoices();
    }
    @GetMapping("/invoice-details/{invoiceId}")
    public ResponseEntity<Object> getInvoiceDetails(@PathVariable("invoiceId") String invoiceId) {
        Object invoiceDetails = invoiceService.getInvoiceDetailsByInvoiceId(invoiceId);

        if (invoiceDetails != null) {
            return ResponseEntity.ok(invoiceDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
