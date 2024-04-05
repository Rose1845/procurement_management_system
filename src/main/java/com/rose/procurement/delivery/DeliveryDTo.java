package com.rose.procurement.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTo {
    private LocalDateTime deliveryDate;
    private String receivedBy;
    private Set<DeliveryItemDTo> itemDToSet;
    private LocalDateTime deliveredOn;
    private LocalDateTime expectedOn;
    private LocalDateTime receivedOn;
}
