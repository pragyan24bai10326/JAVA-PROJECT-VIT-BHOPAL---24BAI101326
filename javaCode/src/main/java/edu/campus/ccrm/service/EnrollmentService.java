package edu.campus.ccrm.service;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.exception.EnrollmentNotFoundException;
import edu.campus.ccrm.exception.InvalidEnrollmentException;
import edu.campus.ccrm.exception.InvalidGradeException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Service class for managing enrollment operations.
 * Implements business logic for enrollment management with functional
 * programming.
 */
public class EnrollmentService {
    private final Map<String, Enrollment> enrollments;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.enrollments = new HashMap<>();
        this.studentService = studentService;
        this.courseService = courseService;
    }

    /**
     * Enrolls a student in a course
     */
    public Enrollment enrollStudent(Student student, String courseCode, Semester semester) {
        Course course = courseService.getCourseByCode(courseCode);

        // Validate enrollment eligibility
        validateEnrollment(student, course, semester);

        String enrollmentId = generateEnrollmentId(student.getStudentId(), courseCode, semester);

        Enrollment enrollment = new Enrollment.Builder()
                .enrollmentId(enrollmentId)
                .student(student)
                .course(course)
                .semester(semester)
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollments.put(enrollmentId, enrollment);

        // Update student's enrollments
        updateStudentEnrollments(student, enrollment);

        return enrollment;
    }

    /**
     * Records a grade for an enrollment
     */
    public void recordGrade(String enrollmentId, Grade grade, String notes) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);

        if (!enrollment.isActive()) {
            throw new InvalidEnrollmentException("Cannot record grade for inactive enrollment");
        }

        Enrollment updated = new Enrollment.Builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .student(enrollment.getStudent())
                .course(enrollment.getCourse())
                .semester(enrollment.getSemester())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .grade(grade)
                .notes(notes)
                .status(grade == Grade.INCOMPLETE ? EnrollmentStatus.INCOMPLETE : EnrollmentStatus.COMPLETED)
                .build();

        enrollments.put(enrollmentId, updated);

        // Update student's enrollments
        updateStudentEnrollments(enrollment.getStudent(), updated);
    }

    /**
     * Withdraws a student from a course
     */
    public void withdrawFromCourse(String enrollmentId, String reason) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);

        if (!enrollment.isActive()) {
            throw new InvalidEnrollmentException("Cannot withdraw from inactive enrollment");
        }

        Enrollment withdrawn = new Enrollment.Builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .student(enrollment.getStudent())
                .course(enrollment.getCourse())
                .semester(enrollment.getSemester())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .grade(Grade.WITHDRAWAL)
                .notes(reason)
                .status(EnrollmentStatus.WITHDRAWN)
                .build();

        enrollments.put(enrollmentId, withdrawn);

        // Update student's enrollments
        updateStudentEnrollments(enrollment.getStudent(), withdrawn);
    }

    /**
     * Retrieves enrollment by ID
     */
    public Enrollment getEnrollmentById(String enrollmentId) {
        Enrollment enrollment = enrollments.get(enrollmentId);
        if (enrollment == null) {
            throw new EnrollmentNotFoundException("Enrollment not found with ID: " + enrollmentId);
        }
        return enrollment;
    }

    /**
     * Gets all enrollments for a student using functional programming
     */
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getStudent().getStudentId().equals(studentId))
                .sorted(Comparator.comparing(Enrollment::getSemester)
                        .thenComparing(e -> e.getCourse().getCourseCode()))
                .collect(Collectors.toList());
    }

    /**
     * Gets all enrollments for a course using functional programming
     */
    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getCourse().getCourseCode().equals(courseCode))
                .sorted(Comparator.comparing(Enrollment::getSemester)
                        .thenComparing(e -> e.getStudent().getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Gets enrollments by semester using functional programming
     */
    public List<Enrollment> getEnrollmentsBySemester(Semester semester) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.getSemester().equals(semester))
                .sorted(Comparator.comparing(e -> e.getStudent().getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Gets active enrollments using functional programming
     */
    public List<Enrollment> getActiveEnrollments() {
        return enrollments.values().stream()
                .filter(Enrollment::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets completed enrollments using functional programming
     */
    public List<Enrollment> getCompletedEnrollments() {
        return enrollments.values().stream()
                .filter(Enrollment::isCompleted)
                .collect(Collectors.toList());
    }

    /**
     * Calculates GPA for a student in a specific semester using functional
     * programming
     */
    public double calculateSemesterGPA(String studentId, Semester semester) {
        List<Enrollment> semesterEnrollments = enrollments.values().stream()
                .filter(e -> e.getStudent().getStudentId().equals(studentId))
                .filter(e -> e.getSemester().equals(semester))
                .filter(e -> e.getGrade() != null && e.getGrade().countsTowardsGPA())
                .collect(Collectors.toList());

        if (semesterEnrollments.isEmpty()) {
            return 0.0;
        }

        double totalQualityPoints = semesterEnrollments.stream()
                .mapToDouble(Enrollment::getQualityPoints)
                .sum();

        int totalCredits = semesterEnrollments.stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();

        return totalCredits > 0 ? totalQualityPoints / totalCredits : 0.0;
    }

    /**
     * Gets enrollment statistics using functional programming
     */
    public Map<String, Object> getEnrollmentStatistics() {
        return Map.of(
                "totalEnrollments", enrollments.size(),
                "activeEnrollments", getActiveEnrollments().size(),
                "completedEnrollments", getCompletedEnrollments().size(),
                "enrollmentsBySemester", enrollments.values().stream()
                        .collect(Collectors.groupingBy(Enrollment::getSemester, Collectors.counting())),
                "enrollmentsByCourse", enrollments.values().stream()
                        .collect(Collectors.groupingBy(e -> e.getCourse().getCourseCode(), Collectors.counting())),
                "averageGrade", enrollments.values().stream()
                        .filter(e -> e.getGrade() != null && e.getGrade().countsTowardsGPA())
                        .mapToDouble(e -> e.getGrade().getNumericValue())
                        .average().orElse(0.0));
    }

    /**
     * Validates enrollment eligibility
     */
    private void validateEnrollment(Student student, Course course, Semester semester) {
        if (!student.getStatus().canEnroll()) {
            throw new InvalidEnrollmentException("Student cannot enroll: " + student.getStatus());
        }

        if (!course.isAvailable()) {
            throw new InvalidEnrollmentException("Course is not available: " + course.getStatus());
        }

        // Check if already enrolled in this course for this semester
        boolean alreadyEnrolled = enrollments.values().stream()
                .anyMatch(e -> e.getStudent().equals(student) &&
                        e.getCourse().equals(course) &&
                        e.getSemester().equals(semester) &&
                        e.isActive());

        if (alreadyEnrolled) {
            throw new InvalidEnrollmentException("Student already enrolled in this course for this semester");
        }

        // Check credit limit
        int currentSemesterCredits = enrollments.values().stream()
                .filter(e -> e.getStudent().equals(student))
                .filter(e -> e.getSemester().equals(semester))
                .filter(Enrollment::isActive)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();

        if (currentSemesterCredits + course.getCredits() > 21) {
            throw new InvalidEnrollmentException("Credit limit exceeded for semester");
        }

        // Check prerequisites
        Set<String> completedCourses = student.getCompletedCourses().stream()
                .map(Course::getCourseCode)
                .collect(Collectors.toSet());

        if (!course.meetsPrerequisites(completedCourses)) {
            throw new InvalidEnrollmentException("Prerequisites not met for course: " + course.getCourseCode());
        }
    }

    /**
     * Generates unique enrollment ID
     */
    private String generateEnrollmentId(String studentId, String courseCode, Semester semester) {
        return String.format("%s_%s_%s_%d",
                studentId, courseCode, semester.getSeason(), semester.getYear());
    }

    /**
     * Updates student's enrollments (helper method)
     */
    private void updateStudentEnrollments(Student student, Enrollment enrollment) {
        Set<Enrollment> updatedEnrollments = student.getEnrollments().stream()
                .filter(e -> !e.getEnrollmentId().equals(enrollment.getEnrollmentId()))
                .collect(Collectors.toSet());
        updatedEnrollments.add(enrollment);

        // This would require updating the student object, which is handled by the
        // service layer
    }

    /**
     * Gets all enrollments with optional filtering
     */
    public List<Enrollment> getAllEnrollments(Predicate<Enrollment> filter) {
        return enrollments.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Enrollment::getSemester)
                        .thenComparing(e -> e.getStudent().getLastName())
                        .thenComparing(e -> e.getCourse().getCourseCode()))
                .collect(Collectors.toList());
    }

    /**
     * Gets all enrollments
     */
    public List<Enrollment> getAllEnrollments() {
        return getAllEnrollments(enrollment -> true);
    }
}
