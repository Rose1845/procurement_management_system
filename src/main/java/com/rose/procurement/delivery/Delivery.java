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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "delivery_items",
            joinColumns = {
                    @JoinColumn(name = "delivery_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "itemId")
            }
    )@JsonIgnore
  private Set<Item> items;

    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
    private LocalDateTime deliveredDate;
    private LocalDateTime receivedDate;
    private LocalDateTime expectedOn;
    private LocalDateTime receivedOn;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "purchase_order_id")
//    private PurchaseOrder purchaseOrder;
//    private LocalDateTime deliveryDate;
//    @OneToMany(mappedBy = "delivery", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<DeliveryItem> deliveryItems;
//    private String receivedBy;
//    private String status;
//    private String trackingNumber;
//    private String deliveredVia;
//    private String billNumber;
//    private String invoiceNumber;
//    private String address;
//    private LocalDateTime deliveredOn;
//    private LocalDateTime expectedOn;
//    private LocalDateTime receivedOn;
//
//    // Getters and setters
//    public Delivery(PurchaseOrder purchaseOrder) {
//        this.purchaseOrder = purchaseOrder;
//        // Set other default values or initialize other fields as needed
//    }
}
