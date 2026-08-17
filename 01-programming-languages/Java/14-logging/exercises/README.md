# Logging Exercises

Practice Java logging through hands-on exercises.

## Exercise 1: Configure Logback with Multiple Appenders

**Problem Statement:**
Configure a Logback logging system with three appenders: console (INFO level), file (DEBUG level), and email (ERROR level). Each appender should have a distinct format and output destination.

**Expected Behavior:**
- Console appender shows INFO and above with a simple format.
- File appender writes DEBUG and above to `application.log` with timestamps.
- Email appender sends ERROR messages to an configured email address.
- Each appender can be enabled/disabled independently via configuration.
- Log levels are correctly filtered per appender.

**Hints:**
- Use `logback.xml` with `<appender>` elements for each target.
- Use `LevelFilter` or `ThresholdFilter` to control per-appender levels.
- Use `SMTPAppender` for email with `SMTPHost`, `SMTPPort`, and `From` settings.
- Use `<logger>` elements to set per-package log levels.

---

## Exercise 2: Implement Structured Logging

**Problem Statement:**
Implement structured logging that outputs log entries as JSON objects. Each log entry should include timestamp, level, logger name, message, thread name, and custom fields. Use a Logback `MessageConverter` to produce JSON output.

**Expected Behavior:**
- Each log line is a valid JSON object.
- Standard fields include: `timestamp`, `level`, `logger`, `message`, `thread`.
- Custom fields can be added via MDC (e.g., `userId`, `requestId`).
- JSON output is one line per log entry (JSON Lines format).
- The logger can be configured to output plain text or JSON via configuration.

**Hints:**
- Create a custom `MessageConverter` extending `ClassicConverter`.
- Use `ObjectMapper` to serialize log events to JSON.
- Use `MDC.get("userId")` to include MDC values in the JSON output.
- Configure the converter in `logback.xml` with `<conversionRule>`.

---

## Exercise 3: Custom Log Level

**Problem Statement:**
Define a custom log level `AUDIT` between INFO and WARN for security-relevant events. Register it with Logback and SLF4J so it can be used like any built-in level.

**Expected Behavior:**
- `logger.audit("User logged in: {}", username)` compiles and works.
- The AUDIT level can be configured independently in `logback.xml`.
- AUDIT messages appear in a separate log file.
- AUDIT level is filterable (e.g., show only AUDIT and above).
- The custom level works with MDC and structured logging.

**Hints:**
- Define the level using `Level.valueOf("AUDIT", 35000)` (between INFO=20000 and WARN=30000).
- Register with `ILoggerFactory` using `ch.qos.logback.classic.Logger`.
- Add a `<level>` element in `logback.xml` for the AUDIT level.
- Override `toString()` to display the custom level name in output.

---

## Exercise 4: MDC Context for Logs

**Problem Statement:**
Implement MDC (Mapped Diagnostic Context) to enrich log output with request-specific information. Create a servlet filter that populates MDC with request ID, user ID, and IP address, then clears it after the request.

**Expected Behavior:**
- Each HTTP request gets a unique `requestId` in MDC.
- The `userId` is set after authentication and appears in all logs for that request.
- The client `ipAddress` is included in log output.
- After the request completes, MDC is cleared to prevent leaking between threads.
- Logs from the same request share the same requestId for correlation.

**Hints:**
- Use `MDC.put("requestId", UUID.randomUUID().toString())` in a filter's `doFilter`.
- Use `try-finally` to ensure `MDC.clear()` runs even if the request fails.
- Configure Logback pattern with `%X{requestId}` to include MDC values.
- Use `%X{userId:-anonymous}` for a default value when userId is not set.

---

## Exercise 5: Configure Log Rotation

**Problem Statement:**
Configure Logback's `RollingFileAppender` with time-based and size-based rotation policies. Set up daily rotation, size limit per file, total size cap, and maximum file count.

**Expected Behavior:**
- Logs rotate daily, creating files named `app-2024-01-15.log`.
- Each file is limited to 10MB; rotation occurs when the limit is reached.
- Total log storage is capped at 500MB across all rotated files.
- Maximum of 30 days of logs are retained.
- Old files beyond the retention policy are deleted automatically.

