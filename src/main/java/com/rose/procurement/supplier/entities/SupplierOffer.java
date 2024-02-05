package com.rose.procurement.supplier.entities;

import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private BigDecimal unitPrice;

    public SupplierOffer(Supplier supplier) {
        this.supplier = supplier;
    }
}
