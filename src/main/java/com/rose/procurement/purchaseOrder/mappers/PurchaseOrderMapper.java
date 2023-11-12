package com.rose.procurement.purchaseOrder.mappers;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseOrderMapper {
    PurchaseOrderMapper INSTANCE = Mappers.getMapper(PurchaseOrderMapper.class);

    PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto);

    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, @MappingTarget PurchaseOrder purchaseOrder);
}