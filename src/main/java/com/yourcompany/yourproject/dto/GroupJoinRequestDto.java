package com.yourcompany.yourproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupJoinRequestDto {
    private List<Long> groupIds;
}

