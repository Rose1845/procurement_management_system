package com.rose.procurement.supplier.controller;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.request.SupplierRequest;
import com.rose.procurement.supplier.services.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @PostMapping
    public Supplier createSupplier(@RequestBody SupplierRequest supplierRequest){
        return supplierService.createSupplier(supplierRequest);
    }
    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();

    }
    @GetMapping("/supplier/{id}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable("id") Long vendorId) {
        Supplier supplier = supplierService.getSupplierById(vendorId);
        if (supplier != null) {
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
