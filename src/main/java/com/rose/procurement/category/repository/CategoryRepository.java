package com.rose.procurement.category.repository;


import com.rose.procurement.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

   Optional<Category>  findByCategoryId(Long categoryId);

   Optional<Category> findByCategoryId(Category category);
}
