package academy.javaengineering.logging.logback.examples;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * LogbackJsonConfiguration - JSON structured logging configuration
 *
 * Demonstrates:
 * - JSON layout configuration for structured logging
 * - Logstash-compatible JSON format
 * - Custom JSON field mapping
 * - MDC integration with JSON output
 * - Programmatic JSON configuration
 *
 * JSON logging is essential for log aggregation systems like
 * ELK Stack, Splunk, Datadog, and CloudWatch.
 *
 * Required dependency:
 *   net.logstash.logback:logstash-logback-encoder:7.4
 */
public class LogbackJsonConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LogbackJsonConfiguration.class);

    public static void main(String[] args) {
        System.out.println("=== JSON Logging Configuration ===\n");

        System.out.println("JSON logging is used for structured log aggregation.");
        System.out.println("Popular encoders: logstash-logback-encoder, logback-json-classic\n");

        printJsonConfigExamples();
        demonstrateMdcJsonLogging();

        System.out.println("\n=== Logstash JSON Encoder Configuration ===");
        printLogstashConfig();
    }

    static void printJsonConfigExamples() {
        System.out.println("--- logback.xml JSON Configuration Examples ---\n");

        System.out.println("1. Basic JSON encoder (logstash-logback-encoder):");
        System.out.println("""
            <configuration>
                <appender name="JSON_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
                </appender>

                <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/app.json</file>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/app.%d{yyyy-MM-dd}.json</fileNamePattern>
                        <maxHistory>30</maxHistory>
                    </rollingPolicy>
                    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
                </appender>

                <root level="INFO">
                    <appender-ref ref="JSON_CONSOLE"/>
                    <appender-ref ref="JSON_FILE"/>
                </root>
            </configuration>
            """);

        System.out.println("2. Custom JSON fields:");
        System.out.println("""
            <encoder class="net.logstash.logback.encoder.LogstashEncoder">
                <customFields>{"app":"myapp","env":"production","version":"1.0.0"}</customFields>
                <fieldNames>
                    <timestamp>[ignore]</timestamp>
                    <message>log_message</message>
                    <level>log_level</level>
                    <logger>logger_name</logger>
                </fieldNames>
                <includeMdcKeyName>requestId</includeMdcKeyName>
                <includeMdcKeyName>userId</includeMdcKeyName>
            </encoder>
            """);

        System.out.println("3. LoggingEventCompositeJsonEncoder (maximum customization):");
        System.out.println("""
            <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder>
                <providers>
                    <timestamp/>
                    <version>1</version>
                    <logLevel/>
                    <logLevelValue/>
                    <loggerName/>
                    <pattern>
                        <pattern>
                            {
                            "application": "my-service",
                            "environment": "%mdc{env}",
                            "thread": "%thread",
                            "class": "%logger{36}"
                            }
                        </pattern>
                    </pattern>
                    <message/>
                    <arguments/>
                    <stackTrace>
                        <fieldName>stack_trace</fieldName>
                        <throwableConverter class="net.logstash.logback.stacktrace.ShortenedThrowableConverter">
                            <maxDepthPerThrowable>30</maxDepthPerThrowable>
                            <maxLength>2048</maxLength>
                            <rootCauseFirst>true</rootCauseFirst>
                        </throwableConverter>
                    </stackTrace>
                </providers>
            </encoder>
            """);
    }

    static void demonstrateMdcJsonLogging() {
        System.out.println("\n--- MDC Integration with JSON ---\n");

        // Set MDC values that will appear in JSON output
        MDC.put("requestId", "req-abc-123");
        MDC.put("userId", "user-456");
        MDC.put("traceId", "trace-789");
        MDC.put("spanId", "span-012");

        logger.info("User login successful");
        logger.warn("Slow query detected: SELECT * FROM users");
        logger.error("Database connection failed", new RuntimeException("Connection refused"));

        // JSON output would look like:
        System.out.println("JSON output format:");
        System.out.println("""
            {
              "@timestamp": "2024-01-15T10:30:45.123+00:00",
              "level": "INFO",
              "logger_name": "LogbackJsonConfiguration",
              "thread_name": "main",
              "message": "User login successful",
              "requestId": "req-abc-123",
              "userId": "user-456",
              "traceId": "trace-789",
              "spanId": "span-012",
              "application": "my-service",
              "environment": "production"
            }
            """);

        MDC.clear();
    }

    static void printLogstashConfig() {
        System.out.println("\n--- Logstash Encoder Dependencies ---\n");
        System.out.println("Maven:");
        System.out.println("""
            <dependency>
                <groupId>net.logstash.logback</groupId>
                <artifactId>logstash-logback-encoder</artifactId>
                <version>7.4</version>
            </dependency>
            """);

        System.out.println("Gradle:");
        System.out.println("  implementation 'net.logstash.logback:logstash-logback-encoder:7.4'");

        System.out.println("\nBenefits of JSON logging:");
        System.out.println("  - Structured, machine-parseable logs");
        System.out.println("  - Easy filtering by fields (level, logger, MDC)");
        System.out.println("  - Compatible with ELK, Splunk, Datadog, CloudWatch");
        System.out.println("  - Supports distributed tracing (traceId, spanId)");
        System.out.println("  - Custom fields for application context");
    }
}
