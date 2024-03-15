package com.rose.procurement.category;

import com.rose.procurement.category.dtos.CategoryDto;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class CategoryControllerTest {
    private CategoryRepository categoryRepository;

   @BeforeEach
   void testMethod(){
       categoryRepository = new CategoryRepository() {
           @Override
           public Optional<Category> findByCategoryId(Long categoryId) {
               return Optional.empty();
           }

           @Override
           public Optional<Category> findByCategoryId(Category category) {
               return Optional.empty();
           }

           @Override
           public boolean existsByCategoryName(String categoryName) {
               return false;
           }

           @Override
           public void flush() {

           }

           @Override
           public <S extends Category> S saveAndFlush(S entity) {
               return null;
           }

           @Override
           public <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities) {
               return null;
           }

           @Override
           public void deleteAllInBatch(Iterable<Category> entities) {

           }

           @Override
           public void deleteAllByIdInBatch(Iterable<Long> longs) {

           }

           @Override
           public void deleteAllInBatch() {

           }

           @Override
           public Category getOne(Long aLong) {
               return null;
           }

           @Override
           public Category getById(Long aLong) {
               return null;
           }

           @Override
           public Category getReferenceById(Long aLong) {
               return null;
           }

           @Override
           public <S extends Category> List<S> findAll(Example<S> example) {
               return null;
           }

           @Override
           public <S extends Category> List<S> findAll(Example<S> example, Sort sort) {
               return null;
           }

           @Override
           public <S extends Category> List<S> saveAll(Iterable<S> entities) {
               return null;
           }

           @Override
           public List<Category> findAll() {
               return null;
           }

           @Override
           public List<Category> findAllById(Iterable<Long> longs) {
               return null;
           }

           @Override
           public <S extends Category> S save(S entity) {
               return null;
           }

           @Override
           public Optional<Category> findById(Long aLong) {
               return Optional.empty();
           }

           @Override
           public boolean existsById(Long aLong) {
               return false;
           }

           @Override
           public long count() {
               return 0;
           }

           @Override
           public void deleteById(Long aLong) {

           }

           @Override
           public void delete(Category entity) {

           }

           @Override
           public void deleteAllById(Iterable<? extends Long> longs) {

           }

           @Override
           public void deleteAll(Iterable<? extends Category> entities) {

           }

           @Override
           public void deleteAll() {

           }

           @Override
           public List<Category> findAll(Sort sort) {
               return null;
           }

           @Override
           public Page<Category> findAll(Pageable pageable) {
               return null;
           }

           @Override
           public <S extends Category> Optional<S> findOne(Example<S> example) {
               return Optional.empty();
           }

           @Override
           public <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable) {
               return null;
           }

           @Override
           public <S extends Category> long count(Example<S> example) {
               return 0;
           }

           @Override
           public <S extends Category> boolean exists(Example<S> example) {
               return false;
           }

           @Override
           public <S extends Category, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
               return null;
           }
       };
   }

    @Test
    void createCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName("test");
        Category category = CategoryMapper.MAPPER.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        CategoryMapper.MAPPER.toDto(savedCategory);

    }

    @Test
    void getAllCategories() {
       List<Category> categories = categoryRepository.findAll();
    }
}