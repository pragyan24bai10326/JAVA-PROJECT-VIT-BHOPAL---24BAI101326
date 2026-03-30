package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when a course is not found in the system.
 */
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
