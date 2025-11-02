package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long uid;
    private String userName;
    private String email;
    private String role;
    private String personalEmail;
    private String phoneNumber;
    private String address;
    // Note: Password should not be included in DTOs for security reasons
}
