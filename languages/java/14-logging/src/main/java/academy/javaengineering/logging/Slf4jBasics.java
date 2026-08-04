package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Demonstrates SLF4J basics: logger creation, log levels, parameterized logging,
 * exception logging, and markers.
 */
public class Slf4jBasics {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jBasics.class);

    private static final Logger namedLogger = LoggerFactory.getLogger("CustomNamedLogger");

    private final Logger instanceLogger = LoggerFactory.getLogger(getClass());

    public void demonstrateLogLevels() {
        logger.trace("This is a TRACE message - most verbose level");
        logger.debug("This is a DEBUG message - fine-grained diagnostic info");
        logger.info("This is an INFO message - general operational messages");
        logger.warn("This is a WARN message - potential problems");
        logger.error("This is an ERROR message - serious failures");
    }

    public void demonstrateParameterizedLogging() {
        String username = "john_doe";
        int itemCount = 42;
        double price = 29.99;

        logger.info("User {} logged in successfully", username);
        logger.debug("Processing {} items at ${} each", itemCount, price);
        logger.warn("User {} has {} items in cart totaling ${}", username, itemCount, price);

        logger.info("Simple message without parameters");
    }

    public void demonstrateMultipleParameters() {
        String userId = "USR-12345";
        String operation = "UPDATE";
        String resource = "UserProfile";
        long duration = 150L;

        logger.info("User {} performed {} on {} in {}ms", userId, operation, resource, duration);
        logger.debug("Request {} - {} {} completed in {}ms",
                java.util.UUID.randomUUID(), operation, resource, duration);
    }

    public void demonstrateExceptionLogging() {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            logger.error("Division by zero occurred", e);
        }

        try {
            String nullStr = null;
            nullStr.length();
        } catch (NullPointerException e) {
            logger.error("Null pointer encountered during string operation", e);
        }

        try {
            throw new java.io.IOException("Connection refused to database");
        } catch (java.io.IOException e) {
            logger.error("Failed to connect to database at localhost:5432", e);
        }
    }

    public void demonstrateExceptionWithMessage() {
        try {
            throw new IllegalStateException("Invalid state for order processing");
        } catch (IllegalStateException e) {
            logger.error("Order {} cannot be processed in current state", "ORD-999", e);
        }
    }

    public void demonstrateMarkers() {
        Marker securityMarker = MarkerFactory.getMarker("SECURITY");
        Marker auditMarker = MarkerFactory.getMarker("AUDIT");
        auditMarker.add(securityMarker);

        logger.info(securityMarker, "User logged in from new IP address: 192.168.1.100");
        logger.warn(securityMarker, "Failed login attempt for user admin");
        logger.info(auditMarker, "User performed admin action: DELETE from users");

        Marker performanceMarker = MarkerFactory.getMarker("PERFORMANCE");
        logger.info(performanceMarker, "Query execution time exceeded threshold: {}ms", 5000);
    }

    public void demonstrateConditionalLogging() {
        if (logger.isDebugEnabled()) {
            String expensiveComputation = performExpensiveOperation();
            logger.debug("Expensive operation result: {}", expensiveComputation);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Detailed trace info: {}", getDetailedSystemState());
        }

        logger.info("Always executed regardless of log level");
    }

    public void demonstrateLoggerCreationPatterns() {
        Logger staticLogger = LoggerFactory.getLogger(Slf4jBasics.class);
        Logger stringLogger = LoggerFactory.getLogger("com.example.StringLogger");
        Logger innerClassLogger = LoggerFactory.getLogger(Slf4jBasics.InnerClass.class);

        staticLogger.info("Static logger using class reference");
        stringLogger.info("Logger created with string name");
        innerClassLogger.info("Logger for inner class");
    }

    public void demonstratePrintfStyle() {
        String message = String.format("User %s scored %d points", "alice", 95);
        logger.info(message);

        logger.info("User {} scored {} points", "alice", 95);

        logger.atInfo()
                .addArgument("alice")
                .addArgument(95)
                .log("User {} scored {} points");
    }

    public void demonstrateAddKeyValuePairs() {
        logger.atInfo()
                .addKeyValue("userId", "USR-789")
                .addKeyValue("action", "LOGIN")
                .addKeyValue("ip", "10.0.0.1")
                .log("User login event");

        logger.atDebug()
                .addKeyValue("orderId", "ORD-456")
                .addKeyValue("amount", 299.99)
                .addKeyValue("currency", "USD")
                .log("Order placed successfully");
    }

    public void demonstrateMarkerHierarchy() {
        Marker parentMarker = MarkerFactory.getMarker("APP");
        Marker childMarker = MarkerFactory.getMarker("AUTH");
        childMarker.add(parentMarker);

        logger.info(childMarker, "Authentication event with parent marker");
        logger.debug(childMarker, "Detailed auth info with inherited context");
    }

    public void demonstrateFluentApi() {
        logger.atInfo()
                .addKeyValue("action", "USER_CREATED")
                .addKeyValue("timestamp", System.currentTimeMillis())
                .log("User created successfully");

        logger.atDebug()
                .setMessage("Processing batch {} of {}")
                .addArgument(1)
                .addArgument(10)
                .log();

        logger.atWarn()
                .addKeyValue("queueSize", 1000)
                .addKeyValue("threshold", 500)
                .log("Queue size approaching threshold");
    }

    public void demonstrateLogLevelsHierarchy() {
        logger.trace("Level 0: TRACE - finest granularity");
        logger.debug("Level 1: DEBUG - diagnostic information");
        logger.info("Level 2: INFO - general messages");
        logger.warn("Level 3: WARN - potential issues");
        logger.error("Level 4: ERROR - serious problems");
    }

    public void demonstrateArgumentArrays() {
        Object[] args = new Object[]{"param1", "param2", "param3"};
        logger.info("Processing {} and {} with {}", args);

        logger.info("Single argument: {}", "value");
        logger.info("Two arguments: {} and {}", "first", "second");
        logger.info("Three arguments: {}, {}, and {}", "one", "two", "three");
    }

    public void demonstrateNestedExceptionLogging() {
        Exception rootCause = new RuntimeException("Root cause");
        Exception intermediate = new IllegalStateException("Intermediate error", rootCause);
        Exception wrapper = new RuntimeException("Wrapper error", intermediate);

        logger.error("Caught wrapped exception", wrapper);
        logger.error("Exception chain: {} -> {} -> {}",
                wrapper.getMessage(),
                intermediate.getMessage(),
                rootCause.getMessage());
    }

    private String performExpensiveOperation() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Computed Result";
    }

    private String getDetailedSystemState() {
        return "Thread: " + Thread.currentThread().getName() +
                ", Memory: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MB" +
                ", Available Processors: " + Runtime.getRuntime().availableProcessors();
    }

    static class InnerClass {
        private static final Logger innerLogger = LoggerFactory.getLogger(InnerClass.class);

        public void doWork() {
            innerLogger.info("Inner class working");
        }
    }

    public void demonstrateLogRotation() {
        for (int i = 0; i < 5; i++) {
            logger.info("Rotation test message {}", i);
        }
        logger.info("Log rotation configured in logback.xml");
    }

    public void demonstrateCustomAppenders() {
        logger.info("Custom appender demonstration");
        logger.debug("This message can be routed to custom destinations");
        logger.error("Error messages can trigger alerts via custom appenders");
    }

    public static void main(String[] args) {
        Slf4jBasics demo = new Slf4jBasics();

        System.out.println("=== SLF4J Basics Demo ===");

        System.out.println("\n--- Log Levels ---");
        demo.demonstrateLogLevels();

        System.out.println("\n--- Parameterized Logging ---");
        demo.demonstrateParameterizedLogging();

        System.out.println("\n--- Multiple Parameters ---");
        demo.demonstrateMultipleParameters();

        System.out.println("\n--- Exception Logging ---");
        demo.demonstrateExceptionLogging();

        System.out.println("\n--- Exception with Message ---");
        demo.demonstrateExceptionWithMessage();

        System.out.println("\n--- Markers ---");
        demo.demonstrateMarkers();

        System.out.println("\n--- Conditional Logging ---");
        demo.demonstrateConditionalLogging();

        System.out.println("\n--- Logger Creation Patterns ---");
        demo.demonstrateLoggerCreationPatterns();

        System.out.println("\n--- Printf Style ---");
        demo.demonstratePrintfStyle();

        System.out.println("\n--- Key-Value Pairs ---");
        demo.demonstrateAddKeyValuePairs();

        System.out.println("\n--- Marker Hierarchy ---");
        demo.demonstrateMarkerHierarchy();

        System.out.println("\n--- Fluent API ---");
        demo.demonstrateFluentApi();

        System.out.println("\n--- Log Levels Hierarchy ---");
        demo.demonstrateLogLevelsHierarchy();

        System.out.println("\n--- Argument Arrays ---");
        demo.demonstrateArgumentArrays();

        System.out.println("\n--- Nested Exception Logging ---");
        demo.demonstrateNestedExceptionLogging();

        System.out.println("\n--- Log Rotation ---");
        demo.demonstrateLogRotation();

        System.out.println("\n--- Custom Appenders ---");
        demo.demonstrateCustomAppenders();

        System.out.println("\n--- Inner Class Logger ---");
        new InnerClass().doWork();
    }
}
