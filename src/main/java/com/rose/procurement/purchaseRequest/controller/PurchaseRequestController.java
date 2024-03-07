package com.rose.procurement.purchaseRequest.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.purchaseRequest.entities.OfferItemUpdateDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.services.PurchaseRequestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api/v1/purchase-request")
public class PurchaseRequestController {
    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @PostMapping("/create")
    public PurchaseRequestDto createPurchaseRequest(@RequestBody @Valid  PurchaseRequestDto purchaseRequest) throws ProcureException {
        log.info("controller PR....");
        System.out.println(purchaseRequest);
           return     purchaseRequestService.createPurchaseRequest(purchaseRequest);
    }
    @GetMapping
    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestService.getAllPurchaseRequests();
    }
    @GetMapping("/{id}")
    public Optional<PurchaseRequestDto> getPurchaseRequestById(@PathVariable Long id) {
       return purchaseRequestService.getPurchaseRequestById(id);
    }
//    @PatchMapping("/{purchaseRequestId}/edit-offer-unit-prices")
//    public ResponseEntity<List<PurchaseRequestItemDetail>> editOfferUnitPrices(
//            @PathVariable Long purchaseRequestId,
//            @RequestBody List<PurchaseRequestItemDetail> itemDetails) {
//        try {
//            List<PurchaseRequestItemDetail> updatedItemDetails = purchaseRequestService.editOfferUnitPrices(purchaseRequestId, itemDetails);
//            return new ResponseEntity<>(updatedItemDetails, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @PatchMapping("/{purchaseRequestId}/edit2-offer-unit-prices2")
    public ResponseEntity<List<PurchaseRequestItemDetail>> editOfferUnitPrices(
            @PathVariable Long purchaseRequestId,
            @RequestParam String supplierId,
            @RequestBody List<PurchaseRequestItemDetail> itemDetails) {
        try {
            List<PurchaseRequestItemDetail> updatedItemDetails = purchaseRequestService.editOfferUnitPrices2(purchaseRequestId, supplierId, itemDetails);
            return new ResponseEntity<>(updatedItemDetails, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
