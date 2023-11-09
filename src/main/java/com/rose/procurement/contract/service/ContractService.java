package com.rose.procurement.contract.service;

import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.contract.request.ContractRequest;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {
    private final SupplierRepository supplierRepository;
    private final ContractRepository contractRepository;
    public ContractService(SupplierRepository supplierRepository, ContractRepository contractRepository) {
        this.supplierRepository = supplierRepository;
        this.contractRepository = contractRepository;
    }
    public Contract createContract(ContractRequest contractRequest) {
        Optional<Supplier> supplier = supplierRepository.findByVendorId(contractRequest.getSupplier().getVendorId());
        if(supplier.isEmpty()){
            throw new IllegalStateException("supplier with id doesnot exists");
        }
        Contract contract = Contract.builder()
                .contractEndDate(contractRequest.getContractEndDate())
                .contractTitle(contractRequest.getContractTitle())
                .contractStartDate(contractRequest.getContractStartDate())
                .termsAndConditions(contractRequest.getTermsAndConditions())
                .contractType(contractRequest.getContractType())
                .supplier(supplier.get())
                .build();
        return contractRepository.save(contract);

    }

    public List<Contract> getAllContracts(){
        return new ArrayList<>(contractRepository.findAll()) ;
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

    public Contract updateContract(String contractId,ContractRequest contractRequest) {
        Contract contract = contractRepository.findById(contractId).orElseThrow();
            contract.setContractEndDate(contractRequest.getContractEndDate());
            contract.setContractTitle(contractRequest.getContractTitle());
            contract.setContractType(contractRequest.getContractType());
            contract.setSupplier(contractRequest.getSupplier());
            contract.setTermsAndConditions(contractRequest.getTermsAndConditions());
            contract.setContractStartDate(contractRequest.getContractStartDate());
        return contractRepository.save(contract);

    }
}
