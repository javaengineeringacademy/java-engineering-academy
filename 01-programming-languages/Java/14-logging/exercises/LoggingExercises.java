package academy.javaengineering.exercises;

import java.util.*;
import java.util.logging.*;

/**
 * Exercises: Logging (SLF4J concepts, Log Levels)
 *
 * Complete the TODO sections below.
 */
public class LoggingExercises {

    // TODO 1: Implement a simple Logger wrapper
    public static class SimpleLogger {
        private final String name;
        private final List<String> logEntries = new ArrayList<>();

        public SimpleLogger(String name) {
            this.name = name;
        }

        public void info(String message) {
            // TODO: add INFO level log entry with timestamp
        }

        public void debug(String message) {
            // TODO: add DEBUG level log entry with timestamp
        }

        public void warn(String message) {
            // TODO: add WARN level log entry with timestamp
        }

        public void error(String message) {
            // TODO: add ERROR level log entry with timestamp
        }

        public void error(String message, Throwable t) {
            // TODO: add ERROR level log entry with exception info
        }

        public List<String> getLogEntries() {
            return new ArrayList<>(logEntries);
        }

        public String getLastEntry() {
            return logEntries.isEmpty() ? null : logEntries.get(logEntries.size() - 1);
        }
    }

    // TODO 2: Implement a Logger with level filtering
    public static class LevelFilteringLogger {
        public enum Level { DEBUG, INFO, WARN, ERROR }

        private final String name;
        private final Level minLevel;
        private final List<String> entries = new ArrayList<>();

        public LevelFilteringLogger(String name, Level minLevel) {
            this.name = name;
            this.minLevel = minLevel;
        }

        public void log(Level level, String message) {
            // TODO: implement - only log if level >= minLevel
        }

        public List<String> getEntries() {
            return new ArrayList<>(entries);
        }
    }

    // TODO 3: Implement a MDC-like context map
    public static class MdcContext {
        private final ThreadLocal<Map<String, String>> context = ThreadLocal.withInitial(HashMap::new);

        public void put(String key, String value) {
            // TODO: implement
        }

        public String get(String key) {
            // TODO: implement
            return null;
        }

        public void remove(String key) {
            // TODO: implement
        }

        public void clear() {
            // TODO: implement
        }

        public Map<String, String> getCopyOfContextMap() {
            // TODO: implement - return copy of current context
            return new HashMap<>();
        }
    }

    // TODO 4: Implement a structured log formatter
    public static class StructuredFormatter {
        public String format(String level, String logger, String message, Map<String, String> context) {
            // TODO: implement JSON-like format
            // {"level":"INFO","logger":"MyClass","message":"Hello","context":{"reqId":"123"}}
            return "";
        }
    }

    // TODO 5: Implement a PerformanceLogger that measures execution time
    public static class PerformanceLogger {
        private final List<String> measurements = new ArrayList<>();

        public void startTimer(String operation) {
            // TODO: record start time
        }

        public long endTimer(String operation) {
            // TODO: calculate and record duration
            return 0;
        }

        public List<String> getMeasurements() {
            return new ArrayList<>(measurements);
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        LoggingExercises exercises = new LoggingExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== LoggingExercises Tests ===\n");

        // Test 1
        total++;
        SimpleLogger logger = new SimpleLogger("TestLogger");
        logger.info("Application started");
        logger.debug("Debug message");
        logger.warn("Warning message");
        logger.error("Error occurred");
        if (logger.getLogEntries().size() == 4
            && logger.getLogEntries().get(0).contains("INFO")
            && logger.getLogEntries().get(3).contains("ERROR")) {
            System.out.println("Test 1 PASSED: SimpleLogger basic");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: SimpleLogger basic");
        }

        total++;
        logger.error("Exception", new RuntimeException("test"));
        if (logger.getLastEntry().contains("Exception")) {
            System.out.println("Test 1b PASSED: SimpleLogger error with exception");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: SimpleLogger error with exception");
        }

        // Test 2
        total++;
        LevelFilteringLogger filtered = new LevelFilteringLogger("Filtered", LevelFilteringLogger.Level.WARN);
        filtered.log(LevelFilteringLogger.Level.DEBUG, "debug");
        filtered.log(LevelFilteringLogger.Level.INFO, "info");
        filtered.log(LevelFilteringLogger.Level.WARN, "warn");
        filtered.log(LevelFilteringLogger.Level.ERROR, "error");
        if (filtered.getEntries().size() == 2) {
            System.out.println("Test 2 PASSED: LevelFilteringLogger");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: LevelFilteringLogger - " + filtered.getEntries().size());
        }

        // Test 3
        total++;
        MdcContext mdc = new MdcContext();
        mdc.put("userId", "12345");
        mdc.put("requestId", "abc-def");
        if ("12345".equals(mdc.get("userId")) && "abc-def".equals(mdc.get("requestId"))) {
            System.out.println("Test 3a PASSED: MdcContext put/get");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: MdcContext put/get");
        }

        total++;
        Map<String, String> copy = mdc.getCopyOfContextMap();
        mdc.remove("userId");
        if (copy.size() == 2 && mdc.get("userId") == null) {
            System.out.println("Test 3b PASSED: MdcContext copy/remove");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: MdcContext copy/remove");
        }

        // Test 4
        total++;
        StructuredFormatter formatter = new StructuredFormatter();
        Map<String, String> ctx = Map.of("reqId", "123");
        String formatted = formatter.format("INFO", "MyClass", "Hello", ctx);
        if (formatted.contains("INFO") && formatted.contains("MyClass") && formatted.contains("reqId")) {
            System.out.println("Test 4 PASSED: StructuredFormatter");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: StructuredFormatter - " + formatted);
        }

        // Test 5
        total++;
        PerformanceLogger perf = new PerformanceLogger();
        perf.startTimer("loadData");
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        long duration = perf.endTimer("loadData");
        if (duration >= 40 && perf.getMeasurements().size() == 1) {
            System.out.println("Test 5 PASSED: PerformanceLogger");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: PerformanceLogger - " + duration + "ms");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
