package com.rose.procurement.users.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserWithoutPasswordRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    @Max(value = 10,message = "phonenumber 8 characters")
    private String phoneNumber;
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
}


