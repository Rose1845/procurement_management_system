package com.rose.procurement.supplier.controller;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.supplier.services.ReportService;
import com.rose.procurement.supplier.services.SupplierService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
//@PreAuthorize(
//        "hasRole('ADMIN')"
//)
public class SupplierController {
    private final SupplierService supplierService;
    private final ReportService reportService;
    private final SupplierRepository supplierRepository;


    public SupplierController(SupplierService supplierService, ReportService reportService, SupplierRepository supplierRepository) {
        this.supplierService = supplierService;
        this.reportService = reportService;
        this.supplierRepository = supplierRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority({'ADMIN','EMPLOYEE'})")
    public SupplierDto createSupplier(@RequestBody @Valid SupplierDto supplierRequest) throws ProcureException {
//        if(result.hasErrors()){
//            throw new MethodArgumentNotValidException((MethodParameter) null, result);
//        }
        return supplierService.createSupplier(supplierRequest);
    }
    @GetMapping("/export/suppliers")
    public ResponseEntity<ByteArrayResource> exportCategories() throws IOException {
        byte[] csvData = supplierService.exportSuppliersToCsv();

        ByteArrayResource resource = new ByteArrayResource(csvData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=suppliers.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @GetMapping("/all")
    public Page<Supplier> findAllPurchaseOrders1(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return supplierRepository.findAll(pageable);
    }

    //    @GetMapping("/jasperpdf/export")
//    public void createPDF(HttpServletResponse response) throws IOException, JRException {
//        response.setContentType("application/pdf");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
//        response.setHeader(headerKey, headerValue);
//
//        reportService.exportJasperReport(response);
//    }
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Integer> uploadSuppliers(@RequestPart("file") MultipartFile file) {
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
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/supplier/{id}")
    public Supplier getSupplier(@PathVariable("id") String vendorId) {
        return supplierService.getSupplierById(vendorId);
    }

    @PutMapping("{id}")
    public Supplier updateSupplier(@PathVariable("id") String vendorId, @RequestBody SupplierDto supplierRequest) {
        return supplierService.updateSupplier(vendorId, supplierRequest);
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<String> deleteSupplierByVendorId(@PathVariable String vendorId) {
        supplierService.deleteSupplierByVendorIdNative(vendorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Supplier deleted successfully");
    }
    @GetMapping("/pagination")
    public Page<Supplier> findAllCategories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size,Sort.by(direction, sortField));
        Page<Supplier> filteredSuppliers = null;
        if (name != null && !name.isEmpty()) {
            // Search by name with pagination and sorting
            filteredSuppliers = supplierRepository.findByNameContaining(name, pageable);
        }
        else {
            // No filters applied, return all orders with pagination and sorting
            filteredSuppliers = supplierRepository.findAll(pageable);
        }
        return filteredSuppliers;
    }
    @DeleteMapping
    public String deleteSupplier() {
        return supplierService.deleteAll();
    }

    @GetMapping("/report/")
    public ResponseEntity<Resource> generatePurchaseOrderReport() throws JRException, IOException {
        return reportService.exportReport();
    }
}

