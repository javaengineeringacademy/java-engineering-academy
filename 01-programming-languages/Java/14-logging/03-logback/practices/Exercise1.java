package academy.javaengineering.logging.logback.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Exercise 1: Configure Logback for a production application.
 *
 * Create a logback.xml configuration that:
 * 1. Logs INFO and above to console
 * 2. Logs DEBUG and above to a rolling file
 * 3. File rotates daily, keeps 30 days
 * 4. File size limit of 100MB per file
 * 5. Total disk limit of 10GB
 * 6. Specific packages at DEBUG level
 * 7. Async logging for file appender
 *
 * Then implement the application code that exercises all log levels.
 */
public class Exercise1 {

    // TODO: Create logger
    // TODO: Implement methods that demonstrate all log levels
    // TODO: Create a method that generates enough logs to test rotation

    public void runApplication() {
        // TODO: Log startup (INFO)
        // TODO: Process some data (DEBUG)
        // TODO: Handle warnings (WARN)
        // TODO: Simulate error (ERROR)
        // TODO: Log shutdown (INFO)
    }
}
