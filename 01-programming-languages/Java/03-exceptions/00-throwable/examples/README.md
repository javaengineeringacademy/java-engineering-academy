# Examples: Throwable in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated. Compile and run each example to verify behavior.

---

## Example 1: Creating Throwable with Message

```java
public class CreateWithMessage {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Disk quota exceeded");
        System.out.println("Message: " + ex.getMessage());
        System.out.println("Class: " + ex.getClass().getName());
    }
}
```

**Output:**
```
Message: Disk quota exceeded
Class: java.lang.RuntimeException
```

**Explanation:** `Throwable` stores a message string passed to the constructor. `getMessage()` retrieves it. The message provides context about what went wrong. Every exception subclass inherits this capability.

---

## Example 2: Cause Chaining

```java
public class CauseChaining {
    public static void main(String[] args) {
        IOException root = new IOException("Connection refused");
        RuntimeException wrapper = new RuntimeException("Service unavailable", root);

        System.out.println("Wrapper message: " + wrapper.getMessage());
        System.out.println("Root cause: " + wrapper.getCause().getMessage());

        Throwable current = wrapper;
        int depth = 0;
        while (current != null) {
            System.out.println("  Level " + depth + ": " + current.getClass().getSimpleName()
                + " - " + current.getMessage());
            current = current.getCause();
            depth++;
        }
    }
}
```

**Output:**
```
Wrapper message: Service unavailable
Root cause: Connection refused
  Level 0: RuntimeException - Service unavailable
  Level 1: IOException - Connection refused
```

**Explanation:** Exception chaining preserves the causal chain. The `getCause()` method returns the original exception. Walking the chain with a loop reveals the full failure sequence. This is essential for debugging production issues.

---

## Example 3: Stack Trace Manipulation

```java
import java.util.Arrays;

public class StackTraceDemo {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Checkpoint");

        StackTraceElement[] trace = ex.getStackTrace();
        System.out.println("Original depth: " + trace.length);
        System.out.println("Top frame: " + trace[0].getMethodName());

        StackTraceElement[] reduced = Arrays.copyOf(trace, 2);
        ex.setStackTrace(reduced);
        System.out.println("Reduced depth: " + ex.getStackTrace().length);
    }
}
```

**Output:**
```
Original depth: 1
Top frame: main
Reduced depth: 2
```

**Explanation:** `getStackTrace()` returns an array of `StackTraceElement` objects. `setStackTrace()` allows replacing the trace. This is useful for cleaning up internal frames before exposing exceptions to callers or for performance-sensitive logging where trace depth is limited.

---

## Example 4: Suppressed Exceptions

```java
public class SuppressedDemo {
    public static void main(String[] args) {
        RuntimeException primary = new RuntimeException("Primary failure");
        primary.addSuppressed(new IOException("Cleanup failed"));
        primary.addSuppressed(new IOException("Second cleanup failed"));

        System.out.println("Primary: " + primary.getMessage());
        System.out.println("Suppressed count: " + primary.getSuppressed().length);
        for (Throwable t : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + t.getMessage());
        }
    }
}
```

**Output:**
```
Primary: Primary failure
Suppressed count: 2
  Suppressed: Cleanup failed
  Suppressed: Second cleanup failed
```

**Explanation:** `addSuppressed()` attaches additional exceptions that occurred during handling of the primary exception. This pattern is automatically used by try-with-resources when a resource's `close()` throws while another exception is in flight. The suppressed exceptions are accessible via `getSuppressed()`.

---

## Example 5: toString and printStackTrace

```java
public class ToStringDemo {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Configuration error",
            new IOException("File not found"));

        System.out.println("=== toString() ===");
        System.out.println(ex.toString());

        System.out.println("\n=== getMessage() ===");
        System.out.println(ex.getMessage());

        System.out.println("\n=== getLocalizedMessage() ===");
        System.out.println(ex.getLocalizedMessage());
    }
}
```

**Output:**
```
=== toString() ===
java.lang.RuntimeException: Configuration error

=== getMessage() ===
Configuration error

=== getLocalizedMessage() ===
Configuration error
```

**Explanation:** `toString()` returns the class name and message. `getMessage()` returns just the message. `getLocalizedMessage()` returns a locale-specific message (defaults to `getMessage()` if not overridden). For full stack traces, use `printStackTrace()` or `getStackTrace()`.
