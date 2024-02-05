package com.rose.procurement.supplier.repository;

import com.rose.procurement.supplier.entities.SupplierOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierOfferRepository extends JpaRepository<SupplierOffer,Long> {
}
