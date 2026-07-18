package com.careerconnect.model;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResult {

    private boolean eligible;
    private List<String> reasons;

    public EligibilityResult(boolean eligible, List<String> reasons) {
        this.eligible = eligible;
        this.reasons = reasons != null ? new ArrayList<>(reasons) : new ArrayList<>();
    }

    public boolean isEligible() {
        return eligible;
    }

    public List<String> getReasons() {
        return new ArrayList<>(reasons);
    }
}
