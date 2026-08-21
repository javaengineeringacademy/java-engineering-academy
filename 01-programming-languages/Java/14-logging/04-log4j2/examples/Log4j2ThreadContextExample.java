package academy.javaengineering.logging.log4j2.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

/**
 * Example: Log4j 2 ThreadContext (equivalent to MDC).
 */
public class Log4j2ThreadContextExample {

    private static final Logger logger = LogManager.getLogger(Log4j2ThreadContextExample.class);

    public static void main(String[] args) {
        String requestId = UUID.randomUUID().toString();

        // ThreadContext replaces MDC in Log4j 2
        ThreadContext.put("requestId", requestId);
        ThreadContext.put("userId", "user-123");
        ThreadContext.put("sessionId", "sess-456");

        try {
            processRequest();
        } finally {
            ThreadContext.clearAll();
        }
    }

    private static void processRequest() {
        logger.info("Request started");
        logger.debug("Validating input");
        logger.info("Request completed");
    }
}
