package com.careerconnect.repository;

import com.careerconnect.model.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCompanyRepository implements CompanyRepository {

    private final Map<String, Company> storage = new ConcurrentHashMap<>();

    @Override
    public Company save(Company company) {
        storage.put(company.getId(), company);
        return company;
    }

    @Override
    public Optional<Company> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByName(String name) {
        return storage.values().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }
}
