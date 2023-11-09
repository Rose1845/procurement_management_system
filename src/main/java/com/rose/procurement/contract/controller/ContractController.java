package com.rose.procurement.contract.controller;


import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.request.ContractRequest;
import com.rose.procurement.contract.service.ContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
    @PostMapping
    public Contract createContract(@RequestBody ContractRequest contractRequest){
        return contractService.createContract(contractRequest);
    }
    @GetMapping
    public List<Contract> getAllContracts(){
        return contractService.getAllContracts();
    }
    @GetMapping("{id}")
    public Optional<Contract> getContract(@PathVariable("id") String contractId){
        return contractService.getContract(contractId);
    }
    @DeleteMapping("{id}")
    public String deleteContract(@PathVariable("id") String contractId){
        return contractService.deleteContract(contractId);
    }
    @PutMapping("{id}")
    public Contract updateContract(@PathVariable("id") String contractId,@RequestBody ContractRequest contractRequest){
        return contractService.updateContract(contractId,contractRequest);
    }

}
