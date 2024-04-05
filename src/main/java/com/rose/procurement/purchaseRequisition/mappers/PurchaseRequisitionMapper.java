package com.rose.procurement.purchaseRequisition.mappers;

import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisitionDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseRequisitionMapper {
    PurchaseRequisitionMapper MAPPER = Mappers.getMapper(PurchaseRequisitionMapper.class);

    PurchaseRequisition toEntity(PurchaseRequisitionDto purchaseRequisitionDto);

    PurchaseRequisitionDto toDto(PurchaseRequisition purchaseRequisition);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PurchaseRequisition partialUpdate(PurchaseRequisitionDto purchaseRequisitionDto, @MappingTarget PurchaseRequisition purchaseRequisition);
}

