package com.rose.procurement.purchaseRequest.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.services.PurchaseRequestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("api/v1/purchase-request")
public class PurchaseRequestController {
    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority({'ADMIN','EMPLOYEE'})")
    public PurchaseRequestDto createPurchaseRequest(@RequestBody @Valid  PurchaseRequestDto purchaseRequest) throws ProcureException {
        log.info("controller PR....");
        System.out.println(purchaseRequest);
           return   purchaseRequestService.createPurchaseRequest(purchaseRequest);
    }
    @GetMapping
    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestService.getAllPurchaseRequests();
    }
    @GetMapping("/{id}")
    public Optional<PurchaseRequestDto> getPurchaseRequestById(@PathVariable Long id) {
       return purchaseRequestService.getPurchaseRequestById(id);
    }
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseRequest>> getPurchaseRequestsBySupplier(@PathVariable Long supplierId) {
        List<PurchaseRequest> purchaseRequests = purchaseRequestService.findPurchaseRequestsBySupplierId(supplierId);
        return ResponseEntity.ok(purchaseRequests);
    }
    @PostMapping("/{purchaseRequestId}/accept-offer")
    public ResponseEntity<String> acceptOffer(@PathVariable Long purchaseRequestId, @RequestParam String supplierId) {
        try {
            purchaseRequestService.acceptOffer(purchaseRequestId, supplierId);
            return ResponseEntity.ok("Offer accepted successfully and other suppliers have been notified.");
        } catch (EntityNotFoundException enfe) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body("Error: " + iae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
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
    @GetMapping("/send-offer-to-suppliers/{id}")
    public String sendRequestOffer(@PathVariable("id") Long purchaseRequestId) throws ProcureException {
        return purchaseRequestService.sendApprovalEmailToSuppliers(purchaseRequestId);
    }
    @GetMapping("/{purchaseRequestId}/supplier/{vendorId}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestDetailsForSupplier(
            @PathVariable Long purchaseRequestId,
            @PathVariable String vendorId) {

        Optional<PurchaseRequest> purchaseRequestDetails = purchaseRequestService.getPurchaseRequestDetailsForSupplier(purchaseRequestId, vendorId);

        return purchaseRequestDetails.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
