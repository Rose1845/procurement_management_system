package com.rose.procurement.offer;

import com.fasterxml.jackson.annotation.*;
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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Supplier supplier;
    @JsonManagedReference
    @OneToMany(mappedBy = "offer",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OfferItem> offerItems;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_request_id")
    private PurchaseRequest purchaseRequest;
}


