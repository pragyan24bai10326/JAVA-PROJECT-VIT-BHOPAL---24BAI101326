package edu.campus.ccrm.domain.interfaces;

/**
 * Interface for objects that can validate their state.
 * Demonstrates interface segregation and validation patterns.
 */
public interface Validatable {
    /**
     * Validates the current state of the object
     * 
     * @return true if valid, false otherwise
     */
    boolean isValid();

    /**
     * Returns validation error messages
     * 
     * @return collection of validation error messages
     */
    java.util.List<String> getValidationErrors();

    /**
     * Validates the object and throws an exception if invalid
     * 
     * @throws IllegalArgumentException if validation fails
     */
    default void validate() {
        if (!isValid()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", getValidationErrors()));
        }
    }
}
