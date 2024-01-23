package com.rose.procurement.supplier.controller;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.request.SupplierRequest;
import com.rose.procurement.supplier.services.SupplierService;
import jakarta.validation.Valid;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
//@PreAuthorize(
//        "hasRole('ADMIN')"
//)
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierDto createSupplier(@RequestBody @Valid SupplierDto supplierRequest){
//        if(result.hasErrors()){
//            throw new MethodArgumentNotValidException((MethodParameter) null, result);
//        }
        return supplierService.createSupplier(supplierRequest);
    }
    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Integer> uploadSuppliers(@RequestPart("file")MultipartFile file){
        return ResponseEntity.ok(supplierService.uploadSuppliers(file));

    }
    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }
    @GetMapping("/supplier/{id}")
    public Supplier getSupplier(@PathVariable("id") Long vendorId) {
       return supplierService.getSupplierById(vendorId);
    }
    @PutMapping("{id}")
    public Supplier updateSupplier(@PathVariable("id") Long vendorId , @RequestBody SupplierRequest supplierRequest){
        return supplierService.updateSupplier(vendorId, supplierRequest);
    }
    @DeleteMapping("{id}")
    public String deleteSupplier(@PathVariable("id") Long vendorId){
        return supplierService.deleteSupplier(vendorId);
    }
}
