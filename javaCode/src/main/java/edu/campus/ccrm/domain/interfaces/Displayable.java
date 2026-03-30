package edu.campus.ccrm.domain.interfaces;

/**
 * Interface for objects that can be displayed in various formats.
 * Demonstrates interface segregation and polymorphism.
 */
public interface Displayable {
    /**
     * Returns a formatted string representation of the object
     */
    String toDisplayString();

    /**
     * Returns a short summary of the object
     */
    String getSummary();

    /**
     * Returns a detailed description of the object
     */
    String getDetailedDescription();
}
