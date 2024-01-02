package com.rose.procurement.purchaseRequisition.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requisitionId;
    @NotNull
    @NotBlank
    private String requisitionTitle;
    @NotNull
    @NotBlank
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate dateNeeded;
    @CreationTimestamp
    private Timestamp dateCreated;
    @Enumerated
    private ApprovalStatus approvalStatus;
    @ManyToMany(fetch = FetchType.LAZY)
            @JoinTable(
                  name = "requisition_items",
                    joinColumns = {
                          @JoinColumn(name = "purchase_requisition_id",referencedColumnName = "requisitionId")
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

//    @Column(name = "created_by")
//    private String createdBy;
}
