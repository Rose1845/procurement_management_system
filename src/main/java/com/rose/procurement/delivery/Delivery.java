package com.rose.procurement.delivery;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receivedBy;
    private String deliveredVia;
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Set<DeliveryItem> items;
    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
    private LocalDateTime deliveredOn;
    private LocalDateTime expectedOn;
    private LocalDateTime receivedOn;


}
