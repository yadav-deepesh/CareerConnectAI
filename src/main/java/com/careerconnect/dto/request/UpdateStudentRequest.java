package com.careerconnect.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public class UpdateStudentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Programme is required")
    private String programme;

    @Min(value = 2020, message = "Graduation year must be 2020 or later")
    private int graduationYear;

    @DecimalMin(value = "0.0", message = "CGPA cannot be negative")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private double cgpa;

    @Min(value = 0, message = "Active backlogs cannot be negative")
    private int activeBacklogs;

    private List<String> skills;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }

    public int getGraduationYear() { return graduationYear; }
    public void setGraduationYear(int graduationYear) { this.graduationYear = graduationYear; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public int getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(int activeBacklogs) { this.activeBacklogs = activeBacklogs; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
