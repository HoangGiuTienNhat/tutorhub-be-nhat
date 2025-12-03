package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.UpdateProfileRequest;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    /**
     * Get current user's profile
     * Endpoint: GET /profile
     */
    @GetMapping
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String email = authentication.getName();
            log.info("Getting profile for user: {}", email);

            UserDto user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Failed to get profile", e);
            return ResponseEntity.status(500).body("Failed to get profile: " + e.getMessage());
        }
    }

    /**
     * Update current user's profile
     * Endpoint: PUT /profile
     * Only allows updating: personalEmail, phoneNumber, address
     */
    @PutMapping
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String email = authentication.getName();
            log.info("Updating profile for user: {}", email);
            log.info("Update request: personalEmail={}, phoneNumber={}, address={}",
                    request.getPersonalEmail(), request.getPhoneNumber(), request.getAddress());

            UserDto updatedUser = userService.updateProfile(email, request);
            log.info("Profile updated successfully for user: {}", email);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Failed to update profile", e);
            return ResponseEntity.status(500).body("Failed to update profile: " + e.getMessage());
        }
    }
}

