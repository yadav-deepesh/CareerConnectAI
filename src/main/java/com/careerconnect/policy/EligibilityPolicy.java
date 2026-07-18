package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;

public interface EligibilityPolicy {

    EligibilityResult evaluate(Student student, PlacementDrive drive);
}
