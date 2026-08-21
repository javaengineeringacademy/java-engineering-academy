package academy.javaengineering.logging.slf4j.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 1: Implement MDC-based request tracing.
 *
 * Requirements:
 * 1. Create a RequestTracer class that manages MDC context
 * 2. Generate unique request IDs
 * 3. Add userId, sessionId, and requestId to MDC
 * 4. Provide a method to start and end request tracking
 * 5. Ensure MDC is always cleaned up
 *
 * Expected log output should include: [requestId] [userId] [sessionId]
 */
public class Exercise1 {

    // TODO: Create logger

    /**
     * Starts request tracing by adding context to MDC.
     * Returns a traceId for the request.
     */
    public String startTrace(String userId, String sessionId) {
        // TODO: Generate unique request ID
        // TODO: Add userId, sessionId, requestId to MDC
        // TODO: Log trace start
        // Return the traceId
        return null;
    }

    /**
     * Ends request tracing by cleaning up MDC.
     */
    public void endTrace() {
        // TODO: Log trace end
        // TODO: Clear MDC
    }

    /**
     * Wraps a Runnable with request tracing.
     */
    public Runnable withTrace(Runnable task, String userId, String sessionId) {
        // TODO: Implement wrapping that ensures startTrace/endTrace
        return null;
    }
}
