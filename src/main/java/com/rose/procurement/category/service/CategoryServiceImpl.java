package com.rose.procurement.category.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.category.request.CategoryRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(CategoryRequest categoryRequest){
        Category category = Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .build();
        return categoryRepository.save(category);
    }


}
