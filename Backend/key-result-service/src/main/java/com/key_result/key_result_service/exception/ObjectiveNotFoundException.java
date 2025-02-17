package com.key_result.key_result_service.exception;

public class ObjectiveNotFoundException extends RuntimeException {

    public ObjectiveNotFoundException(String message) {
        super(message);
    }

    public ObjectiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
