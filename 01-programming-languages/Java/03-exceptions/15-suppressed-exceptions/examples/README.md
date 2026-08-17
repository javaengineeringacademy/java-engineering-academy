# Examples: Suppressed Exceptions in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Manual Suppressed Exceptions

```java
public class ManualSuppressed {
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

**Explanation:** `addSuppressed()` attaches additional exceptions that occurred during handling of the primary exception. The suppressed exceptions are accessible via `getSuppressed()`. This preserves all failure information.

---

## Example 2: TWR Suppressed Exceptions

```java
import java.io.*;

public class TWRSuppressed {
    static class FlakyResource implements AutoCloseable {
        String name;
        FlakyResource(String name) { this.name = name; }
        void use() { System.out.println("Using " + name); }
        @Override public void close() {
            System.out.println("Closing " + name);
            if ("db".equals(name)) throw new RuntimeException("Close failed");
        }
    }

    public static void main(String[] args) {
        try (var file = new FlakyResource("file");
             var db = new FlakyResource("db")) {
            file.use();
            db.use();
            throw new RuntimeException("Primary error");
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable t : e.getSuppressed()) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}
```

**Output:**
```
Using file
Using db
Closing db
Closing file
Primary: Primary error
Suppressed: Close failed
```

**Explanation:** When `db.close()` throws during cleanup, that exception is added as suppressed to the primary exception. Both exceptions are preserved — the primary and the cleanup failure. Resources are closed in reverse order.

---

## Example 3: Suppressed Exception Message

```java
public class SuppressedMessage {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("Original");
        } catch (RuntimeException e) {
            e.addSuppressed(new RuntimeException("Suppressed 1"));
            e.addSuppressed(new RuntimeException("Suppressed 2"));

            System.out.println("Exception message: " + e.getMessage());
            System.out.println("Suppressed exceptions:");
            for (Throwable t : e.getSuppressed()) {
                System.out.println("  - " + t.getMessage());
            }
        }
    }
}
```

**Output:**
```
Exception message: Original
Suppressed exceptions:
  - Suppressed 1
  - Suppressed 2
```

**Explanation:** The primary exception's message is independent of suppressed exceptions. Each suppressed exception has its own message and stack trace. All are preserved for debugging.

---

## Example 4: catch and addSuppressed

```java
public class CatchAndSuppress {
    static void processWithCleanup(String input) {
        RuntimeException primary = null;
        try {
            int value = Integer.parseInt(input);
            System.out.println("Value: " + value);
        } catch (NumberFormatException e) {
            primary = new RuntimeException("Parse failed");
        } finally {
            try {
                System.out.println("Cleanup: releasing resources");
            } catch (Exception cleanupEx) {
                if (primary != null) {
                    primary.addSuppressed(cleanupEx);
                } else {
                    throw new RuntimeException("Cleanup failed", cleanupEx);
                }
            }
        }
        if (primary != null) throw primary;
    }

    public static void main(String[] args) {
        try {
            processWithCleanup("abc");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Suppressed: " + e.getSuppressed().length);
        }
    }
}
```

**Output:**
```
Cleanup: releasing resources
Error: Parse failed
Suppressed: 0
```

**Explanation:** This pattern manually manages suppressed exceptions in catch-finally blocks. If a primary exception exists, cleanup exceptions are added as suppressed. Otherwise, the cleanup exception becomes the primary. This mimics what TWR does automatically.
