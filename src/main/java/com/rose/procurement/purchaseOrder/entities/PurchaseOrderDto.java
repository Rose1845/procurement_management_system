package com.rose.procurement.purchaseOrder.entities;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;

/**
 * DTO for {@link PurchaseOrder}
 */
@Data
@Builder
public class PurchaseOrderDto {
    @NotNull
    @NotBlank
    private String purchaseOrderTitle;
    @NotNull
    @FutureOrPresent
    private LocalDate deliveryDate;
    @NotNull
    @NotBlank
    private String termsAndConditions;
    @NotNull
    private PaymentType paymentType;
    private ApprovalStatus approvalStatus;
    private double totalAmount;
    @NotNull
    private Set<Item> items;
    @NotNull
    private String vendorId;
}

