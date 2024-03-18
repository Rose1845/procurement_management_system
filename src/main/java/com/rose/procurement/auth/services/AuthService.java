package com.rose.procurement.auth.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.auth.requests.LoginRequest;
import com.rose.procurement.auth.requests.RegisterRequest;
import com.rose.procurement.auth.responses.LoginResponse;
import com.rose.procurement.auth.responses.RegistrationResponse;
import com.rose.procurement.config.JwtService;
import com.rose.procurement.roles.dao.RoleDao;
import com.rose.procurement.roles.entity.Role;
import com.rose.procurement.token.dao.TokenDao;
import com.rose.procurement.token.entity.Token;
import com.rose.procurement.token.nums.TokenType;
import com.rose.procurement.users.dao.UserDao;
import com.rose.procurement.users.entity.User;
import com.rose.procurement.utils.MD5UserAvatar;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final TokenDao tokenDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public RegistrationResponse register(RegisterRequest request) throws ProcureException {

        Optional<Role> defaultRole= Optional.ofNullable(this.validateUserExistAndGetDefaultRole(request, userDao, roleDao));
        User newUser= User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .username(request.getUsername())
                .avatar(MD5UserAvatar.generateAvatar(request.getEmail()))
                .roles(Set.of(defaultRole.get()))
                .build();
        var savedUser = userDao.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        saveUserToken(savedUser, jwtToken);
        LoginResponse loginResponse= LoginResponse.builder()
                .token(jwtToken).build();
        RegistrationResponse registrationResponse= RegistrationResponse.builder()
                .auth(loginResponse)
                .user(savedUser)
                .build();
        return registrationResponse;
    }

    public Role validateUserExistAndGetDefaultRole(RegisterRequest request, UserDao userDao, RoleDao roleDao) throws ProcureException {
       User user = userDao.findByEmailOrUsername(request.getEmail(), request.getUsername());

        if(user!=null){
            if (user.getEmail().toString()==request.getEmail().toString()){
                throw  ProcureException
                        .builder()
                        .message("User email already exist")
                        .statusCode(409).metadata("account/conflict")
                        .build();
            }if (user.getUsername().equals(request.getUsername())){
                throw ProcureException
                        .builder().message("User email already exist").build();
            }
        }
        Role defaultRole = roleDao.findDefaultRole(true);
        if (defaultRole==null){
            throw  ProcureException
                    .builder()
                    .message("Please initialize roles first").metadata("/role").statusCode(500).build();
        }
        return defaultRole;
    }

    public User getUserprofile(String username) throws ProcureException {
        User optUser= userDao.findByUsername(username);
        if (optUser==null){
            throw ProcureException.builder().message("Profile not found").metadata("/profile-fetch").statusCode(404).build();
        }
        return optUser;
    }

    public LoginResponse authenticate(LoginRequest request) throws ProcureException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userDao.findByUsername(request.getUsername());
               if (user==null){
                   throw ProcureException.builder().message("User account not found").build();
               }
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return LoginResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
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

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenDao.findAllValidTokenByUser(Long.valueOf(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token->tokenDao.deleteById(Math.toIntExact(token.getId())));
    }
}