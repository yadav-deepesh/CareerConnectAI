package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;

import java.util.List;

public class GraduationYearPolicy implements EligibilityPolicy {

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        if (drive.getGraduationYear() == 0) {
            return new EligibilityResult(true, List.of("No graduation year restriction"));
        }

        if (student.getGraduationYear() == drive.getGraduationYear()) {
            return new EligibilityResult(true, List.of("Graduation year matches: " + student.getGraduationYear()));
        }

        return new EligibilityResult(false, List.of(
                "Required graduation year: " + drive.getGraduationYear() + "; student graduation year: " + student.getGraduationYear()));
    }
}
