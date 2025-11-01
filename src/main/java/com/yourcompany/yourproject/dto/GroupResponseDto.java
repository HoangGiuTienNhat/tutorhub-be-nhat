package com.yourcompany.yourproject.dto;

import java.time.LocalDate;
import java.util.Set;

public class GroupResponseDto {

    private Long id;
    private String groupName;
    private String description;
    private int studentLimit;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserDto tutor;
    private FacultyResponseDto faculty;
    private Set<TopicResponseDto> topics;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getStudentLimit() {
        return studentLimit;
    }

    public void setStudentLimit(int studentLimit) {
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

    public UserDto getTutor() {
        return tutor;
    }

    public void setTutor(UserDto tutor) {
        this.tutor = tutor;
    }

    public FacultyResponseDto getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyResponseDto faculty) {
        this.faculty = faculty;
    }

    public Set<TopicResponseDto> getTopics() {
        return topics;
    }

    public void setTopics(Set<TopicResponseDto> topics) {
        this.topics = topics;
    }
}

