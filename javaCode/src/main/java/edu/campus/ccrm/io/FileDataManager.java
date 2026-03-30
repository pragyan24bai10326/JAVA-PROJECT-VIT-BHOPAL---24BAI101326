package edu.campus.ccrm.io;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.exception.DataImportException;
import edu.campus.ccrm.exception.DataExportException;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages file I/O operations for the CCRM system.
 * Implements CSV and text file operations using NIO.2 and Streams.
 */
public class FileDataManager {
    private static final String DATA_DIR = "data";
    private static final String STUDENTS_FILE = "students.csv";
    private static final String COURSES_FILE = "courses.csv";
    private static final String ENROLLMENTS_FILE = "enrollments.csv";
    private static final String BACKUP_DIR = "backups";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Initializes the data directory structure
     */
    public void initializeDataDirectory() throws IOException {
        Path dataPath = Paths.get(DATA_DIR);
        Path backupPath = Paths.get(BACKUP_DIR);

        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        if (!Files.exists(backupPath)) {
            Files.createDirectories(backupPath);
        }
    }

    /**
     * Exports students to CSV file using Streams and NIO.2
     */
    public void exportStudentsToCSV(List<Student> students) throws DataExportException {
        try {
            Path filePath = Paths.get(DATA_DIR, STUDENTS_FILE);

            List<String> lines = students.stream()
                    .map(this::studentToCSVLine)
                    .collect(Collectors.toList());

            // Add header
            lines.add(0, getStudentCSVHeader());

            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new DataExportException("Failed to export students to CSV", e);
        }
    }

