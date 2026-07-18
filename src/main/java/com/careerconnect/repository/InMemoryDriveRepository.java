package com.careerconnect.repository;

import com.careerconnect.model.PlacementDrive;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDriveRepository implements DriveRepository {

    private final Map<String, PlacementDrive> storage = new ConcurrentHashMap<>();

    @Override
    public PlacementDrive save(PlacementDrive drive) {
        storage.put(drive.getId(), drive);
        return drive;
    }

    @Override
    public Optional<PlacementDrive> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<PlacementDrive> findAll() {
        return new ArrayList<>(storage.values());
    }
}
