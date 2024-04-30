package com.rose.procurement.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE','APPROVER'})")
    public Delivery getDeliveryById(@PathVariable("id") Long id) {
        return deliveryService.getDeliveryById(id);
    }
    @GetMapping("/order-delivery/{id}")
    public Delivery getDeliveryByOrderId(@PathVariable("id") Long purchaseOrderId) {
        return deliveryService.getDeliveryByOrderId(purchaseOrderId);
    }

    @PostMapping("/{purchaseOrderId}/deliveries")
@PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
public Delivery createDeliveryForPurchaseOrder(
        @PathVariable Long purchaseOrderId,
        @RequestBody DeliveryDTo deliveryDTo
) {
  return deliveryService.createDelivery(purchaseOrderId,deliveryDTo);
}
}
