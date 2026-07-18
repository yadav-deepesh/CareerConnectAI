package com.careerconnect.policy;

import com.careerconnect.model.EligibilityResult;
import com.careerconnect.model.PlacementDrive;
import com.careerconnect.model.Student;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompositeEligibilityPolicy implements EligibilityPolicy {

    private final List<EligibilityPolicy> policies;

    public CompositeEligibilityPolicy() {
        this.policies = List.of(
                new CgpaPolicy(),
                new BacklogPolicy(),
                new SkillPolicy(),
                new GraduationYearPolicy()
        );
    }

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        boolean allPassed = true;
        List<String> allReasons = new ArrayList<>();

        for (EligibilityPolicy policy : policies) {
            EligibilityResult result = policy.evaluate(student, drive);
            if (!result.isEligible()) {
                allPassed = false;
            }
            allReasons.addAll(result.getReasons());
        }

        return new EligibilityResult(allPassed, allReasons);
    }
}
