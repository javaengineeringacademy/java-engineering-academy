package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates logback.xml configuration patterns, appenders, pattern layouts,
 * level configuration, and conditional configuration.
 */
public class LogbackConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LogbackConfiguration.class);

    private static final Logger appLogger = LoggerFactory.getLogger("academy.javaengineering.logging.App");
    private static final Logger securityLogger = LoggerFactory.getLogger("academy.javaengineering.logging.Security");
    private static final Logger perfLogger = LoggerFactory.getLogger("academy.javaengineering.logging.Performance");

    public void demonstrateConsoleAppender() {
        logger.info("This message goes to the console via CONSOLE appender");
        logger.debug("Debug messages also appear on console when level is DEBUG");
        logger.warn("Warning messages are displayed prominently");
    }

    public void demonstrateFileAppender() {
        appLogger.info("This message is written to logs/application.log");
        appLogger.debug("Debug level messages are captured in the file");
        appLogger.error("Errors are logged to both console and file");
    }

    public void demonstrateErrorFileAppender() {
        appLogger.error("This ERROR goes to both application.log and error.log");
        appLogger.warn("This WARN goes to application.log only (below ERROR threshold)");
        appLogger.info("This INFO goes to application.log only");
    }

    public void demonstratePatternLayout() {
        logger.info("Pattern includes timestamp, thread, level, logger name, and message");
        logger.debug("Each component of the pattern provides specific information");
        logger.warn("Timestamp format: %d{HH:mm:ss.SSS}");
    }

    public void demonstrateLoggerLevels() {
        appLogger.trace("TRACE - most verbose, rarely used in production");
        appLogger.debug("DEBUG - fine-grained diagnostic information");
        appLogger.info("INFO - general operational messages");
        appLogger.warn("WARN - unexpected but recoverable situations");
        appLogger.error("ERROR - serious failures requiring attention");
    }

    public void demonstratePackageLevelConfiguration() {
        logger.info("academy.javaengineering.logging package is set to DEBUG level");
        perfLogger.info("PerformanceLogging class is overridden to INFO level");
        securityLogger.debug("Security events can be logged at DEBUG level");
    }

    public void demonstrateAsyncAppender() {
        for (int i = 0; i < 10; i++) {
            logger.info("Async message {} - processed without blocking", i);
        }
    }

    public void demonstrateConditionalConfiguration() {
        logger.info("Conditional config allows different setups per environment");
        logger.debug("Development can have verbose logging");
        logger.warn("Production typically uses INFO or WARN level");
    }

    public void demonstrateMDCIntegration() {
        org.slf4j.MDC.put("requestId", "REQ-" + System.currentTimeMillis());
        org.slf4j.MDC.put("userId", "USR-001");
        org.slf4j.MDC.put("sessionId", "SES-" + java.util.UUID.randomUUID());

        logger.info("Request processed with MDC context");
        logger.debug("All MDC values appear in log output");

        org.slf4j.MDC.clear();
    }

    public void demonstrateLogbackFeatures() {
        logger.info("Rolling file policy keeps logs manageable");
        logger.info("Time-based rolling creates daily log files");
        logger.info("maxHistory=30 retains logs for 30 days");
        logger.info("ThresholdFilter on ERROR_FILE only captures ERROR and above");
    }

    public void demonstratePatternComponents() {
        logger.info("Pattern %d{HH:mm:ss.SSS} - timestamp in milliseconds");
        logger.info("Pattern %thread - current thread name");
        logger.info("Pattern %-5level - log level with 5 char width");
        logger.info("Pattern %logger{36} - logger name abbreviated to 36 chars");
        logger.info("Pattern %msg - log message content");
        logger.info("Pattern %n - platform-independent newline");
    }

    public void demonstrateEncoderOptions() {
        logger.info("Encoder wraps layout and stream");
        logger.info("PatternLayoutEncoder is most common encoder");
        logger.info("Charset can be specified: UTF-8 is recommended");
        logger.info("Output pattern can include exception info with %ex");
    }

    public void demonstrateFilterLevels() {
        logger.trace("TRACE level - below all filters in production");
        logger.debug("DEBUG level - enabled for academy package");
        logger.info("INFO level - enabled globally");
        logger.warn("WARN level - always displayed");
        logger.error("ERROR level - captured in error.log via ThresholdFilter");
    }

    public void demonstrateRollingPolicies() {
        logger.info("TimeBasedRollingPolicy - rolls by time");
        logger.info("SizeAndTimeBasedRollingPolicy - rolls by size and time");
        logger.info("FixedWindowRollingPolicy - rolls by file size");
        logger.info("TriggeringPolicy determines when rolling occurs");
    }

    public void demonstrateAppenderTypes() {
        logger.info("ConsoleAppender - writes to stdout/stderr");
        logger.info("FileAppender - writes to a single file");
        logger.info("RollingFileAppender - rolls files based on policy");
        logger.info("AsyncAppender - non-blocking logging wrapper");
        logger.info("SocketAppender - sends logs over network");
    }

    public void demonstrateLoggerHierarchy() {
        logger.info("Root logger is the ancestor of all loggers");
        logger.info("Package loggers inherit level from parent");
        logger.info("Logger name '.' indicates hierarchy separator");
        logger.info("academy.javaengineering.logging is parent of academy.javaengineering.logging.App");
    }

    public void demonstrateEvaluatorFilters() {
        logger.info("EvaluatorFilter uses event evaluators");
        logger.info("GEventEvaluator evaluates Groovy expressions");
        logger.info("JaninoEventEvaluator evaluates Java expressions");
        logger.info("LevelFilter matches exact log level");
        logger.info("RangeFilter matches level within a range");
    }

    public void demonstrateCustomPatternLayouts() {
        logger.info("Pattern: %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        logger.info("Custom patterns can include MDC: %X{requestId}");
        logger.info("Exception formatting: %msg%n%rEx{full}");
        logger.info("Client IP from MDC: %X{clientIP}");
    }

    public void demonstrateContextValues() {
        org.slf4j.MDC.put("application", "demo-app");
        org.slf4j.MDC.put("environment", "development");
        org.slf4j.MDC.put("version", "1.0.0");

        logger.info("Context values appear in log output");
        logger.info("MDC values are thread-local");
        logger.info("Clear MDC after use to prevent memory leaks");

        org.slf4j.MDC.clear();
    }

    public static void main(String[] args) {
        LogbackConfiguration demo = new LogbackConfiguration();

        System.out.println("=== Logback Configuration Demo ===");

        System.out.println("\n--- Console Appender ---");
        demo.demonstrateConsoleAppender();

        System.out.println("\n--- File Appender ---");
        demo.demonstrateFileAppender();

        System.out.println("\n--- Error File Appender ---");
        demo.demonstrateErrorFileAppender();

        System.out.println("\n--- Pattern Layout ---");
        demo.demonstratePatternLayout();

        System.out.println("\n--- Logger Levels ---");
        demo.demonstrateLoggerLevels();

        System.out.println("\n--- Package Level Configuration ---");
        demo.demonstratePackageLevelConfiguration();

        System.out.println("\n--- Async Appender ---");
        demo.demonstrateAsyncAppender();

        System.out.println("\n--- Conditional Configuration ---");
        demo.demonstrateConditionalConfiguration();

        System.out.println("\n--- MDC Integration ---");
        demo.demonstrateMDCIntegration();

        System.out.println("\n--- Logback Features ---");
        demo.demonstrateLogbackFeatures();

        System.out.println("\n--- Pattern Components ---");
        demo.demonstratePatternComponents();

        System.out.println("\n--- Encoder Options ---");
        demo.demonstrateEncoderOptions();

        System.out.println("\n--- Filter Levels ---");
        demo.demonstrateFilterLevels();

        System.out.println("\n--- Rolling Policies ---");
        demo.demonstrateRollingPolicies();

        System.out.println("\n--- Appender Types ---");
        demo.demonstrateAppenderTypes();

        System.out.println("\n--- Logger Hierarchy ---");
        demo.demonstrateLoggerHierarchy();

        System.out.println("\n--- Evaluator Filters ---");
        demo.demonstrateEvaluatorFilters();

        System.out.println("\n--- Custom Pattern Layouts ---");
        demo.demonstrateCustomPatternLayouts();

        System.out.println("\n--- Context Values ---");
        demo.demonstrateContextValues();
    }
}
