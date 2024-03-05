package com.rose.procurement.purchaseRequest.repository;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRequestItemDetailRepository extends JpaRepository<PurchaseRequestItemDetail,Long> {
    List<PurchaseRequestItemDetail> findBySupplier_VendorId(String supplierId);
}
