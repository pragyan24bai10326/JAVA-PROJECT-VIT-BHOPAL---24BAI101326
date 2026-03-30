package edu.campus.ccrm.domain;

import edu.campus.ccrm.domain.enums.*;
import java.util.*;

/**
 * Represents a Course in the Campus Course & Records Management system.
 * Implements encapsulation and immutability patterns.
 */
public final class Course {
    private final String courseCode;
    private final String courseName;
    private final String description;
    private final int credits;
    private final String department;
    private final String instructor;
    private final CourseStatus status;
    private final Set<String> prerequisites;
    private final Map<String, String> courseSchedule;

    // Private constructor for Builder pattern
    private Course(Builder builder) {
        this.courseCode = builder.courseCode;
        this.courseName = builder.courseName;
        this.description = builder.description;
        this.credits = builder.credits;
        this.department = builder.department;
        this.instructor = builder.instructor;
        this.status = builder.status;
        this.prerequisites = Collections.unmodifiableSet(new HashSet<>(builder.prerequisites));
        this.courseSchedule = Collections.unmodifiableMap(new HashMap<>(builder.courseSchedule));
    }

    // Getters (immutable access)
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public int getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    public String getInstructor() {
        return instructor;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public Set<String> getPrerequisites() {
        return prerequisites;
    }

    public Map<String, String> getCourseSchedule() {
        return courseSchedule;
    }

    /**
     * Checks if this course is available for enrollment
     */
    public boolean isAvailable() {
        return status == CourseStatus.ACTIVE;
    }

    /**
     * Checks if a student meets prerequisites
     */
    public boolean meetsPrerequisites(Set<String> completedCourses) {
        return completedCourses.containsAll(prerequisites);
    }

    /**
     * Gets course level based on course code
     */
    public CourseLevel getCourseLevel() {
        if (courseCode.matches(".*[1-4]\\d{2}.*")) {
            return CourseLevel.UNDERGRADUATE;
        } else if (courseCode.matches(".*[5-9]\\d{2}.*")) {
            return CourseLevel.GRADUATE;
        }
        return CourseLevel.UNDERGRADUATE; // Default
    }

    /**
     * Builder pattern implementation
     */
    public static class Builder {
        private String courseCode;
        private String courseName;
        private String description;
        private int credits;
        private String department;
        private String instructor;
        private CourseStatus status = CourseStatus.ACTIVE;
        private Set<String> prerequisites = new HashSet<>();
        private Map<String, String> courseSchedule = new HashMap<>();

        public Builder courseCode(String courseCode) {
            this.courseCode = courseCode;
            return this;
        }

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder status(CourseStatus status) {
            this.status = status;
            return this;
        }

        public Builder prerequisites(Set<String> prerequisites) {
            this.prerequisites = prerequisites != null ? new HashSet<>(prerequisites) : new HashSet<>();
            return this;
        }

        public Builder courseSchedule(Map<String, String> courseSchedule) {
            this.courseSchedule = courseSchedule != null ? new HashMap<>(courseSchedule) : new HashMap<>();
            return this;
        }

        public Builder addPrerequisite(String prerequisite) {
            if (prerequisite != null && !prerequisite.trim().isEmpty()) {
                this.prerequisites.add(prerequisite);
            }
            return this;
        }

        public Builder addSchedule(String day, String time) {
            if (day != null && time != null) {
                this.courseSchedule.put(day, time);
            }
            return this;
        }

        public Course build() {
            validate();
            return new Course(this);
        }

        private void validate() {
            if (courseCode == null || courseCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code is required");
            }
            if (courseName == null || courseName.trim().isEmpty()) {
                throw new IllegalArgumentException("Course name is required");
            }
            if (credits <= 0 || credits > 6) {
                throw new IllegalArgumentException("Credits must be between 1 and 6");
            }
            if (department == null || department.trim().isEmpty()) {
                throw new IllegalArgumentException("Department is required");
            }
            if (instructor == null || instructor.trim().isEmpty()) {
                throw new IllegalArgumentException("Instructor is required");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("""
                Course Information:
                ===================
                Code: %s
                Name: %s
                Description: %s
                Credits: %d
                Department: %s
                Instructor: %s
                Status: %s
                Level: %s
                Prerequisites: %s
                Schedule: %s
                """,
                courseCode, courseName, description, credits, department, instructor,
                status, getCourseLevel(),
                prerequisites.isEmpty() ? "None" : String.join(", ", prerequisites),
                courseSchedule.isEmpty() ? "TBA" : courseSchedule.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Course course = (Course) obj;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
