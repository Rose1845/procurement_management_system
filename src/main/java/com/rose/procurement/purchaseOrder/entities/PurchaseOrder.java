package com.rose.procurement.purchaseOrder.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.delivery.Delivery;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "purchase_order")
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;
    @Column(name = "purchase_order_title")
    private String purchaseOrderTitle;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    @Column(name = "terms_and_conditions")
    private String termsAndConditions;
    @Enumerated
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Enumerated
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;
    @OneToOne(mappedBy = "purchaseOrder")
    @JsonIgnore
    private Delivery delivery;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_items",
            joinColumns = {
                    @JoinColumn(name = "purchase_order_id",referencedColumnName = "purchase_order_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "item_id")
            }
    )@JsonIgnore
    private Set<Item> items;
    @OneToOne(mappedBy = "purchaseOrder")
    @JsonIgnore
    private Invoice invoice;
    private BigDecimal totalAmount;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        if (items != null && !items.isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Item item : items) {
                totalAmount = totalAmount.add(item.getTotalPrice());
            }
            return totalAmount;
        } else {
            return BigDecimal.ZERO;
        }
    }
}










