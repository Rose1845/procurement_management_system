package com.rose.procurement.supplier.services;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierOffer;
import com.rose.procurement.supplier.repository.SupplierOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierOfferService {
    private final SupplierOfferRepository supplierOfferRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    public SupplierOffer submitSupplierOffer(SupplierOffer supplierOffer) {
        // Save the supplier offer to the database
        return supplierOfferRepository.save(supplierOffer);
    }

    public SupplierOffer addSupplierOffer(Long purchaseRequestId, SupplierOffer supplierOffer) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
    for(Item item : purchaseRequest.get().getItems()){
        supplierOffer.setItem(item);
    }
     SupplierOffer supplierOffer1 = new SupplierOffer();
     supplierOffer1.setUnitPrice(supplierOffer.getUnitPrice());
     supplierOffer.setSuppliers(purchaseRequest.get().getSuppliers());
        return supplierOfferRepository.save(supplierOffer);
    }
}
