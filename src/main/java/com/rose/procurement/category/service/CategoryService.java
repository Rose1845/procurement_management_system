package com.rose.procurement.category.service;

import com.rose.procurement.category.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {



    CategoryDto createCategory(CategoryDto categoryDto);


    List<CategoryDto> getAllCategories();

}
