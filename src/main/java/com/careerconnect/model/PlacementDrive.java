package com.careerconnect.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlacementDrive {

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

    private DriveStatus status;
    private LocalDate createdAt;

    public PlacementDrive(String id, String companyId, String role, String location,
                          double packageLpa, LocalDate deadline, List<String> requiredSkills,
                          double minCgpa, int maxBacklogs, List<String> allowedProgrammes,
                          int graduationYear) {
        this.id = id;
        this.companyId = companyId;
        this.role = role;
        this.location = location;
        this.packageLpa = packageLpa;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
        this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs;
        this.allowedProgrammes = allowedProgrammes != null ? new ArrayList<>(allowedProgrammes) : new ArrayList<>();
        this.graduationYear = graduationYear;
        this.status = DriveStatus.OPEN;
        this.createdAt = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public double getPackageLpa() {
        return packageLpa;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public List<String> getRequiredSkills() {
        return new ArrayList<>(requiredSkills);
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public int getMaxBacklogs() {
        return maxBacklogs;
    }

    public List<String> getAllowedProgrammes() {
        return new ArrayList<>(allowedProgrammes);
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public DriveStatus getStatus() {
        return status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public boolean isDeadlinePassed() {
        return LocalDate.now().isAfter(deadline);
    }
}
