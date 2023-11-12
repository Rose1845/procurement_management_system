package com.rose.procurement.supplier.entities;

import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.utils.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Supplier}
 */
@Data
@Builder
public class SupplierDto{
    @NotNull
    @NotEmpty
    @NotBlank
   private String name;
    private String contactPerson;
    private String contactInformation;
    @NotNull
    private Address address;
    @Email
    private String email;
    private String phoneNumber;
    private  PaymentType paymentType;
    @NotNull
    private String termsAndConditions;
}