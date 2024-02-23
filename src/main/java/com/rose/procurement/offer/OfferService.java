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

//@Service
//public class OfferService {
//
//    private final PurchaseRequestRepository purchaseRequestRepository;
//    private final OfferRepository offerRepository;
//
//    @Autowired
//    public OfferService(PurchaseRequestRepository purchaseRequestRepository, OfferRepository offerRepository) {
//        this.purchaseRequestRepository = purchaseRequestRepository;
//        this.offerRepository = offerRepository;
//    }

//    public Offer createOfferForPurchaseRequest(Long purchaseRequestId, OfferItemDto offerItemDto) {
//        // Retrieve the purchase request
//        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
//
//        // Create Offer
//        Offer offer = new Offer();
//        offer.setPurchaseRequest(purchaseRequest.get());
////        offerRepository.save(offer);
//
//        // Create Offer Items
//        Set<OfferItem> offerItems = new HashSet<>();
//        for (Item item : purchaseRequest.get().getItems()) {
//            OfferItem offerItem = new OfferItem();
//            offerItem.setItem(item);
//            offerItem.setOffer(offer);
//            offerItem.setOfferUnitPrice(offerItemDto.getOfferUnitPrice());
//            offerItems.add(offerItem);
//        }
//        // Set Offer Items in Offer
//        offer.setItems(offerItems);
//        // Save Offer and Offer Items
//        offerRepository.save(offer);
//
//        return offer;
//    }

    @Service
    public class OfferService {

        private final ItemRepository itemRepository;
        private final PurchaseRequestRepository purchaseRequestRepository;
        private final OfferRepository offerRepository;
        private final SupplierRepository supplierRepository;

        @Autowired
        public OfferService(ItemRepository itemRepository, PurchaseRequestRepository purchaseRequestRepository, OfferRepository offerRepository, SupplierRepository supplierRepository) {
            this.itemRepository = itemRepository;
            this.purchaseRequestRepository = purchaseRequestRepository;
            this.offerRepository = offerRepository;
            this.supplierRepository = supplierRepository;
        }
        @Transactional
        public Offer createOffersForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiOfferDto) {
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

            for (OfferDto offerDto : multiOfferDto.getOfferDtoSet()) {
                Supplier supplier = supplierRepository.findById(offerDto.getSupplierId())
                        .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

                for (OfferItemDto itemDto : offerDto.getItemDtoSet()) {
                    Item item = itemRepository.findById(itemDto.getItemId())
                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

                    OfferItem offerItem = OfferItem.builder()
                            .item(item)
                            .offer(singleOffer)
                            .offerUnitPrice(itemDto.getOfferUnitPrice())
                            .offerTotalPrice(itemDto.getOfferUnitPrice())  // You may want to adjust this calculation
                            .build();
                    offerItems.add(offerItem);
                }

                // Set the common supplier for the entire offer
                singleOffer.setSupplier(supplier);
            }

            // Set all the items to the single offer
            singleOffer.setItems(offerItems);

            // Save the single offer for the entire purchase request
            return offerRepository.save(singleOffer);
        }
//        public Offer createOffersForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiOfferDto) {
//            Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
//            if (purchaseRequestOptional.isEmpty()) {
//                throw new EntityNotFoundException("Purchase request not found");
//            }
//            PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//            Offer newOffer = new Offer();
//
//            Set<OfferItem> offerItems = new HashSet<>();
//
//            for (OfferDto offerDto : multiOfferDto.getOfferDtoSet()) {
//                Supplier supplier = supplierRepository.findById(offerDto.getSupplierId())
//                        .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
//
//                for (OfferItemDto item : offerDto.getItemDtoSet()) {
//                    Item item1 = itemRepository.findById(item.getItemId())
//                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//                    OfferItem offerItem = new OfferItem();
//                    offerItem.setItem(item1);
//                    offerItem.setOffer(newOffer);
//                    offerItem.setOfferUnitPrice(item.getOfferUnitPrice());
//                    offerItems.add(offerItem);
//                }
//                newOffer.setSupplier(supplier);
//            }
//
//            // Create a new offer for the entire purchase request
//            newOffer.setPurchaseRequest(purchaseRequest);
//            newOffer.setItems(offerItems);
//
//            // Save the single offer for the entire purchase request
//            return offerRepository.save(newOffer);
//        }
        public Optional<Offer> getOffer(Long id){
            return offerRepository.findById(id);
        }


