package edu.campus.ccrm.domain;

import edu.campus.ccrm.domain.enums.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a Student in the Campus Course & Records Management system.
 * Implements encapsulation, immutability patterns, and functional programming.
 */
public final class Student {
    private final String studentId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final LocalDate enrollmentDate;
    private final StudentStatus status;
    private final Set<Enrollment> enrollments;
    private final Map<String, Double> gpaHistory;

    // Private constructor for Builder pattern
    private Student(Builder builder) {
        this.studentId = builder.studentId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.dateOfBirth = builder.dateOfBirth;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.enrollmentDate = builder.enrollmentDate;
        this.status = builder.status;
        this.enrollments = Collections.unmodifiableSet(new HashSet<>(builder.enrollments));
        this.gpaHistory = Collections.unmodifiableMap(new HashMap<>(builder.gpaHistory));
    }

    // Getters (immutable access)
    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public Map<String, Double> getGpaHistory() {
        return gpaHistory;
    }

    /**
     * Calculates current GPA using Stream API and functional programming
     */
    public double calculateCurrentGPA() {
        return enrollments.stream()
                .filter(e -> e.getGrade() != null && e.getGrade() != Grade.INCOMPLETE)
                .filter(e -> e.getSemester().equals(getCurrentSemester()))
                .mapToDouble(e -> e.getGrade().getNumericValue())
                .average()
                .orElse(0.0);
    }

    /**
     * Gets current semester using date logic
     */
    private Semester getCurrentSemester() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        if (month >= 1 && month <= 5)
            return new Semester(year, "SPRING");
        if (month >= 6 && month <= 8)
            return new Semester(year, "SUMMER");
        return new Semester(year, "FALL");
    }

    /**
     * Gets completed courses using functional programming
     */
    public List<Course> getCompletedCourses() {
        return enrollments.stream()
                .filter(e -> e.getGrade() != null && e.getGrade().isPassing())
                .map(Enrollment::getCourse)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets total credits earned using Stream API
     */
    public int getTotalCreditsEarned() {
        return enrollments.stream()
                .filter(e -> e.getGrade() != null && e.getGrade().isPassing())
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
    }

    /**
     * Checks if student can enroll in more courses (credit limit check)
     */
    public boolean canEnrollInMoreCourses(int additionalCredits) {
        int currentSemesterCredits = getCurrentSemesterCredits();
        return (currentSemesterCredits + additionalCredits) <= 21; // Max 21 credits per semester
    }

    private int getCurrentSemesterCredits() {
        return enrollments.stream()
                .filter(e -> e.getSemester().equals(getCurrentSemester()))
                .filter(e -> e.getGrade() == null || e.getGrade() == Grade.INCOMPLETE)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
    }

    /**
     * Builder pattern implementation
     */
    public static class Builder {
        private String studentId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String email;
        private String phoneNumber;
        private String address;
        private LocalDate enrollmentDate;
        private StudentStatus status = StudentStatus.ACTIVE;
        private Set<Enrollment> enrollments = new HashSet<>();
        private Map<String, Double> gpaHistory = new HashMap<>();

        public Builder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder enrollmentDate(LocalDate enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }

        public Builder status(StudentStatus status) {
            this.status = status;
            return this;
        }

        public Builder enrollments(Set<Enrollment> enrollments) {
            this.enrollments = enrollments != null ? new HashSet<>(enrollments) : new HashSet<>();
            return this;
        }

        public Builder gpaHistory(Map<String, Double> gpaHistory) {
            this.gpaHistory = gpaHistory != null ? new HashMap<>(gpaHistory) : new HashMap<>();
            return this;
        }

        public Student build() {
            validate();
            return new Student(this);
        }

        private void validate() {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID is required");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name is required");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Valid email is required");
            }
            if (enrollmentDate == null) {
                throw new IllegalArgumentException("Enrollment date is required");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("""
                Student Profile:
                ================
                ID: %s
                Name: %s %s
                Email: %s
                Phone: %s
                Address: %s
                DOB: %s
                Enrollment Date: %s
                Status: %s
                Current GPA: %.2f
                Total Credits Earned: %d
                Completed Courses: %d
                """,
                studentId, firstName, lastName, email, phoneNumber, address,
                dateOfBirth.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                enrollmentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                status, calculateCurrentGPA(), getTotalCreditsEarned(), getCompletedCourses().size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
