package com.rose.procurement.auth.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
        @Email
        @NotEmpty
        private String email;
        @Size(min = 8,max = 32,message = "Password should have minimum of 8-32 characters")
        @NotNull
        @NotEmpty
        private String password;
        @NotNull
        @NotEmpty
        private String username;
        @NotNull
        @NotEmpty
        @Pattern(regexp = "\\+\\d{1,3}\\d{3,14}", message = "Phone number must start with a country code and contain only digits")
        private String phoneNumber;
        @NotNull
        @NotEmpty
        private String firstName;
        @NotNull
        @NotEmpty
        private String lastName;
        private String avatar;

}
