package edu.campus.ccrm.domain.enums;

/**
 * Represents the academic level of a course.
 */
public enum CourseLevel {
    UNDERGRADUATE("Undergraduate", "Course is for undergraduate students"),
    GRADUATE("Graduate", "Course is for graduate students"),
    DOCTORAL("Doctoral", "Course is for doctoral students"),
    CONTINUING_EDUCATION("Continuing Education", "Course is for continuing education");

    private final String displayName;
    private final String description;

    CourseLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
