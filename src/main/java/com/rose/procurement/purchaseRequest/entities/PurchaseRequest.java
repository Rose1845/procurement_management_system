package com.rose.procurement.purchaseRequest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "_purchase_request")
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseRequestId;
    private String purchaseRequestTitle;
    private LocalDate dueDate;
    private String termsAndConditions;
    @OneToOne(mappedBy = "purchaseRequest")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;
}
