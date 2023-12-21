package com.rose.procurement.purchaseOrder.repository;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    @Query(value = "SELECT * FROM supplier s INNER JOIN purchase_order pos " +
            "ON s.vendor_id = pos.vendor_id WHERE pos.purchase_order_id = :purchaseOrderId",
            nativeQuery = true)
    Optional<Supplier> findSuppliersByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    Optional<PurchaseOrder> findByPurchaseOrderId(Long purchaseOrderId);

    Set<Item> findItemsByPurchaseOrderId(Long purchaseOrderId);

}
