package com.objective.objective_service.exception;

public class ObjectiveNotFoundException extends RuntimeException {

    public ObjectiveNotFoundException(String message) {
        super(message);
    }

    public ObjectiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
