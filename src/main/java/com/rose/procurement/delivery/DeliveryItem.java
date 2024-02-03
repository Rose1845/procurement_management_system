package com.rose.procurement.delivery;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_items")
public class DeliveryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    @JsonBackReference
    private Delivery delivery;
    @ManyToOne
    @JoinColumn(name = "item_delivered_id")
    private Item item;
    private BigDecimal quantityReceived;

    // ... (other fields related to delivery items)

    // ... (getters and setters)
}