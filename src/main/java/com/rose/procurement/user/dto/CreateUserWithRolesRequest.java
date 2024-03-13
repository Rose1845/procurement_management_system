package com.rose.procurement.user.dto;

import com.rose.procurement.user.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserWithRolesRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 32,message = "Password should have minimum of 8-32 characters")
    private String password;
    @NotBlank
    @NotNull
    private String firstname;
    @NotBlank
    @NotNull
    private String lastname;
    private Role role;
}
