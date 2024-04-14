package com.rose.procurement.auth.controllers;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.auth.entity.ForgotPassword;
import com.rose.procurement.auth.repository.ForgotPasswordRepository;
import com.rose.procurement.auth.requests.ChangePassword;
import com.rose.procurement.auth.requests.EmailInput;
import com.rose.procurement.auth.requests.OptInput;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.users.dao.UserDao;
import com.rose.procurement.users.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/forgotPassword")
public class ForgotPasswordController {

    private final UserDao userRepository;
    private final EmailService emailService;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserDao userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // send mail for email verification
    @PostMapping("/verifyMail")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailInput emailInput) throws ProcureException {
        User user = userRepository.findByEmail(emailInput.getEmail());
        if (user == null) {
            throw ProcureException.builder().metadata("User Account").message("email doesn't exist").build();
        }

        // Check if there is an active OTP for the user
        ForgotPassword existingForgotPassword = forgotPasswordRepository.findByUserAndExpirationTimeAfter(user, new Date());
        if (existingForgotPassword != null) {
            // Update the expiration time of the existing OTP
            existingForgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 300 * 1000));
            forgotPasswordRepository.save(existingForgotPassword);
        } else {
            // Generate a new OTP and create a new entry
            int otp = otpGenerator();
            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 300 * 1000))
                    .user(user)
                    .build();
            emailService.sendEmail(user.getEmail(), "OTP for Forgot Password Request", String.valueOf(otp));
            forgotPasswordRepository.save(fp);
        }
        return ResponseEntity.ok("Email sent for verification!");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@RequestBody OptInput optInput, @PathVariable String email) throws ProcureException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw ProcureException.builder().metadata("User Account").message("email doesn't exist")
                    .build();
        }

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(optInput.getOtp(), user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed!");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
