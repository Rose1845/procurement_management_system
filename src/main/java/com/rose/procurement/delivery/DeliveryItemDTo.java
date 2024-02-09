package com.rose.procurement.delivery;

import lombok.*;

@Data
@Setter
@Getter
public class DeliveryItemDTo {
    private String itemId;
    private int quantityDelivered;
    private int quantityReceived;

    public DeliveryItemDTo() {}

    public DeliveryItemDTo(String itemId, int quantityDelivered, int quantityReceived) {
        this.itemId = itemId;
        this.quantityDelivered = quantityDelivered;
        this.quantityReceived = quantityReceived;
    }

}
