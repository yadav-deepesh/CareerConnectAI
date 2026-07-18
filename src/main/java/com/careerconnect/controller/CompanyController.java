package com.careerconnect.controller;

import com.careerconnect.dto.request.CreateCompanyRequest;
import com.careerconnect.dto.response.CompanyResponse;
import com.careerconnect.model.Company;
import com.careerconnect.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        Company company = companyService.createCompany(
                request.getName(), request.getSector(), request.getDescription()
        );
        return new ResponseEntity<>(toResponse(company), HttpStatus.CREATED);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable String companyId) {
        Company company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(toResponse(company));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> companies = companyService.getAllCompanies().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companies);
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(c.getId(), c.getName(), c.getSector(), c.getDescription());
    }
}
