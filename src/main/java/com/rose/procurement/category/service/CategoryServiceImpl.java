package com.rose.procurement.category.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto){
        log.info("Received category payload",categoryDto);

//        Category category = categoryMapper.toEntity(categoryDto);
//        category.setCategoryName(categoryDto.getCategoryName());


        Category category = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }


}
