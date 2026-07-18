package com.careerconnect.controller;

import com.careerconnect.dto.request.CreateDriveRequest;
import com.careerconnect.dto.response.DriveResponse;
import com.careerconnect.dto.response.EligibilityResponse;
import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.service.DriveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drives")
public class DriveController {

    private final DriveService driveService;

    public DriveController(DriveService driveService) {
        this.driveService = driveService;
    }

    @PostMapping
    public ResponseEntity<DriveResponse> createDrive(@Valid @RequestBody CreateDriveRequest request) {
        PlacementDrive drive = driveService.createDrive(
                request.getCompanyId(), request.getRole(), request.getLocation(),
                request.getPackageLpa(), request.getDeadline(), request.getRequiredSkills(),
                request.getMinCgpa(), request.getMaxBacklogs(),
                request.getAllowedProgrammes(), request.getGraduationYear()
        );
        return new ResponseEntity<>(toResponse(drive), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DriveResponse>> getDrives(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location) {

        List<DriveResponse> drives = driveService.filterDrives(companyId, role, status, location)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(drives);
    }

    @GetMapping("/{driveId}")
    public ResponseEntity<DriveResponse> getDrive(@PathVariable String driveId) {
        PlacementDrive drive = driveService.getDriveById(driveId);
        return ResponseEntity.ok(toResponse(drive));
    }

    @GetMapping("/{driveId}/eligibility/{studentId}")
    public ResponseEntity<EligibilityResponse> checkEligibility(@PathVariable String driveId,
                                                                  @PathVariable String studentId) {
        EligibilityResult result = driveService.evaluateEligibility(driveId, studentId);
        EligibilityResponse response = new EligibilityResponse(studentId, driveId, result.isEligible(), result.getReasons());
        return ResponseEntity.ok(response);
    }

    private DriveResponse toResponse(PlacementDrive d) {
        return new DriveResponse(d.getId(), d.getCompanyId(), d.getRole(), d.getLocation(),
                d.getPackageLpa(), d.getDeadline(), d.getRequiredSkills(),
                d.getMinCgpa(), d.getMaxBacklogs(), d.getAllowedProgrammes(),
                d.getGraduationYear(), d.getStatus().name(), d.getCreatedAt());
    }
}
