package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticDto {
    private String userId;
    private String userName;
    private String facultyName;
    private long groupCount;
    private long consultationCount;
}

