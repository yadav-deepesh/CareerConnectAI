package com.careerconnect.repository;

import com.careerconnect.model.Application;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository {

    Application save(Application application);

    Optional<Application> findById(String id);

    List<Application> findByStudentId(String studentId);

    Optional<Application> findByDriveIdAndStudentId(String driveId, String studentId);

    List<Application> findAll();
}
