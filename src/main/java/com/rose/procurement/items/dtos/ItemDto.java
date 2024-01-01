package com.rose.procurement.items.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.rose.procurement.items.entity.Item}
 */
@Data
@Builder
//@JsonIdentityInfo(generator = ObjectIdGenerators)
//@NoArgsConstructor
public class ItemDto {
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    private Long categoryId;
    private double totalPrice;
    private Long vendorId;
}