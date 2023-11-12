package com.rose.procurement.contract.mappers;

import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {
    Contract toEntity(ContractDto contractDto);

    ContractDto toDto(Contract contract);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contract partialUpdate(ContractDto contractDto, @MappingTarget Contract contract);
}