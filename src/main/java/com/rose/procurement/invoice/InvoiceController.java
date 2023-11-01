package com.rose.procurement.invoice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoiceDto){
        return invoiceService.createInvoice(invoiceDto);
    }
    @GetMapping
    public List<Invoice> getAll(){
        return invoiceService.getAllInvoices();
    }
}
