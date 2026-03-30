package edu.campus.ccrm.cli;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.service.*;
import edu.campus.ccrm.io.FileDataManager;
import edu.campus.ccrm.exception.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Command Line Interface for the Campus Course & Records Manager.
 * Implements a menu-driven workflow with robust exception handling.
 */
public class CLIMenu {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final FileDataManager fileDataManager;
    private boolean running;

    public CLIMenu() {
        this.scanner = new Scanner(System.in);
        this.courseService = new CourseService();
        this.fileDataManager = new FileDataManager();
        this.running = true;

        // Initialize services with cross-references
        StudentService tempStudentService = new StudentService(null);
        this.enrollmentService = new EnrollmentService(tempStudentService, courseService);
        this.studentService = new StudentService(enrollmentService);

        try {
            fileDataManager.initializeDataDirectory();
            loadInitialData();
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize data directory: " + e.getMessage());
        }
    }

    /**
     * Main menu loop using functional programming approach
     */
    public void run() {
        System.out.println("=".repeat(60));
        System.out.println("   Campus Course & Records Manager (CCRM)");
        System.out.println("=".repeat(60));

        while (running) {
            try {
                displayMainMenu();
                int choice = getValidIntegerInput(1, 8);
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }

        System.out.println("Thank you for using CCRM!");
        scanner.close();
    }

    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Reports & Transcripts");
        System.out.println("5. Data Import/Export");
        System.out.println("6. Backup & Restore");
        System.out.println("7. System Statistics");
        System.out.println("8. Exit");
        System.out.println("=".repeat(40));
        System.out.print("Enter your choice (1-8): ");
    }

    /**
     * Handles main menu choice using functional programming
     */
    private void handleMainMenuChoice(int choice) {
        Map<Integer, Runnable> menuActions = Map.of(
                1, this::studentManagementMenu,
                2, this::courseManagementMenu,
                3, this::enrollmentManagementMenu,
                4, this::reportsMenu,
                5, this::dataManagementMenu,
                6, this::backupMenu,
                7, this::systemStatisticsMenu,
                8, () -> running = false);

        menuActions.getOrDefault(choice, () -> {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }).run();
    }

    /**
     * Student Management Menu
     */
    private void studentManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("STUDENT MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. Add New Student");
            System.out.println("2. Update Student");
            System.out.println("3. View Student Details");
            System.out.println("4. List All Students");
            System.out.println("5. Search Students");
            System.out.println("6. Deactivate Student");
            System.out.println("7. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-7): ");

            int choice = getValidIntegerInput(1, 7);

