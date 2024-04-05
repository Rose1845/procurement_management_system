package com.rose.procurement.supplier.entities;

import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.utils.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for {@link Supplier}
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String contactPerson;
    @NotNull
    @NotBlank
    private String contactInformation;
    @NotNull
    private Address address;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String phoneNumber;
    @NotNull
    private PaymentType paymentType;
    @NotNull
    private String termsAndConditions;
}

