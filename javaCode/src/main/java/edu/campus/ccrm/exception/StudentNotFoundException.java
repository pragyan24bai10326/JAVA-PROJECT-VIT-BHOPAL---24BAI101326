package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when a student is not found in the system.
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
