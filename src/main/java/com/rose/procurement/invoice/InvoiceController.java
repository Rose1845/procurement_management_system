package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
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
//    @GetMapping("/{invoiceId}")
//    public ResponseEntity<List<InvoiceDetailsDTO>> getInvoiceDetails1(@PathVariable("invoiceId") String invoiceId) {
//        List<Object[]> result = invoiceService.getInvoiceDetails1ByInvoiceId(invoiceId);
//
//        List<InvoiceDetailsDTO> invoiceDetailsList = new ArrayList<>();
//
//        for (Object[] outerArray : result) {
//            for (Object innerArray : outerArray) {
//                Object[] row = (Object[]) innerArray;
//
//                InvoiceDetailsDTO invoiceDetailsDTO = new InvoiceDetailsDTO();
//                invoiceDetailsDTO.setInvoiceId((String) row[0]);
//                invoiceDetailsDTO.setInvoiceTimestamp((LocalDateTime) row[1]);
//                invoiceDetailsDTO.setInvoiceDate((String) row[2]);
//                invoiceDetailsDTO.setPurchaseOrderNumber((String) row[3]);
//                invoiceDetailsDTO.setSupplierId((int) row[4]);
//                invoiceDetailsDTO.setPurchaseOrderTimestamp((LocalDateTime) row[5]);
//                invoiceDetailsDTO.setOrderItemsQuantity((int) row[6]);
//                invoiceDetailsDTO.setItemQuantity((int) row[7]);
//                invoiceDetailsDTO.setItemDiscount((int) row[8]);
//                invoiceDetailsDTO.setItemDate((LocalDateTime) row[9]);
//                invoiceDetailsDTO.setSupplierName((String) row[10]);
//                invoiceDetailsDTO.setSupplierBox((String) row[11]);
//                invoiceDetailsDTO.setSupplierCountry((String) row[12]);
//                invoiceDetailsDTO.setSupplierCity((String) row[13]);
//                invoiceDetailsDTO.setSupplierLocation((String) row[14]);
//
//                // Add mappings for other fields
//
//                invoiceDetailsList.add(invoiceDetailsDTO);
//            }
//        }
//
//        if (!invoiceDetailsList.isEmpty()) {
//            return ResponseEntity.ok(invoiceDetailsList);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
@GetMapping("/{id}/report")
public ResponseEntity<byte[]> exportReport(@PathVariable("id") String invoiceId) {
    try {
        // Call the service method to generate and export the report
        invoiceService.generateAndExportReport(invoiceId);

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


}
