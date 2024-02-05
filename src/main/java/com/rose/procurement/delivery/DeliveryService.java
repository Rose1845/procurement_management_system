package com.rose.procurement.delivery;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final PurchaseOrderMapper  purchaseOrderMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, PurchaseOrderService purchaseOrderService, PurchaseOrderMapper purchaseOrderMapper, PurchaseOrderRepository purchaseOrderRepository) {
        this.deliveryRepository = deliveryRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    public Delivery createDelivery(Long purchaseOrderId, Delivery delivery) {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);
        Delivery delivery1 = new Delivery();
        purchaseOrder.ifPresent(delivery1::setPurchaseOrder);
        delivery1.setDeliveryDate(delivery.getDeliveryDate());
        delivery1.setBillNumber(delivery.getBillNumber());
        delivery1.setReceivedBy(delivery.getReceivedBy());
        delivery1.setExpectedOn(delivery.getExpectedOn());
        delivery1.setInvoiceNumber(delivery.getBillNumber());
        delivery1.setDeliveredOn(delivery.getDeliveredOn());
        List<DeliveryItem> deliveryItems = new ArrayList<>();
        for (Item orderItem : purchaseOrder.get().getItems()) {
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setItem(orderItem);
            deliveryItem.setDelivery(delivery1);
            deliveryItem.setQuantityReceived(deliveryItem.getQuantityReceived());
            deliveryItems.add(deliveryItem);
        }
        delivery1.setDeliveryItems(deliveryItems);
        delivery1.setReceivedOn(delivery.getReceivedOn());
        delivery1.setDeliveredVia(delivery.getDeliveredVia());
        delivery1.setStatus(delivery.getStatus());
        delivery1.setTrackingNumber(delivery.getTrackingNumber());
        return deliveryRepository.save(delivery1);
//        Delivery delivery1 = deliveryMapper.toEntity(delivery);
//        purchaseOrder.ifPresent(delivery1::setPurchaseOrder);
//        delivery1.setPurchaseOrder(purchaseOrder.get());
//        delivery1.setDeliveryDate(delivery.getDeliveryDate());
//        delivery1.setBillNumber(delivery.getBillNumber());
//        delivery1.setReceivedBy(delivery.getReceivedBy());
//        delivery1.setExpectedOn(delivery.getExpectedOn());
//        delivery1.setInvoiceNumber(delivery.getBillNumber());
//        delivery1.setDeliveredOn(delivery.getDeliveredOn());
//        delivery1.setReceivedOn(delivery.getReceivedOn());
//        delivery1.setDeliveredVia(delivery.getDeliveredVia());
//        delivery1.setStatus(delivery.getStatus());
//        delivery1.setTrackingNumber(delivery.getTrackingNumber());
//        Delivery savedDelivery = deliveryRepository.save(delivery);
//        return deliveryMapper.toDto(savedDelivery);
    }
}
