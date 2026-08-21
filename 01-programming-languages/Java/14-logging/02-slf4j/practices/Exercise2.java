package academy.javaengineering.logging.slf4j.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 2: Implement proper parameterized logging for a service.
 *
 * Requirements:
 * 1. Add parameterized logging to all methods
 * 2. Use appropriate log levels
 * 3. Include context in log messages (IDs, counts, durations)
 * 4. Guard expensive operations with level checks
 * 5. Log exceptions properly (as last argument)
 */
public class Exercise2 {

    // TODO: Create logger

    public void processBatch(String batchId, int expectedCount) {
        // TODO: Log batch start with expected count

        int processed = 0;
        int failed = 0;

        for (int i = 0; i < expectedCount; i++) {
            try {
                // Simulate processing
                processItem(batchId, i);
                processed++;
            } catch (Exception e) {
                failed++;
                // TODO: Log individual item failure
            }
        }

        // TODO: Log batch completion with summary
    }

    private void processItem(String batchId, int index) {
        // TODO: Log item processing start (only at TRACE level)
        // Simulate work
        if (Math.random() < 0.1) {
            throw new RuntimeException("Simulated processing error");
        }
    }

    public String convertData(String input, String format) {
        // TODO: Log conversion start
        // TODO: Log conversion complete with duration
        return input;
    }
}
