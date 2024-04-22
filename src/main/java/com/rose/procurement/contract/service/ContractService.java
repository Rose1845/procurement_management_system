package com.rose.procurement.contract.service;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.mappers.ContractMapper;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ContractStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Optional<Supplier> supplier = supplierRepository.findById(contractRequest.getVendorId());

        Contract contract1 = ContractMapper.MAPPER.toEntity(contractRequest);
        contract1.setContractStatus(checkContractEndDateExpired(contractRequest.getContractEndDate()) ? ContractStatus.EXPIRED : ContractStatus.OPEN);

        contract1.setContractEndDate(contractRequest.getContractEndDate());
        contract1.setContractTitle(contractRequest.getContractTitle());
        contract1.setContractStartDate(contractRequest.getContractStartDate());
        contract1.setTermsAndConditions(contractRequest.getTermsAndConditions());
        contract1.setContractType(contractRequest.getContractType());
        contract1.setContractStatus(ContractStatus.OPEN);
        supplier.ifPresent(contract1::setSupplier);
        Set<Item> items = new HashSet<>(contract1.getItems());
        contract1.setItems(new HashSet<>(items));
        Contract savedCOntract = contractRepository.save(contract1);
        log.info("contract data",savedCOntract);
        return ContractMapper.MAPPER.toDto(savedCOntract);
    }
    public List<Contract> getAllContracts(){
        //        return new ArrayList<>(contractRepository.findAll());

        List<Contract> existingContracts = contractRepository.findAll();
        for (Contract existingContract : existingContracts) {
            if (existingContract.checkContractEndDateExpired()) {
                existingContract.setContractStatus(ContractStatus.EXPIRED);
                contractRepository.save(existingContract);
            }
        }
        return existingContracts;
    }
    public List<Contract> getAllContractsOpen(){
        List<Contract> existingContracts = contractRepository.findAll();
        // Filter contracts with "open" status
        return existingContracts.stream()
                .filter(contract -> "OPEN".equalsIgnoreCase(String.valueOf(contract.getContractStatus())))
                .collect(Collectors.toList());
    }
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
        contract.setContractStatus(checkContractEndDateExpired(contractRequest.getContractEndDate()) ? ContractStatus.EXPIRED : ContractStatus.OPEN);
        contract.setContractEndDate(contractRequest.getContractEndDate());
        Set<Item> items = contract.getItems();
        contract.setItems(items);
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
        if(contractOptional.get().checkContractEndDateExpired())
            contractOptional.get().setContractStatus(ContractStatus.EXPIRED);
        return contractOptional.map(contractMapper::toDto);
    }
    public Set<Item> getContractItems(String contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("contract  not found with id: " + contractId));
        return contract.getItems();
//        return contractRepository.findItemsByContractId(contractId);
    }
    public List<Contract> findContractsByMonth(int month) {
        return contractRepository.findContractByMonth(month);
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

     public String sendApprovalEmailToSupplier(String contractId) throws ProcureException {
         log.info("\"sending contract....",contractId);

       if(contractId == null ){
           throw ProcureException.builder().message("COntract id not found").metadata("id").build();
       }
         Optional<Contract> contractOptional = contractRepository.findById(contractId);

         if (contractOptional.isPresent()) {
             Contract contract = contractOptional.get();

             // Specify the email content
             String subject = "Contract Approval Request";
             String editLink = "http://192.168.1.106:3000/public/contract/approve/" + contractId;
             String text = "Dear Supplier, \n\n"
                     + "A contract requires your approval. Please review and take appropriate action.\n\n"
                     + "Contract Title: " + contract.getContractTitle() + "\n"
                     + "Contract Type: " + contract.getContractType() + "\n"
                     + "To approve or reject, click the following link: " + editLink + "\n"
                     + "\n\nBest Regards,\nProcureSwift Company";
             log.info("done building email template...");

             // Send the email
             emailService.sendEmail(contract.getSupplier().getEmail(), subject, text);
             log.info("done sending...");

             // Update the contract status to PENDING_SUPPLIER_ACCEPTANCE
             contract.setContractStatus(ContractStatus.PENDING_SUPPLIER_ACCEPTANCE);
             contractRepository.save(contract);
             return "contract sent!!";
         } else {
             log.info("didn't send becoz that contrcat not found ");
             ProcureException.builder().message("an error occured").metadata("email sending").build();
             return "email not sent becoz contrcat not found by request id";
         }

         }

    public Contract updateApprovalStatus(String contractId, ContractStatus contractStatus) throws ProcureException {
        // Retrieve the existing contract from the database
        Contract existingContract = contractRepository.findById(contractId).orElseThrow(()->new ProcureException("id already exists"));
        // Update the approval status
        existingContract.setContractStatus(contractStatus);
        // Save the updated contract in the database
        return contractRepository.save(existingContract);
    }
    public Contract renewContract(String contractId, LocalDate contractEndDate) throws ProcureException {
        // Retrieve the existing contract from the database
        Contract existingContract = contractRepository.findById(contractId).orElseThrow(()->new ProcureException("id already exists"));
        existingContract.setContractEndDate(contractEndDate);
        // Update the approval status
        existingContract.setContractStatus(ContractStatus.RENEW);
        // Save the updated contract in the database
        return contractRepository.save(existingContract);
    }

    public Contract editDueDate(String contractId, LocalDate contractEndDate) throws ProcureException {
        // Retrieve the existing contract from the database
        Contract existingContract = contractRepository.findById(contractId).orElseThrow(()->new ProcureException("id already exists"));
        existingContract.setContractEndDate(contractEndDate);
        // Update the approval status
        // Save the updated contract in the database
        return contractRepository.save(existingContract);
    }

    public Optional<ContractDto> cloneContract(String contractId) {
        Optional<Contract> originalContractOptional = contractRepository.findByIdWithItems(contractId);

        return originalContractOptional.map(originalContract -> {
            // Create a deep copy of the original contract
            Contract clonedContract = Contract.builder()
                    .contractTitle(originalContract.getContractTitle())
                    .contractType(originalContract.getContractType())
                    .contractStartDate(originalContract.getContractStartDate())
                    .contractEndDate(originalContract.getContractEndDate())
                    .termsAndConditions(originalContract.getTermsAndConditions())
                    .contractStatus(ContractStatus.OPEN)
                    // Set approval status for the clone
                    .items(new HashSet<>(originalContract.getItems())) // Copy items
                    .supplier(originalContract.getSupplier()) // Copy supplier
                    .createdAt(LocalDateTime.now()) // Reset creation timestamp
                    .updatedAt(LocalDateTime.now()) // Reset update timestamp
                    .build();
            // Save the cloned contract
            Contract savedClonedContract = contractRepository.save(clonedContract);

            return contractMapper.toDto(savedClonedContract);
        });
    }

    private boolean checkContractEndDateExpired(LocalDate contractEndDate) {
        // Check if the contract end date is in the past
        return contractEndDate != null && contractEndDate.isBefore(LocalDate.now());
    }
}
