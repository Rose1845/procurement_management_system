package com.rose.procurement.users.controllers;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.users.entity.User;
import com.rose.procurement.users.requests.CreateUserWithRolesRequest;
import com.rose.procurement.users.requests.UpdateUserPasswordRequest;
import com.rose.procurement.users.requests.UpdateUserWithoutPasswordRequest;
import com.rose.procurement.users.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findUsers() throws ProcureException {
        return ResponseEntity.ok().body( userService.findUsers());
    }
    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<Optional<User>> findUserById(@PathVariable("userId") @NotNull Long userId) throws ProcureException {
        return ResponseEntity.ok().body( userService.findUserById(userId));
    }
    @PostMapping("/new")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public User createUserWithRoles(@RequestBody @Valid CreateUserWithRolesRequest request) throws ProcureException {
        return userService.createUserWithRoles(request);
    }
    @PutMapping("/{userId}/update")
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE','APPROVER'})")
    public User updateUserWithRole(@RequestBody @Valid UpdateUserWithoutPasswordRequest request, @PathVariable @NotEmpty Long userId) throws ProcureException {
        return userService.updateUserWithoutPassword(userId,request);
    }

    @PatchMapping("/{userId}/update/password")
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE','APPROVER'})")
    public User updatePassword(@RequestBody @Valid UpdateUserPasswordRequest request, @PathVariable @NotEmpty Long userId) throws ProcureException {
        return userService.updateUserPassword(userId,request);
    }

    @PatchMapping("/{userId}/update/remove-role")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public User removeUserRoles(@RequestPart @Valid Set<Long> request, @PathVariable @NotEmpty Long userId) throws ProcureException {
        return userService.removeRolesFromUser(userId,request);
    }

    @PatchMapping("/{userId}/update/add-role")
    @PreAuthorize("hasAuthority({'ADMIN'})")
    public User addRoleToUser(@RequestPart @Valid Set<Long> useRoles, @PathVariable @NotEmpty Long userId) throws ProcureException {
        return userService.addRolesToUser(userId,useRoles);
    }
}
