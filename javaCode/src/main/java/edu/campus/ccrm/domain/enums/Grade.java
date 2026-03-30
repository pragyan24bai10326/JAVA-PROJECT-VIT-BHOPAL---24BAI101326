package edu.campus.ccrm.domain.enums;

/**
 * Represents academic grades with numeric values for GPA calculation.
 * Implements immutability and provides utility methods.
 */
public enum Grade {
    A_PLUS(4.0, "A+"),
    A(4.0, "A"),
    A_MINUS(3.7, "A-"),
    B_PLUS(3.3, "B+"),
    B(3.0, "B"),
    B_MINUS(2.7, "B-"),
    C_PLUS(2.3, "C+"),
    C(2.0, "C"),
    C_MINUS(1.7, "C-"),
    D_PLUS(1.3, "D+"),
    D(1.0, "D"),
    F(0.0, "F"),
    INCOMPLETE(0.0, "I"),
    WITHDRAWAL(0.0, "W"),
    PASS(3.0, "P"),
    NO_PASS(0.0, "NP");

    private final double numericValue;
    private final String displayValue;

    Grade(double numericValue, String displayValue) {
        this.numericValue = numericValue;
        this.displayValue = displayValue;
    }

    public double getNumericValue() {
        return numericValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Checks if this grade is passing
     */
    public boolean isPassing() {
        return this != F && this != NO_PASS && this != INCOMPLETE && this != WITHDRAWAL;
    }

    /**
     * Checks if this grade counts towards GPA
     */
    public boolean countsTowardsGPA() {
        return this != INCOMPLETE && this != WITHDRAWAL && this != PASS && this != NO_PASS;
    }

    /**
     * Converts numeric grade to Grade enum
     */
    public static Grade fromNumericValue(double value) {
        return switch ((int) (value * 10)) {
            case 40 -> A_PLUS;
            case 37 -> A_MINUS;
            case 33 -> B_PLUS;
            case 30 -> B;
            case 27 -> B_MINUS;
            case 23 -> C_PLUS;
            case 20 -> C;
            case 17 -> C_MINUS;
            case 13 -> D_PLUS;
            case 10 -> D;
            default -> F;
        };
    }

    /**
     * Converts letter grade string to Grade enum
     */
    public static Grade fromLetterGrade(String letter) {
        return switch (letter.toUpperCase().trim()) {
            case "A+" -> A_PLUS;
            case "A" -> A;
            case "A-" -> A_MINUS;
            case "B+" -> B_PLUS;
            case "B" -> B;
            case "B-" -> B_MINUS;
            case "C+" -> C_PLUS;
            case "C" -> C;
            case "C-" -> C_MINUS;
            case "D+" -> D_PLUS;
            case "D" -> D;
            case "F" -> F;
            case "I" -> INCOMPLETE;
            case "W" -> WITHDRAWAL;
            case "P" -> PASS;
            case "NP" -> NO_PASS;
            default -> throw new IllegalArgumentException("Invalid grade: " + letter);
        };
    }

    @Override
    public String toString() {
        return displayValue;
    }
}
