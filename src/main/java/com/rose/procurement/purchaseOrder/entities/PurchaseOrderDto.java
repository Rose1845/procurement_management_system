package com.rose.procurement.purchaseOrder.entities;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for {@link PurchaseOrder}
 */
@Data
@Builder
public class PurchaseOrderDto{
    private String purchaseOrderTitle;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    private PaymentType paymentType;
    private Category category;
    private Supplier supplier;
}