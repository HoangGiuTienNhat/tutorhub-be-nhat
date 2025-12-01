package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentReviewResponseDto {

    private Long id;
    private Long consultationId;
    private Long tutorId;
    private UserDto student;
    private boolean attendance;
    private Integer score;
    private String feedback;
    private LocalDateTime lastUpdatedAt;
}

