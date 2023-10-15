package com.rose.procurement.supplier.entities;

import com.rose.procurement.contract.entities.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vendorId;
    private String name;
    private String contactPerson;
    private String contactInformation;
    private String address;
    private String email;
    private String phoneNumber;
    private String paymentTerms;
    private String termsAndConditions;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Contract> contracts;
}
