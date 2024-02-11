package com.rose.procurement.offer;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MultiOfferDto {
    Set<OfferDto> offerDtoSet;

}
