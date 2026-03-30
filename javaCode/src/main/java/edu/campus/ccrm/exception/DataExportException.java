package edu.campus.ccrm.exception;

/**
 * Custom exception thrown when data export operations fail.
 */
public class DataExportException extends Exception {
    public DataExportException(String message) {
        super(message);
    }

    public DataExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
