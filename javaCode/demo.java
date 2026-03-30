import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.service.*;
import edu.campus.ccrm.io.FileDataManager;
import java.time.LocalDate;

/**
 * Demo class to showcase CCRM functionality
 */
public class demo {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   Campus Course & Records Manager (CCRM) Demo");
        System.out.println("=".repeat(60));

        try {
            // Initialize services
            CourseService courseService = new CourseService();
            StudentService studentService = new StudentService(null);
            EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);
            FileDataManager fileManager = new FileDataManager();

            // Initialize data directory
            fileManager.initializeDataDirectory();

            // Create sample courses
            System.out.println("\n1. Creating Sample Courses:");
            System.out.println("-".repeat(40));

            Course cs101 = courseService.createCourse(
                    "CS101", "Introduction to Programming",
                    "Basic programming concepts and Java fundamentals",
                    3, "Computer Science", "Dr. Smith",
                    java.util.Set.of(), java.util.Map.of());
            System.out.println("Created: " + cs101.getCourseCode() + " - " + cs101.getCourseName());

            Course math101 = courseService.createCourse(
                    "MATH101", "Calculus I",
                    "Differential and integral calculus",
                    4, "Mathematics", "Dr. Johnson",
                    java.util.Set.of(), java.util.Map.of());
            System.out.println("Created: " + math101.getCourseCode() + " - " + math101.getCourseName());

            // Create sample students
            System.out.println("\n2. Creating Sample Students:");
            System.out.println("-".repeat(40));

            Student student1 = studentService.createStudent(
                    "STU001", "John", "Doe", "john.doe@university.edu",
                    "(555) 123-4567", "123 Main St, City, State",
                    LocalDate.of(2000, 1, 15), LocalDate.of(2024, 1, 15));
            System.out.println("Created: " + student1.getFirstName() + " " + student1.getLastName() + " ("
                    + student1.getStudentId() + ")");

            Student student2 = studentService.createStudent(
                    "STU002", "Jane", "Smith", "jane.smith@university.edu",
                    "(555) 987-6543", "456 Oak Ave, City, State",
                    LocalDate.of(2001, 3, 22), LocalDate.of(2024, 1, 15));
            System.out.println("Created: " + student2.getFirstName() + " " + student2.getLastName() + " ("
                    + student2.getStudentId() + ")");

            // Create enrollments
            System.out.println("\n3. Creating Sample Enrollments:");
            System.out.println("-".repeat(40));

            Semester spring2024 = new Semester(2024, Semester.Season.SPRING);

            // Note: This is a simplified demo - in a real application,
            // you'd need proper service integration for enrollments
            System.out.println("Enrollment system ready for: " + spring2024);

            // Display statistics
            System.out.println("\n4. System Statistics:");
            System.out.println("-".repeat(40));

            java.util.Map<String, Object> studentStats = studentService.getStudentStatistics();
            java.util.Map<String, Object> courseStats = courseService.getCourseStatistics();

            System.out.println("Total Students: " + studentStats.get("totalStudents"));
            System.out.println("Active Students: " + studentStats.get("activeStudents"));
            System.out.println("Total Courses: " + courseStats.get("totalCourses"));
            System.out.println("Active Courses: " + courseStats.get("activeCourses"));

            // Demonstrate polymorphism
            System.out.println("\n5. Demonstrating Polymorphism:");
            System.out.println("-".repeat(40));

            System.out.println("Student toString():");
            System.out.println(student1.toString());

            System.out.println("\nCourse toString():");
            System.out.println(cs101.toString());

            // Demonstrate functional programming
            System.out.println("\n6. Functional Programming Demo:");
            System.out.println("-".repeat(40));

            java.util.List<Student> allStudents = studentService.getAllStudents();
            double averageGPA = allStudents.stream()
                    .mapToDouble(Student::calculateCurrentGPA)
                    .average()
                    .orElse(0.0);
            System.out.println("Average GPA of all students: " + String.format("%.2f", averageGPA));

            // Search functionality
            System.out.println("\n7. Search Functionality Demo:");
            System.out.println("-".repeat(40));

            java.util.List<Student> searchResults = studentService.searchStudentsByName("John");
            System.out.println("Students found with 'John' in name: " + searchResults.size());

            java.util.List<Course> courseSearchResults = courseService.searchCoursesByName("Programming");
            System.out.println("Courses found with 'Programming' in name: " + courseSearchResults.size());

            // File operations demo
            System.out.println("\n8. File Operations Demo:");
            System.out.println("-".repeat(40));

            try {
                fileManager.exportStudentsToCSV(allStudents);
                fileManager.exportCoursesToCSV(courseService.getAllCourses());
                System.out.println("Data exported to CSV files successfully!");

                fileManager.createBackup();
                System.out.println("Backup created successfully!");

                java.util.List<String> backups = fileManager.listAvailableBackups();
                System.out.println("Available backups: " + backups.size());

            } catch (Exception e) {
                System.out.println("File operations error: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("Demo completed successfully!");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("Demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
