//package com.rose.procurement.purchaseRequest.services;
//
//import com.rose.procurement.delivery.DeliveryItem;
//import com.rose.procurement.items.entity.Item;
//import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
//import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItem;
//import com.rose.procurement.purchaseRequest.repository.PurchaseRequestItemRepository;
//import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PurchaseRequestItemService {
//    private final PurchaseRequestItemRepository purchaseRequestItemRepository;
//    private final PurchaseRequestRepository purchaseRequestRepository;
////    public PurchaseRequestItem createRequestItem(Long purchaseRequestId PurchaseRequestItem purca) {
////        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
////        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
////        List<PurchaseRequestItem> purchaseRequestItems = new ArrayList<>();
////
////        for (Item orderItem : purchaseRequest.get().getItems()) {
////            PurchaseRequestItem purchaseRequestItem1 = new PurchaseRequestItem();
////            purchaseRequestItem1.setItem(orderItem);
////            purchaseRequestItem1.getPurchaseRequest(purchaseRequest);
////            purchaseRequestItems.add(purchaseRequestItem1);
////        }
////        return purchaseRequestItemRepository.save(purchaseRequestItem);
////    }
//
//    public PurchaseRequestItem addPurchaseRequestItem(Long purchaseRequestId, Item purchaseRequestItem) {
//        // Retrieve the purchase request by ID
//        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId)
//                .orElseThrow(() -> new RuntimeException("Purchase request not found"));
//
//        // Set the relationship and save the purchase request item
//        purchaseRequestItem.setPurchaseRequest(purchaseRequest);
//        purchaseRequest.getItems().add(purchaseRequestItem);
//
//        return purchaseRequestItemRepository.save(purchaseRequestItem);
//    }
//}
