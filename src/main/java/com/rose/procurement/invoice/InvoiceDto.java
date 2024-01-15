package com.rose.procurement.invoice;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for {@link Invoice}
 */
@Data
@Builder
public class InvoiceDto{
    @NotNull
    @NotBlank
    private String invoiceNumber;
    @FutureOrPresent
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private double totalAmount;
    @NotNull
    private  Long purchaseOrderId;
}