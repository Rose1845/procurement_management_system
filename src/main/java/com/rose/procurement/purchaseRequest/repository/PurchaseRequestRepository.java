package com.rose.procurement.purchaseRequest.repository;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

    Optional<PurchaseRequest> findByPurchaseRequestIdAndSuppliers_VendorId(Long purchaseRequestId, String vendorId);

    @Query("SELECT pr FROM PurchaseRequest pr JOIN pr.suppliers s WHERE s.vendorId = :vendorId")
    List<PurchaseRequest> findAllBySupplierId(@Param("vendorId") Long vendorId);//
//    List<PurchaseRequest> findBySupplier(Long supplierId);
}
