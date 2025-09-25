package com.yourcompany.yourproject.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long uid;
    private String userName;
    private String email;
    private String role;
    // Note: Password should not be included in DTOs for security reasons
}
