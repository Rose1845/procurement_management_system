package com.rose.procurement.offer;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OfferItemService {
    private final OfferItemRepository offerItemRepository;
    private final SupplierRepository supplierRepository;
    private final OfferRepository offerRepository;

    public OfferItemService(OfferItemRepository offerItemRepository,
                            SupplierRepository supplierRepository,
                            OfferRepository offerRepository) {
        this.offerItemRepository = offerItemRepository;
        this.supplierRepository = supplierRepository;
        this.offerRepository = offerRepository;
    }

    public List<OfferItem> findOfferItemsByOffer(Long offerId) {
        return offerItemRepository.findByOfferId(offerId);
    }
}
