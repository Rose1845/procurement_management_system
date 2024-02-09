package com.rose.procurement.delivery;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItemDTo {
    private String itemId;
    private int quantityDelivered;
    private int quantityReceived;

}
