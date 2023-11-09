package com.rose.procurement.invoice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDto {
//    private String invoiceId;
    private String invoiceNumber;
    private LocalDate dueDate;
    private double subTotalAmount;
    private PurchaseOrder purchaseOrder;
}