//package com.rose.procurement.purchaseRequest.controller;
//
//import com.rose.procurement.items.entity.Item;
//import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItem;
//import com.rose.procurement.purchaseRequest.services.PurchaseRequestItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/request-item")
//@RequiredArgsConstructor
//public class PurchaseRequestItemController {
//    private final PurchaseRequestItemService purchaseRequestItemService;
//    @PostMapping("/{purchaseRequestId}")
//    public Item addPurchaseRequestItem(
//            @PathVariable Long purchaseRequestId,
//            @RequestBody Item purchaseRequestItem) {
//        return purchaseRequestItemService.addPurchaseRequestItem(purchaseRequestId, purchaseRequestItem);
//    }
//}
