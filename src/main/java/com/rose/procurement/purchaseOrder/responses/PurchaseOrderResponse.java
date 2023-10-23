package com.rose.procurement.purchaseOrder.responses;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class PurchaseOrderResponse {
    private String purchaseOrderId;
    private String purchaseOrderTitle;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    private String paymentTerms;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "items")
    private List<Item> items;
    @OneToOne
    private Supplier supplier;
}
