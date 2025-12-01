package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDto {

    private Long id;
    private int rating;
    private String comment;
    private Long consultationId;
    private UserDto user;
    private LocalDateTime createdAt;

}

