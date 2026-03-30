package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when an enrollment is not found in the system.
 */
public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
