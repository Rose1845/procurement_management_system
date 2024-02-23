package com.rose.procurement.delivery;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

//    public Delivery createDelivery(Long purchaseOrderId, Delivery delivery) {
//        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);
//        Delivery delivery1 = new Delivery();
//        purchaseOrder.ifPresent(delivery1::setPurchaseOrder);
//        delivery1.setDeliveryDate(delivery.getDeliveryDate());
//        delivery1.setBillNumber(delivery.getBillNumber());
//        delivery1.setReceivedBy(delivery.getReceivedBy());
//        delivery1.setExpectedOn(delivery.getExpectedOn());
//        delivery1.setInvoiceNumber(delivery.getBillNumber());
//        delivery1.setDeliveredOn(delivery.getDeliveredOn());
//        List<DeliveryItem> deliveryItems = new ArrayList<>();
//        for (Item orderItem : purchaseOrder.get().getItems()) {
//            DeliveryItem deliveryItem = new DeliveryItem();
//            deliveryItem.setItem(orderItem);
//            deliveryItem.setDelivery(delivery1);
//            deliveryItem.setQuantityReceived(deliveryItem.getQuantityReceived());
//            deliveryItems.add(deliveryItem);
//        }
//        delivery1.setDeliveryItems(deliveryItems);
//        delivery1.setReceivedOn(delivery.getReceivedOn());
//        delivery1.setDeliveredVia(delivery.getDeliveredVia());
//        delivery1.setStatus(delivery.getStatus());
//        delivery1.setTrackingNumber(delivery.getTrackingNumber());
//        return deliveryRepository.save(delivery1);
////        Delivery delivery1 = deliveryMapper.toEntity(delivery);
////        purchaseOrder.ifPresent(delivery1::setPurchaseOrder);
////        delivery1.setPurchaseOrder(purchaseOrder.get());
////        delivery1.setDeliveryDate(delivery.getDeliveryDate());
////        delivery1.setBillNumber(delivery.getBillNumber());
////        delivery1.setReceivedBy(delivery.getReceivedBy());
////        delivery1.setExpectedOn(delivery.getExpectedOn());
////        delivery1.setInvoiceNumber(delivery.getBillNumber());
////        delivery1.setDeliveredOn(delivery.getDeliveredOn());
////        delivery1.setReceivedOn(delivery.getReceivedOn());
////        delivery1.setDeliveredVia(delivery.getDeliveredVia());
////        delivery1.setStatus(delivery.getStatus());
////        delivery1.setTrackingNumber(delivery.getTrackingNumber());
////        Delivery savedDelivery = deliveryRepository.save(delivery);
////        return deliveryMapper.toDto(savedDelivery);
//    }
//    public void createDeliveryForPurchaseOrder(Long purchaseOrderId, Set<DeliveryItemDTo> itemDeliveries) {
//        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
//                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found"));
////
////        if (purchaseOrder.getDelivery() != null) {
////            throw new IllegalStateException("Delivery already exists for this PurchaseOrder");
////        }
//
//        Delivery delivery = new Delivery();
//        delivery.setPurchaseOrder(purchaseOrder);
//        /* delivery.setDeliveredDate(deliveredDate); */
//        for (DeliveryItemDTo itemDeliveryDTO : itemDeliveries) {
//            Item item = itemRepository.findById(itemDeliveryDTO.getItemId())
//                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//            // Update quantityDelivered and quantityReceived for each item in the delivery
//            item.setQuantityDelivered(itemDeliveryDTO.getQuantityDelivered());
//            item.setQuantityReceived(itemDeliveryDTO.getQuantityReceived());
//        }
//        delivery.setItems((new HashSet<>(itemDeliveries));
//        deliveryRepository.save(delivery);
//
//        // Update PurchaseOrder status and other fields
////        purchaseOrder.setDelivery(delivery);
////        purchaseOrder.setDeliveryStatus(PurchaseOrder.DeliveryStatus.IN_DELIVERY);
////        // Update other status fields as needed
//        purchaseOrderRepository.save(purchaseOrder);
//    }

    public Delivery createDelivery(Long purchaseOrderId, DeliveryDTo deliveryDTo) {
        // Retrieve PurchaseOrder
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder not found"));

        // Create Delivery
        Delivery delivery = Delivery.builder()
                .receivedBy(deliveryDTo.getReceivedBy())
                .purchaseOrder(purchaseOrder)
                .deliveredDate(deliveryDTo.getDeliveryDate())
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
