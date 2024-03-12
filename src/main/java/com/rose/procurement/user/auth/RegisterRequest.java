package com.rose.procurement.user.auth;

import com.rose.procurement.user.Role;
import jakarta.persistence.Column;
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
  @NotNull
  private String firstname;
  @NotNull
  private String lastname;
  @NotNull
  @Email
  @Column(unique = true)
  private String email;
  @NotNull
  private String password;
}
