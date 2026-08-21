package academy.javaengineering.logging.mdc.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exercise 1: Implement a request context manager.
 *
 * Requirements:
 * 1. Create a RequestContext class that manages MDC
 * 2. Generate unique request IDs
 * 3. Add userId, service, version, and timestamp
 * 4. Provide methods to start, update, and end context
 * 5. Handle thread pool propagation
 *
 * Usage:
 * RequestContext.start("user-123");
 * try {
 *     processRequest();
 * } finally {
 *     RequestContext.end();
 * }
 */
public class Exercise1 {

    // TODO: Create logger
    // TODO: Implement RequestContext class

    public void processRequest() {
        // TODO: Start context with userId
        // TODO: Process through multiple methods
        // TODO: End context in finally
    }
}
