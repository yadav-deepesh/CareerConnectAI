package com.careerconnect.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class CreateDriveRequest {

    @NotBlank(message = "Company ID is required")
    private String companyId;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Location is required")
    private String location;

    @DecimalMin(value = "0.1", message = "Package must be greater than 0")
    private double packageLpa;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    private List<String> requiredSkills;

    @DecimalMin(value = "0.0", message = "Min CGPA cannot be negative")
    @DecimalMax(value = "10.0", message = "Min CGPA cannot exceed 10.0")
    private double minCgpa;

    @Min(value = 0, message = "Max backlogs cannot be negative")
    private int maxBacklogs;

    private List<String> allowedProgrammes;

    @Min(value = 2020, message = "Graduation year must be 2020 or later")
    private int graduationYear;

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public int getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(int maxBacklogs) { this.maxBacklogs = maxBacklogs; }

    public List<String> getAllowedProgrammes() { return allowedProgrammes; }
    public void setAllowedProgrammes(List<String> allowedProgrammes) { this.allowedProgrammes = allowedProgrammes; }

    public int getGraduationYear() { return graduationYear; }
    public void setGraduationYear(int graduationYear) { this.graduationYear = graduationYear; }
}
