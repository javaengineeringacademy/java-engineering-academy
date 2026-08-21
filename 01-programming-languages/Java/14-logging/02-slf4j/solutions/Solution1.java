package academy.javaengineering.logging.slf4j.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 1: Request tracing with MDC.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    private static final String REQUEST_ID_KEY = "requestId";
    private static final String USER_ID_KEY = "userId";
    private static final String SESSION_ID_KEY = "sessionId";

    public String startTrace(String userId, String sessionId) {
        String requestId = UUID.randomUUID().toString();

        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(USER_ID_KEY, userId);
        MDC.put(SESSION_ID_KEY, sessionId);

        logger.info("Trace started for user={}", userId);
        return requestId;
    }

    public void endTrace() {
        String requestId = MDC.get(REQUEST_ID_KEY);
        logger.info("Trace ended");
        MDC.clear();
    }

    public Runnable withTrace(Runnable task, String userId, String sessionId) {
        return () -> {
            startTrace(userId, sessionId);
            try {
                task.run();
            } finally {
                endTrace();
            }
        };
    }
}
