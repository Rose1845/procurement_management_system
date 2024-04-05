package com.rose.procurement.invoice;

import jakarta.validation.constraints.FutureOrPresent;
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
    private String invoiceNumber;
    @FutureOrPresent
    @NotNull
    private LocalDate dueDate;
    @FutureOrPresent
    @NotNull
    private LocalDate invoiceDate;
    private Long purchaseOrderId;

}