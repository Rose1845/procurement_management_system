package com.rose.procurement.offer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OfferSupplier {
    @Id
    private Long id;
    private String vendorId;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    @JsonBackReference("offerItems")
    private Offer offer;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<OfferItem> offerItems;
}
