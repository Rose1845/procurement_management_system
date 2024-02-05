package com.rose.procurement.purchaseRequest.mappers;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PurchaseRequestMapper {
    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);
   @Mappings({
           @Mapping(target = "items",source = "items"),
           @Mapping(target = "suppliers", source = "suppliers")

   })
    PurchaseRequest toEntity(PurchaseRequestDto purchaseRequestDto);
    @Mappings({
            @Mapping(target = "items",source = "items"),
            @Mapping(target = "suppliers", source = "suppliers")

    })
    PurchaseRequestDto toDto(PurchaseRequest purchaseRequest);
    // Add this mapping for items
  @Mappings({
          @Mapping(target = "items", source = "items"),
          @Mapping(target = "suppliers", source = "suppliers")
  })
        // Add this mapping for items
    List<PurchaseRequest> toEntityList(List<PurchaseRequestDto> purchaseRequestDtos);

    @Mappings({
            @Mapping(target = "items", source = "items"),
            @Mapping(target = "suppliers", source = "suppliers")
    })
// Add this mapping for items
    List<PurchaseRequestDto> toDtoList(List<PurchaseOrder> purchaseOrders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PurchaseRequest partialUpdate(PurchaseRequestDto purchaseRequestDto, @MappingTarget PurchaseRequest purchaseOrder);
}