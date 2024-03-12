package com.rose.procurement.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/{id}")
    public Delivery getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

//    @PostMapping("/add/{purchaseOrderId}")
//    public Delivery createDelivery(@PathVariable Long purchaseOrderId, @RequestBody Delivery delivery) {
//        return deliveryService.createDelivery(purchaseOrderId, delivery);
//    }

//    @PostMapping("/{purchaseOrderId}/deliveries")
//    public ResponseEntity<String> createDeliveryForPurchaseOrder(
//            @PathVariable Long purchaseOrderId,
//            @RequestBody DeliveryItemDTo itemDeliveries
//    ) {
//        try {
//            deliveryService.createDeliveryForPurchaseOrder(purchaseOrderId, itemDeliveries);
//            return ResponseEntity.ok("Delivery created successfully");
//        } catch (EntityNotFoundException | IllegalStateException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
@PostMapping("/{purchaseOrderId}/deliveries")
public Delivery createDeliveryForPurchaseOrder(
        @PathVariable Long purchaseOrderId,
        @RequestBody DeliveryDTo deliveryDTo
) {
  return deliveryService.createDelivery(purchaseOrderId,deliveryDTo);
}
}
