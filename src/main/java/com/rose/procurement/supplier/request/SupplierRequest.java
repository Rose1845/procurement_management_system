package com.rose.procurement.supplier.request;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SupplierRequest {
    private String name;
    private String contactPerson;
    private String contactInformation;
    private String address;
    private String email;
    private String phoneNumber;
    private String paymentTerms;
    private String termsAndConditions;
}
