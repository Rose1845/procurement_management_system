package com.rose.procurement.contract.controller;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.service.ContractService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
    @PostMapping
    public ContractDto createContract(@RequestBody @Valid ContractDto contractRequest){
        return contractService.createContract(contractRequest);
    }
    @GetMapping
    public List<ContractDto> getAllContracts(){
        return contractService.getAllContracts();
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
    @PutMapping("{id}")
    public Contract updateContract(@PathVariable("id") String contractId,@RequestBody ContractDto contractRequest) throws ProcureException {
        return contractService.updateContract(contractId,contractRequest);
    }
//    @PatchMapping("/{contractId}/approve")
//    public Contract approveContract(@PathVariable String contractId) throws ProcureException {
//        return contractService.sendContractForApproval(contractId);
//    }
    @GetMapping("/send-to-supplier/{id}")
    public ResponseEntity<Optional<Contract>> sendContractToSupplier(@PathVariable("id") String contractId) {
        Optional<Contract> sendContract = contractService.sendApprovalEmailToSupplier(contractId);
        return ResponseEntity.ok(sendContract);
    }

    // Step 3: Edit Approval Status by Supplier
    @PatchMapping("/edit-contract/{contractId}")
    public ResponseEntity<Contract> editContractApprovalStatus(
            @PathVariable String contractId,
            @RequestParam String approvalStatus) throws ProcureException {
        // Implement logic to update the contract approval status
        Contract updatedContract = contractService.updateApprovalStatus(contractId, ApprovalStatus.valueOf(approvalStatus));
        return ResponseEntity.ok(updatedContract);
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
