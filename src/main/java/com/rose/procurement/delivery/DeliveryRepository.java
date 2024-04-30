package com.rose.procurement.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findDeliveriesByPurchaseOrder_PurchaseOrderId(Long purchaseOrderId);
}
