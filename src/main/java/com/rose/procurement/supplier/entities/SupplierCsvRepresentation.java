package com.rose.procurement.supplier.entities;

import com.opencsv.bean.CsvBindByName;
import com.rose.procurement.enums.PaymentType;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierCsvRepresentation {
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "contact_person")
    private String contactPerson;
    @CsvBindByName(column = "contact_info")
    private String contactInformation;
    @CsvBindByName(column = "box")
    private String p_o_box;
    @CsvBindByName(column = "city")
    private String city;
    @CsvBindByName(column = "location")
    private String location;
    @CsvBindByName(column = "country")
    private String country;
    @CsvBindByName(column = "email")
    private String email;
    @CsvBindByName(column = "phone_number")
    private String phoneNumber;
    @CsvBindByName(column = "payment_type")
    private PaymentType paymentType;
    @CsvBindByName(column = "terms")
    private String termsAndConditions;
}
