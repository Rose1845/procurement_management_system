package com.rose.procurement.offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferSupplierRepository extends JpaRepository<OfferSupplier,Long> {
}
