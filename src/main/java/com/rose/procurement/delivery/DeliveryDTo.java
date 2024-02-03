package com.rose.procurement.delivery;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTo {
    private Long purchaseOrderId;
    private LocalDateTime deliveryDate;
    private String receivedBy;
    private String status;
    private String trackingNumber;
    private String deliveredVia;
    private String billNumber;
    private String invoiceNumber;
    private String address;
    private LocalDateTime deliveredOn;
    private LocalDateTime expectedOn;
    private LocalDateTime receivedOn;
}
