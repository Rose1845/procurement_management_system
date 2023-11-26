package com.rose.procurement.purchaseRequest.controller;


import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.services.PurchaseRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/purchase-request")
public class PurchaseRequestController {
    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @PostMapping
    public PurchaseRequestDto createPurchaseRequest(@RequestBody PurchaseRequestDto purchaseRequest) {
        return purchaseRequestService.createPurchaseRequest(purchaseRequest);
    }

    @GetMapping
    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestService.getAllPurchaseRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestById(@PathVariable Long id) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestService.getPurchaseRequestById(id);

        return purchaseRequest.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to associate a Purchase Order with a Purchase Request
    @PostMapping("/{purchaseRequestId}/create-purchase-order")
    public ResponseEntity<PurchaseRequest> createPurchaseOrder(
            @PathVariable Long purchaseRequestId,
            @RequestBody PurchaseOrder purchaseOrder) {
        PurchaseRequest updatedPurchaseRequest = purchaseRequestService.createPurchaseOrder(purchaseRequestId, purchaseOrder);
        return new ResponseEntity<>(updatedPurchaseRequest, HttpStatus.OK);
    }


        }
