package com.rose.procurement.supplier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.utils.address.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vendorId;
    private String name;
    private String contactPerson;
    private String contactInformation;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "box", column = @Column(name = "p.o.box")),
            @AttributeOverride(name = "country",column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "location", column = @Column(name = "location"))
    })
    private Address address;
    @Email
    private String email;
    private String phoneNumber;
    @Enumerated
    private PaymentType paymentType;
    private String termsAndConditions;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contract> contracts;
    @OneToMany(mappedBy = "supplier",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders;


}
