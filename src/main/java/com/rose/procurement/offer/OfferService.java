package com.rose.procurement.offer;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;



    @Service
    public class OfferService {

        private final ItemRepository itemRepository;
        private final PurchaseRequestRepository purchaseRequestRepository;
        private final OfferRepository offerRepository;
        private final OfferItemRepository offerItemRepository;
        private final SupplierRepository supplierRepository;

        @Autowired
        public OfferService(ItemRepository itemRepository, PurchaseRequestRepository purchaseRequestRepository, OfferRepository offerRepository, OfferItemRepository offerItemRepository, SupplierRepository supplierRepository) {
            this.itemRepository = itemRepository;
            this.purchaseRequestRepository = purchaseRequestRepository;
            this.offerRepository = offerRepository;
            this.offerItemRepository = offerItemRepository;
            this.supplierRepository = supplierRepository;
        }
        @Transactional
        public Offer createOfferForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiOfferDto) {
            Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
            if (purchaseRequestOptional.isEmpty()) {
                throw new EntityNotFoundException("Purchase request not found");
            }
            PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
            // Create a single offer for the entire purchase request
            Offer singleOffer = Offer.builder()
                    .purchaseRequest(purchaseRequest)
                    .build();
            Set<OfferItem> offerItems = new HashSet<>();
            for(Supplier supplier : purchaseRequest.getSuppliers()){
                for (OfferDto offerDto : multiOfferDto.getOfferDtoSet()) {
                    for (OfferItemDto itemDto : offerDto.getItemDtoSet()) {
                        for(Item item: purchaseRequest.getItems()){
                            OfferItem offerItem = OfferItem.builder()
                                    .item(item)
                                    .offer(singleOffer)
                                    .supplier(supplier)  // Set the supplier for each offer item
                                    .offerUnitPrice(itemDto.getOfferUnitPrice())
                                    .offerTotalPrice(itemDto.getOfferUnitPrice())  // You may want to adjust this calculation
                                    .build();
                            offerItems.add(offerItem);
                        }
                    }
                }
            }

            // Set all the items to the single offer
            singleOffer.setOfferItems(offerItems);
            return offerRepository.save(singleOffer);
        }
        public Optional<Offer> getOffer(Long id){
            return offerRepository.findById(id);
        }


    }

