package logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * LoggingBasics - java.util.logging, SLF4J basics
 *
 * Covers:
 * - java.util.logging (JUL) basics
 * - Log levels
 * - Handlers and Formatters
 * - SLF4J overview
 * - Log4j2 overview
 */
public class LoggingBasics {

    // Create logger using java.util.logging
    private static final Logger logger = Logger.getLogger(LoggingBasics.class.getName());

    public static void main(String[] args) {
        System.out.println("=== Java Util Logging (JUL) ===");
        julBasics();

        System.out.println("\n=== Log Levels ===");
        logLevels();

        System.out.println("\n=== Configuration ===");
        configuration();

        System.out.println("\n=== SLF4J Overview ===");
        slf4jOverview();

        System.out.println("\n=== Log4j2 Overview ===");
        log4j2Overview();

        System.out.println("\n=== Best Practices ===");
        bestPractices();
    }

    static void julBasics() {
        // Basic logging
        logger.info("This is an info message");
        logger.warning("This is a warning message");
        logger.severe("This is an error message");

        // Logging with parameters
        String user = "John";
        int attempts = 3;
        logger.info("User " + user + " attempted " + attempts + " times");
        logger.info(String.format("User %s attempted %d times", user, attempts));

        // Logging with exception
        try {
            throw new RuntimeException("Something went wrong");
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error occurred", e);
        }
    }

    static void logLevels() {
        System.out.println("Log Levels (from least to most severe):");
        System.out.println();
        System.out.println("ALL     - All messages");
        System.out.println("FINEST  - Detailed information");
        System.out.println("FINER   - Fine-grained information");
        System.out.println("FINE    - Debug-level messages");
        System.out.println("CONFIG  - Configuration messages");
        System.out.println("INFO    - Informational messages");
        System.out.println("WARNING - Warning messages");
        System.out.println("SEVERE  - Error messages");
        System.out.println("OFF     - Disable all logging");
        System.out.println();

        // Demonstrate different levels
        logger.setLevel(Level.ALL);
        logger.finest("Finest level message");
        logger.finer("Finer level message");
        logger.fine("Fine level message");
        logger.config("Config level message");
        logger.info("Info level message");
        logger.warning("Warning level message");
        logger.severe("Severe level message");
    }

    static void configuration() {
        // Programmatic configuration
        Logger rootLogger = Logger.getLogger("");

        // Remove default handlers
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Add custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new CustomFormatter());
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.ALL);

        // Test custom formatter
        logger.info("Message with custom formatter");
        logger.warning("Warning with custom formatter");
    }

    static void slf4jOverview() {
        System.out.println("SLF4J (Simple Logging Facade for Java):");
        System.out.println();
        System.out.println("Dependency in pom.xml:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.slf4j</groupId>");
        System.out.println("    <artifactId>slf4j-api</artifactId>");
        System.out.println("    <version>2.0.9</version>");
        System.out.println("  </dependency>");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  import org.slf4j.Logger;");
        System.out.println("  import org.slf4j.LoggerFactory;");
        System.out.println();
        System.out.println("  Logger logger = LoggerFactory.getLogger(MyClass.class);");
        System.out.println("  logger.info(\"Hello {}\", name);");
        System.out.println("  logger.debug(\"Debug: {}\", details);");
        System.out.println("  logger.error(\"Error occurred\", exception);");
        System.out.println();
        System.out.println("Placeholders: {}, {}, {}");
        System.out.println("Performance: Check if enabled before expensive operations");
        System.out.println("  if (logger.isDebugEnabled()) {");
        System.out.println("      logger.debug(\"Expensive: {}\", computeValue());");
        System.out.println("  }");
    }

    static void log4j2Overview() {
        System.out.println("Log4j2:");
        System.out.println();
        System.out.println("Dependency in pom.xml:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.apache.logging.log4j</groupId>");
        System.out.println("    <artifactId>log4j-core</artifactId>");
        System.out.println("    <version>2.20.0</version>");
        System.out.println("  </dependency>");
        System.out.println();
        System.out.println("Configuration (log4j2.xml):");
        System.out.println("  <Configuration>");
        System.out.println("    <Appenders>");
        System.out.println("      <Console name=\"Console\" target=\"SYSTEM_OUT\">");
        System.out.println("        <PatternLayout pattern=\"%d{HH:mm:ss} [%t] %-5level %logger{36} - %msg%n\"/>");
        System.out.println("      </Console>");
        System.out.println("    </Appenders>");
        System.out.println("    <Loggers>");
        System.out.println("      <Root level=\"info\">");
        System.out.println("        <AppenderRef ref=\"Console\"/>");
        System.out.println("      </Root>");
        System.out.println("    </Loggers>");
        System.out.println("  </Configuration>");
        System.out.println();
        System.out.println("Features:");
        System.out.println("- Async logging support");
        System.out.println("- Garbage-free logging");
        System.out.println("- Multiple output targets");
        System.out.println("- Advanced filtering");
    }

    static void bestPractices() {
        System.out.println("Logging Best Practices:");
        System.out.println();
        System.out.println("1. Use SLF4J as facade");
        System.out.println("   - Decouple from implementation");
        System.out.println("   - Easy to switch frameworks");
        System.out.println();
        System.out.println("2. Choose appropriate log level");
        System.out.println("   - ERROR: System errors, failures");
        System.out.println("   - WARN: Unexpected conditions");
        System.out.println("   - INFO: Important business events");
        System.out.println("   - DEBUG: Debugging information");
        System.out.println("   - TRACE: Detailed tracing");
        System.out.println();
        System.out.println("3. Include context");
        System.out.println("   - User ID, request ID");
        System.out.println("   - Relevant parameters");
        System.out.println();
        System.out.println("4. Don't log sensitive data");
        System.out.println("   - Passwords, credit cards");
        System.out.println("   - Personal information");
        System.out.println();
        System.out.println("5. Use parameterized messages");
        System.out.println("   - logger.info(\"User {} logged in\", userId)");
        System.out.println("   - Avoid: logger.info(\"User \" + userId)");
        System.out.println();
        System.out.println("6. Consider performance");
        System.out.println("   - Check log level before expensive operations");
        System.out.println("   - Use async logging for high throughput");
    }

    // Custom formatter for demonstration
    static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return String.format("[%s] %s - %s%n",
                record.getLevel(),
                record.getLoggerName(),
                record.getMessage());
        }
    }
}