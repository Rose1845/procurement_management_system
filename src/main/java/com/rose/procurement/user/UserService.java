package com.rose.procurement.user;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.config.JwtService;
import com.rose.procurement.user.auth.AuthenticationResponse;
import com.rose.procurement.user.dto.CreateUserWithRolesRequest;
import com.rose.procurement.user.token.Token;
import com.rose.procurement.user.token.TokenRepository;
import com.rose.procurement.user.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // save the new password
        repository.save(user);
    }
    public User creatUserWithRole(CreateUserWithRolesRequest user) throws ProcureException {

        if (repository.existsByEmail(user.getEmail())) {
            throw ProcureException.builder().message("User email already exist").build();
        }
        User newUser = User.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .role(Role.valueOf(user.getRole().name()))
                .build();
        var savedUser = repository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        var refreshToken = jwtService.generateRefreshToken(newUser);
        saveUserToken(savedUser, jwtToken);
        AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return repository.save(savedUser);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    public List<User> findUsers() throws ProcureException {
        List<User> users = new ArrayList<>(repository.findAll());
        if (users.toArray().length < 1) {
            throw ProcureException.builder().message("No user records").metadata("user/fetch").statusCode(404).build();
        }
        return users;
    }
}
