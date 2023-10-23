package com.rose.procurement.supplier.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SupplierResponse {
    private String name;
    private String contactPerson;
    private String address;
    private String email;
    private String phoneNumber;
    private String paymentTerms;
    private String termsAndConditions;
}
