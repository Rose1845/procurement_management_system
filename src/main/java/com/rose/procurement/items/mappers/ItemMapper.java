package com.rose.procurement.items.mappers;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "categoryId", target = "category.categoryId")
    Item toEntity(ItemDto itemDto);
    @Mapping(source = "category.categoryId", target = "categoryId")
    ItemDto toDto(Item item);
//    List<Item> toEntityList(List<ItemDto>itemDtoList);
//
//    List<ItemDto> toDtoList(List<Item> itemList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item partialUpdate(ItemDto itemDto, @MappingTarget Item item);

}