package org.example.naghshegard.Controller;


import org.example.naghshegard.Dto.*;
import org.example.naghshegard.Jwt.JwtUtil;
import org.example.naghshegard.Model.User;
import org.example.naghshegard.Service.UserService;
import org.example.naghshegard.Service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final VerificationService verificationService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtil jwtUtil,
                          VerificationService verificationService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.verificationService = verificationService;
    }

    @PostMapping("/register/send-code")
    public ResponseEntity<?> sendRegisterCode(@Valid @RequestBody EmailRequest request) {
        try {
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            verificationService.sendVerificationCode(request.getEmail(), "REGISTER");
            return ResponseEntity.ok("Verification code sent to your email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification code: " + e.getMessage());
        }
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verifyAndRegister(@Valid @RequestBody RegisterWithCodeRequest request) {
        try {
            boolean isValid = verificationService.verifyCode(request.getEmail(), request.getCode(), "REGISTER");
            if (!isValid) {
                return ResponseEntity.badRequest().body("Invalid or expired verification code");
            }

            User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password/send-code")
    public ResponseEntity<?> sendResetCode(@Valid @RequestBody EmailRequest request) {
        try {
            if (!userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email not found");
            }

            verificationService.sendVerificationCode(request.getEmail(), "RESET_PASSWORD");
            return ResponseEntity.ok("Reset code sent to your email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send reset code: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            boolean isValid = verificationService.verifyCode(request.getEmail(), request.getCode(), "RESET_PASSWORD");
            if (!isValid) {
                return ResponseEntity.badRequest().body("Invalid or expired verification code");
            }

            userService.updatePassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findByUsername(request.getUsername());
            String token = jwtUtil.generateToken(request.getUsername(), user.getRole().name());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(request.getUsername());
            response.setRole(user.getRole().name());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().name());
            response.put("enabled", user.isEnabled());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
}