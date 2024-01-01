package com.rose.procurement.category.mappers;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.dtos.CategoryDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    @Mappings({
            @Mapping(target = "categoryName", source = "categoryDto.categoryName"),
            // Add other mappings if needed
    })
    Category toEntity(CategoryDto categoryDto);

    @Mappings({
            @Mapping(target = "categoryName", source = "category.categoryName"),
            // Add other mappings if needed
    })
    CategoryDto toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "categoryName", source = "categoryDto.categoryName"),
            // Add other mappings if needed
    })
    Category partialUpdate(CategoryDto categoryDto, @MappingTarget Category category);
}