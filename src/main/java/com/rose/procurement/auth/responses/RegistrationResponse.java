package com.rose.procurement.auth.responses;

import com.rose.procurement.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegistrationResponse {
    private LoginResponse auth;
    private User user;
}
