package com.rose.procurement.supplier.services;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.supplier.entities.SupplierOffer;
import com.rose.procurement.supplier.repository.SupplierOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierOfferService {
    private final SupplierOfferRepository supplierOfferRepository;
    private final ItemRepository itemRepository;
    public SupplierOffer submitSupplierOffer(SupplierOffer supplierOffer) {
        // Save the supplier offer to the database
        return supplierOfferRepository.save(supplierOffer);
    }

    public SupplierOffer addSupplierOffer(String itemId, SupplierOffer supplierOffer) {
        // Retrieve the purchase request item by ID
        Item purchaseRequestItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Purchase request item not found"));

        // Set the relationship and save the supplier offer
        supplierOffer.setItem(purchaseRequestItem);
        purchaseRequestItem.getSupplierOffers().add(supplierOffer);

        return supplierOfferRepository.save(supplierOffer);
    }
}
