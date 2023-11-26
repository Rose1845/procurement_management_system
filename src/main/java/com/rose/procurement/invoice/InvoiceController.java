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
    public InvoiceDto createInvoice(@RequestBody InvoiceDto invoiceDto){
        return invoiceService.createInvoice(invoiceDto);
    }
    @GetMapping
    public List<InvoiceDto> getAll(){
        return invoiceService.getAllInvoices();
    }
}
