package edu.campus.ccrm.domain.enums;

/**
 * Represents the status of an enrollment in the system.
 */
public enum EnrollmentStatus {
    ACTIVE("Active", "Student is currently enrolled in the course"),
    COMPLETED("Completed", "Student has completed the course with a grade"),
    WITHDRAWN("Withdrawn", "Student has withdrawn from the course"),
    DROPPED("Dropped", "Student has been dropped from the course"),
    INCOMPLETE("Incomplete", "Student has incomplete work in the course");

    private final String displayName;
    private final String description;

    EnrollmentStatus(String displayName, String description) {
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
     * Checks if enrollment is still active
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Checks if enrollment is finalized
     */
    public boolean isFinalized() {
        return this == COMPLETED || this == WITHDRAWN || this == DROPPED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
