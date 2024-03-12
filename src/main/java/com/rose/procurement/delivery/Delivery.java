package com.rose.procurement.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.items.entity.Item;
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
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Set<DeliveryItem> items;
    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
    private LocalDateTime deliveredDate;
    private LocalDateTime receivedDate;
    private LocalDateTime expectedOn;
    private LocalDateTime receivedOn;


}
