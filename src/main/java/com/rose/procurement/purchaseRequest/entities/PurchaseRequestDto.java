package com.rose.procurement.purchaseRequest.entities;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link PurchaseRequest}
 */
@Data
@Builder
public class PurchaseRequestDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String purchaseRequestTitle;
    @NotNull
    @FutureOrPresent
    private LocalDate dueDate;
    @NotNull
    @NotBlank
    private String termsAndConditions;
    @NotNull
    private Long vendorId;
    @NotNull
    private Set<Item> items;
}

