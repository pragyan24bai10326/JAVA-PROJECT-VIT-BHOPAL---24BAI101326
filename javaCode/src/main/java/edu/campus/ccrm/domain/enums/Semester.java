package edu.campus.ccrm.domain.enums;

/**
 * Represents academic semesters with year and season information.
 * Implements immutability and provides utility methods.
 */
public final class Semester implements Comparable<Semester> {
    private final int year;
    private final Season season;

    public Semester(int year, Season season) {
        this.year = year;
        this.season = season;
    }

    public Semester(int year, String season) {
        this(year, Season.valueOf(season.toUpperCase()));
    }

    public int getYear() {
        return year;
    }

    public Season getSeason() {
        return season;
    }

    /**
     * Gets the next semester in sequence
     */
    public Semester next() {
        return switch (season) {
            case SPRING -> new Semester(year, Season.SUMMER);
            case SUMMER -> new Semester(year, Season.FALL);
            case FALL -> new Semester(year + 1, Season.SPRING);
        };
    }

    /**
     * Gets the previous semester in sequence
     */
    public Semester previous() {
        return switch (season) {
            case SPRING -> new Semester(year - 1, Season.FALL);
            case SUMMER -> new Semester(year, Season.SPRING);
            case FALL -> new Semester(year, Season.SUMMER);
        };
    }

    /**
     * Compares semesters chronologically
     */
    public int compareTo(Semester other) {
        int yearComparison = Integer.compare(this.year, other.year);
        if (yearComparison != 0)
            return yearComparison;
        return this.season.compareTo(other.season);
    }

    @Override
    public String toString() {
        return season + " " + year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Semester semester = (Semester) obj;
        return year == semester.year && season == semester.season;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(year, season);
    }

    public enum Season {
        SPRING, SUMMER, FALL
    }
}
