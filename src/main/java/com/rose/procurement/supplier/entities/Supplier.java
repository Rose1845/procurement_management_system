package com.rose.procurement.supplier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
}
