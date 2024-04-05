package com.rose.procurement.users.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 32,message = "Password should have minimum of 8-32 characters")
    private String password;
    private String phoneNumber;
    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
}
