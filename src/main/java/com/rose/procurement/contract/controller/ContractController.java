package com.rose.procurement.contract.controller;


import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.service.ContractService;
import com.rose.procurement.items.entity.Item;
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
    public ContractDto createContract(@RequestBody ContractDto contractRequest){
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
    @GetMapping("items/{id}")
    public Set<Item> getAllContractItems(@PathVariable("id") String contactId){
        return contractService.getContractItems(contactId);
    }
    @DeleteMapping("{id}")
    public String deleteContract(@PathVariable("id") String contractId){
        return contractService.deleteContract(contractId);
    }
    @PutMapping("{id}")
    public Contract updateContract(@PathVariable("id") String contractId,@RequestBody ContractDto contractRequest){
        return contractService.updateContract(contractId,contractRequest);
    }

}
