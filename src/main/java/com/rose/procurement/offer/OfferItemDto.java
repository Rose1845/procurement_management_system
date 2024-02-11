package com.rose.procurement.offer;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OfferItemDto {
    private String itemId;
//    private Integer vendorId;
    private double offerUnitPrice;
}
