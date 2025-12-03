package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank

    @NotNull(message = "User ID cannot be null")
    private Long uid;
    private String userName;

    @NotBlank
    private String password;

    // optional: if not provided, default to STUDENT
    private String role;

    // optional for tutors/admins; required for students in this requirement
    private Long facultyId;
}
