package com.rose.procurement.purchaseRequest.mappers;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseRequestMapper {
    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);

    PurchaseRequest toEntity(PurchaseRequestDto purchaseRequestDto);

    PurchaseRequestDto toDto(PurchaseRequest purchaseRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PurchaseRequest partialUpdate(PurchaseRequestDto purchaseRequestDto, @MappingTarget PurchaseRequest purchaseOrder);
}