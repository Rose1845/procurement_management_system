package com.rose.procurement.contract.service;

import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.mappers.ContractMapper;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.test.Student;
import com.rose.procurement.test.StudentDto;
import org.mapstruct.ap.internal.model.Mapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractService {
    private final SupplierRepository supplierRepository;
    private final ContractRepository contractRepository;

    public ContractService(SupplierRepository supplierRepository, ContractRepository contractRepository,
                           ItemRepository itemRepository) {
        this.supplierRepository = supplierRepository;
        this.contractRepository = contractRepository;
    }
    public ContractDto createContract(ContractDto contractRequest) {
        Optional<Supplier> supplier = supplierRepository.findByVendorId(contractRequest.getSupplier().getVendorId());
        if(supplier.isEmpty()){
            throw new IllegalStateException("supplier with id doesnot exists");
        }

        Contract contract1 = ContractMapper.MAPPER.toEntity(contractRequest);
        contract1.setContractEndDate(contractRequest.getContractEndDate());
        contract1.setContractTitle(contractRequest.getContractTitle());
        contract1.setContractStartDate(contractRequest.getContractStartDate());
        contract1.setTermsAndConditions(contractRequest.getTermsAndConditions());
        contract1.setContractType(contractRequest.getContractType());
        contract1.setSupplier(supplier.get());
        Set<Item> items = new HashSet<>(contract1.getItems());
        contract1.setItems(new HashSet<>(items));
        Contract savedCOntract = contractRepository.save(contract1);
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

    public Contract updateContract(String contractId,ContractDto contractRequest) {
        Contract contract = contractRepository.findById(contractId).orElseThrow();
            contract.setContractEndDate(contractRequest.getContractEndDate());
            contract.setContractTitle(contractRequest.getContractTitle());
            contract.setContractType(contractRequest.getContractType());
            contract.setSupplier(contractRequest.getSupplier());
            contract.setTermsAndConditions(contractRequest.getTermsAndConditions());
            contract.setContractStartDate(contractRequest.getContractStartDate());
        return contractRepository.save(contract);

    }


    public Set<Item> getContractItems(String contractId) {
        return contractRepository.findItemsByContractId(contractId);
    }
}
