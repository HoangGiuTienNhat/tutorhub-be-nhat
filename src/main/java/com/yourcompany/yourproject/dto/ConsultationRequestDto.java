package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationRequestDto {

    @NotBlank(message = "Topic is required")
    private String topic;

    private String description;

    @NotNull(message = "Consultation date is required")
    private LocalDate consultationDate;

    @NotNull(message = "Consultation time is required")
    private LocalTime consultationTime;

    @NotBlank(message = "Type is required (ONLINE or OFFLINE)")
    private String type;

    private String locationLink;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    private String status;

    // Getters and Setters
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

