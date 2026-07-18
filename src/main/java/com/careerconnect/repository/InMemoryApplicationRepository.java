package com.careerconnect.repository;

import com.careerconnect.model.Application;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryApplicationRepository implements ApplicationRepository {

    private final Map<String, Application> storage = new ConcurrentHashMap<>();

    @Override
    public Application save(Application application) {
        storage.put(application.getId(), application);
        return application;
    }

    @Override
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Application> findByStudentId(String studentId) {
        return storage.values().stream()
                .filter(app -> app.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Application> findByDriveIdAndStudentId(String driveId, String studentId) {
        return storage.values().stream()
                .filter(app -> app.getDriveId().equals(driveId) && app.getStudentId().equals(studentId))
                .findFirst();
    }

    @Override
    public List<Application> findAll() {
        return new ArrayList<>(storage.values());
    }
}