**Hints:**
- Use `TimeBasedRollingPolicy` with `%d{yyyy-MM-dd}` in the filename pattern.
- Use `SizeAndTimeBasedRollingPolicy` for combined size and time rotation.
- Set `<maxFileSize>10MB</maxFileSize>` and `<maxHistory>30</maxHistory>`.
- Set `<totalSizeCap>500MB</totalSizeCap>` for total storage limit.

---

## Exercise 6: Audit Logging

**Problem Statement:**
Build an audit logging system that records all user actions in an application. Create an `AuditLogger` that captures who did what, when, and the result (success/failure). Store audit logs in a separate file with a structured format.

**Expected Behavior:**
- Each audit entry includes: timestamp, userId, action, resource, result, and IP address.
- Audit logs are written to a dedicated `audit.log` file.
- Audit entries are tamper-evident (include a hash of the entry).
- The `AuditLogger` is a singleton that can be accessed globally.
- Audit logging is asynchronous to not block the main application thread.

**Hints:**
- Use `LoggerFactory.getLogger("AUDIT")` with a dedicated logger name.
- Use a separate appender in `logback.xml` for the audit logger.
- Compute a simple hash (SHA-256) of the audit entry string.
- Use `AsyncAppender` or a background thread for asynchronous writes.

## Interview Questions

1. **What is the difference between SLF4J, Logback, and Log4j2?**
   SLF4J is a logging facade (API) that provides a unified interface. Logback and Log4j2 are implementations. SLF4J lets you swap implementations without changing code: `LoggerFactory.getLogger(MyClass.class)`. Logback is the native SLF4J implementation with better performance. Log4j2 offers async logging and plugin architecture. Use SLF4J API + Logback or Log4j2 as the backend.

2. **What are the differences between log levels and when should you use each?**
   - **ERROR**: System failures requiring immediate attention (database down, payment failed)
   - **WARN**: Unexpected but recoverable situations (deprecated API usage, retry succeeded)
   - **INFO**: Significant business events (user login, order placed, request completed)
   - **DEBUG**: Diagnostic information for troubleshooting (method entry/exit, variable values)
   - **TRACE**: Extremely detailed information (SQL queries, HTTP headers)
   Use INFO for production monitoring, DEBUG for development, ERROR for alerts, WARN for anomalies.

3. **What is MDC and why is it important?**
   MDC (Mapped Diagnostic Context) attaches request-specific data (requestId, userId, IP) to all log messages within a thread. It's essential for distributed systems—when tracing a request across services, the requestId correlates logs. MDC uses thread-local storage, so data is automatically scoped to the current request. Always clear MDC in `finally` blocks to prevent data leaks between threads.

4. **What is the difference between synchronous and asynchronous logging?**
   Synchronous logging writes each log message immediately in the calling thread, blocking until I/O completes. Asynchronous logging buffers messages in a queue and writes them in a background thread. Async is 5-10x faster for high-throughput applications but risks losing logs on crash (buffer not flushed). Use sync for error/critical logs, async for debug/info in high-traffic systems.

5. **How do you configure log rotation and why is it necessary?**
   Log rotation manages log file size and retention. Configure `RollingFileAppender` with time-based (daily), size-based (10MB per file), and total cap (500MB) policies. Without rotation, logs fill disk. With only size-based rotation, you lose time context. Combined policies give you both: files named `app-2024-01-15.log` with 10MB max, 30 days retention, 500MB total cap.

6. **What are structured logs and why are they preferred?**
   Structured logs output JSON objects instead of unstructured text. Each log entry has fields: `timestamp`, `level`, `logger`, `message`, `thread`, plus custom fields (userId, requestId). Benefits: machine-parseable for log aggregation (ELK, Splunk), filterable by field, searchable with queries, and compatible with log analysis tools. Use JSON Lines format (one JSON object per line).

7. **How do you implement audit logging in Java?**
   Audit logging records who did what, when, and the result. Create a dedicated logger (`LoggerFactory.getLogger("AUDIT")`) with a separate appender writing to `audit.log`. Include: timestamp, userId, action, resource, result (success/failure), and IP address. Use `AsyncAppender` for non-blocking writes. Compute a hash of each entry for tamper evidence. Audit logs should be append-only and stored separately from application logs.

