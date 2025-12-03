package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyStudentDto {

    private Long studentId;
    private String studentName;
    private String email;
    private Long groupId;
    private String groupName;
    private Integer score;
    private String feedback;
    private boolean attendance;
}

