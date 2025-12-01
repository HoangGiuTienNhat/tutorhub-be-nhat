package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationMemberReviewDto {

    // Student Info
    private Long studentId;
    private String studentName;
    private String studentEmail;

    // Review Info (can be null if not yet reviewed)
    private Boolean attendance;
    private Integer score;
    private String feedback;
}

