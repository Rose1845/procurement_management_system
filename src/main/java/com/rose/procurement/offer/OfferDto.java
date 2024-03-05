package com.rose.procurement.offer;

import com.rose.procurement.supplier.entities.Supplier;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OfferDto {
    private  Set<OfferItemDto> itemDtoSet;
//    private Supplier supplier;
}
