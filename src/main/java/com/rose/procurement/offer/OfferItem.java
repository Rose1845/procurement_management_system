package com.rose.procurement.offer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rose.procurement.delivery.Delivery;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OfferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
//    @ManyToOne
//    @JoinColumn(name = "supplier_id")
//    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    @JsonBackReference("offerItems")
    private Offer offer;
    private double offerUnitPrice;
    private double offerTotalPrice;
    @Transient
    private double getOfferTotalPrice(){
        return item.getQuantity() * offerTotalPrice;
    }
}
