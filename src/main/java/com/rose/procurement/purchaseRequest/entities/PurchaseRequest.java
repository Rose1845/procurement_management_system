package com.rose.procurement.purchaseRequest.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "purchaseRequestId")

@EntityListeners(AuditingEntityListener.class)
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseRequestId;
    private String purchaseRequestTitle;
    private LocalDate dueDate;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "request_suppliers",
            joinColumns = {
                    @JoinColumn(name = "purchase_request_id", referencedColumnName = "purchaseRequestId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "vendor_id", referencedColumnName = "vendor_id")
            }
    )
    @JsonIgnore
    private Set<Supplier> suppliers;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PurchaseRequestItemDetail> itemDetails;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "request_items",
            joinColumns = {
                    @JoinColumn(name = "purchase_request_id", referencedColumnName = "purchaseRequestId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
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


