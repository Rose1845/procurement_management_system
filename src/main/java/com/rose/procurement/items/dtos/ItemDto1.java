package com.rose.procurement.items.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.rose.procurement.items.entity.Item}
 */
@Data
@Builder
public class ItemDto1 {
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
   private Long categoryCategoryId;
   private  Long supplierVendorId;
}