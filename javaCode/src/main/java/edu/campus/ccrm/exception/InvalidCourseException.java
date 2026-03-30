package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when a course operation is invalid.
 */
public class InvalidCourseException extends RuntimeException {
    public InvalidCourseException(String message) {
        super(message);
    }

    public InvalidCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
