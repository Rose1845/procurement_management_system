package com.rose.procurement.items.dtos;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link com.rose.procurement.items.entity.Item}
 */
@Data
@Builder
public class ItemDto {
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    private Category category;
    private Supplier supplier;
}