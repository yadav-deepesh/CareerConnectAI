package com.careerconnect.service;

import com.careerconnect.exception.DuplicateResourceException;
import com.careerconnect.exception.ResourceNotFoundException;
import com.careerconnect.model.Company;
import com.careerconnect.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(String name, String sector, String description) {
        if (companyRepository.existsByName(name)) {
            throw new DuplicateResourceException("A company with name '" + name + "' already exists.");
        }

        String id = "CMP-" + String.format("%03d", idCounter.getAndIncrement());
        Company company = new Company(id, name, sector, description);
        companyRepository.save(company);
        logger.info("Created company: {} ({})", id, name);
        return company;
    }

    public Company getCompanyById(String companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}
