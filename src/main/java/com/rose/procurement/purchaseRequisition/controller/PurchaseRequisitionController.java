package com.rose.procurement.purchaseRequisition.controller;

import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisitionDto;
import com.rose.procurement.purchaseRequisition.services.PurchaseRequisitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/purchase-requisition")
public class PurchaseRequisitionController {
    private final PurchaseRequisitionService purchaseRequisitionService;

    public PurchaseRequisitionController(PurchaseRequisitionService purchaseRequisitionService) {
        this.purchaseRequisitionService = purchaseRequisitionService;
    }

    @PostMapping
    public PurchaseRequisitionDto createPurchaseRequisition(@RequestBody PurchaseRequisitionDto purchaseRequisitionDto){
        return purchaseRequisitionService.createPurchaseRequistion(purchaseRequisitionDto);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseRequisition>> getAllPurchaseRequisitions() {
        List<PurchaseRequisition> purchaseRequisitions = purchaseRequisitionService.getAllPurchaseRequisitions();
        return new ResponseEntity<>(purchaseRequisitions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> getPurchaseRequisitionById(@PathVariable("id") Long requisitionId) {
        Optional<PurchaseRequisition> purchaseRequisition = purchaseRequisitionService.getPurchaseRequisitionById(requisitionId);

        return purchaseRequisition.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
