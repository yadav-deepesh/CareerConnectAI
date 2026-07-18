package com.careerconnect.model;

public class Company {

    private String id;
    private String name;
    private String sector;
    private String description;

    public Company(String id, String name, String sector, String description) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSector() {
        return sector;
    }

    public String getDescription() {
        return description;
    }
}
