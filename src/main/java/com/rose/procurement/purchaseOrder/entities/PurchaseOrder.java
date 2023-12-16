package com.rose.procurement.purchaseOrder.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseOrderId;
    private String purchaseOrderTitle;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    @Enumerated
    private PaymentType paymentType;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_items",
            joinColumns = {
                    @JoinColumn(name = "purchase_order_id",referencedColumnName = "purchaseOrderId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "itemId")
            }
    )@JsonIgnore
    private Set<Item> items;
    @OneToOne(mappedBy = "purchaseOrder")
    @JsonIgnore
    private Invoice invoice;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;
}
