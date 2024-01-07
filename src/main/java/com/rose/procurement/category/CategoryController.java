package com.rose.procurement.category;

import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto  categoryDto){
        log.info("category");
        return categoryService.createCategory(categoryDto);
    }
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
