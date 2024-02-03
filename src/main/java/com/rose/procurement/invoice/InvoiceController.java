package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    public Optional<InvoiceDto> getInvoicesByInvoiceId(@PathVariable String invoiceId) {
        return invoiceService.getInvoicesByInvoiceId(invoiceId);
    }
    @GetMapping
    public List<Invoice> getAll(){
        return invoiceService.getAllInvoices();
    }
    @GetMapping("/invoice-details/{invoiceId}")
    public List<?> getInvoiceWithDetails(@PathVariable String invoiceId) {
        return invoiceService.getInvoiceWithDetails(invoiceId);
    }
    @GetMapping("/{invoiceId}")
    public ResponseEntity<List<InvoiceDetailsDTO>> getInvoiceDetails1(@PathVariable("invoiceId") String invoiceId) {
        List<Object[]> result = invoiceService.getInvoiceDetails1ByInvoiceId(invoiceId);

        List<InvoiceDetailsDTO> invoiceDetailsList = new ArrayList<>();

        for (Object[] outerArray : result) {
            for (Object innerArray : outerArray) {
                Object[] row = (Object[]) innerArray;

                InvoiceDetailsDTO invoiceDetailsDTO = new InvoiceDetailsDTO();
                invoiceDetailsDTO.setInvoiceId((String) row[0]);
                invoiceDetailsDTO.setInvoiceTimestamp((LocalDateTime) row[1]);
                invoiceDetailsDTO.setInvoiceDate((String) row[2]);
                invoiceDetailsDTO.setPurchaseOrderNumber((String) row[3]);
                invoiceDetailsDTO.setSupplierId((int) row[4]);
                invoiceDetailsDTO.setPurchaseOrderTimestamp((LocalDateTime) row[5]);
                invoiceDetailsDTO.setOrderItemsQuantity((int) row[6]);
                invoiceDetailsDTO.setItemQuantity((int) row[7]);
                invoiceDetailsDTO.setItemDiscount((int) row[8]);
                invoiceDetailsDTO.setItemDate((LocalDateTime) row[9]);
                invoiceDetailsDTO.setSupplierName((String) row[10]);
                invoiceDetailsDTO.setSupplierBox((String) row[11]);
                invoiceDetailsDTO.setSupplierCountry((String) row[12]);
                invoiceDetailsDTO.setSupplierCity((String) row[13]);
                invoiceDetailsDTO.setSupplierLocation((String) row[14]);

                // Add mappings for other fields

                invoiceDetailsList.add(invoiceDetailsDTO);
            }
        }

        if (!invoiceDetailsList.isEmpty()) {
            return ResponseEntity.ok(invoiceDetailsList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
