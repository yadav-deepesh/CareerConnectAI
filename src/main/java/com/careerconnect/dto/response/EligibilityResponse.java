package com.careerconnect.dto.response;

import java.util.List;

public class EligibilityResponse {

    private String studentId;
    private String driveId;
    private boolean eligible;
    private List<String> reasons;

    public EligibilityResponse(String studentId, String driveId, boolean eligible, List<String> reasons) {
        this.studentId = studentId;
        this.driveId = driveId;
        this.eligible = eligible;
        this.reasons = reasons;
    }

    public String getStudentId() { return studentId; }
    public String getDriveId() { return driveId; }
    public boolean isEligible() { return eligible; }
    public List<String> getReasons() { return reasons; }
}
