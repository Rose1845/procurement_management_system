package com.rose.procurement.purchaseRequest.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.offer.OfferItem;
import com.rose.procurement.purchaseRequest.entities.OfferItemUpdateDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
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

//    @PatchMapping("/{purchaseRequestId}/suppliers/{supplierId}/offer-items")
//    public ResponseEntity<PurchaseRequestDto> updateOfferItemsForSupplier(
//            @PathVariable Long purchaseRequestId,
//            @PathVariable String supplierId,
//            @RequestBody List<OfferItemUpdateDto> offerItemUpdateDtoList) {
//        try {
//            PurchaseRequestDto updatedPurchaseRequest = purchaseRequestService.updateOfferItemsForSupplier(purchaseRequestId, supplierId, offerItemUpdateDtoList);
//            return ResponseEntity.ok(updatedPurchaseRequest);
//        } catch (ProcureException e) {
//            // Handle custom exceptions
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }


}
