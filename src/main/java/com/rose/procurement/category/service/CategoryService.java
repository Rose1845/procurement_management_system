package com.rose.procurement.category.service;

import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {



    CategoryDto createCategory(CategoryDto categoryDto);


    List<Category> getAllCategories();

}
