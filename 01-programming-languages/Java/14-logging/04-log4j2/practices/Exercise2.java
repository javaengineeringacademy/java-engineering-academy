package academy.javaengineering.logging.log4j2.practices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

/**
 * Exercise 2: Implement ThreadContext-based request tracing.
 *
 * Requirements:
 * 1. Create a RequestScope class that manages ThreadContext
 * 2. Generate unique trace IDs
 * 3. Add request metadata (userId, sessionId, service, version)
 * 4. Ensure proper cleanup in finally blocks
 * 5. Demonstrate propagation across method calls
 *
 * Expected log output should include all ThreadContext values.
 */
public class Exercise2 {

    // TODO: Create logger

    public void processRequest(String userId, String sessionId) {
        // TODO: Start request scope
        // TODO: Log request with context
        // TODO: Process through multiple methods
        // TODO: Clean up scope
    }

    private void validateInput() {
        // TODO: Log with context
    }

    private void processData() {
        // TODO: Log with context
    }
}
