package academy.javaengineering.logging.bestpractices.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Solution 2: SafeLogger that enforces best practices.
 */
public class Solution2 {

    public static class SafeLogger {
        private final Logger logger;
        
        private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w.-]+@[\\w.-]+\\.\\w+");
        private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{3}[-.]?\\d{3}[-.]?\\d{4}");
        private static final Pattern SSN_PATTERN = Pattern.compile("\\d{3}-\\d{2}-\\d{4}");
        private static final Pattern CARD_PATTERN = Pattern.compile("\\d{4}[-]?\\d{4}[-]?\\d{4}[-]?\\d{4}");

        public SafeLogger(Class<?> clazz) {
            this.logger = LoggerFactory.getLogger(clazz);
        }

        public void info(String message, Object... args) {
            logger.info(sanitize(message), sanitizeArgs(args));
        }

        public void debug(String message, Object... args) {
            logger.debug(sanitize(message), sanitizeArgs(args));
        }

        public void warn(String message, Object... args) {
            logger.warn(sanitize(message), sanitizeArgs(args));
        }

        public void error(String message, Throwable t, Object... args) {
            logger.error(sanitize(message), sanitizeArgs(args), t);
        }

        private String sanitize(String message) {
            String result = message;
            result = EMAIL_PATTERN.matcher(result).replaceAll("****@****.***");
            result = PHONE_PATTERN.matcher(result).replaceAll("***-***-****");
            result = SSN_PATTERN.matcher(result).replaceAll("***-**-****");
            return result;
        }

        private Object[] sanitizeArgs(Object[] args) {
            Object[] sanitized = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    sanitized[i] = sanitize((String) args[i]);
                } else {
                    sanitized[i] = args[i];
                }
            }
            return sanitized;
        }
    }

    public static void main(String[] args) {
        SafeLogger safeLogger = new SafeLogger(Solution2.class);

        MDC.put("requestId", UUID.randomUUID().toString());
        try {
            safeLogger.info("User {} authenticated", "john@example.com");
            safeLogger.debug("Card: {}", "4111-1111-1111-1111");
            safeLogger.warn("Phone: {}", "555-123-4567");
        } finally {
            MDC.clear();
        }
    }
}
