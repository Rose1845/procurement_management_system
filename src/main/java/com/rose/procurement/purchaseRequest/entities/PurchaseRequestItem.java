//package com.rose.procurement.purchaseRequest.entities;
//
//import com.rose.procurement.items.entity.Item;
//import com.rose.procurement.supplier.entities.SupplierOffer;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.HashSet;
//import java.util.Set;
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Builder
//@Setter
//public class PurchaseRequestItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
////    private BigDecimal unitPrice;
//    @ManyToOne
//    @JoinColumn(name = "p_request_item")
//    private Item item;
//    @ManyToOne
//    @JoinColumn(name = "purchase_request_id")
//    private PurchaseRequest purchaseRequest;
//    @OneToMany(mappedBy = "purchaseRequestItem", cascade = CascadeType.ALL)
//    private Set<SupplierOffer> supplierOffers = new HashSet<>();
//}
//
//
