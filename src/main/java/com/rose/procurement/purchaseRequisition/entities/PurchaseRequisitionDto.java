package com.rose.procurement.purchaseRequisition.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank
   private String requisitionTitle;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @FutureOrPresent
    private LocalDate dateNeeded;
    private ApprovalStatus approvalStatus;
    @NotNull
    private Set<Item> items;
}