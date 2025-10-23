package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.LoginRequest;
import com.yourcompany.yourproject.dto.JwtResponse;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.service.UserService;
import com.yourcompany.yourproject.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Attempting to authenticate user: {}", loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication successful for user: {}", loginRequest.getEmail());

            String jwt = tokenProvider.generateToken(authentication);
            log.info("JWT token generated successfully");

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            log.info("Attempting to register user: {}", user.getEmail());
            UserDto createdUser = userService.createUser(user);
            log.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            log.error("User registration failed for: {}", user.getEmail(), e);
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }
}
