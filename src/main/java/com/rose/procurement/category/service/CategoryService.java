package com.rose.procurement.category.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.request.CategoryRequest;

public interface CategoryService {


    Category createCategory(CategoryRequest categoryRequest);

//    Category createCategory(CategoryRequest categoryRequest);
}
