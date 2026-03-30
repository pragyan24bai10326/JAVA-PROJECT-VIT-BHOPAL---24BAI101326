package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when an enrollment operation is invalid.
 */
public class InvalidEnrollmentException extends RuntimeException {
    public InvalidEnrollmentException(String message) {
        super(message);
    }

    public InvalidEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
