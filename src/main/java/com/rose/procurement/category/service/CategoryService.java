package com.rose.procurement.category.service;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CategoryService {



    CategoryDto createCategory(CategoryDto categoryDto) throws ProcureException;


    List<Category> getAllCategories();

    String deleteCategory(Long categoryId);
    Category updateCategory(Long categoryId, CategoryDto categoryDto);

    Integer uploadCategories(MultipartFile file);

    InputStreamResource generateTemplate();

    Category getCategoryById(Long categoryId);
}
