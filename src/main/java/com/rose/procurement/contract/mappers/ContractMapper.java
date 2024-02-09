package com.rose.procurement.contract.mappers;

import com.rose.procurement.contract.dtos.ContractDto;
import com.rose.procurement.contract.entities.Contract;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {
    ContractMapper MAPPER = Mappers.getMapper(ContractMapper.class);
    @Mappings({
            @Mapping(target = "supplier.vendorId", source = "vendorId"),
            @Mapping(target = "items", source = "items") // Add this mapping for items

            // Add other mappings as needed
    })    Contract toEntity(ContractDto contractDto);
    @Mappings({
            @Mapping(target = "vendorId", source = "supplier.vendorId"),
            @Mapping(target = "items", source = "items") // Add this mapping for items

            // Add other mappings as needed
    })    ContractDto toDto(Contract contract);

    @Mapping(target = "items", source = "items") // Add this mapping for items
    List<Contract> toEntityList(List<ContractDto> contractDtoList);

    @Mapping(target = "items", source = "items") // Add this mapping for items
    List<ContractDto> toDtoList(List<Contract> contractList);
//    ContractMapper MAPPER = Mappers.getMapper(ContractMapper.class);
//    Contract toEntity(ContractDto contractDto);
//
//    ContractDto toDto(Contract contract);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Contract partialUpdate(ContractDto contractDto, @MappingTarget Contract contract);
}