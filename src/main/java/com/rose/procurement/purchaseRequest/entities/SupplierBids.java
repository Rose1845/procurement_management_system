package com.rose.procurement.purchaseRequest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupplierBids {
    @Id
    private Long id;
    private double supplierPrice;
    @ManyToOne
    private PurchaseRequest purchaseRequest;
}