8. **What is a custom log level and when would you create one?**
   Custom log levels fill gaps between standard levels. An `AUDIT` level (between INFO and WARN) captures security-relevant events without polluting INFO or triggering WARN alerts. Define using `Level.valueOf("AUDIT", 35000)` (between INFO=20000 and WARN=30000). Register with Logback's `ILoggerFactory`. Use custom levels when existing levels don't semantically match your use case and you need independent filtering.

9. **What are the best practices for logging in production?**
   - Log at appropriate levels (don't use DEBUG in production)
   - Include correlation IDs (requestId) for tracing
   - Use structured logging (JSON) for machine parsing
   - Never log sensitive data (passwords, credit cards, SSNs)
   - Configure log rotation with size and time limits
   - Use async logging for high-throughput services
   - Monitor ERROR logs for alerting
   - Keep log messages concise and actionable

10. **How do you troubleshoot logging configuration issues?**
    - Add `<statusListener class="ch.qos.logback.core.status.OnConsoleStatusListener"/>` to see Logback's internal status
    - Check classpath for conflicting logging implementations (SLF4J + Log4j)
    - Verify `logback.xml` is in the root of classpath (not in a package)
    - Use `LoggerFactory.getILoggerFactory()` to confirm which implementation is loaded
    - Enable Logback debug mode: `java -Dlogback.debug=true -jar app.jar`

## Pitfalls

1. **Logging in Loops** — Logging every iteration of a high-frequency loop (10,000+ iterations) generates massive output. Use DEBUG level for loops, or log summary statistics instead of individual entries.

2. **String Concatenation in Log Statements** — `logger.info("User: " + user.getName())` always evaluates the concatenation, even if INFO is disabled. Use parameterized logging: `logger.info("User: {}", user.getName())`.

3. **Logging Sensitive Data** — Passwords, credit card numbers, SSNs, and API keys must never appear in logs. Use a custom `MessageConverter` that masks sensitive fields. Apply `@Loggable` annotation with exclusions.

4. **Not Clearing MDC** — Forgetting `MDC.clear()` in `finally` blocks causes data leaks between threads. In servlet filters, always clear MDC after `chain.doFilter()`. In Spring, use `@Around` advice with try-finally.

5. **Using System.out/System.err** — `System.out.println()` bypasses logging frameworks, ignores log levels, and can't be configured. Always use SLF4J logger. Replace `e.printStackTrace()` with `logger.error("Operation failed", e)`.

6. **Swallowing Exceptions Without Logging** — `catch (Exception e) { /* ignore */ }` hides errors. At minimum, log at WARN level: `logger.warn("Operation failed", e)`. For expected exceptions (like validation), log at DEBUG.

7. **Over-Logging** — Logging every method entry/exit in production creates noise. Use TRACE for extreme verbosity and enable it only for specific packages during debugging.

8. **Not Configuring Log Rotation** — Logs without rotation fill disk in hours. Always configure `RollingFileAppender` with size and time limits. Monitor disk usage as a safety net.

## Performance

1. **Parameterized Logging** — `logger.debug("Value: {}", value)` is 5-10x faster than `logger.debug("Value: " + value)` because the string concatenation is skipped when DEBUG is disabled. Always use `{}` placeholders.

2. **Async Logging Throughput** — Logback's `AsyncAppender` with a 512-element queue achieves 100,000+ events/second vs. 10,000/second for synchronous. Use `discardingThreshold=0` to prevent discarding DEBUG messages under load.

3. **Log Level Evaluation** — `if (logger.isDebugEnabled())` check adds ~1ns overhead. With parameterized logging, this check is automatic. Only use explicit checks for expensive computations before logging.

4. **MDC Overhead** — `MDC.put()` uses `ThreadLocal`, costing ~5ns per call. In high-throughput services, MDC is negligible compared to I/O. Clear MDC promptly to prevent memory leaks.

5. **Structured Logging Serialization** — JSON serialization adds ~10-50 microseconds per log event. For 10,000 events/second, this adds 0.5s of CPU time. Use `ObjectMapper` caching and pre-allocated buffers.

6. **File Appender Buffering** — Logback buffers writes (default 8KB). Flushing on every message reduces throughput. Configure `immediateFlush=false` for async appenders, `true` for sync appenders logging ERROR level.

7. **Log File Size Management** — `SizeAndTimeBasedRollingPolicy` with `maxFileSize=10MB` and `maxHistory=30` uses ~300MB disk. Monitor with `totalSizeCap=500MB`. Use compression (`zip` pattern) for archived logs.

8. **SMTP Appender Performance** — Email alerts per ERROR event are slow (100-500ms per email). Buffer errors and send digest emails every 5 minutes using `EvaluatorFilter` with `TimeBasedTriggeringPolicy`.

## Examples

```java
// Structured Logging with MDC
public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", getCurrentUserId());
            MDC.put("ipAddress", req.getRemoteAddr());
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}

// Audit Logger
public class AuditLogger {
    private static final Logger AUDIT = LoggerFactory.getLogger("AUDIT");
    
    public void logAction(String userId, String action, String resource, boolean success) {
        AUDIT.info("userId={} action={} resource={} success={} timestamp={}",
            userId, action, resource, success, Instant.now());
    }
}

// Custom Level Definition
public class AuditLevel {
    public static final Level AUDIT = Level.valueOf("AUDIT", 35000);
    
    public static void audit(Logger logger, String message, Object... args) {
        if (logger.isEnabledFor(AUDIT)) {
            logger.log(AUDIT, message, args);
        }
    }
}

// logback.xml Configuration
// <configuration>
//   <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
//     <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
//       <pattern>%d{ISO8601} [%level] [%logger] [%thread] %msg%n</pattern>
//     </encoder>
//   </appender>
//   <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
//     <file>app.log</file>
//     <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
//       <fileNamePattern>app-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
//       <maxFileSize>10MB</maxFileSize>
//       <maxHistory>30</maxHistory>
//       <totalSizeCap>500MB</totalSizeCap>
//     </rollingPolicy>
//     <encoder><pattern>%d{ISO8601} [%level] %msg%n</pattern></encoder>
//   </appender>
//   <root level="INFO">
//     <appender-ref ref="JSON"/>
//     <appender-ref ref="FILE"/>
//   </root>
// </configuration>
```

## Internal Working

Logback initializes by parsing `logback.xml` using `JoranConfigurator`. Appenders are created and attached to loggers via the logger hierarchy. When `logger.info("msg")` is called, SLF4J delegates to Logback's `Logger`, which checks the effective level, creates a `LoggingEvent` with timestamp, thread, level, and message, then passes it to all attached appenders. Appenders format the event (via `Encoder`) and write to destinations (console, file, network). Async appenders buffer events in a `LinkedBlockingDeque` and write in a background thread.

## Why This Concept Exists

Logging exists because software is a black box without observable internal state. Logs provide the only runtime visibility into what happened, when, and why. Without structured logging, debugging production issues requires reproducing problems locally—often impossible. Logback/Log4j2 emerged because `System.out.println()` couldn't handle multi-threaded applications, log rotation, different output formats, or filtering. SLF4J was created to decouple application code from specific logging implementations, enabling framework-agnostic logging APIs.

## Overview

Java logging provides visibility into application behavior at runtime. SLF4J is the standard API (`LoggerFactory.getLogger()`). Logback (native SLF4J impl) and Log4j2 are popular backends. Key concepts: log levels (ERROR, WARN, INFO, DEBUG, TRACE), MDC for request correlation, structured logging (JSON) for machine parsing, log rotation for disk management, and async logging for performance. Configure via `logback.xml` or `log4j2.xml`. Always log at the right level, use parameterized messages, and never log sensitive data.

## References

- [Logback Manual](https://logback.qos.ch/manual/index.html)
- [Log4j2 Manual](https://logging.apache.org/log4j/2.x/manual/index.html)
- [SLF4J Manual](https://www.slf4j.org/manual.html)
- [Structured Logging with Logback](https://www.baeldung.com/logback-structured-logging)
- [MDC with Logback](https://logback.qos.ch/manual/mdc.html)
- [Log Rotation Best Practices](https://www.baeldung.com/logback-rolling-file-appender)
- [Related: ELK Stack for Log Aggregation](https://www.elastic.co/what-is/elk-stack)
- [Related: Micrometer Metrics](https://micrometer.io/)
