package com.rose.procurement.supplier.mappers;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SupplierMapper {

    SupplierMapper MAPPER = Mappers.getMapper(SupplierMapper.class);
    Supplier toEntity(SupplierDto supplierDto);

    SupplierDto toDto(Supplier supplier);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Supplier partialUpdate(SupplierDto supplierDto, @MappingTarget Supplier supplier);
}