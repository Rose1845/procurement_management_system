package com.rose.procurement.purchaseRequest.repository;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest,Long> {

    Optional<PurchaseRequest> findByPurchaseRequestIdAndSuppliers_VendorId(Long purchaseRequestId, String vendorId);
//
//    List<PurchaseRequest> findBySupplier(Long supplierId);
}
