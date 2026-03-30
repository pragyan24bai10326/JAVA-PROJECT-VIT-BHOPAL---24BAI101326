package edu.campus.ccrm;

import edu.campus.ccrm.cli.CLIMenu;

/**
 * Main application class for the Campus Course & Records Manager (CCRM).
 * Implements the Singleton pattern for application instance management.
 */
public class CampusRecordsManager {
    private static volatile CampusRecordsManager instance;
    private final CLIMenu cliMenu;

    /**
     * Private constructor for Singleton pattern
     */
    private CampusRecordsManager() {
        this.cliMenu = new CLIMenu();
    }

    /**
     * Gets the singleton instance of the application
     */
    public static CampusRecordsManager getInstance() {
        if (instance == null) {
            synchronized (CampusRecordsManager.class) {
                if (instance == null) {
                    instance = new CampusRecordsManager();
                }
            }
        }
        return instance;
    }

    /**
     * Starts the application
     */
    public void start() {
        System.out.println("Starting Campus Course & Records Manager...");
        cliMenu.run();
    }

    /**
     * Main entry point of the application
     */
    public static void main(String[] args) {
        try {
            // Enable assertions for development
            assert args != null : "Arguments array should not be null";

            CampusRecordsManager app = CampusRecordsManager.getInstance();
            app.start();

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
