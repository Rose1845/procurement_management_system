package com.rose.procurement.items.entity;

import com.fasterxml.jackson.annotation.*;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.document.File;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemId;
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    @Min(1)
    private int quantity;
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
    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<PurchaseRequest> purchaseRequests;
    @ManyToMany(mappedBy = "items",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseRequisition> purchaseRequisitions;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Transient // This annotation indicates that the field should not be persisted in the database
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}
