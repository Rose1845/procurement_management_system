package com.rose.procurement.supplier.controller;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierOffer;
import com.rose.procurement.supplier.services.SupplierOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplier-offers")
public class SupplierOfferController {
    private final SupplierOfferService supplierOfferService;

    @PostMapping
    public ResponseEntity<SupplierOffer> submitSupplierOffer(@RequestBody SupplierOffer supplierOffer) {
        SupplierOffer submittedOffer = supplierOfferService.submitSupplierOffer(supplierOffer);
        return new ResponseEntity<>(submittedOffer, HttpStatus.CREATED);
    }
    @PostMapping("/{id}")
    public SupplierOffer addSupplierOffer(
            @PathVariable("id") Long purchaseRequestId ,
            @RequestBody SupplierOffer supplierOffer) {
        return supplierOfferService.addSupplierOffer(purchaseRequestId, supplierOffer);
    }

    // Other methods as needed
}