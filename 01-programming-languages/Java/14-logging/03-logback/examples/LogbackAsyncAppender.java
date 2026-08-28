package academy.javaengineering.logging.logback.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LogbackAsyncAppender - Async appender configuration for high-throughput logging
 *
 * Demonstrates:
 * - AsyncAppender configuration
 * - Queue sizing and discarding thresholds
 * - NeverBlock vs blocking behavior
 * - Performance comparison: sync vs async
 * - Thread safety considerations
 *
 * Key async-appender settings:
 *   queueSize          - Size of the event queue (default: 256)
 *   discardingThreshold - Queue level below which events are discarded (default: 20% of queueSize)
 *   neverBlock         - Don't block caller if queue is full (default: false)
 *   includeCallerData  - Include caller info (MDC, etc.) - has overhead
 */
public class LogbackAsyncAppender {

    private static final Logger logger = LoggerFactory.getLogger(LogbackAsyncAppender.class);

    public static void main(String[] args) {
        System.out.println("=== Logback Async Appender Example ===\n");

        demonstrateAsyncConfiguration();
        System.out.println();
        demonstratePerformanceComparison();
        System.out.println();
        printAsyncAppenderConfiguration();
    }

    static void demonstrateAsyncConfiguration() {
        System.out.println("--- Async Appender Configuration ---\n");

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        // Create encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        // Create sync file appender (wrapped by async)
        FileAppender<ILoggingEvent> syncFileAppender = new FileAppender<>();
        syncFileAppender.setContext(context);
        syncFileAppender.setEncoder(encoder);
        syncFileAppender.setFile("logs/async-app.log");
        syncFileAppender.setName("SYNC_FILE");
        syncFileAppender.start();

        // Create async appender wrapping the file appender
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(context);
        asyncAppender.addAppender(syncFileAppender);
        asyncAppender.setName("ASYNC_FILE");
        asyncAppender.setQueueSize(1024);
        asyncAppender.setDiscardingThreshold(0); // Don't discard any events
        asyncAppender.setNeverBlock(false); // Block if queue full
        asyncAppender.setIncludeCallerData(false); // Don't include caller info (faster)
        asyncAppender.start();

        // Configure root logger with async appender
        ch.qos.logback.classic.Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(asyncAppender);

        // Simulate high-throughput logging
        System.out.println("Logging 1000 messages with async appender...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            logger.info("Async message #{}", i);
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Completed in %d ms%n", elapsed);
    }

    static void demonstratePerformanceComparison() {
        System.out.println("--- Sync vs Async Performance ---\n");

        int messageCount = 5000;
        int threadCount = 4;

        // Sync logging benchmark
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%-5level %msg%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        ch.qos.logback.classic.Logger syncLogger = context.getLogger("SyncBenchmark");
        syncLogger.addAppender(consoleAppender);

        // Measure sync
        long syncStart = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int msgNum = i;
            executor.submit(() -> {
                syncLogger.info("Sync message {}", msgNum);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long syncElapsed = System.nanoTime() - syncStart;
        executor.shutdown();

        System.out.printf("Sync  logging: %,d messages in %d ms (%,d msg/sec)%n",
                messageCount, syncElapsed / 1_000_000,
                messageCount * 1_000_000_000L / syncElapsed);

        // The async version would typically be 3-10x faster
        System.out.println("\nAsync logging typically provides 3-10x throughput improvement");
        System.out.println("because log I/O (disk writes) happens on a separate thread.");
    }

    static void printAsyncAppenderConfiguration() {
        System.out.println("\n--- Equivalent logback.xml ---\n");
        System.out.println("""
                <!-- Async wrapper around sync appender -->
                <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
                    <queueSize>1024</queueSize>
                    <discardingThreshold>0</discardingThreshold>
                    <neverBlock>false</neverBlock>
                    <includeCallerData>false</includeCallerData>
                    <appender-ref ref="SYNC_FILE"/>
                </appender>

                <!-- The sync appender being wrapped -->
                <appender name="SYNC_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/app.log</file>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/app.%d{yyyy-MM-dd}.log</fileNamePattern>
                        <maxHistory>30</maxHistory>
                        <totalSizeCap>10GB</totalSizeCap>
                    </rollingPolicy>
                    <encoder>
                        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                </appender>
                """);

        System.out.println("\nAsync Appender Settings:");
        System.out.println("┌──────────────────┬────────────────────────────────────────────────┐");
        System.out.println("│ Setting          │ Description                                    │");
        System.out.println("├──────────────────┼────────────────────────────────────────────────┤");
        System.out.println("│ queueSize        │ Event queue size (default: 256)                │");
        System.out.println("│ discardingThreshold │ Queue level to discard events (default: 20%) │");
        System.out.println("│ neverBlock       │ Don't block when queue full (default: false)   │");
        System.out.println("│ includeCallerData│ Include caller data like MDC (default: false)  │");
        System.out.println("└──────────────────┴────────────────────────────────────────────────┘");
    }
}
