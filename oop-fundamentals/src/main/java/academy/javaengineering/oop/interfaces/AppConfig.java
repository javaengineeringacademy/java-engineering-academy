package academy.javaengineering.oop.interfaces;

/**
 * AppConfig - Interface demonstrating constants.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface AppConfig {

    int MAX_RETRIES = 3;
    long TIMEOUT_MS = 5000;
    String APP_NAME = "Java Engineering Academy";
    String VERSION = "1.0.0";

    // All fields in interfaces are implicitly:
    // public static final

    static void printConfig() {
        System.out.println("  App: " + APP_NAME);
        System.out.println("  Version: " + VERSION);
        System.out.println("  Max Retries: " + MAX_RETRIES);
        System.out.println("  Timeout: " + TIMEOUT_MS + "ms");
    }
}