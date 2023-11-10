package com.rose.procurement.items.request;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    private Category category;
    private Supplier supplier;

}
