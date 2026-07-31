package academy.javaengineering.oop.interfaces;

/**
 * AdvancedLogger - Demonstrates interface inheritance.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface AdvancedLogger extends Logger, Timestamped {

    default void logWithLevel(String level, String message) {
        logWithTimestamp("[" + level + "] " + message);
    }

    void clearLogs();
}