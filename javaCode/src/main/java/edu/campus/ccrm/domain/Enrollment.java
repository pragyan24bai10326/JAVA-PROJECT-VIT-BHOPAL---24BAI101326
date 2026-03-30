package edu.campus.ccrm.domain;

import edu.campus.ccrm.domain.enums.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an Enrollment relationship between a Student and Course.
 * Implements encapsulation and immutability patterns.
 */
public final class Enrollment {
    private final String enrollmentId;
    private final Student student;
    private final Course course;
    private final Semester semester;
    private final LocalDate enrollmentDate;
    private final Grade grade;
    private final String notes;
    private final EnrollmentStatus status;

    // Private constructor for Builder pattern
    private Enrollment(Builder builder) {
        this.enrollmentId = builder.enrollmentId;
        this.student = builder.student;
        this.course = builder.course;
        this.semester = builder.semester;
        this.enrollmentDate = builder.enrollmentDate;
        this.grade = builder.grade;
        this.notes = builder.notes;
        this.status = builder.status;
    }

    // Getters (immutable access)
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Semester getSemester() {
        return semester;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getNotes() {
        return notes;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    /**
     * Checks if enrollment is completed (has a grade)
     */
    public boolean isCompleted() {
        return grade != null && status == EnrollmentStatus.COMPLETED;
    }

    /**
     * Checks if enrollment is active
     */
    public boolean isActive() {
        return status == EnrollmentStatus.ACTIVE;
    }

    /**
     * Gets quality points for GPA calculation
     */
    public double getQualityPoints() {
        if (grade == null)
            return 0.0;
        return grade.getNumericValue() * course.getCredits();
    }

    /**
     * Builder pattern implementation
     */
    public static class Builder {
        private String enrollmentId;
        private Student student;
        private Course course;
        private Semester semester;
        private LocalDate enrollmentDate;
        private Grade grade;
        private String notes;
        private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

        public Builder enrollmentId(String enrollmentId) {
            this.enrollmentId = enrollmentId;
            return this;
        }

        public Builder student(Student student) {
            this.student = student;
            return this;
        }

        public Builder course(Course course) {
            this.course = course;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Builder enrollmentDate(LocalDate enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }

        public Builder grade(Grade grade) {
            this.grade = grade;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder status(EnrollmentStatus status) {
            this.status = status;
            return this;
        }

        public Enrollment build() {
            validate();
            return new Enrollment(this);
        }

        private void validate() {
            if (enrollmentId == null || enrollmentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Enrollment ID is required");
            }
            if (student == null) {
                throw new IllegalArgumentException("Student is required");
            }
            if (course == null) {
                throw new IllegalArgumentException("Course is required");
            }
            if (semester == null) {
                throw new IllegalArgumentException("Semester is required");
            }
            if (enrollmentDate == null) {
                throw new IllegalArgumentException("Enrollment date is required");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("""
                Enrollment Details:
                ===================
                ID: %s
                Student: %s %s (%s)
                Course: %s - %s
                Semester: %s
                Enrollment Date: %s
                Grade: %s
                Status: %s
                Notes: %s
                """,
                enrollmentId,
                student.getFirstName(), student.getLastName(), student.getStudentId(),
                course.getCourseCode(), course.getCourseName(),
                semester,
                enrollmentDate,
                grade != null ? grade : "Not Graded",
                status,
                notes != null ? notes : "None");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Enrollment enrollment = (Enrollment) obj;
        return Objects.equals(enrollmentId, enrollment.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }
}
