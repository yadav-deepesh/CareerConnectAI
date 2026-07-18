package com.careerconnect.dto.response;

import java.time.LocalDate;
import java.util.List;

public class DriveResponse {

    private String id;
    private String companyId;
    private String role;
    private String location;
    private double packageLpa;
    private LocalDate deadline;
    private List<String> requiredSkills;
    private double minCgpa;
    private int maxBacklogs;
    private List<String> allowedProgrammes;
    private int graduationYear;
    private String status;
    private LocalDate createdAt;

    public DriveResponse(String id, String companyId, String role, String location,
                         double packageLpa, LocalDate deadline, List<String> requiredSkills,
                         double minCgpa, int maxBacklogs, List<String> allowedProgrammes,
                         int graduationYear, String status, LocalDate createdAt) {
        this.id = id;
        this.companyId = companyId;
        this.role = role;
        this.location = location;
        this.packageLpa = packageLpa;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills;
        this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs;
        this.allowedProgrammes = allowedProgrammes;
        this.graduationYear = graduationYear;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getCompanyId() { return companyId; }
    public String getRole() { return role; }
    public String getLocation() { return location; }
    public double getPackageLpa() { return packageLpa; }
    public LocalDate getDeadline() { return deadline; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public double getMinCgpa() { return minCgpa; }
    public int getMaxBacklogs() { return maxBacklogs; }
    public List<String> getAllowedProgrammes() { return allowedProgrammes; }
    public int getGraduationYear() { return graduationYear; }
    public String getStatus() { return status; }
    public LocalDate getCreatedAt() { return createdAt; }
}
