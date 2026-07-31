package academy.javaengineering.oop.interfaces;

/**
 * Timestamped - Interface for adding timestamp functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Timestamped {

    default void logWithTimestamp(String message) {
        System.out.println("  [" + java.time.LocalDateTime.now() + "] " + message);
    }

    default String getTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}