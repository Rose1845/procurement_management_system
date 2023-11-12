package com.rose.procurement.contract.service;

import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.mappers.ContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractMapperImpl implements ContractMapper {

    @Override
    public Contract toEntity(ContractDto contractDto) {

        return Contract.builder()
                .contractStartDate(contractDto.getContractStartDate())
                .contractType(contractDto.getContractType())
                .contractTitle(contractDto.getContractTitle() )
                .supplier(contractDto.getSupplier())
                .termsAndConditions(contractDto.getTermsAndConditions())
                .contractEndDate(contractDto.getContractEndDate())
                .build();
    }

    @Override
    public ContractDto toDto(Contract contract) {

        ContractDto contractDto = ContractDto.builder()
                .contractStartDate(contract.getContractStartDate())
                .contractTitle(contract.getContractTitle())
                .contractType(contract.getContractType())
                .contractEndDate(contract.getContractEndDate())
                .supplier(contract.getSupplier())
                .termsAndConditions(contract.getTermsAndConditions())
                .build();
        BeanUtils.copyProperties(contractDto,contract);
        return contractDto;
    }

    @Override
    public Contract partialUpdate(ContractDto contractDto, Contract contract) {
        return null;
    }
}
