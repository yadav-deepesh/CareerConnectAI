package com.careerconnect.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SubmitApplicationRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
