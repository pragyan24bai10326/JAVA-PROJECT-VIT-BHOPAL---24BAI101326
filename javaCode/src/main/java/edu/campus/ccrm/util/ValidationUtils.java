package edu.campus.ccrm.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * Demonstrates utility class patterns and validation logic.
 */
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$");

    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile(
            "^[A-Z]{2,3}\\d{3,6}$");

    private static final Pattern COURSE_CODE_PATTERN = Pattern.compile(
            "^[A-Z]{2,4}\\d{3,4}[A-Z]?$");

    private ValidationUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates phone number format
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * Validates student ID format
     */
    public static boolean isValidStudentId(String studentId) {
        return studentId != null && STUDENT_ID_PATTERN.matcher(studentId).matches();
    }

    /**
     * Validates course code format
     */
    public static boolean isValidCourseCode(String courseCode) {
        return courseCode != null && COURSE_CODE_PATTERN.matcher(courseCode).matches();
    }

    /**
     * Validates date string format
     */
    public static boolean isValidDateString(String dateString, String pattern) {
        try {
            LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ofPattern(pattern));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates that a string is not null or empty
     */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates that a number is within a range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validates that a number is within a range
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Validates credit hours (typically 1-6)
     */
    public static boolean isValidCredits(int credits) {
        return isInRange(credits, 1, 6);
    }

    /**
     * Validates GPA range (0.0-4.0)
     */
    public static boolean isValidGPA(double gpa) {
        return isInRange(gpa, 0.0, 4.0);
    }

    /**
     * Validates age based on date of birth
     */
    public static boolean isValidAge(LocalDate dateOfBirth, int minAge, int maxAge) {
        if (dateOfBirth == null)
            return false;

        LocalDate now = LocalDate.now();
        int age = now.getYear() - dateOfBirth.getYear();

        // Adjust for birthday not yet occurred this year
        if (now.getMonthValue() < dateOfBirth.getMonthValue() ||
                (now.getMonthValue() == dateOfBirth.getMonthValue()
                        && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }

        return age >= minAge && age <= maxAge;
    }

    /**
     * Sanitizes input string to prevent injection attacks
     */
    public static String sanitizeInput(String input) {
        if (input == null)
            return null;
        return input.trim().replaceAll("[<>\"'&]", "");
    }

    /**
     * Validates enrollment date is not in the future
     */
    public static boolean isValidEnrollmentDate(LocalDate enrollmentDate) {
        return enrollmentDate != null && !enrollmentDate.isAfter(LocalDate.now());
    }

    /**
     * Validates that a date is not in the future
     */
    public static boolean isNotFutureDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    /**
     * Validates that a date is not too far in the past (e.g., more than 100 years
     * ago)
     */
    public static boolean isNotTooOldDate(LocalDate date) {
        if (date == null)
            return false;
        LocalDate hundredYearsAgo = LocalDate.now().minusYears(100);
        return date.isAfter(hundredYearsAgo);
    }
}
