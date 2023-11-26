package com.rose.procurement.purchaseRequest.entities;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for {@link PurchaseRequest}
 */
@Data
@Builder
public class PurchaseRequestDto {
    private String purchaseRequestTitle;
    private LocalDate dueDate;
    private String termsAndConditions;
    private Item item;
}