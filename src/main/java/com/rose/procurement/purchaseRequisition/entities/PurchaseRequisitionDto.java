package com.rose.procurement.purchaseRequisition.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link PurchaseRequisition}
 */
@Data
@Builder
public class PurchaseRequisitionDto {
   private String requisitionTitle;
    private String description;
    private LocalDate dateNeeded;
    private ApprovalStatus approvalStatus;
    private Set<Item> items;
}