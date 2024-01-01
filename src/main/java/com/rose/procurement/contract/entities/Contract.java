package com.rose.procurement.contract.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@EntityListeners(AuditingEntityListener.class)
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String contractId;
    private String contractTitle;
    private String contractType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractEndDate;
    private String termsAndConditions;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "contract_items",
            joinColumns = {
                    @JoinColumn(name = "contract_id",referencedColumnName = "contractId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "itemId")
            }
    )@JsonIgnore
    private Set<Item> items;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
//    @CreatedBy
//    @Column(name = "created_by")
//    private Integer createdBy;

}
