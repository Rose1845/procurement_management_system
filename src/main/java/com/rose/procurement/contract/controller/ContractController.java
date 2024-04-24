package com.rose.procurement.contract.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.dtos.RenewDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.contract.service.ContractService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.ContractStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/contract")
public class ContractController {
    private final ContractService contractService;
    private final ContractRepository contractRepository;

    public ContractController(ContractService contractService,
                              ContractRepository contractRepository) {
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }
    @PostMapping
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public ContractDto createContract(@RequestBody @Valid ContractDto contractRequest){
        return contractService.createContract(contractRequest);
    }
    @GetMapping
    public List<Contract> getAllContracts(){
        return contractService.getAllContracts();
    }

    @GetMapping("/status-open")
    public List<Contract> getAllContractsOpen(){
        return contractService.getAllContractsOpen();
    }
    @GetMapping("{id}")
    public Optional<Contract> getContract(@PathVariable("id") String contractId){
        return contractService.getContract(contractId);
    }
    @GetMapping("/contract-items/{contractId}")
    public ResponseEntity<ContractDto> getContractWithItems(@PathVariable String contractId) {
        return contractService.getContractWithItems(contractId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/paginations")
    public Page<Contract> findAllContracts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) ContractStatus contractStatus,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String contractTitle,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Contract> filteredContracts = null;
        if (contractTitle != null && !contractTitle.isEmpty() && startDate != null && endDate != null) {
            // Search by name and createdAt date range with pagination and sorting
            filteredContracts = contractRepository.findByContractTitleContainingAndCreatedAtBetween(
                    contractTitle, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (contractTitle != null && !contractTitle.isEmpty()) {
            // Search by name with pagination and sorting
            filteredContracts = contractRepository.findByContractTitleContaining(contractTitle, pageable);
        } else if (startDate != null && endDate != null) {
            // Search by createdAt date range with pagination and sorting
            filteredContracts = contractRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable);
        } else if (contractStatus != null && supplierId != null) {
            // Filter by both approval status and supplier with pagination and sorting
            filteredContracts = contractRepository.findByContractStatusAndSupplier_VendorId(contractStatus, supplierId, pageable);
        } else if (contractStatus != null) {
            // Filter only by approval status with pagination and sorting
            filteredContracts = contractRepository.findByContractStatus(contractStatus,pageable);
        } else if (supplierId != null) {
            // Filter only by supplier with pagination and sorting
            filteredContracts = contractRepository.findBySupplier_VendorId(supplierId, pageable);
        } else {
            // No filters applied, return all orders with pagination and sorting
            filteredContracts = contractRepository.findAll(pageable);
        }
        return filteredContracts;
    }


    //    @GetMapping("send-contract/{id}")
//    public Contract contractApproval(@PathVariable("id") String contractId) throws ProcureException {
//        return contractService.sendContractForApproval(contractId);
//    }
    @GetMapping("items/{id}")
    public Set<Item> getAllContractItems(@PathVariable("id") String contactId){
        return contractService.getContractItems(contactId);
    }
    @DeleteMapping("{id}")
    public String deleteContract(@PathVariable("id") String contractId){
        return contractService.deleteContract(contractId);
    }
    @PostMapping("clone-contract/{id}")
    public Optional<ContractDto> cloneContract(@PathVariable("id") String contractId){
        return contractService.cloneContract(contractId);
    }
    @PutMapping("{id}")

    public Contract updateContract(@PathVariable("id") String contractId,@RequestBody ContractDto contractRequest) throws ProcureException {
        return contractService.updateContract(contractId,contractRequest);
    }
//    @PatchMapping("/{contractId}/approve")
//    public Contract approveContract(@PathVariable String contractId) throws ProcureException {
//        return contractService.sendContractForApproval(contractId);
//    }
    @PostMapping("/send-to-supplier/{id}")
    public String sendContractToSupplier(@PathVariable("id") String contractId) throws ProcureException {
        return   contractService.sendApprovalEmailToSupplier(contractId);
    }

    // Step 3: Edit Approval Status by Supplier
    @PatchMapping("/edit-contract/{contractId}")
    public ResponseEntity<Contract> editContractApprovalStatus(
            @PathVariable String contractId,
            @RequestParam String contractStatus) throws ProcureException {
        // Implement logic to update the contract approval status
        Contract updatedContract = contractService.updateApprovalStatus(contractId, ContractStatus.valueOf(contractStatus));
        return ResponseEntity.ok(updatedContract);
    }
    @PatchMapping("/renew/{contractId}")
    public ResponseEntity<Contract> renewContract(
            @PathVariable String contractId,
            @RequestBody RenewDto renewDto) throws ProcureException {
        // Implement logic to update the contract approval status
        Contract updatedContract = contractService.renewContract(contractId,renewDto);
        return ResponseEntity.ok(updatedContract);
    }

    @PatchMapping("/editDate/{contractId}")
    public ResponseEntity<Contract> editDueDate(
            @PathVariable String contractId,
            @RequestBody RenewDto renewDto) throws ProcureException {
        // Implement logic to update the contract approval status
        Contract updatedContract = contractService.editDueDate(contractId,renewDto);
        return ResponseEntity.ok(updatedContract);
    }

    @GetMapping("co/{month}")
    public List<Contract> findContractsByMonth(@PathVariable("month") int month) {
        return contractService.findContractsByMonth(month);
    }
    // Step 4: Alert Organization of Supplier's Response (This could be done asynchronously in a real application)
    // This is just an example, you may want to use a message queue for real-world scenarios.
    @Async
    public void alertOrganizationOfSupplierResponse(String contractId, String supplierResponse) {
        // Implement logic to send an alert/notification to the organization
        // This can be an email, push notification, etc.
        // OrganizationNotificationService.sendNotification(contractId, supplierResponse);
    }

}

