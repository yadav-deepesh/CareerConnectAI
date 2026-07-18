package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkillPolicy implements EligibilityPolicy {

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        List<String> requiredSkills = drive.getRequiredSkills();
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return new EligibilityResult(true, List.of("No specific skills required"));
        }

        List<String> studentSkillsLower = student.getSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        List<String> missingSkills = new ArrayList<>();
        for (String required : requiredSkills) {
            if (!studentSkillsLower.contains(required.toLowerCase())) {
                missingSkills.add(required);
            }
        }

        if (missingSkills.isEmpty()) {
            return new EligibilityResult(true, List.of("All required skills are present"));
        }

        return new EligibilityResult(false, List.of("Missing required skill(s): " + String.join(", ", missingSkills)));
    }
}
