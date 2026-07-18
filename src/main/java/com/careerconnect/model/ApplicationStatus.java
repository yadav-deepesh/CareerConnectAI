package com.careerconnect.model;

import java.util.Map;
import java.util.Set;

public enum ApplicationStatus {

    SUBMITTED,
    UNDER_REVIEW,
    SHORTLISTED,
    SELECTED,
    REJECTED,
    WITHDRAWN;

    private static final Map<ApplicationStatus, Set<ApplicationStatus>> VALID_TRANSITIONS = Map.of(
            SUBMITTED, Set.of(UNDER_REVIEW, WITHDRAWN),
            UNDER_REVIEW, Set.of(SHORTLISTED, REJECTED, WITHDRAWN),
            SHORTLISTED, Set.of(SELECTED, REJECTED),
            SELECTED, Set.of(),
            REJECTED, Set.of(),
            WITHDRAWN, Set.of()
    );

    public boolean canTransitionTo(ApplicationStatus next) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(next);
    }
}
