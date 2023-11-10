package com.rose.procurement.purchaseOrder.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.invoice.Invoice;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseOrderId;
    private String purchaseOrderTitle;
    private LocalDate deliveryDate;
    private String termsAndConditions;
    @Enumerated
    private PaymentType paymentType;
   @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
   @JoinColumn(name = "category_id")
   @JsonIgnore
  private Category category;
   @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
   @JoinColumn(name = "vendorId")
     private  Supplier supplier;
    @OneToOne(mappedBy = "purchaseOrder")
    @JsonIgnore
    private Invoice invoice;
}
