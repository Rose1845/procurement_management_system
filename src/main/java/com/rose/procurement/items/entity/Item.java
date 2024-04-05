package com.rose.procurement.items.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.delivery.DeliveryItem;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequisition.entities.PurchaseRequisition;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "item")
@EntityListeners(AuditingEntityListener.class)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "item_description")
    private String itemDescription;
    @Min(value = 1, message = "minimum quantity required is 1")
    private int quantity = 1;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Contract> contracts;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PurchaseRequestItemDetail> itemDetails;
    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private Category category;
    //    @ManyToOne
//    @JoinColumn(name = "supplierId")
//    @JsonIgnore
//    private Supplier supplier;
    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private Set<PurchaseRequest> purchaseRequests;
    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private Set<PurchaseRequisition> purchaseRequisitions;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    //    @ManyToMany(mappedBy = "items", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private Set<SupplierOffer> supplierOffers;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<DeliveryItem> deliveriesItems;
    //    @Column(nullable = true, columnDefinition = "int default 0")
//    private int quantityDelivered =0 ;
//    @Column(nullable = true, columnDefinition = "int default 0")
//    private int quantityReceived = 0;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(
            nullable = false,
            updatable = false,
            name = "created_by"
    )
    private Integer createdBy;
//    @Transient // This annotation indicates that the field should not be persisted in the database
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    public double getTotalPrice() {
//        return quantity * unitPrice;
//
//    }
//    @Transient // This annotation indicates that the field should not be persisted in the database
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    public BigDecimal getTotalPrice() {
//        return BigDecimal.valueOf(quantity).multiply(BigDecimal.valueOf(unitPrice));
//    }

}
