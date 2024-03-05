package com.rose.procurement.purchaseRequest.controller;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.services.PurchaseRequestItemDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase-request-item-details")
public class PurchaseRequestItemDetailController {

    private final PurchaseRequestItemDetailService itemDetailService;

    @GetMapping
    public ResponseEntity<List<PurchaseRequestItemDetail>> getAllItemDetails() {
        List<PurchaseRequestItemDetail> itemDetails = itemDetailService.getAllItemDetails();
        return new ResponseEntity<>(itemDetails, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestItemDetail> getItemDetailById(@PathVariable Long id) {
        PurchaseRequestItemDetail itemDetail = itemDetailService.getItemDetailById(id);
        if (itemDetail != null) {
            return new ResponseEntity<>(itemDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/purchase-request/{purchaseRequestId}")
//    public ResponseEntity<List<PurchaseRequestItemDetail>> getItemDetailsByPurchaseRequestId(
//            @PathVariable Long purchaseRequestId) {
//        List<PurchaseRequestItemDetail> itemDetails = itemDetailService.getItemDetailsByPurchaseRequestId(purchaseRequestId);
//        return new ResponseEntity<>(itemDetails, HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<PurchaseRequestItemDetail> createItemDetail(
            @RequestBody PurchaseRequestItemDetail itemDetail) {
        PurchaseRequestItemDetail createdItemDetail = itemDetailService.createItemDetail(itemDetail);
        return new ResponseEntity<>(createdItemDetail, HttpStatus.CREATED);
    }
    @GetMapping("/supplier-offer-details/{supplierId}")
    public ResponseEntity<List<PurchaseRequestItemDetail>> getDetailsBySupplierId(@PathVariable String supplierId) {
        List<PurchaseRequestItemDetail> itemDetails = itemDetailService.getDetailsBySupplierId(supplierId);
        return new ResponseEntity<>(itemDetails, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemDetail(@PathVariable Long id) {
        itemDetailService.deleteItemDetail(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/edit-offer-unit-prices")
    public ResponseEntity<List<PurchaseRequestItemDetail>> editOfferUnitPrices(
            @RequestBody List<PurchaseRequestItemDetail> itemDetails) {
        // Perform necessary validation and logic
        // Update offer unit prices in the database
        // Assuming you have a service method for batch updating offer unit prices
        List<PurchaseRequestItemDetail> updatedItemDetails = itemDetailService.editOfferUnitPrices(itemDetails);

        return new ResponseEntity<>(updatedItemDetails, HttpStatus.OK);
    }

}
