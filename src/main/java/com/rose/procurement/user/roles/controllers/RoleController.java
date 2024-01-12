package com.rose.procurement.user.roles.controllers;

import com.rose.procurement.user.roles.entity.Role;
import com.rose.procurement.user.roles.requests.CreateRoleRequest;
import com.rose.procurement.user.roles.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;
    @SneakyThrows
    @GetMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Optional<Optional<List<Role>>>> getRoles() {
        return ResponseEntity.ok().body(Optional.ofNullable(roleService.findRoles()));
    }
    @SneakyThrows
    @GetMapping("/create")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Optional<Role>> createRole(
            @RequestBody @Valid CreateRoleRequest request
    ) {
        return ResponseEntity.ok().body(Optional.ofNullable(roleService.createRole(request)));
    }
    @SneakyThrows
    @GetMapping("/{roleId}/get")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Optional<Optional<Role>>> findRoleById(
            @PathVariable @NotBlank @NotNull Long roleId
    ) {
        return ResponseEntity.ok().body(Optional.ofNullable(roleService.findRoleById(roleId)));
    }
    @SneakyThrows
    @PostMapping("/role/{roleName}/get")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Optional<Optional<Role>>> findRoleById(
            @PathVariable @NotBlank @NotNull String roleName
    ) {
        return ResponseEntity.ok().body(Optional.of(Optional.ofNullable(roleService.findRoleByName(roleName))));
    }
}
