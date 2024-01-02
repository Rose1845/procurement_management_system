package com.rose.procurement.invoice;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
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
    private LocalDate dueDate;
    private double totalAmount;
    private  Long purchaseOrderId;
}