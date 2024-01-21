package com.rose.procurement.contract.service;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.mappers.ContractMapper;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ContractService {
    private final SupplierRepository supplierRepository;
    private final ContractRepository contractRepository;
    private final EmailService emailService;
    private final ContractMapper contractMapper;

    public ContractService(SupplierRepository supplierRepository, ContractRepository contractRepository,
                           ItemRepository itemRepository, EmailService emailService, ContractMapper contractMapper) {
        this.supplierRepository = supplierRepository;
        this.contractRepository = contractRepository;
        this.emailService = emailService;
        this.contractMapper = contractMapper;
    }
    public ContractDto createContract(ContractDto contractRequest) {
//        if (contractRequest.getSupplier().getVendorId() == null) {
//            throw new IllegalArgumentException("Supplier must be provided in the contract request");
//        }

        Optional<Supplier> supplier = supplierRepository.findById(contractRequest.getVendorId());

//        if (supplier.isEmpty()) {
//            throw new IllegalStateException("Supplier with id does not exist");
//        }

        Contract contract1 = ContractMapper.MAPPER.toEntity(contractRequest);
        contract1.setContractEndDate(contractRequest.getContractEndDate());
        contract1.setContractTitle(contractRequest.getContractTitle());
        contract1.setContractStartDate(contractRequest.getContractStartDate());
        contract1.setTermsAndConditions(contractRequest.getTermsAndConditions());
        contract1.setContractType(contractRequest.getContractType());
        contract1.setApprovalStatus(ApprovalStatus.PENDING);
//        supplier.ifPresent(contract1.getSupplier().getVendorId());
//        contract1.setSupplier(supplier.get());
        supplier.ifPresent(contract1::setSupplier);
        Set<Item> items = new HashSet<>(contract1.getItems());
        contract1.setItems(new HashSet<>(items));
        Contract savedCOntract = contractRepository.save(contract1);
        log.info("contract data",savedCOntract);
        return ContractMapper.MAPPER.toDto(savedCOntract);
    }

    public List<ContractDto> getAllContracts(){
        List<ContractDto> contractDtos = new ArrayList<>();
        List<Contract> students = contractRepository.findAll();
        students.forEach(student -> {
            ContractDto contractDto = ContractMapper.MAPPER.toDto(student);
            contractDtos.add(contractDto);
        });
        return contractDtos;  }
    public String deleteContract(String contractId){
        Optional<Contract> contract = contractRepository.findById(contractId);
        if(contract.isPresent()){
            contractRepository.deleteById(contractId);
        } else {
            return "contract with id " + contractId + "do not exist";
        }
        return "contract deleted successfully";
    }
    public Optional<Contract> getContract(String contractId){
        return  contractRepository.findById(contractId);
    }

    public Contract updateContract(String contractId,ContractDto contractRequest) throws ProcureException {
        Contract contract = contractRepository.findById(contractId).orElseThrow(()-> new ProcureException("contract id do not exists"));
            contract.setContractEndDate(contractRequest.getContractEndDate());
            contract.setContractTitle(contractRequest.getContractTitle());
            contract.setContractType(contractRequest.getContractType());
            Supplier supplier = supplierRepository.findById(contractRequest.getVendorId()).orElseThrow(()->new RuntimeException("no supplier with id"+ contractRequest.getVendorId()));
            contract.setSupplier(supplier);
            contract.setTermsAndConditions(contractRequest.getTermsAndConditions());
            contract.setContractStartDate(contractRequest.getContractStartDate());
        return contractRepository.save(contract);
    }
    public Optional<ContractDto> getContractWithItems(String contractId) {
        Optional<Contract> contractOptional = contractRepository.findByIdWithItems(contractId);
        return contractOptional.map(contractMapper::toDto);
    }
    public Set<Item> getContractItems(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("contract  not found with id: " + contractId));

        return contract.getItems();
//        return contractRepository.findItemsByContractId(contractId);
    }
     /** send email to supplier for contract approval **/
//    public Contract sendContractForApproval(String contractId) throws ProcureException {
//        // Retrieve the contract from the database
//        Contract contract = contractRepository.findById(contractId)
//                .orElseThrow(() -> new ProcureException("Contract not found with id: " + contractId));
//
//        // Check if the contract is not already approved
//        if (contract.getApprovalStatus() != ApprovalStatus.APPROVED) {
//            // Send email to the supplier
//            sendApprovalEmailToSupplier(contractId);
//
//            // Update the contract status
//            contract.setApprovalStatus(ApprovalStatus.APPROVED); // or whatever status is appropriate
//
//            // Save the updated contract to the database
//            return contractRepository.save(contract);
//        } else {
//            // Contract is already approved, handle accordingly (throw an exception or return null, for example)
//            return null;
//        }
//    }

    public Optional<Contract> sendApprovalEmailToSupplier(String contractId) {
        Optional<Contract> contract1 = contractRepository.findById(contractId);
        String editLink = "http://localhost:8081/api/v1/contract/edit-contract/" + contractId;
        if(contract1.isPresent()){
            Set<Item> items = contract1.get().getItems();
        }
        // Specify the email content
        String subject = "Contract Approval Request";
        String text = "Dear Supplier, \n\n"
                + "A contract requires your approval. Please review and take appropriate action.\n\n"
                + "Contract Title: " + contract1.get().getContractTitle() + "\n"
                + "Contract Type: " + contract1.get().getContractType() + "\n"
                + "To approve or reject, click the following link: " + editLink + "\n"
                + "\n\nBest Regards,\nProcureSwift Company";

        // Send the email
        emailService.sendEmail(contract1.get().getSupplier().getEmail(), subject, text);
        return contract1;
    }

    public Contract updateApprovalStatus(String contractId, ApprovalStatus approvalStatus) throws ProcureException {
        // Retrieve the existing contract from the database
        Contract existingContract = contractRepository.findById(contractId).orElseThrow(()->new ProcureException("id already exists"));

        // Update the approval status
        existingContract.setApprovalStatus(approvalStatus);

        // Save the updated contract in the database
        return contractRepository.save(existingContract);
    }
}
