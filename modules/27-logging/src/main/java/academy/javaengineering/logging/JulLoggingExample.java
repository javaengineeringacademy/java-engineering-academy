package academy.javaengineering.logging;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Demonstrates Java Util Logging (JUL) framework.
 * JUL is the built-in logging framework in Java.
 */
public class JulLoggingExample {

    private static final Logger logger = Logger.getLogger(JulLoggingExample.class.getName());

    public static void main(String[] args) {
        configureLogger();
        demonstrateLoggingLevels();
        demonstrateParameters();
        demonstrateExceptionLogging();
    }

    private static void configureLogger() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(handler);
    }

    private static void demonstrateLoggingLevels() {
        logger.severe("This is a SEVERE error message");
        logger.warning("This is a WARNING message");
        logger.info("This is an INFO message");
        logger.fine("This is a FINE message");
        logger.finer("This is a FINER message");
        logger.finest("This is a FINEST message");
    }

    private static void demonstrateParameters() {
        String userName = "JohnDoe";
        int userId = 12345;
        
        logger.info("User logged in: " + userName + " (ID: " + userId + ")");
        logger.log(Level.INFO, "User logged in: {0} (ID: {1})", new Object[]{userName, userId});
    }

    private static void demonstrateExceptionLogging() {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            logger.log(Level.SEVERE, "Arithmetic error occurred", e);
        }
    }
}
