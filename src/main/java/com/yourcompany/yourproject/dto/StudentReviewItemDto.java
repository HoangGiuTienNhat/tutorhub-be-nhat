package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentReviewItemDto {

    @NotNull
    private Long studentId;

    @NotNull
    private Boolean attendance;

    private Integer score;

    private String feedback;
}

