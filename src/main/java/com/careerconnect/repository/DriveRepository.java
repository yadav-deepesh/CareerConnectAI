package com.careerconnect.repository;

import com.careerconnect.model.PlacementDrive;
import java.util.List;
import java.util.Optional;

public interface DriveRepository {

    PlacementDrive save(PlacementDrive drive);

    Optional<PlacementDrive> findById(String id);

    List<PlacementDrive> findAll();
}
