package edu.campus.ccrm.domain.enums;

/**
 * Represents the status of a student in the system.
 */
public enum StudentStatus {
    ACTIVE("Active", "Student is currently enrolled and active"),
    INACTIVE("Inactive", "Student is not currently enrolled"),
    GRADUATED("Graduated", "Student has completed their degree"),
    SUSPENDED("Suspended", "Student is temporarily suspended"),
    DROPPED("Dropped", "Student has been dropped from the institution"),
    ON_LEAVE("On Leave", "Student is on temporary leave");

    private final String displayName;
    private final String description;

    StudentStatus(String displayName, String description) {
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
     * Checks if student can enroll in courses
     */
    public boolean canEnroll() {
        return this == ACTIVE || this == ON_LEAVE;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
