package com.rose.procurement.supplier.controller;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.services.ReportService;
import com.rose.procurement.supplier.services.SupplierService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
//@PreAuthorize(
//        "hasRole('ADMIN')"
//)
public class SupplierController {
    private final SupplierService supplierService;
    private final ReportService reportService;


    public SupplierController(SupplierService supplierService, ReportService reportService) {
        this.supplierService = supplierService;
        this.reportService = reportService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierDto createSupplier(@RequestBody @Valid SupplierDto supplierRequest){
//        if(result.hasErrors()){
//            throw new MethodArgumentNotValidException((MethodParameter) null, result);
//        }
        return supplierService.createSupplier(supplierRequest);
    }
    @GetMapping("/jasperpdf/export")
    public void createPDF(HttpServletResponse response) throws IOException, JRException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        reportService.exportJasperReport(response);
    }
    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Integer> uploadSuppliers(@RequestPart("file")MultipartFile file){
        return ResponseEntity.ok(supplierService.uploadSuppliers(file));

    }
    @GetMapping("/template/download/single")
    public ResponseEntity<InputStreamResource> downloadSingleSupplierTemplate() {
        InputStreamResource resource = supplierService.generateTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=single_supplier_template.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }
    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }
    @GetMapping("/supplier/{id}")
    public Supplier getSupplier(@PathVariable("id") String vendorId) {
       return supplierService.getSupplierById(vendorId);
    }
    @PutMapping("{id}")
    public Supplier updateSupplier(@PathVariable("id") String vendorId , @RequestBody SupplierDto supplierRequest){
        return supplierService.updateSupplier(vendorId, supplierRequest);
    }
    @DeleteMapping("{id}")
    public String deleteSupplier(@PathVariable("id") String vendorId){
        return supplierService.deleteSupplier(vendorId);
    }
    @DeleteMapping
    public String deleteSupplier(){
        return supplierService.deleteAll();
    }

    @GetMapping("/report/{format}")
    public String generatePurchaseOrderReport(@PathVariable("format") String format) throws JRException, FileNotFoundException {
        return reportService.exportReport(format);
    }
}

