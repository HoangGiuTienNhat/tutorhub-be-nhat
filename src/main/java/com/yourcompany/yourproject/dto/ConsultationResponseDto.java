package com.yourcompany.yourproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationResponseDto {

    private Long id;
    private String topic;
    private String description;
    private LocalDate consultationDate;
    private LocalTime consultationTime;
    private String type;
    private String locationLink;
    private String status;
    private Long groupId;
    private String groupName;

    // Constructor
    public ConsultationResponseDto() {
    }

    public ConsultationResponseDto(Long id, String topic, String description, LocalDate consultationDate,
            LocalTime consultationTime, String type, String locationLink, String status, Long groupId,
            String groupName) {
        this.id = id;
        this.topic = topic;
        this.description = description;
        this.consultationDate = consultationDate;
        this.consultationTime = consultationTime;
        this.type = type;
        this.locationLink = locationLink;
        this.status = status;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDate consultationDate) {
        this.consultationDate = consultationDate;
    }

    public LocalTime getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(LocalTime consultationTime) {
        this.consultationTime = consultationTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

