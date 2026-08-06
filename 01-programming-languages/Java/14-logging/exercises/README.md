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
