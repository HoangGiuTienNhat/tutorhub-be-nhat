package com.yourcompany.yourproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDto {
    private Long id;
    private String name;
    private long tutorCount;
    private long studentCount;
    private long groupCount;
    private long consultationCount;
}

