package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates performance logging: lazy evaluation, string concatenation costs,
 * async logging, and performance monitoring patterns.
 */
public class PerformanceLogging {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceLogging.class);
    private static final Logger perfLogger = LoggerFactory.getLogger("academy.javaengineering.logging.Performance");

    public void demonstrateLazyEvaluation() {
        if (logger.isDebugEnabled()) {
            logger.debug("Expensive debug info: {}", computeExpensiveString());
        }

        logger.atDebug()
                .setMessage("Lazy computation: {}")
                .addArgument(this::computeExpensiveString)
                .log();

        if (logger.isTraceEnabled()) {
            logger.trace("Trace info: {}", getDetailedTrace());
        }

        logger.atTrace()
                .setMessage("Lazy trace: {}")
                .addArgument(this::getDetailedTrace)
                .log();
    }

    public void demonstrateStringConcatenationCost() {
        String userId = "USR-12345";
        int iterations = 1000;
        double result = 3.14159;

        logger.info("User {} processed {} items with result {}",
                userId, iterations, result);

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            logger.info("Iteration {} - User {} processed {} items",
                    i, userId, iterations);
        }
        long stringConcatTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            logger.info("Iteration {} - User {} processed {} items with result {}",
                    i, userId, iterations, result);
        }
        long parameterizedTime = System.nanoTime() - startTime;

        perfLogger.info("String concatenation overhead comparison:");
        perfLogger.info("  Parameterized: {}ns per call", parameterizedTime / 1000);
    }

    public void demonstratePerformanceMonitoring() {
        long startTime = System.currentTimeMillis();

        try {
            simulateWork();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Operation completed in {}ms", duration);

            perfLogger.atInfo()
                    .addKeyValue("operation", "simulateWork")
                    .addKeyValue("duration", duration)
                    .addKeyValue("status", "SUCCESS")
                    .log("Performance metric recorded");
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Operation failed after {}ms", duration, e);
        }
    }

    public void demonstrateBatchLogging() {
        for (int i = 0; i < 5; i++) {
            logger.info("Batch item {} processed successfully", i);
        }

        logger.info("Batch processing completed: {} items processed", 5);
    }

    public void demonstrateConditionalExpensiveLogging() {
        String debugData = null;
        if (logger.isDebugEnabled()) {
            debugData = buildDebugReport();
            logger.debug("Debug report: {}", debugData);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Full state: {}", getFullApplicationState());
        }

        logger.atDebug()
                .setMessage("Formatted debug: {}")
                .addArgument(this::buildDebugReport)
                .log();
    }

    public void demonstrateAsyncLogging() {
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    logger.info("Thread {} - message {}", threadId, j);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted", e);
            }
        }
    }

    public void demonstratePerformanceMetrics() {
        long startTime = System.nanoTime();
        double result = fibonacci(20);
        long duration = System.nanoTime() - startTime;

        perfLogger.atInfo()
                .addKeyValue("function", "fibonacci")
                .addKeyValue("input", 20)
                .addKeyValue("output", result)
                .addKeyValue("durationNanos", duration)
                .log("Function execution metrics");

        logger.info("Fibonacci(20) = {} computed in {}ns", result, duration);
    }

    private String computeExpensiveString() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "ExpensiveResult{" + System.currentTimeMillis() + "}";
    }

    private String getDetailedTrace() {
        return "Trace{thread=" + Thread.currentThread().getName() +
                ", memory=" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB}";
    }

    private void simulateWork() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String buildDebugReport() {
        return "DebugReport{threads=" + Thread.activeCount() +
                ", memory=" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB}";
    }

    private String getFullApplicationState() {
        return "AppState{uptime=" + ManagementFactory.getRuntimeMXBean().getUptime() +
                "ms, threads=" + Thread.activeCount() + "}";
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        PerformanceLogging demo = new PerformanceLogging();

        System.out.println("=== Performance Logging Demo ===");

        System.out.println("\n--- Lazy Evaluation ---");
        demo.demonstrateLazyEvaluation();

        System.out.println("\n--- String Concatenation Cost ---");
        demo.demonstrateStringConcatenationCost();

        System.out.println("\n--- Performance Monitoring ---");
        demo.demonstratePerformanceMonitoring();

        System.out.println("\n--- Batch Logging ---");
        demo.demonstrateBatchLogging();

        System.out.println("\n--- Conditional Expensive Logging ---");
        demo.demonstrateConditionalExpensiveLogging();

        System.out.println("\n--- Async Logging ---");
        demo.demonstrateAsyncLogging();

        System.out.println("\n--- Performance Metrics ---");
        demo.demonstratePerformanceMetrics();
    }

    private static class ManagementFactory {
        static RuntimeMXBean getRuntimeMXBean() {
            return new RuntimeMXBean();
        }
    }

    private static class RuntimeMXBean {
        long getUptime() {
            return System.currentTimeMillis();
        }
    }
}
