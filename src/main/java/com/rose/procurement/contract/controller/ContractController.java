package com.rose.procurement.contract.controller;


import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.request.ContractRequest;
import com.rose.procurement.contract.service.ContractService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
