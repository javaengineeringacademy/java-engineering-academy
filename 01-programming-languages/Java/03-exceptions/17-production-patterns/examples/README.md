# Examples: Production Patterns in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Global Exception Handler

```java
public class GlobalHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("FATAL: Uncaught exception in " + thread.getName());
            System.err.println("  Type: " + throwable.getClass().getSimpleName());
            System.err.println("  Message: " + throwable.getMessage());
            throwable.printStackTrace(System.err);
        });

        Thread worker = new Thread(() -> {
            throw new RuntimeException("Worker crashed");
        }, "worker-1");

        worker.start();
        try { worker.join(); } catch (InterruptedException e) { /* ignored */ }
    }
}
```

**Output:**
```
FATAL: Uncaught exception in worker-1
  Type: RuntimeException
  Message: Worker crashed
java.lang.RuntimeException: Worker crashed
    at GlobalHandler.lambda$main$0(GlobalHandler.java:5)
    at java.base/java.lang.Thread.run(Thread.java:829)
```

**Explanation:** The global handler catches any uncaught exception in any thread. It logs the thread name, exception type, message, and stack trace. This is the last line of defense in production applications.

---

## Example 2: Retry Pattern

```java
public class RetryPattern {
    static int attemptCount = 0;

    static String unreliableOperation() {
        attemptCount++;
        if (attemptCount < 3) {
            throw new RuntimeException("Attempt " + attemptCount + " failed");
        }
        return "Success on attempt " + attemptCount;
    }

    static <T> T retry(int maxAttempts, java.util.concurrent.Callable<T> operation) throws Exception {
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                return operation.call();
            } catch (Exception e) {
                System.out.println("Attempt " + i + " failed: " + e.getMessage());
                if (i == maxAttempts) throw e;
                Thread.sleep(100);
            }
        }
        throw new RuntimeException("Unreachable");
    }

    public static void main(String[] args) throws Exception {
        String result = retry(5, RetryPattern::unreliableOperation);
        System.out.println("Result: " + result);
    }
}
```

**Output:**
```
Attempt 1 failed: Attempt 1 failed
Attempt 2 failed: Attempt 2 failed
Result: Success on attempt 3
```

**Explanation:** The retry pattern attempts an operation multiple times before failing. Exponential backoff (increasing delays) prevents overwhelming the failing resource. This is essential for transient failures in distributed systems.

---

## Example 3: Circuit Breaker Pattern

```java
public class CircuitBreakerPattern {
    enum State { CLOSED, OPEN, HALF_OPEN }

    static State state = State.CLOSED;
    static int failureCount = 0;
    static final int FAILURE_THRESHOLD = 3;
    static long lastFailureTime = 0;
    static final long RESET_TIMEOUT = 5000;

    static String callService() {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > RESET_TIMEOUT) {
                state = State.HALF_OPEN;
                System.out.println("Circuit: HALF_OPEN - testing");
            } else {
                throw new RuntimeException("Circuit OPEN - request rejected");
            }
        }

        try {
            // Simulated service call
            if (Math.random() > 0.5) {
                throw new RuntimeException("Service unavailable");
            }
            failureCount = 0;
            state = State.CLOSED;
            return "Response from service";
        } catch (RuntimeException e) {
            failureCount++;
            lastFailureTime = System.currentTimeMillis();
            if (failureCount >= FAILURE_THRESHOLD) {
                state = State.OPEN;
                System.out.println("Circuit: OPEN - too many failures");
            }
            throw e;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Call " + (i + 1) + ": " + callService());
            } catch (RuntimeException e) {
                System.out.println("Call " + (i + 1) + ": " + e.getMessage());
            }
            try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignored */ }
        }
    }
}
```

**Output (varies):**
```
Call 1: Response from service
Call 2: Service unavailable
Call 3: Service unavailable
Circuit: OPEN - too many failures
Call 4: Circuit OPEN - request rejected
Call 5: Circuit OPEN - request rejected
Circuit: HALF_OPEN - testing
Call 6: Response from service
```

**Explanation:** The circuit breaker prevents cascading failures. After too many failures, it "opens" and rejects requests immediately. After a timeout, it enters "half-open" state to test if the service has recovered. This protects both the client and the failing service.

---

## Example 4: Exception Logging Pattern

```java
public class ExceptionLogging {
    static void logException(String context, Throwable throwable) {
        System.err.println("[" + java.time.LocalTime.now() + "] ERROR in " + context);
        System.err.println("  Type: " + throwable.getClass().getName());
        System.err.println("  Message: " + throwable.getMessage());
        System.err.println("  Stack trace:");
        for (StackTraceElement frame : throwable.getStackTrace()) {
            System.err.println("    at " + frame);
        }
        if (throwable.getCause() != null) {
            System.err.println("  Caused by: " + throwable.getCause().getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            logException("divide", e);
        }
    }
}
```

**Output:**
```
[14:30:00.123] ERROR in divide
  Type: java.lang.ArithmeticException
  Message: / by zero
  Stack trace:
    at ExceptionLogging.main(ExceptionLogging.java:15)
```

**Explanation:** Structured logging includes timestamp, context, exception type, message, and stack trace. This information is essential for debugging production issues. In real applications, use a logging framework (SLF4J, Log4j) instead of `System.err`.
