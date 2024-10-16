package com.rose.procurement.purchaseRequisition.controller;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisitionDto;
import com.rose.procurement.purchaseRequisition.repository.PurchaseRequisitionRepository;
import com.rose.procurement.purchaseRequisition.services.PurchaseRequisitionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/purchase-requisition")
public class PurchaseRequisitionController {
    private final PurchaseRequisitionService purchaseRequisitionService;
    private final PurchaseRequisitionRepository purchaseRequisitionRepository;

    public PurchaseRequisitionController(PurchaseRequisitionService purchaseRequisitionService, PurchaseRequisitionRepository purchaseRequisitionRepository) {
        this.purchaseRequisitionService = purchaseRequisitionService;
        this.purchaseRequisitionRepository = purchaseRequisitionRepository;
    }

    @PostMapping
    public PurchaseRequisitionDto createPurchaseRequisition(@RequestBody @Valid PurchaseRequisitionDto purchaseRequisitionDto) {
        return purchaseRequisitionService.createPurchaseRequistion(purchaseRequisitionDto);
    }
    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasAuthority({'APPROVER'})")
    public PurchaseRequisition ApproveRequisition(@PathVariable("id") Long requisitionId,@RequestParam ApprovalStatus approvalStatus) throws ProcureException {
        return purchaseRequisitionService.updateApprovalStatus(requisitionId,approvalStatus);
    }
    @GetMapping("/all-by-pagination")
    public Page<PurchaseRequisition> findAllRequestByPagination(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRequisitionRepository.findAll(pageable);
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
