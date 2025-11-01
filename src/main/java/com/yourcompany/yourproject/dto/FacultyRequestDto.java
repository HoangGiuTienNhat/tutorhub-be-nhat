package com.yourcompany.yourproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class FacultyRequestDto {

    @NotEmpty(message = "Faculty name cannot be empty.")
    @Size(max = 255, message = "Faculty name must be less than 255 characters.")
    private String name;

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

