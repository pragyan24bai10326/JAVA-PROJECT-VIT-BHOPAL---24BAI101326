package edu.campus.ccrm.service;

import edu.campus.ccrm.domain.*;
import edu.campus.ccrm.domain.enums.*;
import edu.campus.ccrm.exception.CourseNotFoundException;
import edu.campus.ccrm.exception.InvalidCourseException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Service class for managing course operations.
 * Implements business logic for course management with functional programming.
 */
public class CourseService {
    private final Map<String, Course> courses;

    public CourseService() {
        this.courses = new HashMap<>();
    }

    /**
     * Creates a new course using Builder pattern
     */
    public Course createCourse(String courseCode, String courseName, String description,
            int credits, String department, String instructor,
            Set<String> prerequisites, Map<String, String> schedule) {

        Course course = new Course.Builder()
                .courseCode(courseCode)
                .courseName(courseName)
                .description(description)
                .credits(credits)
                .department(department)
                .instructor(instructor)
                .status(CourseStatus.ACTIVE)
                .prerequisites(prerequisites)
                .courseSchedule(schedule)
                .build();

        courses.put(courseCode, course);
        return course;
    }

    /**
     * Updates course information
     */
    public Course updateCourse(String courseCode, String courseName, String description,
            int credits, String department, String instructor,
            Set<String> prerequisites, Map<String, String> schedule) {
        Course existing = getCourseByCode(courseCode);

        Course updated = new Course.Builder()
                .courseCode(courseCode)
                .courseName(courseName)
                .description(description)
                .credits(credits)
                .department(department)
                .instructor(instructor)
                .status(existing.getStatus())
                .prerequisites(prerequisites)
                .courseSchedule(schedule)
                .build();

        courses.put(courseCode, updated);
        return updated;
    }

    /**
     * Retrieves course by code
     */
    public Course getCourseByCode(String courseCode) {
        Course course = courses.get(courseCode);
        if (course == null) {
            throw new CourseNotFoundException("Course not found with code: " + courseCode);
        }
        return course;
    }

    /**
     * Lists all courses with optional filtering using functional programming
     */
    public List<Course> getAllCourses(Predicate<Course> filter) {
        return courses.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Course::getCourseCode))
                .collect(Collectors.toList());
    }

    /**
     * Lists all courses
     */
    public List<Course> getAllCourses() {
        return getAllCourses(course -> true);
    }

    /**
     * Searches courses by name using functional programming
     */
    public List<Course> searchCoursesByName(String name) {
        String searchTerm = name.toLowerCase();
        return courses.values().stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm) ||
                        course.getDescription().toLowerCase().contains(searchTerm))
                .sorted(Comparator.comparing(Course::getCourseCode))
                .collect(Collectors.toList());
    }

    /**
     * Filters courses by department using functional programming
     */
    public List<Course> getCoursesByDepartment(String department) {
        return getAllCourses(course -> course.getDepartment().equalsIgnoreCase(department));
    }

    /**
     * Filters courses by instructor using functional programming
     */
    public List<Course> getCoursesByInstructor(String instructor) {
        return getAllCourses(course -> course.getInstructor().equalsIgnoreCase(instructor));
    }

    /**
     * Filters courses by credit range using functional programming
     */
    public List<Course> getCoursesByCreditRange(int minCredits, int maxCredits) {
        return getAllCourses(course -> course.getCredits() >= minCredits && course.getCredits() <= maxCredits);
    }

    /**
     * Filters courses by level using functional programming
     */
    public List<Course> getCoursesByLevel(CourseLevel level) {
        return getAllCourses(course -> course.getCourseLevel() == level);
    }

    /**
     * Gets available courses (active status) using functional programming
     */
    public List<Course> getAvailableCourses() {
        return getAllCourses(Course::isAvailable);
    }

    /**
     * Deactivates a course (soft delete)
     */
    public void deactivateCourse(String courseCode) {
        Course course = getCourseByCode(courseCode);
        Course deactivated = new Course.Builder()
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .credits(course.getCredits())
                .department(course.getDepartment())
                .instructor(course.getInstructor())
                .status(CourseStatus.INACTIVE)
                .prerequisites(course.getPrerequisites())
                .courseSchedule(course.getCourseSchedule())
                .build();

        courses.put(courseCode, deactivated);
    }

    /**
     * Gets course prerequisites using functional programming
     */
    public List<Course> getCoursePrerequisites(String courseCode) {
        Course course = getCourseByCode(courseCode);
        return course.getPrerequisites().stream()
                .map(this::getCourseByCode)
                .collect(Collectors.toList());
    }

    /**
     * Checks if student meets course prerequisites
     */
    public boolean meetsPrerequisites(String courseCode, Set<String> completedCourses) {
        Course course = getCourseByCode(courseCode);
        return course.meetsPrerequisites(completedCourses);
    }

    /**
     * Gets courses that can be taken by a student (prerequisites met)
     */
    public List<Course> getEligibleCourses(Set<String> completedCourses) {
        return getAvailableCourses().stream()
                .filter(course -> course.meetsPrerequisites(completedCourses))
                .collect(Collectors.toList());
    }

    /**
     * Gets course statistics using functional programming
     */
    public Map<String, Object> getCourseStatistics() {
        return Map.of(
                "totalCourses", courses.size(),
                "activeCourses", getAvailableCourses().size(),
                "coursesByDepartment", courses.values().stream()
                        .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting())),
                "coursesByLevel", courses.values().stream()
                        .collect(Collectors.groupingBy(Course::getCourseLevel, Collectors.counting())),
                "averageCredits", courses.values().stream()
                        .mapToInt(Course::getCredits)
                        .average().orElse(0.0));
    }

    /**
     * Validates course creation data
     */
    public void validateCourseData(String courseCode, String courseName, int credits,
            String department, String instructor) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new InvalidCourseException("Course code is required");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new InvalidCourseException("Course name is required");
        }
        if (credits <= 0 || credits > 6) {
            throw new InvalidCourseException("Credits must be between 1 and 6");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidCourseException("Department is required");
        }
        if (instructor == null || instructor.trim().isEmpty()) {
            throw new InvalidCourseException("Instructor is required");
        }
        if (courses.containsKey(courseCode)) {
            throw new InvalidCourseException("Course code already exists: " + courseCode);
        }
    }
}
