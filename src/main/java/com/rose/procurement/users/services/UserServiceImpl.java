package com.rose.procurement.users.services;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.auth.responses.LoginResponse;
import com.rose.procurement.auth.responses.RegistrationResponse;
import com.rose.procurement.auth.services.AuthService;
import com.rose.procurement.config.JwtService;
import com.rose.procurement.roles.dao.RoleDao;
import com.rose.procurement.roles.entity.Role;
import com.rose.procurement.token.dao.TokenDao;
import com.rose.procurement.token.entity.Token;
import com.rose.procurement.token.nums.TokenType;
import com.rose.procurement.users.dao.UserDao;
import com.rose.procurement.users.entity.User;
import com.rose.procurement.users.requests.CreateUserRequest;
import com.rose.procurement.users.requests.CreateUserWithRolesRequest;
import com.rose.procurement.users.requests.UpdateUserPasswordRequest;
import com.rose.procurement.users.requests.UpdateUserWithoutPasswordRequest;
import com.rose.procurement.utils.MD5UserAvatar;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final JwtService jwtService;
    private final  RoleDao roleDao;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest userRequest) {
        Role defaultRole = roleDao.findDefaultRole(true);

        User newUser = User.builder()
                .firstname(userRequest.getFirstName())
                .lastname(userRequest.getLastName())
                .username(userRequest.getUsername())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        return userDao.save(newUser);
    }

    @Override
    public User creatUserWithRole(CreateUserRequest user, Long roleId) throws ProcureException {
        if (userDao.existsByUsername(user.getUsername())) {
            throw ProcureException
                    .builder()
                    .message("Username already exist")
                    .statusCode(401)
                    .metadata("user/registration")
                    .build();
        }
        if (userDao.existsByUsername(user.getEmail())) {
            throw ProcureException.builder().message("User email already exist").build();
        }
        if (!roleDao.existsById(roleId)) {
            throw ProcureException.builder().message("Role does not exist").build();
        }
        User newUser = User.builder()
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(MD5UserAvatar.generateAvatar(user.getEmail()))
                .roles(Set.of(roleDao.findDefaultRole(true)))
                .build();
        var savedUser = userDao.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        saveUserToken(savedUser, jwtToken);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken).build();
        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .auth(loginResponse)
                .user(savedUser)
                .build();

        return registrationResponse.getUser();
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDao.save(token);
    }

    @Override
    public List<User> findUsers() throws ProcureException {
//        List<User> users = new ArrayList<>(userDao.findAll());
        return userDao.findAll();
//        if (users.toArray().length < 1) {
//            throw ProcureException.builder().message("No user records").metadata("user/fetch").statusCode(404).build();
//        }
//        return users;
    }

    @Override
    public User findUserByEmail(String email) throws ProcureException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw ProcureException.builder().message("User not found").statusCode(404).build();
        }
        return user;
    }

    @Override
    public User findUserByUsername(String username) throws ProcureException {

        if (!userDao.existsByUsername(username)) {
            throw ProcureException.builder().message("User not found").build();
        }
        return userDao.findByEmail(username);
    }
    @Override
    public Optional<User> findUserById(Long id) throws ProcureException {
        if (!userDao.existsById(id)) {
            throw ProcureException.builder().message("User account does not exist").build();
        }
        return userDao.findById(id);
    }

    @Override
    public User removeRolesFromUser(Long userId,Set<Long> roles) throws ProcureException {
        if (!userDao.existsById(userId)) {
            throw ProcureException.builder().message("No user record").build();
        }
        User user = userDao.findById(userId).get();
        roles.forEach(role -> {
            if (roleDao.existsById(role)) {
                Optional<Role> role1 = roleDao.findById(role);
                if (user.getRoles().contains(role1)) {
                    user.getRoles().remove(role1);
                }
            }
        });
        return userDao.save(user);
    }

    @Override
    @Transactional
    public User addRolesToUser(Long userId, Set<Long> roleIds) throws ProcureException {
        User user = userDao.findById(userId)
                .orElseThrow(() -> ProcureException.builder().message("User not found").build());

        List<Role> rolesToAdd = new ArrayList<>();
        for (Long roleId : roleIds) {
            Role role = roleDao.findById(roleId)
                    .orElseThrow(() -> ProcureException.builder().message("Role not found").build());
            if (!user.getRoles().contains(role)) {
                rolesToAdd.add(role);
            }
        }

        user.getRoles().addAll(rolesToAdd);
        return userDao.save(user);
    }



    @Override
    public User createUserWithRoles(CreateUserWithRolesRequest request) throws ProcureException {
        if (userDao.existsByUsername(request.getUsername())){
           throw ProcureException.builder().message("Username already taken").build();
        }
        if (userDao.existsByEmail(request.getEmail())){
          throw  ProcureException.builder().message("Email already taken").build();
        }
        Set<Role> roles = roleDao.findAll().stream().filter(role -> request.getRoles().contains(role.getId())).collect(Collectors.toSet());
        User newUser= User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(MD5UserAvatar.generateAvatar(request.getEmail()))
                .roles(roles)
                .build();
        return userDao.save(newUser);
    }

    @Override
    public User updateUserWithoutPassword(Long userId, UpdateUserWithoutPasswordRequest request) throws ProcureException {
        if (!userDao.existsById(userId)){
            throw ProcureException.builder().message("User does not exist").build();
        }
        User user = userDao.findById(userId).get();
               user.setFirstname(request.getFirstName());
                user.setLastname(request.getLastName());
                user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
                user.setAvatar(MD5UserAvatar.generateAvatar(request.getEmail()));
        return userDao.save(user);
    }

    @Override
    public User updateUserPassword(Long userId, UpdateUserPasswordRequest request) throws ProcureException {
        if (!userDao.existsById(userId)){
            throw ProcureException.builder().message("User does not exist").build();
        }
        User user = userDao.findById(userId).get();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userDao.save(user);
    }

    @Override
    public List<User> findUsersByRole(Long role) throws ProcureException {
        Optional<Role> optionalRole = roleDao.findById(role);
        if (optionalRole.isEmpty()) {
            throw ProcureException.builder().message("Role does not exist").build();
        }
        List<User> users = userDao.findAll();//.stream().filter(u -> u.getRoles().contains(optionalRole)).collect(Collectors.toList()));
        if (users.toArray().length < 1) {
            throw ProcureException.builder()
                    .message("No users found")
                    .statusCode(404)
                    .metadata("user-role/no-entry")
                    .build();
        }
        return users;
    }
}

