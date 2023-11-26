package com.rose.procurement.contract.mappers;

import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {
    ContractMapper MAPPER = Mappers.getMapper(ContractMapper.class);

    Contract toEntity(ContractDto contractDto);

    ContractDto toDto(Contract contract);

    List<Contract> toEntityList(List<ContractDto> contractDtoList);

    List<ContractDto> toDtoList(List<Contract> contractList);
//    ContractMapper MAPPER = Mappers.getMapper(ContractMapper.class);
//    Contract toEntity(ContractDto contractDto);
//
//    ContractDto toDto(Contract contract);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Contract partialUpdate(ContractDto contractDto, @MappingTarget Contract contract);
}