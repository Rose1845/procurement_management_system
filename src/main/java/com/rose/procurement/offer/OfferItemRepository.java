package com.rose.procurement.offer;

import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {
    List<OfferItem> findByOfferId(Long offer_id);
//    OfferItem findBySupplier(Optional<Supplier> supplier);
}
