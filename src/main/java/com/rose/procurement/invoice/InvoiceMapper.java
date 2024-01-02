package com.rose.procurement.invoice;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    InvoiceMapper MAPPER= Mappers.getMapper(InvoiceMapper.class);
    @Mapping(target = "invoiceId", ignore = true)
    @Mapping(source="purchaseOrderId",target = "purchaseOrder.purchaseOrderId")
    Invoice toEntity(InvoiceDto invoiceDto);
    @Mapping(source="purchaseOrder.purchaseOrderId",target = "purchaseOrderId")
    InvoiceDto toDto(Invoice invoice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invoice partialUpdate(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);

}