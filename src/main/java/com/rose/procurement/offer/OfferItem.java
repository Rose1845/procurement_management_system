package com.rose.procurement.offer;

import com.fasterxml.jackson.annotation.*;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class OfferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Supplier supplier;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;
    @Column(columnDefinition = "double default 0.0") // Set default value for offerUnitPrice
    private double offerUnitPrice;
    private double offerTotalPrice;
    @Transient
    private double getOfferTotalPrice(){
        return item.getQuantity() * offerTotalPrice;
    }
}
