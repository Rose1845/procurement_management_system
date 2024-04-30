package com.rose.procurement.contract.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.enums.ContractStatus;
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
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    @Lob
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    @Enumerated
    private ContractStatus contractStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contract_items",
            joinColumns = {
                    @JoinColumn(name = "contract_id",referencedColumnName = "contractId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "item_id")
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
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;
    @Transient
    @JsonIgnore
    private static final ContractStatus EXPIRED_STATUS = ContractStatus.EXPIRED;


    @PrePersist
    private void prePersist() {
        // Set contractStatus before persisting the entity
        if (contractStatus == null) {
            if (checkContractEndDateExpired()) {
                contractStatus = ContractStatus.EXPIRED;
            }
        }
    }


    public boolean checkContractEndDateExpired() {
        // Check if the contract end date is in the past
        return contractEndDate != null && contractEndDate.isBefore(LocalDate.now());
    }
}