            switch (choice) {
                case 1 -> addNewStudent();
                case 2 -> updateStudent();
                case 3 -> viewStudentDetails();
                case 4 -> listAllStudents();
                case 5 -> searchStudents();
                case 6 -> deactivateStudent();
                case 7 -> {
                    return;
                }
            }
        }
    }

    /**
     * Course Management Menu
     */
    private void courseManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("COURSE MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. Add New Course");
            System.out.println("2. Update Course");
            System.out.println("3. View Course Details");
            System.out.println("4. List All Courses");
            System.out.println("5. Search Courses");
            System.out.println("6. Deactivate Course");
            System.out.println("7. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-7): ");

            int choice = getValidIntegerInput(1, 7);

            switch (choice) {
                case 1 -> addNewCourse();
                case 2 -> updateCourse();
                case 3 -> viewCourseDetails();
                case 4 -> listAllCourses();
                case 5 -> searchCourses();
                case 6 -> deactivateCourse();
                case 7 -> {
                    return;
                }
            }
        }
    }

    /**
     * Enrollment Management Menu
     */
    private void enrollmentManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("ENROLLMENT MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Record Grade");
            System.out.println("3. Withdraw from Course");
            System.out.println("4. View Enrollment Details");
            System.out.println("5. List Enrollments");
            System.out.println("6. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-6): ");

            int choice = getValidIntegerInput(1, 6);

            switch (choice) {
                case 1 -> enrollStudentInCourse();
                case 2 -> recordGrade();
                case 3 -> withdrawFromCourse();
                case 4 -> viewEnrollmentDetails();
                case 5 -> listEnrollments();
                case 6 -> {
                    return;
                }
            }
        }
    }

    /**
     * Reports Menu
     */
    private void reportsMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("REPORTS & TRANSCRIPTS");
            System.out.println("=".repeat(30));
            System.out.println("1. Generate Student Transcript");
            System.out.println("2. View Student Profile");
            System.out.println("3. Course Enrollment Report");
            System.out.println("4. GPA Report");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-5): ");

            int choice = getValidIntegerInput(1, 5);

            switch (choice) {
                case 1 -> generateTranscript();
                case 2 -> viewStudentProfile();
                case 3 -> courseEnrollmentReport();
                case 4 -> gpaReport();
                case 5 -> {
                    return;
                }
            }
        }
    }

    /**
     * Data Management Menu
     */
    private void dataManagementMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("DATA MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. Export All Data to CSV");
            System.out.println("2. Import Data from CSV");
            System.out.println("3. Export Students Only");
            System.out.println("4. Export Courses Only");
            System.out.println("5. Export Enrollments Only");
            System.out.println("6. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-6): ");

            int choice = getValidIntegerInput(1, 6);

            switch (choice) {
                case 1 -> exportAllData();
                case 2 -> importData();
                case 3 -> exportStudentsOnly();
                case 4 -> exportCoursesOnly();
                case 5 -> exportEnrollmentsOnly();
                case 6 -> {
                    return;
                }
            }
        }
    }

    /**
     * Backup Menu
     */
    private void backupMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("BACKUP & RESTORE");
            System.out.println("=".repeat(30));
            System.out.println("1. Create Backup");
            System.out.println("2. Restore from Backup");
            System.out.println("3. List Available Backups");
            System.out.println("4. Back to Main Menu");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice (1-4): ");

            int choice = getValidIntegerInput(1, 4);

            switch (choice) {
                case 1 -> createBackup();
                case 2 -> restoreFromBackup();
                case 3 -> listAvailableBackups();
                case 4 -> {
                    return;
                }
            }
        }
    }

    /**
     * System Statistics Menu
     */
    private void systemStatisticsMenu() {
        try {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("SYSTEM STATISTICS");
            System.out.println("=".repeat(40));

            Map<String, Object> studentStats = studentService.getStudentStatistics();
            Map<String, Object> courseStats = courseService.getCourseStatistics();
            Map<String, Object> enrollmentStats = enrollmentService.getEnrollmentStatistics();

            System.out.println("STUDENT STATISTICS:");
            System.out.printf("  Total Students: %d%n", studentStats.get("totalStudents"));
            System.out.printf("  Active Students: %d%n", studentStats.get("activeStudents"));
            System.out.printf("  Graduated Students: %d%n", studentStats.get("graduatedStudents"));
            System.out.printf("  Average GPA: %.2f%n", studentStats.get("averageGPA"));
            System.out.printf("  Total Credits Earned: %d%n", studentStats.get("totalCreditsEarned"));

            System.out.println("\nCOURSE STATISTICS:");
            System.out.printf("  Total Courses: %d%n", courseStats.get("totalCourses"));
            System.out.printf("  Active Courses: %d%n", courseStats.get("activeCourses"));
            System.out.printf("  Average Credits: %.1f%n", courseStats.get("averageCredits"));

            System.out.println("\nENROLLMENT STATISTICS:");
            System.out.printf("  Total Enrollments: %d%n", enrollmentStats.get("totalEnrollments"));
            System.out.printf("  Active Enrollments: %d%n", enrollmentStats.get("activeEnrollments"));
            System.out.printf("  Completed Enrollments: %d%n", enrollmentStats.get("completedEnrollments"));
            System.out.printf("  Average Grade: %.2f%n", enrollmentStats.get("averageGrade"));

        } catch (Exception e) {
            System.err.println("Error generating statistics: " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Student Management Methods

    private void addNewStudent() {
        try {
            System.out.println("\n--- Add New Student ---");

            String studentId = getStringInput("Enter Student ID: ");
            String firstName = getStringInput("Enter First Name: ");
            String lastName = getStringInput("Enter Last Name: ");
            String email = getStringInput("Enter Email: ");
            String phoneNumber = getStringInput("Enter Phone Number: ");
            String address = getStringInput("Enter Address: ");
            LocalDate dateOfBirth = getDateInput("Enter Date of Birth (yyyy-MM-dd): ");
            LocalDate enrollmentDate = getDateInput("Enter Enrollment Date (yyyy-MM-dd): ");

            Student student = studentService.createStudent(
                    studentId, firstName, lastName, email, phoneNumber,
                    address, dateOfBirth, enrollmentDate);

            System.out.println("\nStudent created successfully!");
            System.out.println(student);

        } catch (Exception e) {
            System.err.println("Error creating student: " + e.getMessage());
        }

        pauseForInput();
    }

    private void updateStudent() {
        try {
            System.out.println("\n--- Update Student ---");
            String studentId = getStringInput("Enter Student ID to update: ");

            Student existing = studentService.getStudentById(studentId);
            System.out.println("Current student information:");
            System.out.println(existing);

            String firstName = getStringInput("Enter new First Name (or press Enter to keep current): ");
            if (firstName.isEmpty())
                firstName = existing.getFirstName();

            String lastName = getStringInput("Enter new Last Name (or press Enter to keep current): ");
            if (lastName.isEmpty())
                lastName = existing.getLastName();

            String email = getStringInput("Enter new Email (or press Enter to keep current): ");
            if (email.isEmpty())
                email = existing.getEmail();

            String phoneNumber = getStringInput("Enter new Phone Number (or press Enter to keep current): ");
            if (phoneNumber.isEmpty())
                phoneNumber = existing.getPhoneNumber();

            String address = getStringInput("Enter new Address (or press Enter to keep current): ");
            if (address.isEmpty())
                address = existing.getAddress();

            Student updated = studentService.updateStudent(
                    studentId, firstName, lastName, email, phoneNumber, address);

            System.out.println("\nStudent updated successfully!");
            System.out.println(updated);

        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
        }

        pauseForInput();
    }

    private void viewStudentDetails() {
        try {
            System.out.println("\n--- View Student Details ---");
            String studentId = getStringInput("Enter Student ID: ");

            Student student = studentService.getStudentById(studentId);
            System.out.println(student);

            // Show enrollments
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            if (!enrollments.isEmpty()) {
                System.out.println("\nEnrollments:");
                System.out.println("-".repeat(50));
                enrollments.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.err.println("Error retrieving student: " + e.getMessage());
        }

        pauseForInput();
    }

    private void listAllStudents() {
        try {
            System.out.println("\n--- All Students ---");
            List<Student> students = studentService.getAllStudents();

            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                students.forEach(student -> {
                    System.out.printf("%-10s %-20s %-15s %s%n",
                            student.getStudentId(),
                            student.getFirstName() + " " + student.getLastName(),
                            student.getStatus(),
                            String.format("GPA: %.2f", student.calculateCurrentGPA()));
                });
            }

        } catch (Exception e) {
            System.err.println("Error listing students: " + e.getMessage());
        }

        pauseForInput();
    }

    private void searchStudents() {
        try {
            System.out.println("\n--- Search Students ---");
            String searchTerm = getStringInput("Enter search term (name): ");

            List<Student> students = studentService.searchStudentsByName(searchTerm);

            if (students.isEmpty()) {
                System.out.println("No students found matching: " + searchTerm);
            } else {
                System.out.println("\nSearch Results:");
                students.forEach(student -> {
                    System.out.printf("%-10s %-20s %-15s %s%n",
                            student.getStudentId(),
                            student.getFirstName() + " " + student.getLastName(),
                            student.getStatus(),
                            String.format("GPA: %.2f", student.calculateCurrentGPA()));
                });
            }

        } catch (Exception e) {
            System.err.println("Error searching students: " + e.getMessage());
        }

        pauseForInput();
    }

    private void deactivateStudent() {
        try {
            System.out.println("\n--- Deactivate Student ---");
            String studentId = getStringInput("Enter Student ID to deactivate: ");

            Student student = studentService.getStudentById(studentId);
            System.out.println("Student to deactivate:");
            System.out.println(student);

            String confirm = getStringInput("Are you sure you want to deactivate this student? (yes/no): ");
            if ("yes".equalsIgnoreCase(confirm)) {
                studentService.deactivateStudent(studentId);
                System.out.println("Student deactivated successfully!");
            } else {
                System.out.println("Operation cancelled.");
            }

        } catch (Exception e) {
            System.err.println("Error deactivating student: " + e.getMessage());
        }

        pauseForInput();
    }

    // Course Management Methods

    private void addNewCourse() {
        try {
            System.out.println("\n--- Add New Course ---");

            String courseCode = getStringInput("Enter Course Code: ");
            String courseName = getStringInput("Enter Course Name: ");
            String description = getStringInput("Enter Description: ");
            int credits = getValidIntegerInput(1, 6, "Enter Credits (1-6): ");
            String department = getStringInput("Enter Department: ");
            String instructor = getStringInput("Enter Instructor: ");

            Set<String> prerequisites = new HashSet<>();
            String prereqInput = getStringInput("Enter Prerequisites (comma-separated, or press Enter for none): ");
            if (!prereqInput.isEmpty()) {
                prerequisites = Arrays.stream(prereqInput.split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet());
            }

            Course course = courseService.createCourse(
                    courseCode, courseName, description, credits,
                    department, instructor, prerequisites, new HashMap<>());

            System.out.println("\nCourse created successfully!");
            System.out.println(course);

        } catch (Exception e) {
            System.err.println("Error creating course: " + e.getMessage());
        }

        pauseForInput();
    }

    private void updateCourse() {
        try {
            System.out.println("\n--- Update Course ---");
            String courseCode = getStringInput("Enter Course Code to update: ");

            Course existing = courseService.getCourseByCode(courseCode);
            System.out.println("Current course information:");
            System.out.println(existing);

            String courseName = getStringInput("Enter new Course Name (or press Enter to keep current): ");
            if (courseName.isEmpty())
                courseName = existing.getCourseName();

            String description = getStringInput("Enter new Description (or press Enter to keep current): ");
            if (description.isEmpty())
                description = existing.getDescription();

            int credits = getValidIntegerInput(1, 6, "Enter new Credits (1-6, or 0 to keep current): ");
            if (credits == 0)
                credits = existing.getCredits();

            String department = getStringInput("Enter new Department (or press Enter to keep current): ");
            if (department.isEmpty())
                department = existing.getDepartment();

            String instructor = getStringInput("Enter new Instructor (or press Enter to keep current): ");
            if (instructor.isEmpty())
                instructor = existing.getInstructor();

            Course updated = courseService.updateCourse(
                    courseCode, courseName, description, credits,
                    department, instructor, existing.getPrerequisites(), existing.getCourseSchedule());

            System.out.println("\nCourse updated successfully!");
            System.out.println(updated);

        } catch (Exception e) {
            System.err.println("Error updating course: " + e.getMessage());
        }

        pauseForInput();
    }

    private void viewCourseDetails() {
        try {
            System.out.println("\n--- View Course Details ---");
            String courseCode = getStringInput("Enter Course Code: ");

            Course course = courseService.getCourseByCode(courseCode);
            System.out.println(course);

            // Show enrollments
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseCode);
            if (!enrollments.isEmpty()) {
                System.out.println("\nEnrollments:");
                System.out.println("-".repeat(50));
                enrollments.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.err.println("Error retrieving course: " + e.getMessage());
        }

        pauseForInput();
    }

    private void listAllCourses() {
        try {
            System.out.println("\n--- All Courses ---");
            List<Course> courses = courseService.getAllCourses();

            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                courses.forEach(course -> {
                    System.out.printf("%-10s %-30s %-20s %3d %s%n",
                            course.getCourseCode(),
                            course.getCourseName(),
                            course.getDepartment(),
                            course.getCredits(),
                            course.getStatus());
                });
            }

        } catch (Exception e) {
            System.err.println("Error listing courses: " + e.getMessage());
        }

        pauseForInput();
    }

    private void searchCourses() {
        try {
            System.out.println("\n--- Search Courses ---");
            String searchTerm = getStringInput("Enter search term (name or description): ");

            List<Course> courses = courseService.searchCoursesByName(searchTerm);

            if (courses.isEmpty()) {
                System.out.println("No courses found matching: " + searchTerm);
            } else {
                System.out.println("\nSearch Results:");
                courses.forEach(course -> {
                    System.out.printf("%-10s %-30s %-20s %3d %s%n",
                            course.getCourseCode(),
                            course.getCourseName(),
                            course.getDepartment(),
                            course.getCredits(),
                            course.getStatus());
                });
            }

        } catch (Exception e) {
            System.err.println("Error searching courses: " + e.getMessage());
        }

        pauseForInput();
    }

    private void deactivateCourse() {
        try {
            System.out.println("\n--- Deactivate Course ---");
            String courseCode = getStringInput("Enter Course Code to deactivate: ");

            Course course = courseService.getCourseByCode(courseCode);
            System.out.println("Course to deactivate:");
            System.out.println(course);

            String confirm = getStringInput("Are you sure you want to deactivate this course? (yes/no): ");
            if ("yes".equalsIgnoreCase(confirm)) {
                courseService.deactivateCourse(courseCode);
                System.out.println("Course deactivated successfully!");
            } else {
                System.out.println("Operation cancelled.");
            }

        } catch (Exception e) {
            System.err.println("Error deactivating course: " + e.getMessage());
        }

        pauseForInput();
    }

    // Enrollment Management Methods

    private void enrollStudentInCourse() {
        try {
            System.out.println("\n--- Enroll Student in Course ---");
            String studentId = getStringInput("Enter Student ID: ");
            String courseCode = getStringInput("Enter Course Code: ");

            System.out.println("Enter Semester:");
            System.out.println("1. Spring 2024");
            System.out.println("2. Summer 2024");
            System.out.println("3. Fall 2024");
            int semesterChoice = getValidIntegerInput(1, 3);

            Semester semester = switch (semesterChoice) {
                case 1 -> new Semester(2024, Semester.Season.SPRING);
                case 2 -> new Semester(2024, Semester.Season.SUMMER);
                case 3 -> new Semester(2024, Semester.Season.FALL);
                default -> throw new IllegalArgumentException("Invalid semester choice");
            };

            studentService.enrollStudentInCourse(studentId, courseCode, semester);
            System.out.println("Student enrolled successfully!");

        } catch (Exception e) {
            System.err.println("Error enrolling student: " + e.getMessage());
        }

        pauseForInput();
    }

    private void recordGrade() {
        try {
            System.out.println("\n--- Record Grade ---");
            String enrollmentId = getStringInput("Enter Enrollment ID: ");

            System.out.println("Select Grade:");
            Grade[] grades = Grade.values();
            for (int i = 0; i < grades.length; i++) {
                System.out.printf("%d. %s%n", i + 1, grades[i]);
            }

            int gradeChoice = getValidIntegerInput(1, grades.length);
            Grade grade = grades[gradeChoice - 1];

            String notes = getStringInput("Enter Notes (optional): ");

            enrollmentService.recordGrade(enrollmentId, grade, notes);
            System.out.println("Grade recorded successfully!");

        } catch (Exception e) {
            System.err.println("Error recording grade: " + e.getMessage());
        }

        pauseForInput();
    }

    private void withdrawFromCourse() {
        try {
            System.out.println("\n--- Withdraw from Course ---");
            String enrollmentId = getStringInput("Enter Enrollment ID: ");
            String reason = getStringInput("Enter reason for withdrawal: ");

            enrollmentService.withdrawFromCourse(enrollmentId, reason);
            System.out.println("Student withdrawn successfully!");

        } catch (Exception e) {
            System.err.println("Error withdrawing student: " + e.getMessage());
        }

        pauseForInput();
    }

    private void viewEnrollmentDetails() {
        try {
            System.out.println("\n--- View Enrollment Details ---");
            String enrollmentId = getStringInput("Enter Enrollment ID: ");

            Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
            System.out.println(enrollment);

        } catch (Exception e) {
            System.err.println("Error retrieving enrollment: " + e.getMessage());
        }

        pauseForInput();
    }

    private void listEnrollments() {
        try {
            System.out.println("\n--- All Enrollments ---");
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found.");
            } else {
                enrollments.forEach(enrollment -> {
                    System.out.printf("%-20s %-10s %-10s %-15s %s%n",
                            enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                            enrollment.getCourse().getCourseCode(),
                            enrollment.getSemester(),
                            enrollment.getGrade() != null ? enrollment.getGrade() : "IP",
                            enrollment.getStatus());
                });
            }

        } catch (Exception e) {
            System.err.println("Error listing enrollments: " + e.getMessage());
        }

        pauseForInput();
    }

    // Reports Methods

    private void generateTranscript() {
        try {
            System.out.println("\n--- Generate Student Transcript ---");
            String studentId = getStringInput("Enter Student ID: ");

            String transcript = studentService.generateTranscript(studentId);
            System.out.println(transcript);

        } catch (Exception e) {
            System.err.println("Error generating transcript: " + e.getMessage());
        }

        pauseForInput();
    }

    private void viewStudentProfile() {
        try {
            System.out.println("\n--- View Student Profile ---");
            String studentId = getStringInput("Enter Student ID: ");

            Student student = studentService.getStudentById(studentId);
            System.out.println(student);

        } catch (Exception e) {
            System.err.println("Error retrieving student profile: " + e.getMessage());
        }

        pauseForInput();
    }

    private void courseEnrollmentReport() {
        try {
            System.out.println("\n--- Course Enrollment Report ---");
            String courseCode = getStringInput("Enter Course Code: ");

            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseCode);

            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for course: " + courseCode);
            } else {
                System.out.println("Enrollments for " + courseCode + ":");
                System.out.println("-".repeat(60));
                enrollments.forEach(enrollment -> {
                    System.out.printf("%-20s %-15s %s%n",
                            enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                            enrollment.getSemester(),
                            enrollment.getGrade() != null ? enrollment.getGrade() : "In Progress");
                });
            }

        } catch (Exception e) {
            System.err.println("Error generating course enrollment report: " + e.getMessage());
        }

        pauseForInput();
    }

    private void gpaReport() {
        try {
            System.out.println("\n--- GPA Report ---");
            System.out.println("1. All Students GPA");
            System.out.println("2. Students by GPA Range");
            System.out.println("3. Top Performing Students");
            System.out.print("Enter choice (1-3): ");

            int choice = getValidIntegerInput(1, 3);

            switch (choice) {
                case 1 -> {
                    List<Student> students = studentService.getAllStudents();
                    System.out.println("\nAll Students GPA Report:");
                    System.out.println("-".repeat(40));
                    students.forEach(student -> {
                        System.out.printf("%-20s %-10s %.2f%n",
                                student.getFirstName() + " " + student.getLastName(),
                                student.getStudentId(),
                                student.calculateCurrentGPA());
                    });
                }
                case 2 -> {
                    double minGPA = getDoubleInput("Enter minimum GPA: ");
                    double maxGPA = getDoubleInput("Enter maximum GPA: ");
                    List<Student> studentsInRange = studentService.getStudentsByGPARange(minGPA, maxGPA);

                    System.out.printf("\nStudents with GPA between %.2f and %.2f:%n", minGPA, maxGPA);
                    System.out.println("-".repeat(50));
                    studentsInRange.forEach(student -> {
                        System.out.printf("%-20s %-10s %.2f%n",
                                student.getFirstName() + " " + student.getLastName(),
                                student.getStudentId(),
                                student.calculateCurrentGPA());
                    });
                }
                case 3 -> {
                    int topN = getValidIntegerInput(1, 100, "Enter number of top students to show: ");
                    List<Student> topStudents = studentService.getAllStudents().stream()
                            .sorted((s1, s2) -> Double.compare(s2.calculateCurrentGPA(), s1.calculateCurrentGPA()))
                            .limit(topN)
                            .collect(Collectors.toList());

                    System.out.printf("\nTop %d Students by GPA:%n", topN);
                    System.out.println("-".repeat(40));
                    topStudents.forEach(student -> {
                        System.out.printf("%-20s %-10s %.2f%n",
                                student.getFirstName() + " " + student.getLastName(),
                                student.getStudentId(),
                                student.calculateCurrentGPA());
                    });
                }
            }

        } catch (Exception e) {
            System.err.println("Error generating GPA report: " + e.getMessage());
        }

        pauseForInput();
    }

    // Data Management Methods

    private void exportAllData() {
        try {
            System.out.println("\n--- Export All Data ---");
            System.out.println("Exporting all data to CSV files...");

            fileDataManager.exportStudentsToCSV(studentService.getAllStudents());
            fileDataManager.exportCoursesToCSV(courseService.getAllCourses());
            fileDataManager.exportEnrollmentsToCSV(enrollmentService.getAllEnrollments());

            System.out.println("All data exported successfully!");

        } catch (Exception e) {
            System.err.println("Error exporting data: " + e.getMessage());
        }

        pauseForInput();
    }

    private void importData() {
        try {
            System.out.println("\n--- Import Data ---");
            System.out.println("Importing data from CSV files...");

            List<Student> students = fileDataManager.importStudentsFromCSV();
            List<Course> courses = fileDataManager.importCoursesFromCSV();
            List<Enrollment> enrollments = fileDataManager.importEnrollmentsFromCSV();

            System.out.printf("Imported %d students, %d courses, and %d enrollments.%n",
                    students.size(), courses.size(), enrollments.size());

            // Note: In a real application, you'd need to populate the services with the
            // imported data

        } catch (Exception e) {
            System.err.println("Error importing data: " + e.getMessage());
        }

        pauseForInput();
    }

    private void exportStudentsOnly() {
        try {
            System.out.println("\n--- Export Students Only ---");
            fileDataManager.exportStudentsToCSV(studentService.getAllStudents());
            System.out.println("Students exported successfully!");

        } catch (Exception e) {
            System.err.println("Error exporting students: " + e.getMessage());
        }

        pauseForInput();
    }

    private void exportCoursesOnly() {
        try {
            System.out.println("\n--- Export Courses Only ---");
            fileDataManager.exportCoursesToCSV(courseService.getAllCourses());
            System.out.println("Courses exported successfully!");

        } catch (Exception e) {
            System.err.println("Error exporting courses: " + e.getMessage());
        }

        pauseForInput();
    }

    private void exportEnrollmentsOnly() {
        try {
            System.out.println("\n--- Export Enrollments Only ---");
            fileDataManager.exportEnrollmentsToCSV(enrollmentService.getAllEnrollments());
            System.out.println("Enrollments exported successfully!");

        } catch (Exception e) {
            System.err.println("Error exporting enrollments: " + e.getMessage());
        }

        pauseForInput();
    }

    // Backup Methods

    private void createBackup() {
        try {
            System.out.println("\n--- Create Backup ---");
            System.out.println("Creating backup...");

            fileDataManager.createBackup();
            System.out.println("Backup created successfully!");

        } catch (Exception e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }

        pauseForInput();
    }

    private void restoreFromBackup() {
        try {
            System.out.println("\n--- Restore from Backup ---");

            List<String> backups = fileDataManager.listAvailableBackups();
            if (backups.isEmpty()) {
                System.out.println("No backups available.");
                return;
            }

            System.out.println("Available backups:");
            for (int i = 0; i < backups.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, backups.get(i));
            }

            int choice = getValidIntegerInput(1, backups.size(), "Select backup to restore: ");
            String backupDate = backups.get(choice - 1);

            String confirm = getStringInput("Are you sure you want to restore from " + backupDate + "? (yes/no): ");
            if ("yes".equalsIgnoreCase(confirm)) {
                fileDataManager.restoreFromBackup(backupDate);
                System.out.println("Data restored successfully!");
            } else {
                System.out.println("Restore cancelled.");
            }

        } catch (Exception e) {
            System.err.println("Error restoring from backup: " + e.getMessage());
        }

        pauseForInput();
    }

    private void listAvailableBackups() {
        try {
            System.out.println("\n--- Available Backups ---");

            List<String> backups = fileDataManager.listAvailableBackups();
            if (backups.isEmpty()) {
                System.out.println("No backups available.");
            } else {
                System.out.println("Available backups:");
                backups.forEach(backup -> System.out.println("  " + backup));
            }

        } catch (Exception e) {
            System.err.println("Error listing backups: " + e.getMessage());
        }

        pauseForInput();
    }

    // Utility Methods

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getValidIntegerInput(int min, int max) {
        return getValidIntegerInput(min, max, "Enter your choice (" + min + "-" + max + "): ");
    }

    private int getValidIntegerInput(int min, int max, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    private void pauseForInput() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void loadInitialData() {
        // Load initial data from files if they exist
        try {
            List<Student> students = fileDataManager.importStudentsFromCSV();
            List<Course> courses = fileDataManager.importCoursesFromCSV();
            List<Enrollment> enrollments = fileDataManager.importEnrollmentsFromCSV();

            // Note: In a real application, you'd populate the services with this data
            System.out.println("Initial data loaded successfully.");

        } catch (Exception e) {
            System.out.println("No initial data found or error loading data: " + e.getMessage());
        }
    }
}
