package com.example.taskmanagement.controller.exception;

public class TaskProcessingException extends RuntimeException {
    public TaskProcessingException(String message) {
        super(message);
    }

    public TaskProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
