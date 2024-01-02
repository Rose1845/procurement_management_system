package com.rose.procurement.purchaseOrder.mappers;

import com.rose.procurement.contract.mappers.ContractMapper;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseOrderMapper {
    PurchaseOrderMapper MAPPER = Mappers.getMapper(PurchaseOrderMapper.class);

    @Mapping(source = "vendorId", target = "supplier.vendorId")
    PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto);

    @Mapping(source = "supplier.vendorId",target = "vendorId")
    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, @MappingTarget PurchaseOrder purchaseOrder);
}