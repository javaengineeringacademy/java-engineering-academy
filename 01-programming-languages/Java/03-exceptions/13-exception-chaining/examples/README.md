# Examples: Exception Chaining in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Basic Chaining

```java
public class BasicChaining {
    public static void main(String[] args) {
        IOException root = new IOException("Connection refused");
        RuntimeException wrapper = new RuntimeException("Service unavailable", root);

        System.out.println("Wrapper: " + wrapper.getMessage());
        System.out.println("Cause: " + wrapper.getCause().getMessage());
    }
}
```

**Output:**
```
Wrapper: Service unavailable
Cause: Connection refused
```

**Explanation:** The second constructor argument becomes the cause. `getCause()` retrieves it. This preserves the original exception while providing a higher-level message.

---

## Example 2: Multi-Level Chaining

```java
public class MultiLevelChaining {
    public static void main(String[] args) {
        IOException root = new IOException("Disk failure");
        RuntimeException mid = new RuntimeException("Write failed", root);
        Exception top = new Exception("Operation failed", mid);

        Throwable current = top;
        int level = 0;
        while (current != null) {
            System.out.println("Level " + level + ": " + current.getClass().getSimpleName()
                + " - " + current.getMessage());
            current = current.getCause();
            level++;
        }
    }
}
```

**Output:**
```
Level 0: Exception - Operation failed
Level 1: RuntimeException - Write failed
Level 2: IOException - Disk failure
```

**Explanation:** Each exception wraps the previous one. Walking the chain with `getCause()` reveals the full failure sequence. This is essential for debugging complex failures.

---

## Example 3: Exception Translation

```java
public class ExceptionTranslation {
    static class DataAccessException extends RuntimeException {
        DataAccessException(String msg, Throwable cause) { super(msg, cause); }
    }

    static void readDatabase() {
        try {
            throw new java.sql.SQLException("Timeout");
        } catch (java.sql.SQLException e) {
            throw new DataAccessException("Database read failed", e);
        }
    }

    public static void main(String[] args) {
        try {
            readDatabase();
        } catch (DataAccessException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Error: Database read failed
Root cause: SQLException
```

**Explanation:** Exception translation converts low-level exceptions to domain exceptions. The original exception is preserved as the cause. Callers catch the domain type and can access the root cause for diagnostics.

---

## Example 4: Preserve Cause in Rethrow

```java
public class PreserveCause {
    static void process(String input) {
        try {
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (Exception e) {
            throw new RuntimeException("Processing failed: " + input, e);
        }
    }

    public static void main(String[] args) {
        try {
            process("abc");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause type: " + e.getCause().getClass().getSimpleName());
            System.out.println("Cause message: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Error: Processing failed: abc
Cause type: NumberFormatException
Cause message: For input string: "abc"
```

**Explanation:** When catching and rethrowing, always pass the original exception as the cause. This preserves the full context for debugging. Never log and swallow — rethrow with the cause attached.
