# Java Logging Quiz

## Question 1 (MCQ)
What is the primary purpose of SLF4J in Java logging?
- A) It is a logging implementation that writes logs to files
- B) It is a logging facade that provides a unified API for different logging backends
- C) It replaces all other logging frameworks
- D) It only works with Logback

**Answer: B**
**Explanation:** SLF4J (Simple Logging Facade for Java) is an abstraction layer that provides a common API. You can plug in different implementations like Logback or Log4j2 behind it.

---

## Question 2 (MCQ)
Which log level should be used for messages that indicate a potential problem but the application can continue functioning?
- A) ERROR
- B) INFO
- C) WARN
- D) DEBUG

**Answer: C**
**Explanation:** WARN level is used for potentially harmful situations that don't prevent the application from continuing. ERROR indicates a failure that affects the current operation.

---

## Question 3 (MCQ)
What is the recommended way to pass variable data to a log message in SLF4J?
- A) String concatenation: `logger.info("User: " + userId)`
- B) String formatting: `logger.info(String.format("User: %s", userId))`
- C) Parameterized messages: `logger.info("User: {}", userId)`
- D) System.out.println for dynamic data

**Answer: C**
**Explanation:** Parameterized messages with `{}` placeholders are preferred because they avoid string concatenation overhead when the log level is disabled, and they handle exceptions properly.

---

## Question 4 (MCQ)
What is structured logging?
- A) Logging with consistent indentation
- B) Logging events in a machine-readable format like JSON
- C) Organizing log files into folders
- D) Using only the INFO log level

**Answer: B**
**Explanation:** Structured logging outputs logs in a structured format (e.g., JSON) with key-value pairs, making it easier to parse, search, and analyze logs programmatically.

---

## Question 5 (Code Output)
What does this code print?

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Processing item: {}", 42);
        logger.info("Application started");
        logger.warn("Disk space low: {}%", 85);
        logger.error("Failed to connect", new RuntimeException("Connection refused"));
    }
}
```

**Answer:** (output depends on log level configuration, typically shows INFO, WARN, ERROR messages)
**Explanation:** The output depends on the configured log level. By default, DEBUG is hidden. INFO shows "Application started", WARN shows "Disk space low: 85%", and ERROR shows "Failed to connect" with the exception stack trace.

---

## Question 6 (Code Output)
What does this code print?

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        MDC.put("userId", "12345");
        MDC.put("requestId", "req-abc");

        logger.info("User logged in");
        logger.info("Data processed");

        MDC.clear();
    }
}
```

**Answer:** Logs include userId=12345 and requestId=req-abc as context fields (format depends on logback configuration)
**Explanation:** MDC (Mapped Diagnostic Context) adds key-value pairs to all log entries within the thread. These values are automatically included in the log output if configured in the log pattern.

---

## Question 7 (Bug Finding)
Find the bug:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public void processOrder(String orderId) {
        logger.info("Processing order: " + orderId);
        // ... processing ...
        logger.info("Completed order: " + orderId);
    }
}
```

**Bug:** Using string concatenation (`+`) for log messages causes unnecessary string creation even when the log level is disabled. This wastes CPU cycles and memory.
**Fix:** Use parameterized messages:
```java
logger.info("Processing order: {}", orderId);
logger.info("Completed order: {}", orderId);
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public void transferMoney(Account from, Account to, double amount) {
        logger.info("Starting transfer of ${} from {} to {}", amount, from.getId(), to.getId());
        from.debit(amount);
        to.credit(amount);
        logger.info("Transfer complete");
    }
}
```

**Bug:** The log message logs sensitive financial information (amount, account IDs) which could be exposed in logs. Also, there's no exception handling — if the transfer fails, the logs won't capture the error.
**Fix:** Add error handling and consider masking sensitive data:
```java
try {
    from.debit(amount);
    to.credit(amount);
    logger.info("Transfer complete");
} catch (Exception e) {
    logger.error("Transfer failed from {} to {}", from.getId(), to.getId(), e);
    throw e;
}
```

---

## Question 9 (Scenario-based)
Your application generates 10GB of logs per day. You need to: (1) keep 30 days of logs, (2) compress old logs, (3) search logs efficiently, and (4) alert on ERROR-level messages. How should you configure logging?

- A) Write all logs to a single file and use grep for searching
- B) Use Logback with TimeBasedRollingPolicy for daily rotation, GZ compression, structured JSON format, and an ErrorThresholdEvaluator for alerts
- C) Use System.out.println and redirect to a file
- D) Store logs in a relational database

**Answer: B**
**Explanation:** Logback's rolling policy handles rotation and compression automatically. JSON format enables efficient searching with tools like Elasticsearch. ErrorThresholdEvaluator triggers alerts when ERROR logs appear. This is the standard production logging setup.

---

## Question 10 (Architecture Decision)
You are designing a logging system for a distributed system with 50 microservices. Logs need to be correlated across services, searchable in real-time, and retained for compliance. How should you architect this?

- A) Each service writes to its own log file on the local disk
- B) Use SLF4J with Logback in each service, output structured JSON with correlation IDs, ship logs to a centralized platform (ELK/Splunk) via a log collector (Filebeat/Fluentd)
- C) Use a shared network drive for all services
- D) Send all logs to a single database

**Answer: B**
**Explanation:** Structured JSON logs with correlation IDs enable cross-service tracing. A centralized platform provides real-time search and alerting. Log collectors decouple applications from the logging infrastructure. This architecture scales to hundreds of services and meets compliance requirements.
