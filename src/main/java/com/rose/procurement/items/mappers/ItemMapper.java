package com.rose.procurement.items.mappers;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);

//    @Mapping(source = "category.categoryId", target = "category.categoryId")
//    @Mapping(source = "supplier.vendorId", target = "supplier.vendorId")
    Item toEntity(ItemDto itemDto);

    ItemDto toDto(Item item);
//    List<Item> toEntityList(List<ItemDto>itemDtoList);
//
//    List<ItemDto> toDtoList(List<Item> itemList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item partialUpdate(ItemDto itemDto, @MappingTarget Item item);

}