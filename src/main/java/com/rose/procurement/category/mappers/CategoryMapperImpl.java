//package com.rose.procurement.category.mappers;
//
//import com.rose.procurement.category.entity.Category;
//import com.rose.procurement.category.dtos.CategoryDto;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CategoryMapperImpl implements CategoryMapper{
//
//
//    @Override
//    public Category toEntity(CategoryDto categoryDto) {
//        Category category = Category.builder()
//                .categoryName(categoryDto.getCategoryName())
//                .build();
//        BeanUtils.copyProperties(category,categoryDto);
//        return category;
//    }
//
//    @Override
//    public CategoryDto toDto(Category category) {
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setCategoryName(category.getCategoryName());
//        return categoryDto;
//    }
//
//
////    @Override
////    public Category toDto(Category category) {
////
////        Category categoryDto = new Category();
////        categoryDto.setCategoryName(category.getCategoryName());
////        BeanUtils.copyProperties(categoryDto,category);
////        return categoryDto;
////    }
//
//    @Override
//    public Category partialUpdate(CategoryDto categoryDto, Category category) {
//        return null;
//    }
//}
