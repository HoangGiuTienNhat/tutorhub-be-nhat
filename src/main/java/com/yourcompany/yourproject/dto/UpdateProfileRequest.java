package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Email(message = "Personal email should be valid")
    private String personalEmail;

    @Pattern(regexp = "^[0-9\\-\\+\\s\\(\\)]*$", message = "Phone number should contain only digits, spaces, hyphens, plus sign, or parentheses")
    private String phoneNumber;

    private String address;
}

