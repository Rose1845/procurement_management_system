package com.rose.procurement.purchaseOrder.request;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderRequest {
    private String purchaseOrderTitle;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    private PaymentType paymentType;
    private Category category;
    private Supplier supplier;
}
