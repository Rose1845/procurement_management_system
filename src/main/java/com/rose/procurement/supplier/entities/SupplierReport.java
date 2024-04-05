package com.rose.procurement.supplier.entities;

import com.rose.procurement.enums.PaymentType;
import lombok.Data;

@Data
public class SupplierReport {
    private String name;
    //    private String contactPerson;
//    private String contactInformation;
    private String p_o_box;
    //    private String city;
    private String location;
    //    private String country;
    private String email;
    private String phoneNumber;
    private PaymentType paymentType;
//    private String termsAndConditions;
}
