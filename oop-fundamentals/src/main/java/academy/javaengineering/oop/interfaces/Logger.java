package academy.javaengineering.oop.interfaces;

/**
 * Logger - Base interface for logging functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Logger {

    void log(String message);

    default void logError(String message) {
        log("[ERROR] " + message);
    }

    default void logWarning(String message) {
        log("[WARNING] " + message);
    }

    default void logInfo(String message) {
        log("[INFO] " + message);
    }
}