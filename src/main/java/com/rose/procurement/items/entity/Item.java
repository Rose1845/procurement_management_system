package com.rose.procurement.items.entity;

import com.fasterxml.jackson.annotation.*;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.delivery.Delivery;
import com.rose.procurement.document.File;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierOffer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemId;
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    @Min(1)
    private int quantity=1;
    private double unitPrice;
    private double totalPrice;
    @ManyToMany(mappedBy = "items",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Contract> contracts;
    @ManyToMany(mappedBy = "items",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private Category category;
    @ManyToOne
    @JoinColumn(name = "supplierId")
    @JsonIgnore
    private Supplier supplier;
    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private Set<PurchaseRequest> purchaseRequests;
    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private Set<PurchaseRequisition> purchaseRequisitions;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @ManyToMany(mappedBy = "items", cascade = CascadeType.ALL)
    @JsonIgnore

    private Set<SupplierOffer> supplierOffers;
    @ManyToMany(mappedBy = "items", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Delivery> deliveries = new HashSet<>();
    @Column(nullable = true, columnDefinition = "int default 0")
    private int quantityDelivered =0 ;
    @Column(nullable = true, columnDefinition = "int default 0")
    private int quantityReceived = 0;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private Integer createdBy;

    @Transient // This annotation indicates that the field should not be persisted in the database
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getTotalPrice() {
        return quantity * unitPrice;
    }


}
