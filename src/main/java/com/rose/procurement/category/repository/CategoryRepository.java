package com.rose.procurement.category.repository;


import com.rose.procurement.category.entity.Category;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

   Optional<Category>  findByCategoryId(Long categoryId);

   Optional<Category> findByCategoryId(Category category);

    boolean existsByCategoryName(String categoryName);

    Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);

    Optional<Category> findByCategoryName(String categoryName);
}
