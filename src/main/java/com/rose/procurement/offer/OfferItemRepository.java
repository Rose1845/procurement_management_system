package com.rose.procurement.offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {
}
