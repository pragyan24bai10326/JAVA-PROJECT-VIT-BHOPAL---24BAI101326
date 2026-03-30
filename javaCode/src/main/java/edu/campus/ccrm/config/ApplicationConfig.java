package edu.campus.ccrm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for application settings.
 * Implements the Singleton pattern for configuration management.
 */
public class ApplicationConfig {
    private static volatile ApplicationConfig instance;
    private final Properties properties;

    private ApplicationConfig() {
        this.properties = new Properties();
        loadProperties();
    }

    /**
     * Gets the singleton instance of the configuration
     */
    public static ApplicationConfig getInstance() {
        if (instance == null) {
            synchronized (ApplicationConfig.class) {
                if (instance == null) {
                    instance = new ApplicationConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Loads configuration properties from file
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                // Set default properties
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
            setDefaultProperties();
        }
    }

    /**
     * Sets default configuration properties
     */
    private void setDefaultProperties() {
        properties.setProperty("app.name", "Campus Course & Records Manager");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("data.directory", "data");
        properties.setProperty("backup.directory", "backups");
        properties.setProperty("max.credits.per.semester", "21");
        properties.setProperty("max.gpa", "4.0");
        properties.setProperty("min.gpa", "0.0");
        properties.setProperty("max.course.credits", "6");
        properties.setProperty("min.course.credits", "1");
        properties.setProperty("default.date.format", "yyyy-MM-dd");
        properties.setProperty("csv.separator", ",");
        properties.setProperty("backup.retention.days", "30");
    }

    /**
     * Gets a property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a property value with default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets an integer property
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets a double property
     */
    public double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets a boolean property
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, String.valueOf(defaultValue)));
    }

    // Specific configuration getters

    public String getAppName() {
        return getProperty("app.name");
    }

    public String getAppVersion() {
        return getProperty("app.version");
    }

    public String getDataDirectory() {
        return getProperty("data.directory");
    }

    public String getBackupDirectory() {
        return getProperty("backup.directory");
    }

    public int getMaxCreditsPerSemester() {
        return getIntProperty("max.credits.per.semester", 21);
    }

    public double getMaxGPA() {
        return getDoubleProperty("max.gpa", 4.0);
    }

    public double getMinGPA() {
        return getDoubleProperty("min.gpa", 0.0);
    }

    public int getMaxCourseCredits() {
        return getIntProperty("max.course.credits", 6);
    }

    public int getMinCourseCredits() {
        return getIntProperty("min.course.credits", 1);
    }

    public String getDefaultDateFormat() {
        return getProperty("default.date.format", "yyyy-MM-dd");
    }

    public String getCsvSeparator() {
        return getProperty("csv.separator", ",");
    }

    public int getBackupRetentionDays() {
        return getIntProperty("backup.retention.days", 30);
    }
}
