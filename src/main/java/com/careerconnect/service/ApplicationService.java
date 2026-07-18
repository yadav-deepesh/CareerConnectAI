package com.careerconnect.service;

import com.careerconnect.exception.*;
import com.careerconnect.model.*;
import com.careerconnect.policy.EligibilityPolicy;
import com.careerconnect.repository.ApplicationRepository;
import com.careerconnect.repository.DriveRepository;
import com.careerconnect.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final DriveRepository driveRepository;
    private final EligibilityPolicy eligibilityPolicy;
    private final AtomicInteger idCounter = new AtomicInteger(500);

    public ApplicationService(ApplicationRepository applicationRepository,
                                StudentRepository studentRepository,
                                DriveRepository driveRepository,
                                EligibilityPolicy eligibilityPolicy) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.driveRepository = driveRepository;
        this.eligibilityPolicy = eligibilityPolicy;
    }

    public Application submitApplication(String driveId, String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found with ID: " + driveId));

        if (drive.isDeadlinePassed()) {
            throw new DeadlinePassedException("The deadline for drive " + driveId + " has passed. Cannot accept applications.");
        }

        if (applicationRepository.findByDriveIdAndStudentId(driveId, studentId).isPresent()) {
            throw new DuplicateResourceException("Student " + studentId + " has already applied to drive " + driveId + ".");
        }

        EligibilityResult eligibility = eligibilityPolicy.evaluate(student, drive);
        if (!eligibility.isEligible()) {
            String reasons = String.join("; ", eligibility.getReasons());
            throw new IneligibleApplicationException("Student is not eligible for this drive. Reasons: " + reasons);
        }

        String id = "APP-" + idCounter.getAndIncrement();
        Application application = new Application(id, studentId, driveId);
        applicationRepository.save(application);
        logger.info("Application {} created: student {} applied to drive {}", id, studentId, driveId);
        return application;
    }

    public Application getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));
    }

    public List<Application> getApplicationsByStudent(String studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        return applicationRepository.findByStudentId(studentId);
    }

    public Application updateApplicationStatus(String applicationId, String newStatusStr) {
        Application application = getApplicationById(applicationId);

        ApplicationStatus newStatus;
        try {
            newStatus = ApplicationStatus.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatusStr
                    + ". Valid statuses are: SUBMITTED, UNDER_REVIEW, SHORTLISTED, SELECTED, REJECTED, WITHDRAWN");
        }

        if (!application.transitionTo(newStatus)) {
            throw new InvalidTransitionException(
                    "Cannot transition from " + application.getStatus() + " to " + newStatus + " for application " + applicationId);
        }

        applicationRepository.save(application);
        logger.info("Application {} status changed to {}", applicationId, newStatus);
        return application;
    }
}
