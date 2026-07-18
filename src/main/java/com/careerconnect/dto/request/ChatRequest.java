package com.careerconnect.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {

    private String studentId;
    private String driveId;

    @NotBlank(message = "Message is required")
    private String message;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDriveId() { return driveId; }
    public void setDriveId(String driveId) { this.driveId = driveId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
