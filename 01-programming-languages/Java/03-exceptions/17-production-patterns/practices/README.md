# Exercises: Production Patterns in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Retry with Backoff

### Problem

Write a `retryWithBackoff` method that retries a `Callable` operation up to `maxAttempts` times with increasing delays (100ms, 200ms, 400ms...).

### Starter Code

```java
import java.util.concurrent.*;

public class Exercise1 {
    static <T> T retryWithBackoff(int maxAttempts, Callable<T> operation) throws Exception {
        // TODO: Retry with exponential backoff
    }

    public static void main(String[] args) throws Exception {
        int[] attempts = {0};
        String result = retryWithBackoff(5, () -> {
            attempts[0]++;
            if (attempts[0] < 3) throw new RuntimeException("Attempt " + attempts[0] + " failed");
            return "Success";
        });
        System.out.println("Result: " + result + " after " + attempts[0] + " attempts");
    }
}
```

### Expected Output

```
Attempt 1 failed
Attempt 2 failed
Result: Success after 3 attempts
```

### Hints

1. Loop from 1 to `maxAttempts`.
2. Try `operation.call()` and return on success.
3. Catch exception and print failure message.
4. Calculate delay: `100 * Math.pow(2, attempt - 1)`.
5. Use `Thread.sleep(delay)`.
6. Throw on last attempt.

---

## Exercise 2: Exception Logger

### Problem

Write a `logError(String context, Throwable t)` method that prints a formatted error log with timestamp, context, exception type, and message.

### Starter Code

```java
public class Exercise2 {
    static void logError(String context, Throwable t) {
        // TODO: Print formatted error log
    }

    public static void main(String[] args) {
        try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            logError("parseInput", e);
        }
    }
}
```

### Expected Output

```
[<timestamp>] ERROR in parseInput
  Type: NumberFormatException
  Message: For input string: "abc"
```

### Hints

1. Use `java.time.LocalTime.now()` for timestamp.
2. Print `t.getClass().getSimpleName()` for type.
3. Print `t.getMessage()` for message.
4. Format consistently for easy scanning.

---

## Exercise 3: Fallback Pattern

### Problem

Write a `withFallback(Callable<T> primary, T fallback)` method that tries the primary operation and returns the fallback value if it fails.

### Starter Code

```java
import java.util.concurrent.*;

public class Exercise3 {
    static <T> T withFallback(Callable<T> primary, T fallback) {
        // TODO: Try primary, catch exception, return fallback
    }

    public static void main(String[] args) {
        String result1 = withFallback(() -> "primary result", "fallback");
        System.out.println("Result 1: " + result1);

        String result2 = withFallback(() -> {
            throw new RuntimeException("failed");
        }, "fallback");
        System.out.println("Result 2: " + result2);
    }
}
```

### Expected Output

```
Result 1: primary result
Result 2: fallback
```

### Hints

1. Try `primary.call()` and return the result.
2. Catch `Exception` and return the fallback.
3. This pattern provides graceful degradation.
