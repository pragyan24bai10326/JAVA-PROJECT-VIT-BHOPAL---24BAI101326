package edu.campus.ccrm.service;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.exception.StudentNotFoundException;
import edu.campus.ccrm.exception.InvalidEnrollmentException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Service class for managing student operations.
 * Implements business logic for student management with functional programming.
 */
public class StudentService {
    private final Map<String, Student> students;
    private final EnrollmentService enrollmentService;

    public StudentService(EnrollmentService enrollmentService) {
        this.students = new HashMap<>();
        this.enrollmentService = enrollmentService;
    }

    /**
     * Creates a new student using Builder pattern
     */
    public Student createStudent(String studentId, String firstName, String lastName,
            String email, String phoneNumber, String address,
            java.time.LocalDate dateOfBirth, java.time.LocalDate enrollmentDate) {

        Student student = new Student.Builder()
                .studentId(studentId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .enrollmentDate(enrollmentDate)
                .status(StudentStatus.ACTIVE)
                .build();

        students.put(studentId, student);
        return student;
    }

    /**
     * Updates student information
     */
    public Student updateStudent(String studentId, String firstName, String lastName,
            String email, String phoneNumber, String address) {
        Student existing = getStudentById(studentId);

        Student updated = new Student.Builder()
                .studentId(studentId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(existing.getDateOfBirth())
                .enrollmentDate(existing.getEnrollmentDate())
                .status(existing.getStatus())
                .enrollments(existing.getEnrollments())
                .gpaHistory(existing.getGpaHistory())
                .build();

        students.put(studentId, updated);
        return updated;
    }

    /**
     * Retrieves student by ID
     */
    public Student getStudentById(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        return student;
    }

    /**
     * Lists all students with optional filtering using functional programming
     */
    public List<Student> getAllStudents(Predicate<Student> filter) {
        return students.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Student::getLastName)
                        .thenComparing(Student::getFirstName))
                .collect(Collectors.toList());
    }

    /**
     * Lists all students
     */
    public List<Student> getAllStudents() {
        return getAllStudents(student -> true);
    }

    /**
     * Searches students by name using functional programming
     */
    public List<Student> searchStudentsByName(String name) {
        String searchTerm = name.toLowerCase();
        return students.values().stream()
                .filter(student -> student.getFirstName().toLowerCase().contains(searchTerm) ||
                        student.getLastName().toLowerCase().contains(searchTerm) ||
                        (student.getFirstName() + " " + student.getLastName()).toLowerCase().contains(searchTerm))
                .sorted(Comparator.comparing(Student::getLastName)
                        .thenComparing(Student::getFirstName))
                .collect(Collectors.toList());
    }

    /**
     * Filters students by status using functional programming
     */
    public List<Student> getStudentsByStatus(StudentStatus status) {
        return getAllStudents(student -> student.getStatus() == status);
    }

    /**
     * Filters students by GPA range using functional programming
     */
    public List<Student> getStudentsByGPARange(double minGPA, double maxGPA) {
        return getAllStudents(student -> {
            double gpa = student.calculateCurrentGPA();
            return gpa >= minGPA && gpa <= maxGPA;
        });
    }

    /**
     * Deactivates a student (soft delete)
     */
    public void deactivateStudent(String studentId) {
        Student student = getStudentById(studentId);
        Student deactivated = new Student.Builder()
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .address(student.getAddress())
                .dateOfBirth(student.getDateOfBirth())
                .enrollmentDate(student.getEnrollmentDate())
                .status(StudentStatus.INACTIVE)
                .enrollments(student.getEnrollments())
                .gpaHistory(student.getGpaHistory())
                .build();

        students.put(studentId, deactivated);
    }

    /**
     * Enrolls student in a course
     */
    public void enrollStudentInCourse(String studentId, String courseCode, Semester semester) {
        Student student = getStudentById(studentId);

        if (!student.getStatus().canEnroll()) {
            throw new InvalidEnrollmentException("Student cannot enroll: " + student.getStatus());
        }

        enrollmentService.enrollStudent(student, courseCode, semester);
    }

    /**
     * Generates student transcript using functional programming
     */
    public String generateTranscript(String studentId) {
        Student student = getStudentById(studentId);

        StringBuilder transcript = new StringBuilder();
        transcript.append("=".repeat(60)).append("\n");
        transcript.append("OFFICIAL TRANSCRIPT\n");
        transcript.append("=".repeat(60)).append("\n");
        transcript.append("Student: ").append(student.getFirstName()).append(" ").append(student.getLastName())
                .append("\n");
        transcript.append("ID: ").append(student.getStudentId()).append("\n");
        transcript.append("Enrollment Date: ").append(student.getEnrollmentDate()).append("\n");
        transcript.append("=".repeat(60)).append("\n\n");

        // Group enrollments by semester
        Map<Semester, List<Enrollment>> enrollmentsBySemester = student.getEnrollments().stream()
                .collect(Collectors.groupingBy(Enrollment::getSemester,
                        TreeMap::new, // Maintains chronological order
                        Collectors.toList()));

        for (Map.Entry<Semester, List<Enrollment>> entry : enrollmentsBySemester.entrySet()) {
            Semester semester = entry.getKey();
            List<Enrollment> enrollments = entry.getValue();

            transcript.append(semester).append("\n");
            transcript.append("-".repeat(40)).append("\n");

            double semesterGPA = enrollments.stream()
                    .filter(e -> e.getGrade() != null && e.getGrade().countsTowardsGPA())
                    .mapToDouble(Enrollment::getQualityPoints)
                    .sum()
                    / enrollments.stream()
                            .filter(e -> e.getGrade() != null && e.getGrade().countsTowardsGPA())
                            .mapToInt(e -> e.getCourse().getCredits())
                            .sum();

            transcript.append(String.format("Semester GPA: %.2f\n", semesterGPA));
            transcript.append("\n");

            for (Enrollment enrollment : enrollments) {
                transcript.append(String.format("%-10s %-30s %3d %s\n",
                        enrollment.getCourse().getCourseCode(),
                        enrollment.getCourse().getCourseName(),
                        enrollment.getCourse().getCredits(),
                        enrollment.getGrade() != null ? enrollment.getGrade() : "IP"));
            }
            transcript.append("\n");
        }

        transcript.append("=".repeat(60)).append("\n");
        transcript.append(String.format("Overall GPA: %.2f\n", student.calculateCurrentGPA()));
        transcript.append(String.format("Total Credits: %d\n", student.getTotalCreditsEarned()));
        transcript.append("=".repeat(60)).append("\n");

        return transcript.toString();
    }

    /**
     * Gets student statistics using functional programming
     */
    public Map<String, Object> getStudentStatistics() {
        return Map.of(
                "totalStudents", students.size(),
                "activeStudents", getStudentsByStatus(StudentStatus.ACTIVE).size(),
                "graduatedStudents", getStudentsByStatus(StudentStatus.GRADUATED).size(),
                "averageGPA", students.values().stream()
                        .mapToDouble(Student::calculateCurrentGPA)
                        .average().orElse(0.0),
                "totalCreditsEarned", students.values().stream()
                        .mapToInt(Student::getTotalCreditsEarned)
                        .sum());
    }
}
