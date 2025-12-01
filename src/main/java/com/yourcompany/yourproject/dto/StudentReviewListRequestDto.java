package com.yourcompany.yourproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class StudentReviewListRequestDto {

    @NotEmpty
    @Valid
    private List<StudentReviewItemDto> reviews;
}

