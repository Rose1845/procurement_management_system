package com.rose.procurement.purchaseRequest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.offer.Offer;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseRequestId;
    private String purchaseRequestTitle;
    private LocalDate dueDate;
    private String termsAndConditions;
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name = "request_suppliers",
            joinColumns = {
                    @JoinColumn(name = "purchase_request_id",referencedColumnName = "purchaseRequestId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "vendor_id",referencedColumnName = "vendorId")
            }
    )
    @JsonIgnore
    private Set<Supplier> suppliers;
    @OneToOne(mappedBy = "purchaseRequest")
    @JsonIgnore
    private Offer offer;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name = "request_items",
            joinColumns = {
                    @JoinColumn(name = "purchase_request_id",referencedColumnName = "purchaseRequestId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "itemId")
            }
    )
    @JsonIgnore
    private Set<Item> items;
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


