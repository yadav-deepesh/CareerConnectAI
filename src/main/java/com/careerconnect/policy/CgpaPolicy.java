package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;

import java.util.List;

public class CgpaPolicy implements EligibilityPolicy {

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        if (student.getCgpa() >= drive.getMinCgpa()) {
            return new EligibilityResult(true, List.of("CGPA requirement met: " + student.getCgpa() + " >= " + drive.getMinCgpa()));
        }
        return new EligibilityResult(false, List.of("Minimum CGPA required: " + drive.getMinCgpa() + "; current CGPA: " + student.getCgpa()));
    }
}
