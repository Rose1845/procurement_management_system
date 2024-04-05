package com.rose.procurement.items.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

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
    @Min(value = 1, message = "minimum quantity required is 1")
    private int quantity;
    @NotNull
    @DecimalMin(value = "0.00", message = "Unit Price must be greater than zero")
    private BigDecimal unitPrice;
    @NotNull
    private Long categoryId;
    private BigDecimal totalPrice;
//    @NotNull
//    private String vendorId;

}