//        public Offer createOffersForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiSupplierOfferDto) {
//            // Retrieve the purchase request
//            Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
//            if (purchaseRequestOptional.isEmpty()) {
//                throw new EntityNotFoundException("Purchase request not found");
//            }
//            PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//
//            Set<Offer> offers = new HashSet<>();
//
//            for (OfferDto supplierOfferDto : multiSupplierOfferDto.getOfferDtoSet()) {
//                Supplier supplier = supplierRepository.findById(supplierOfferDto.getSupplierId())
//                        .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
//
//                // Check if an offer already exists for the same purchase request and supplier
//                Optional<Offer> existingOffer = offerRepository.findByPurchaseRequestAndSupplier(purchaseRequest, supplier);
//
//                Offer offerToUpdate;
//
//                if (existingOffer.isPresent()) {
//                    // If an offer already exists, update it
//                    offerToUpdate = existingOffer.get();
//                    offerToUpdate.getItems().clear(); // Clear existing offer items for update
//                } else {
//                    // If no offer exists, create a new one
//                    offerToUpdate = new Offer();
//                    offerToUpdate.setPurchaseRequest(purchaseRequest);
//                    offerToUpdate.setSupplier(supplier);
//                }
//
//                Set<OfferItem> offerItems = new HashSet<>();
//
//                for (OfferItemDto item : supplierOfferDto.getItemDtoSet()) {
//                    Item item1 = itemRepository.findById(item.getItemId())
//                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//
//                    OfferItem offerItem = new OfferItem();
//                    offerItem.setItem(item1);
//                    offerItem.setOffer(offerToUpdate);
//                    offerItem.setOfferUnitPrice(item.getOfferUnitPrice());
//                    offerItems.add(offerItem);
//                }
//
//                // Set Offer Items in Offer
//                offerToUpdate.setItems(offerItems);
//                offers.add(offerToUpdate);
//            }
//
//            return offerRepository.saveAll(offers).iterator().next();
//        }

//        public Offer createOffersForPurchaseRequest(Long purchaseRequestId, OfferDto offerDto) {
//            Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
//            if (purchaseRequestOptional.isEmpty()) {
//                throw new EntityNotFoundException("Purchase request not found");
//            }
//            PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//
//            Set<Offer> offers = new HashSet<>();
//
//            for (Supplier supplier : purchaseRequest.getSuppliers()) {
//                Offer newOffer = new Offer();
//                newOffer.setPurchaseRequest(purchaseRequest);
//                newOffer.setSupplier(supplier);
//
//                Set<OfferItem> offerItems = new HashSet<>();
//
//                for (OfferItemDto item : offerDto.getItemDtoSet()) {
//                    Item item1 = itemRepository.findById(item.getItemId())
//                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//                    OfferItem offerItem = new OfferItem();
//                    offerItem.setItem(item1);
//                    offerItem.setOffer(newOffer);
//                    offerItem.setOfferUnitPrice(item.getOfferUnitPrice());
//                    offerItems.add(offerItem);
//                }
//
//                newOffer.setItems(offerItems);
//                offers.add(newOffer);
//            }
//
//            return offerRepository.saveAll(offers).iterator().next();
//        }


//        public Offer createOffersForPurchaseRequest(Long purchaseRequestId, OfferDto offerItemDto) {
//            // Retrieve the purchase request
//            Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
////            if (purchaseRequestOptional.isEmpty()) {
////                // Handle case where the purchase request is not found
////                // You may throw an exception or return an appropriate response
////                throw new EntityNotFoundException("Purchase request not found");
////            }
//            PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//            // Create Offer for each Supplier
//            Offer offer = null;
//            for (Supplier supplier : purchaseRequest.getSuppliers()) {
//                // Create Offer
//                offer = new Offer();
//                offer.setPurchaseRequest(purchaseRequest);
//                offer.setSupplier(supplier);
//                // Save the Offer to generate an ID
////                offer = offerRepository.save(offer);
//
//                // Create Offer Items
//                Set<OfferItem> offerItems = new HashSet<>();
//                for (OfferItemDto item : offerItemDto.getItemDtoSet()) {
//                    Item item1 = itemRepository.findById(item.getItemId())
//                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//                    OfferItem offerItem = new OfferItem();
//                    offerItem.setItem(item1);
////                    offerItem.setSupplier(supplier);
//                    offerItem.setOffer(offer);
//                    offerItem.setOfferUnitPrice(item.getOfferUnitPrice());
//                    offerItems.add(offerItem);
//                }
//
//
//                // Set Offer Items in Offer
//                offer.setItems(offerItems);
//                // Save Offer with Offer Items
//            }
//            assert offer != null;
//            return offerRepository.save(offer);
//
//        }

    }

//}
