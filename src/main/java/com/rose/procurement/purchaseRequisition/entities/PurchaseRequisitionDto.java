package com.rose.procurement.purchaseRequisition.entities;

import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

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