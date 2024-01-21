package com.rose.procurement.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailsDTO {
    private String invoiceId;
    private LocalDateTime invoiceTimestamp;
    private String invoiceDate;
    private String purchaseOrderNumber;
    private int supplierId;
    private LocalDateTime purchaseOrderTimestamp;
    private int orderItemsQuantity;
    private int itemQuantity;
    private int itemDiscount;
    private LocalDateTime itemDate;
    private String supplierName;
    private String supplierBox;
    private String supplierCountry;
    private String supplierCity;
    private String supplierLocation;
}
