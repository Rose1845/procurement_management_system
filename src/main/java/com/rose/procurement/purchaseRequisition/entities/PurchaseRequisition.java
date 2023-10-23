package com.rose.procurement.purchaseRequisition.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String requisitionTitle;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNeeded;
    @CreationTimestamp
    private Timestamp dateCreated;
    private String justification;
    private String priorityLevel;
    private String approvalStatus;
}
