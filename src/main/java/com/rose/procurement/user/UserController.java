package com.rose.procurement.user;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.user.dto.CreateUserWithRolesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    @PostMapping
    public User createUser(@RequestBody CreateUserWithRolesRequest user) throws ProcureException {
        return service.creatUserWithRole(user);
    }

    @GetMapping
    public List<User> getAllUsers() throws ProcureException {
        return service.findUsers();
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
