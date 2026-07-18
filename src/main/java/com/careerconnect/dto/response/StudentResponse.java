package com.careerconnect.dto.response;

import java.util.List;

public class StudentResponse {

    private String id;
    private String name;
    private String email;
    private String programme;
    private int graduationYear;
    private double cgpa;
    private int activeBacklogs;
    private List<String> skills;

    public StudentResponse(String id, String name, String email, String programme,
                           int graduationYear, double cgpa, int activeBacklogs, List<String> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.programme = programme;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
        this.activeBacklogs = activeBacklogs;
        this.skills = skills;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProgramme() { return programme; }
    public int getGraduationYear() { return graduationYear; }
    public double getCgpa() { return cgpa; }
    public int getActiveBacklogs() { return activeBacklogs; }
    public List<String> getSkills() { return skills; }
}
