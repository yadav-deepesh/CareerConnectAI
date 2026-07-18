package com.careerconnect.dto.response;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private String applicationId;
    private String studentId;
    private String driveId;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    public ApplicationResponse(String applicationId, String studentId, String driveId,
                               String status, LocalDateTime submittedAt, LocalDateTime updatedAt) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.driveId = driveId;
        this.status = status;
        this.submittedAt = submittedAt;
        this.updatedAt = updatedAt;
    }

    public String getApplicationId() { return applicationId; }
    public String getStudentId() { return studentId; }
    public String getDriveId() { return driveId; }
    public String getStatus() { return status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
