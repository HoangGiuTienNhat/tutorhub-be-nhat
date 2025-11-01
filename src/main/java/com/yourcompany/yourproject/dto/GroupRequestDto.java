package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class GroupRequestDto {

    @NotEmpty(message = "Group name cannot be empty.")
    @Size(max = 255, message = "Group name must be less than 255 characters.")
    private String groupName;

    @Size(max = 1000, message = "Description must be less than 1000 characters.")
    private String description;

    @NotNull(message = "Student limit cannot be null.")
    @Min(value = 1, message = "Student limit must be at least 1.")
    private Integer studentLimit;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotEmpty(message = "At least one topic must be selected.")
    private Set<Long> topicIds;

    @NotNull(message = "Faculty cannot be null.")
    private Long facultyId;

    // Getters and Setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStudentLimit() {
        return studentLimit;
    }

    public void setStudentLimit(Integer studentLimit) {
        this.studentLimit = studentLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Long> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<Long> topicIds) {
        this.topicIds = topicIds;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}
