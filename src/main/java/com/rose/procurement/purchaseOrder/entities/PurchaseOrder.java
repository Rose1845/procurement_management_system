package com.rose.procurement.purchaseOrder.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseOrder {
    @Id
    private Long id;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long poId;
    private Long vendorId;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    private String paymentTerms;
}
