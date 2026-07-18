package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;

import java.util.List;

public class BacklogPolicy implements EligibilityPolicy {

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        if (student.getActiveBacklogs() <= drive.getMaxBacklogs()) {
            return new EligibilityResult(true, List.of("Backlog requirement met: " + student.getActiveBacklogs() + " <= " + drive.getMaxBacklogs()));
        }
        return new EligibilityResult(false, List.of("Maximum allowed backlogs: " + drive.getMaxBacklogs() + "; current backlogs: " + student.getActiveBacklogs()));
    }
}
