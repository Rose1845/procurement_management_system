package com.rose.procurement.purchaseRequest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.enums.QuoteStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "purchase_request_id")
    @JsonIgnore
    private PurchaseRequest purchaseRequest;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    private BigDecimal offerUnitPrice;
    private double offerTotalPrice;
    @Enumerated
    private QuoteStatus quoteStatus;


    public static double calculateOfferTotalPrice(PurchaseRequestItemDetail itemDetail) {
        BigDecimal unitPrice = itemDetail.getOfferUnitPrice();
        double quantity = itemDetail.item.getQuantity(); // Assuming you have a quantity field in PurchaseRequestItemDetail
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).doubleValue();
    }

    // Calculate total price for all items in a list of PurchaseRequestItemDetail
    public static double calculateTotalPriceForAllItems(List<PurchaseRequestItemDetail> itemDetails) {
        double totalPrice = 0;
        for (PurchaseRequestItemDetail itemDetail : itemDetails) {
            totalPrice += calculateOfferTotalPrice(itemDetail);
        }
        return totalPrice;
    }


    // Other fields as needed
}
