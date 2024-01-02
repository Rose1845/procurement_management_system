package com.rose.procurement.purchaseOrder.entities;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

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
    private ApprovalStatus approvalStatus;
    private Set<Item> items;
     private Long vendorId;
}

