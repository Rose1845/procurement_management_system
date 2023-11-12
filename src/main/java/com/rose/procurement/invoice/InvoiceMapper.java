package com.rose.procurement.invoice;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    @Mapping(target = "invoiceId", ignore = true)
    Invoice toEntity(InvoiceDto invoiceDto);

    InvoiceDto toDto(Invoice invoice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invoice partialUpdate(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);

}