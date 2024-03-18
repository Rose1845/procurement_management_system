package com.rose.procurement.auth.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 8,max = 32,message = "Password should have minimum of 8-32 characters")
    private String password;
}