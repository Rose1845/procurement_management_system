package com.rose.procurement.offer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "offer",cascade = CascadeType.ALL)
    @JsonBackReference("offerItems")
    private Set<OfferItem> items;
    @OneToOne
    @JoinColumn(name = "purchase_request_id")
    private PurchaseRequest purchaseRequest;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonBackReference("offerSupplier")
    private Supplier supplier;




//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "offer_suppliers",
//            joinColumns = {
//                    @JoinColumn(name = "offer_id",referencedColumnName = "id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "supplier_id",referencedColumnName = "vendorId")
//            }
//    )@JsonIgnore
//    private Set<Supplier> suppliers;
}

