package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when a grade operation is invalid.
 */
public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(String message) {
        super(message);
    }

    public InvalidGradeException(String message, Throwable cause) {
        super(message, cause);
    }
}
