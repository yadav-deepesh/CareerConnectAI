package com.careerconnect.exception;

public class DeadlinePassedException extends RuntimeException {
    public DeadlinePassedException(String message) {
        super(message);
    }
}
