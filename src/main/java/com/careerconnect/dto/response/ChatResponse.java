package com.careerconnect.dto.response;

public class ChatResponse {

    private String answer;
    private String model;
    private boolean advisory;

    public ChatResponse(String answer, String model) {
        this.answer = answer;
        this.model = model;
        this.advisory = true;
    }

    public String getAnswer() { return answer; }
    public String getModel() { return model; }
    public boolean isAdvisory() { return advisory; }
}
