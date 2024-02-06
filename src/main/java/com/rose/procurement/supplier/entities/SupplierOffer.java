package com.rose.procurement.supplier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class SupplierOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "offer_suplliers",
            joinColumns = {
                    @JoinColumn(name = "offer_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "supplier_id",referencedColumnName = "vendorId")
            }
    )
    @JsonIgnore
    private Set<Supplier> suppliers;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private BigDecimal unitPrice;

    public SupplierOffer(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
}
