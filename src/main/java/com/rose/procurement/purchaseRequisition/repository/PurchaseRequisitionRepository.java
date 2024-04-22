package com.rose.procurement.purchaseRequisition.repository;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, Long> {
    PurchaseRequisition findByRequisitionId(Long requisitionId);
    @Query("SELECT p FROM PurchaseRequisition p LEFT JOIN  p.items   WHERE p.requisitionId = :requisitionId")
    Optional<PurchaseRequisition> findByIdWithItems(@Param("requisitionId") Long requisitionId);
}


