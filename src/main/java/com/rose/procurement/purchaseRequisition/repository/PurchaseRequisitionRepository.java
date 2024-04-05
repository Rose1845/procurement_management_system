package com.rose.procurement.purchaseRequisition.repository;

import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, Long> {
    PurchaseRequisition findByRequisitionId(Long requisitionId);
}
