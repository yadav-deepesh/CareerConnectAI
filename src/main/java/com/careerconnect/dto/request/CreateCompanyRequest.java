package com.careerconnect.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Sector is required")
    private String sector;

    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
