package com.rose.procurement.delivery;

import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, PurchaseOrderService purchaseOrderService, ItemRepository itemRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.deliveryRepository = deliveryRepository;
        this.itemRepository = itemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    public Delivery getDeliveryByOrderId(Long purchaseOrderId) {
        return deliveryRepository.findDeliveriesByPurchaseOrder_PurchaseOrderId(purchaseOrderId).orElse(null);
    }

    public Delivery createDelivery(Long purchaseOrderId, DeliveryDTo deliveryDTo) {
        // Retrieve PurchaseOrder
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found"));

        // Create Delivery
        Delivery delivery = Delivery.builder()
                .receivedBy(deliveryDTo.getReceivedBy())
                .purchaseOrder(purchaseOrder)
                .deliveredOn(deliveryDTo.getDeliveryDate())
                .deliveredVia(deliveryDTo.getDeliveredVia())
                .receivedOn(deliveryDTo.getReceivedOn())
                .expectedOn(deliveryDTo.getExpectedOn())
                .build();


        Set<DeliveryItem> deliveryItems = new HashSet<>();
        for (DeliveryItemDTo itemDto : deliveryDTo.getItemDToSet()) {
            Item item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setDelivery(delivery);
            deliveryItem.setItem(item);
            deliveryItem.setQuantityDelivered(itemDto.getQuantityDelivered());
            deliveryItem.setQuantityReceived(itemDto.getQuantityReceived());
            deliveryItems.add(deliveryItem);
        }

        delivery.setItems(deliveryItems);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        // Update PurchaseOrder status and other fields
        purchaseOrder.setDelivery(savedDelivery);
        purchaseOrder.setApprovalStatus(ApprovalStatus.IN_DELIVERY);
        purchaseOrderRepository.save(purchaseOrder);
        return savedDelivery;
    }

}
