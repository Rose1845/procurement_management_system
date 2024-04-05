package com.rose.procurement.purchaseOrder.entities;

import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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
    private BigDecimal totalAmount;
    //    @NotNull
    private Set<Item> items;
    //    @NotNull
    private String vendorId;
}

