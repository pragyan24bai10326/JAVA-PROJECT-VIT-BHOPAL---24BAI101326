package edu.campus.ccrm.domain.enums;

/**
 * Represents the status of a course in the system.
 */
public enum CourseStatus {
    ACTIVE("Active", "Course is available for enrollment"),
    INACTIVE("Inactive", "Course is not currently offered"),
    CANCELLED("Cancelled", "Course has been cancelled for the semester"),
    FULL("Full", "Course has reached maximum enrollment"),
    ARCHIVED("Archived", "Course is no longer offered");

    private final String displayName;
    private final String description;

    CourseStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if course accepts new enrollments
     */
    public boolean acceptsEnrollment() {
        return this == ACTIVE;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
