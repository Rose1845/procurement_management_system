package com.rose.procurement.supplier.request;

import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.utils.address.Address;
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
    private Address address;
    private String email;
    private String phoneNumber;
    private PaymentType paymentTerm;
    private String termsAndConditions;
}
