package com.rose.procurement.purchaseRequisition.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requisitionId;
    private Long userId;
    private String itemDescription;
    private int quantity;
    private String description;
    private LocalDate dateNeeded;
    @CreationTimestamp
    private Timestamp dateCreated;
    private String justification;
    private String priorityLevel;
    private String approvalStatus;
}
