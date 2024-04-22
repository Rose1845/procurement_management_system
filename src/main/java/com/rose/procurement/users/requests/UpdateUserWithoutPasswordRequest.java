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
    @Pattern(regexp = "\\+\\d{1,3}\\d{3,14}", message = "Phone number must start with a country code and contain only digits")    private String phoneNumber;
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
}


