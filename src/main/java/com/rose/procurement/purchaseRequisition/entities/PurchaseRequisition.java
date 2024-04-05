package com.rose.procurement.purchaseRequisition.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class PurchaseRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requisitionId;
    private String requisitionTitle;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNeeded;
    @CreationTimestamp
    private Timestamp dateCreated;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "requisition_items",
            joinColumns = {
                    @JoinColumn(name = "purchase_requisition_id", referencedColumnName = "requisitionId")
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
