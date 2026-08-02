package academy.javaengineering.debugging;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Demonstrates logging for debugging.
 */
public class DebugLogger {

    private static final Logger logger = Logger.getLogger(DebugLogger.class.getName());

    public static void logMethodEntry(String methodName, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("ENTRY: ").append(methodName);
        if (args.length > 0) {
            sb.append(" with args: ");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
                if (i < args.length - 1) sb.append(", ");
            }
        }
        logger.info(sb.toString());
    }

    public static void logMethodExit(String methodName, Object result) {
        logger.info(String.format("EXIT: %s returned: %s", methodName, result));
    }

    public static void logException(String methodName, Exception e) {
        logger.log(Level.SEVERE, String.format("EXCEPTION in %s: %s", methodName, e.getMessage()), e);
    }
}
