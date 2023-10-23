package com.rose.procurement.contract.service;

import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.contract.request.ContractRequest;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
}
