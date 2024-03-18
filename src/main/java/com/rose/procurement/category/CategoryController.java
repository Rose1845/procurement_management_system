package com.rose.procurement.category;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE','APPROVER'})")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto  categoryDto) throws ProcureException {
        log.info("category");
        return categoryService.createCategory(categoryDto);
    }
    @GetMapping("{id}")
    public Category getCategoryById(@PathVariable("id") Long categoryId){
        return categoryService.getCategoryById(categoryId);
    }
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public String deleteCategory(@PathVariable("id") Long vendorId){
        return categoryService.deleteCategory(vendorId);
    }
    @PutMapping("{id}")
    public Category updateCategory(@PathVariable("id") Long vendorId, CategoryDto categoryDto){
        return categoryService.updateCategory(vendorId,categoryDto);
    }
    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority({'ADMIN'})")
    ResponseEntity<Integer> uploadSuppliers(@RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(categoryService.uploadCategories(file));
    }
    @GetMapping("/template/download/single")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public ResponseEntity<InputStreamResource> downloadSingleSupplierTemplate() {
        InputStreamResource resource = categoryService.generateTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=single_category_template.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }
}
