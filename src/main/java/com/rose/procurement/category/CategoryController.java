package com.rose.procurement.category;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.category.service.CategoryService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.repo.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }
    @PostMapping
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE','APPROVER'})")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto  categoryDto) throws ProcureException {
        log.info("category");
        return categoryService.createCategory(categoryDto);
    }
    @GetMapping("/export/categories")
    public ResponseEntity<ByteArrayResource> exportCategories() throws IOException {
        byte[] csvData = categoryService.exportCategoriesToCsv();

        ByteArrayResource resource = new ByteArrayResource(csvData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=categories.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/all")
    public Page<Category> findAllPurchaseOrders1(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);
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
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public String deleteCategory(@PathVariable("id") Long vendorId){
        return categoryService.deleteCategory(vendorId);
    }
    @PutMapping("{id}")
    public Category updateCategory(@PathVariable("id") Long vendorId, CategoryDto categoryDto){
        return categoryService.updateCategory(vendorId,categoryDto);
    }
    @PostMapping(value = "upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority({'ADMIN'})")
    ResponseEntity<Integer> uploadSuppliers(@RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(categoryService.uploadCategories(file));
    }
    @GetMapping("/template/download/single")
//    @PreAuthorize("hasAuthority({'ADMIN'})")
    public ResponseEntity<InputStreamResource> downloadSingleSupplierTemplate() {
        InputStreamResource resource = categoryService.generateTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=single_category_template.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }
    @GetMapping("/pagination/categories")
    public Page<Category> findAllCategories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) String categoryName
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> filteredCategories = null;
        if (categoryName != null && !categoryName.isEmpty()) {
            // Search by name with pagination and sorting
            filteredCategories = categoryRepository.findByCategoryNameContaining(categoryName, pageable);
        }
        else {
            // No filters applied, return all orders with pagination and sorting
            filteredCategories = categoryRepository.findAll(pageable);
        }
        return filteredCategories;
    }

}
