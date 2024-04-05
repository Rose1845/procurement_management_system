package com.rose.procurement.delivery;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    DeliveryMapper MAPPER = Mappers.getMapper(DeliveryMapper.class);

//    @Mappings({
//            @Mapping(target = "purchaseOrder.purchaseOrderId", source = "purchaseOrderId"),
//            // Add other mappings as needed
//    })
    Delivery toEntity(DeliveryDTo deliveryDTo);

//    @Mappings({
//            @Mapping(target = "purchaseOrderId", source = "purchaseOrder.purchaseOrderId"),
//            // Add other mappings as needed
//    })
    DeliveryDTo toDto(Delivery delivery);

}
