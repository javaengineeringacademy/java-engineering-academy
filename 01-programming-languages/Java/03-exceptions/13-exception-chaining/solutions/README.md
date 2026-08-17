# Solutions: Exception Chaining in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Basic Chaining

```java
public class Exercise1 {
    static void wrapAndThrow(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Parse error", e);
        }
    }

    public static void main(String[] args) {
        try {
            wrapAndThrow("abc");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Error: Parse error
Cause: For input string: "abc"
```

**Key points:**
- `new RuntimeException(message, cause)` preserves the cause.
- `getCause()` retrieves the original exception.
- The wrapper adds context while preserving the root cause.

---

## Solution 2: Walk the Chain

```java
public class Exercise2 {
    static int chainDepth(Throwable t) {
        int depth = 0;
        Throwable current = t;
        while (current != null) {
            depth++;
            current = current.getCause();
        }
        return depth;
    }

    public static void main(String[] args) {
        RuntimeException ex1 = new RuntimeException("1");
        RuntimeException ex2 = new RuntimeException("2", ex1);
        RuntimeException ex3 = new RuntimeException("3", ex2);

        System.out.println("Depth 1: " + chainDepth(ex1));
        System.out.println("Depth 3: " + chainDepth(ex3));
    }
}
```

**Output:**
```
Depth 1: 1
Depth 3: 3
```

**Key points:**
- Start counting from 1 (the exception itself).
- `getCause()` returns null at the root.
- The loop terminates when there are no more causes.

---

## Solution 3: Exception Translation

```java
import java.io.*;
import java.nio.file.*;

public class Exercise3 {
    static class ConfigException extends RuntimeException {
        ConfigException(String message) { super(message); }
        ConfigException(String message, Throwable cause) { super(message, cause); }
    }

    static String loadConfig(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new ConfigException("Failed to load config: " + path, e);
        }
    }

    public static void main(String[] args) {
        try {
            loadConfig("app.properties");
        } catch (ConfigException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Error: Failed to load config: app.properties
Cause: FileNotFoundException
```

**Key points:**
- `ConfigException` wraps the `IOException` with context.
- The cause chain is preserved for debugging.
- Callers catch the domain-specific exception type.
