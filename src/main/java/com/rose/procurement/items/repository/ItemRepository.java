package com.rose.procurement.items.repository;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,String> {
    @Query("SELECT p FROM Item p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.supplier WHERE p.itemId = :itemId")
    Optional<Item> findItemDetailsById(@Param("itemId") String itemId);

}
