package com.rose.procurement.category.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.CategoryCsvRepresentation;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierCsvRepresentation;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.utils.address.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws ProcureException {
        if (categoryRepository.existsByCategoryName(categoryDto.getCategoryName())) {
            throw ProcureException.builder().message("Category name   already exist").build();
        }
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

    public Category getCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public String deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return "deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        category.setCategoryName(categoryDto.getCategoryName());
        return categoryRepository.save(category);
    }
    @Override
    public Integer uploadCategories(MultipartFile file) {
        Set<CategoryDto> categoryDtos = parseCsv(file);
        categoryRepository.saveAll(convertToEntities(categoryDtos));
        return categoryDtos.size();
    }

    private Set<CategoryDto> parseCsv(MultipartFile file) {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<CategoryCsvRepresentation> headerColumnNameMappingStrategy = new HeaderColumnNameMappingStrategy<>();
            headerColumnNameMappingStrategy.setType(CategoryCsvRepresentation.class);
            CsvToBean<CategoryCsvRepresentation> csvToBean = new CsvToBeanBuilder<CategoryCsvRepresentation>(reader).withMappingStrategy(headerColumnNameMappingStrategy).withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true).build();
            return csvToBean.parse().stream().map(csvLine -> CategoryDto.builder()
                    .categoryName(csvLine.getCategoryName())
                    .build()).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Set<Category> convertToEntities(Set<CategoryDto> categoryDtos) {
        return categoryDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        return Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .build();
    }
    @Override
    public InputStreamResource generateTemplate() {
        // Create a CSV string with header and placeholder values for a single supplier
        String csvTemplate = "name";

        // Convert the CSV string to InputStreamResource
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvTemplate.getBytes());
        return new InputStreamResource(inputStream);
    }


}
