package com.careerconnect.model;

import java.time.LocalDateTime;

public class Application {

    private String id;
    private String studentId;
    private String driveId;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    public Application(String id, String studentId, String driveId) {
        this.id = id;
        this.studentId = studentId;
        this.driveId = driveId;
        this.status = ApplicationStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = this.submittedAt;
    }

    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDriveId() {
        return driveId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean transitionTo(ApplicationStatus newStatus) {
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus;
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
