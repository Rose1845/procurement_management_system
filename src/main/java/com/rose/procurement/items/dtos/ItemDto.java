package com.rose.procurement.items.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @NotNull
    private String itemName;
    @NotBlank
    @NotNull
    private String itemNumber;
    @NotBlank
    @NotNull
    private String itemDescription;
    @NotNull
    private int quantity;
    @NotNull
    private double unitPrice;
    @NotNull
    private Long categoryId;
    @NotNull
    private double totalPrice;
    @NotNull
    private Long vendorId;

}