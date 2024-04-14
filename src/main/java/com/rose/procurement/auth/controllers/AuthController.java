package com.rose.procurement.auth.controllers;
import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.auth.requests.LoginRequest;
import com.rose.procurement.auth.requests.RegisterRequest;
import com.rose.procurement.auth.responses.RegistrationResponse;
import com.rose.procurement.auth.services.AuthService;
import com.rose.procurement.roles.entity.Role;
import com.rose.procurement.roles.services.RoleService;
import com.rose.procurement.users.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RoleService roleService;

    @PostMapping("/register")
    public RegistrationResponse register(
            @Valid @RequestBody RegisterRequest request
    ) throws ProcureException {
        return ResponseEntity.ok().body(authService.register(request)).getBody();
    }
//    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody @Valid LoginRequest request
    ) {
        try {
            return ResponseEntity.ok((authService.authenticate(request)));
        }catch (ProcureException e){
            return ResponseEntity.ok().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/init")
    public ResponseEntity<Optional<List<Role>>> initializeApp() throws ProcureException {
        return ResponseEntity.ok().body(roleService.initializeRoles());
    }

    @PreAuthorize("hasAnyAuthority({'LAWYER','USER','ADMIN'})")
    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable("username") @NotNull @NotEmpty @NotBlank String username) {
        try {

            return ResponseEntity.ok().body(authService.getUserprofile(username));
        } catch (Exception | ProcureException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
