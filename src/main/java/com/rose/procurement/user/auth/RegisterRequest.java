package com.rose.procurement.user.auth;

import com.rose.procurement.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank
  @NotNull
  private String firstname;
  @NotBlank
  @NotNull
  private String lastname;
  @NotBlank
  @NotNull
  @Email
  private String email;
  @NotBlank
  @NotNull
  private String password;
}
