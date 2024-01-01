package com.rose.procurement.category.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto){
        log.info("Received category payload",categoryDto);

        Category category = CategoryMapper.MAPPER.toEntity(categoryDto);
        category.setCategoryName(categoryDto.getCategoryName());

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.MAPPER.toDto(savedCategory);
    }

    @Override
    public List<Category> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream().map(CategoryMapper.MAPPER::toDto).collect(Collectors.toList());
//        return categoryRepository.findAll().stream().map(CategoryMapper.MAPPER.toDto()).collect(Collectors.toList());
        return new ArrayList<>(categoryRepository.findAll());
    }


}
