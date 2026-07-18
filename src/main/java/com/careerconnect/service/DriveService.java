package com.careerconnect.service;

import com.careerconnect.exception.ResourceNotFoundException;
import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;
import com.careerconnect.policy.EligibilityPolicy;
import com.careerconnect.repository.CompanyRepository;
import com.careerconnect.repository.DriveRepository;
import com.careerconnect.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class DriveService {

    private static final Logger logger = LoggerFactory.getLogger(DriveService.class);
    private final DriveRepository driveRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final EligibilityPolicy eligibilityPolicy;
    private final AtomicInteger idCounter = new AtomicInteger(100);

    public DriveService(DriveRepository driveRepository,
                        CompanyRepository companyRepository,
                        StudentRepository studentRepository,
                        EligibilityPolicy eligibilityPolicy) {
        this.driveRepository = driveRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.eligibilityPolicy = eligibilityPolicy;
    }

    public PlacementDrive createDrive(String companyId, String role, String location,
                                       double packageLpa, LocalDate deadline,
                                       List<String> requiredSkills, double minCgpa,
                                       int maxBacklogs, List<String> allowedProgrammes,
                                       int graduationYear) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        if (deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }

        String id = "DRV-" + idCounter.getAndIncrement();
        PlacementDrive drive = new PlacementDrive(id, companyId, role, location, packageLpa,
                deadline, requiredSkills, minCgpa, maxBacklogs, allowedProgrammes, graduationYear);
        driveRepository.save(drive);
        logger.info("Created placement drive: {} for company {}", id, companyId);
        return drive;
    }

    public PlacementDrive getDriveById(String driveId) {
        return driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found with ID: " + driveId));
    }

    public List<PlacementDrive> getAllDrives() {
        return driveRepository.findAll();
    }

    public List<PlacementDrive> filterDrives(String companyId, String role, String status, String location) {
        return driveRepository.findAll().stream()
                .filter(d -> companyId == null || companyId.isBlank() || d.getCompanyId().equalsIgnoreCase(companyId))
                .filter(d -> role == null || role.isBlank() || d.getRole().toLowerCase().contains(role.toLowerCase()))
                .filter(d -> status == null || status.isBlank() || d.getStatus().name().equalsIgnoreCase(status))
                .filter(d -> location == null || location.isBlank() || d.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
    }

    public EligibilityResult evaluateEligibility(String driveId, String studentId) {
        PlacementDrive drive = getDriveById(driveId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        EligibilityResult result = eligibilityPolicy.evaluate(student, drive);
        logger.info("Eligibility check for student {} on drive {}: {}", studentId, driveId, result.isEligible());
        return result;
    }
}