    /**
     * Imports students from CSV file using Streams and NIO.2
     */
    public List<Student> importStudentsFromCSV() throws DataImportException {
        try {
            Path filePath = Paths.get(DATA_DIR, STUDENTS_FILE);

            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            return Files.lines(filePath)
                    .skip(1) // Skip header
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::csvLineToStudent)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new DataImportException("Failed to import students from CSV", e);
        }
    }

    /**
     * Exports courses to CSV file using Streams and NIO.2
     */
    public void exportCoursesToCSV(List<Course> courses) throws DataExportException {
        try {
            Path filePath = Paths.get(DATA_DIR, COURSES_FILE);

            List<String> lines = courses.stream()
                    .map(this::courseToCSVLine)
                    .collect(Collectors.toList());

            // Add header
            lines.add(0, getCourseCSVHeader());

            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new DataExportException("Failed to export courses to CSV", e);
        }
    }

    /**
     * Imports courses from CSV file using Streams and NIO.2
     */
    public List<Course> importCoursesFromCSV() throws DataImportException {
        try {
            Path filePath = Paths.get(DATA_DIR, COURSES_FILE);

            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            return Files.lines(filePath)
                    .skip(1) // Skip header
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::csvLineToCourse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new DataImportException("Failed to import courses from CSV", e);
        }
    }

    /**
     * Exports enrollments to CSV file using Streams and NIO.2
     */
    public void exportEnrollmentsToCSV(List<Enrollment> enrollments) throws DataExportException {
        try {
            Path filePath = Paths.get(DATA_DIR, ENROLLMENTS_FILE);

            List<String> lines = enrollments.stream()
                    .map(this::enrollmentToCSVLine)
                    .collect(Collectors.toList());

            // Add header
            lines.add(0, getEnrollmentCSVHeader());

            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new DataExportException("Failed to export enrollments to CSV", e);
        }
    }

    /**
     * Imports enrollments from CSV file using Streams and NIO.2
     */
    public List<Enrollment> importEnrollmentsFromCSV() throws DataImportException {
        try {
            Path filePath = Paths.get(DATA_DIR, ENROLLMENTS_FILE);

            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            return Files.lines(filePath)
                    .skip(1) // Skip header
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::csvLineToEnrollment)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new DataImportException("Failed to import enrollments from CSV", e);
        }
    }

    /**
     * Creates a backup of all data files using NIO.2
     */
    public void createBackup() throws DataExportException {
        try {
            String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Path backupPath = Paths.get(BACKUP_DIR, "backup_" + timestamp);

            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }

            // Copy all data files
            copyFileToBackup(STUDENTS_FILE, backupPath);
            copyFileToBackup(COURSES_FILE, backupPath);
            copyFileToBackup(ENROLLMENTS_FILE, backupPath);

            // Create backup manifest
            createBackupManifest(backupPath);

        } catch (IOException e) {
            throw new DataExportException("Failed to create backup", e);
        }
    }

    /**
     * Restores data from backup using NIO.2
     */
    public void restoreFromBackup(String backupDate) throws DataImportException {
        try {
            Path backupPath = Paths.get(BACKUP_DIR, "backup_" + backupDate);

            if (!Files.exists(backupPath)) {
                throw new DataImportException("Backup not found: " + backupDate);
            }

            // Restore all data files
            restoreFileFromBackup(STUDENTS_FILE, backupPath);
            restoreFileFromBackup(COURSES_FILE, backupPath);
            restoreFileFromBackup(ENROLLMENTS_FILE, backupPath);

        } catch (IOException e) {
            throw new DataImportException("Failed to restore from backup", e);
        }
    }

    /**
     * Lists available backups using NIO.2
     */
    public List<String> listAvailableBackups() throws IOException {
        Path backupDir = Paths.get(BACKUP_DIR);

        if (!Files.exists(backupDir)) {
            return new ArrayList<>();
        }

        try (Stream<Path> paths = Files.list(backupDir)) {
            return paths.filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.startsWith("backup_"))
                    .map(name -> name.substring(7)) // Remove "backup_" prefix
                    .sorted(Collections.reverseOrder()) // Most recent first
                    .collect(Collectors.toList());
        }
    }

    // Private helper methods

    private String getStudentCSVHeader() {
        return "StudentID,FirstName,LastName,Email,PhoneNumber,Address,DateOfBirth,EnrollmentDate,Status";
    }

    private String getCourseCSVHeader() {
        return "CourseCode,CourseName,Description,Credits,Department,Instructor,Status,Prerequisites";
    }

    private String getEnrollmentCSVHeader() {
        return "EnrollmentID,StudentID,CourseCode,Semester,EnrollmentDate,Grade,Status,Notes";
    }

    private String studentToCSVLine(Student student) {
        return String.join(",",
                escapeCSV(student.getStudentId()),
                escapeCSV(student.getFirstName()),
                escapeCSV(student.getLastName()),
                escapeCSV(student.getEmail()),
                escapeCSV(student.getPhoneNumber()),
                escapeCSV(student.getAddress()),
                student.getDateOfBirth().format(DATE_FORMATTER),
                student.getEnrollmentDate().format(DATE_FORMATTER),
                student.getStatus().toString().toUpperCase());
    }

    private String courseToCSVLine(Course course) {
        return String.join(",",
                escapeCSV(course.getCourseCode()),
                escapeCSV(course.getCourseName()),
                escapeCSV(course.getDescription()),
                String.valueOf(course.getCredits()),
                escapeCSV(course.getDepartment()),
                escapeCSV(course.getInstructor()),
                course.getStatus().toString().toUpperCase(),
                escapeCSV(String.join(";", course.getPrerequisites())));
    }

    private String enrollmentToCSVLine(Enrollment enrollment) {
        return String.join(",",
                escapeCSV(enrollment.getEnrollmentId()),
                escapeCSV(enrollment.getStudent().getStudentId()),
                escapeCSV(enrollment.getCourse().getCourseCode()),
                enrollment.getSemester().toString(),
                enrollment.getEnrollmentDate().format(DATE_FORMATTER),
                enrollment.getGrade() != null ? enrollment.getGrade().toString() : "",
                enrollment.getStatus().toString().toUpperCase(),
                escapeCSV(enrollment.getNotes()));
    }

    private Student csvLineToStudent(String line) {
        try {
            String[] fields = parseCSVLine(line);

            return new Student.Builder()
                    .studentId(fields[0])
                    .firstName(fields[1])
                    .lastName(fields[2])
                    .email(fields[3])
                    .phoneNumber(fields[4])
                    .address(fields[5])
                    .dateOfBirth(LocalDate.parse(fields[6], DATE_FORMATTER))
                    .enrollmentDate(LocalDate.parse(fields[7], DATE_FORMATTER))
                    .status(StudentStatus.valueOf(fields[8].toUpperCase()))
                    .build();

        } catch (Exception e) {
            System.err.println("Error parsing student line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    private Course csvLineToCourse(String line) {
        try {
            String[] fields = parseCSVLine(line);

            Set<String> prerequisites = new HashSet<>();
            if (!fields[7].isEmpty()) {
                prerequisites = Arrays.stream(fields[7].split(";"))
                        .filter(p -> !p.trim().isEmpty())
                        .collect(Collectors.toSet());
            }

            return new Course.Builder()
                    .courseCode(fields[0])
                    .courseName(fields[1])
                    .description(fields[2])
                    .credits(Integer.parseInt(fields[3]))
                    .department(fields[4])
                    .instructor(fields[5])
                    .status(CourseStatus.valueOf(fields[6].toUpperCase()))
                    .prerequisites(prerequisites)
                    .build();

        } catch (Exception e) {
            System.err.println("Error parsing course line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    private Enrollment csvLineToEnrollment(String line) {
        try {
            String[] fields = parseCSVLine(line);

            // Note: This is a simplified version. In a real application,
            // you'd need to resolve the student and course objects from their IDs
            // This would require access to the service layer or a repository pattern

            // Parse semester from string like "SPRING 2024"
            String[] semesterParts = fields[3].split(" ");
            Semester semester = new Semester(Integer.parseInt(semesterParts[1]), semesterParts[0]);

            return new Enrollment.Builder()
                    .enrollmentId(fields[0])
                    .student(createDummyStudent(fields[1])) // Temporary placeholder
                    .course(createDummyCourse(fields[2])) // Temporary placeholder
                    .semester(semester)
                    .enrollmentDate(LocalDate.parse(fields[4], DATE_FORMATTER))
                    .grade(fields[5].isEmpty() ? null : Grade.fromLetterGrade(fields[5]))
                    .status(EnrollmentStatus.valueOf(fields[6].toUpperCase()))
                    .notes(fields.length > 7 ? fields[7] : null)
                    .build();

        } catch (Exception e) {
            System.err.println("Error parsing enrollment line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    private Student createDummyStudent(String studentId) {
        // This is a temporary solution for demo purposes
        // In a real application, you'd resolve this from the student service
        return new Student.Builder()
                .studentId(studentId)
                .firstName("Unknown")
                .lastName("Student")
                .email("unknown@example.com")
                .enrollmentDate(LocalDate.now())
                .build();
    }

    private Course createDummyCourse(String courseCode) {
        // This is a temporary solution for demo purposes
        // In a real application, you'd resolve this from the course service
        return new Course.Builder()
                .courseCode(courseCode)
                .courseName("Unknown Course")
                .credits(3)
                .department("Unknown")
                .instructor("Unknown")
                .build();
    }

    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }

    private String escapeCSV(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private void copyFileToBackup(String fileName, Path backupPath) throws IOException {
        Path sourceFile = Paths.get(DATA_DIR, fileName);
        Path targetFile = backupPath.resolve(fileName);

        if (Files.exists(sourceFile)) {
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void restoreFileFromBackup(String fileName, Path backupPath) throws IOException {
        Path sourceFile = backupPath.resolve(fileName);
        Path targetFile = Paths.get(DATA_DIR, fileName);

        if (Files.exists(sourceFile)) {
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void createBackupManifest(Path backupPath) throws IOException {
        String manifest = String.format("""
                Backup Manifest
                ================
                Created: %s
                Files:
                - %s
                - %s
                - %s
                """,
                LocalDate.now(),
                STUDENTS_FILE,
                COURSES_FILE,
                ENROLLMENTS_FILE);

        Files.write(backupPath.resolve("manifest.txt"), manifest.getBytes());
    }
}
