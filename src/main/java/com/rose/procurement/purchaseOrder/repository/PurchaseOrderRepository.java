package com.rose.procurement.purchaseOrder.repository;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {
}
