# Solutions: Custom Exceptions in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Custom Checked Exception

```java
public class Exercise1 {
    static class AuthenticationException extends Exception {
        private final String username;

        AuthenticationException(String username, String message) {
            super(message);
            this.username = username;
        }

        String getUsername() { return username; }
    }

    static void login(String username, String password) throws AuthenticationException {
        if (!"secret".equals(password)) {
            throw new AuthenticationException(username, "Invalid password");
        }
    }

    public static void main(String[] args) {
        try {
            login("alice", "secret");
            System.out.println("Login successful");
            login("alice", "wrong");
        } catch (AuthenticationException e) {
            System.out.println("Auth failed for " + e.getUsername() + ": " + e.getMessage());
        }
    }
}
```

**Output:**
```
Login successful
Auth failed for alice: Invalid password
```

**Key points:**
- Custom checked exception carries the username.
- Callers must handle or declare the exception.
- The username field provides context for error handling.

---

## Solution 2: Custom Unchecked Exception

```java
import java.util.*;

public class Exercise2 {
    static class DuplicateKeyException extends RuntimeException {
        private final String key;

        DuplicateKeyException(String key) {
            super("Duplicate key: " + key);
            this.key = key;
        }

        String getKey() { return key; }
    }

    static Map<String, String> map = new HashMap<>();

    static void insertIntoMap(String key, String value) {
        if (map.containsKey(key)) {
            throw new DuplicateKeyException(key);
        }
        map.put(key, value);
    }

    public static void main(String[] args) {
        insertIntoMap("name", "Alice");
        System.out.println("Inserted name");
        insertIntoMap("name", "Bob");
    }
}
```

**Output:**
```
Inserted name
Exception in thread "main" DuplicateKeyException: Duplicate key: name
```

**Key points:**
- Unchecked exception for programming errors.
- The key field identifies the duplicate.
- `containsKey` check prevents overwriting existing entries.

---

## Solution 3: Exception with Cause

```java
public class Exercise3 {
    static class ParsingException extends Exception {
        ParsingException(String message) { super(message); }
        ParsingException(String message, Throwable cause) { super(message, cause); }
    }

    static int parseAndDouble(String input) throws ParsingException {
        try {
            return Integer.parseInt(input) * 2;
        } catch (NumberFormatException e) {
            throw new ParsingException("Failed to parse: " + input, e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Result: " + parseAndDouble("21"));
            System.out.println("Result: " + parseAndDouble("abc"));
        } catch (ParsingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Result: 42
Error: Failed to parse: abc
Cause: NumberFormatException
```

**Key points:**
- Both constructors allow flexible exception creation.
- The cause chain is preserved for debugging.
- The wrapper adds context about what operation failed.
