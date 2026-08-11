# Examples: The throw Keyword

## Example 1: Throwing Checked Exceptions

Checked exceptions must be declared in the method signature. The caller is forced to handle or propagate them.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckedExceptionExample {

    static String readFirstLine(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path);
        }
        return Files.readAllLines(path).get(0);
    }

    public static void main(String[] args) {
        try {
            String line = readFirstLine(Path.of("config.txt"));
            System.out.println("First line: " + line);
        } catch (IOException e) {
            System.err.println("Read failed: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Read failed: File not found: config.txt
```

**Explanation:** The method `readFirstLine` declares `throws IOException`. The `throw` statement transfers control to the `catch` block in `main`. The message includes the path so the caller knows which file was missing.

---

## Example 2: Throwing Unchecked Exceptions

Unchecked exceptions do not require a `throws` declaration. They signal programming errors or invalid arguments.

```java
public class UncheckedExceptionExample {

    static double computeDiscount(double price, double percentage) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                "Percentage must be between 0 and 100, got: " + percentage);
        }
        return price * (1 - percentage / 100.0);
    }

    public static void main(String[] args) {
        System.out.println("Discount: " + computeDiscount(100.0, 20.0));

        try {
            computeDiscount(-10.0, 15.0);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            computeDiscount(100.0, 150.0);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Discount: 80.0
Error: Price cannot be negative: -10.0
Error: Percentage must be between 0 and 100, got: 150.0
```

**Explanation:** The method validates arguments and throws `IllegalArgumentException` with a message that includes the actual value. No `throws` declaration is required because `IllegalArgumentException` extends `RuntimeException`.

---

## Example 3: Rethrowing Exceptions

Rethrowing preserves the original exception or replaces it with a more meaningful type while retaining the cause.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RethrowExample {

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:test");
    }

    static void executeQuery(String sql) {
        try (Connection conn = getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            System.out.println("Query executed successfully");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to execute query: " + sql, e);
        }
    }

    public static void main(String[] args) {
        try {
            executeQuery("SELECT * FROM nonexistent_table");
        } catch (DataAccessException e) {
            System.err.println("Data access error: " + e.getMessage());
            System.err.println("Original cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Data access error: Failed to execute query: SELECT * FROM nonexistent_table
Original cause: SQLSyntaxErrorException
```

**Explanation:** The `SQLException` is caught and rethrown as `DataAccessException`. The original exception is passed as the cause, preserving the full stack trace. The caller catches the domain-specific type.

---

## Example 4: Exception Chaining with throw

Exception chaining attaches one exception as the cause of another, creating a trail of context.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExceptionChainingExample {

    static class ConfigException extends RuntimeException {
        ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static String readFile(Path path) throws IOException {
        return Files.readString(path);
    }

    static int parsePort(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ConfigException(
                "Invalid port value in config: '" + value + "'", e);
        }
    }

    static void loadConfig(Path path) {
        try {
            String content = readFile(path);
            int port = parsePort(content);
            System.out.println("Config loaded, port: " + port);
        } catch (IOException e) {
            throw new ConfigException(
                "Cannot read config file: " + path, e);
        }
    }

    public static void main(String[] args) {
        try {
            loadConfig(Path.of("app.properties"));
        } catch (ConfigException e) {
            System.err.println("Config error: " + e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("  Caused by: " + cause.getMessage());
                cause = cause.getCause();
            }
        }
    }
}
```

**Output:**
```
Config error: Cannot read config file: app.properties
  Caused by: app.properties (No such file or directory)
```

**Explanation:** Two different failure modes (file not found, parse error) are both wrapped in `ConfigException`. Each wrapper adds context about what was being attempted. The cause chain is traversed to show the full failure sequence.

---

## Example 5: Custom Exception Creation

Custom exceptions encode domain semantics. The exception hierarchy allows callers to catch at the appropriate level.

```java
public class CustomExceptionExample {

    static class ServiceException extends RuntimeException {
        ServiceException(String message) { super(message); }
        ServiceException(String message, Throwable cause) { super(message, cause); }
    }

    static class ValidationException extends ServiceException {
        ValidationException(String message) { super(message); }
    }

    static class NotFoundException extends ServiceException {
        NotFoundException(String message) { super(message); }
    }

    static class DuplicateException extends ServiceException {
        DuplicateException(String message) { super(message); }
    }

    static void registerUser(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username cannot be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Invalid email: " + email);
        }
        if ("admin".equals(username)) {
            throw new DuplicateException("Username already exists: " + username);
        }
        if (username.length() < 3) {
            throw new ValidationException(
                "Username must be at least 3 characters, got: " + username.length());
        }
        System.out.println("User registered: " + username);
    }

    public static void main(String[] args) {
        String[] testCases = {
            "alice,alice@example.com",
            ",bob@example.com",
            "admin,admin@example.com",
            "ab,charlie@example.com"
        };

        for (String tc : testCases) {
            String[] parts = tc.split(",");
            try {
                registerUser(parts[0], parts[1]);
            } catch (ValidationException e) {
                System.err.println("Validation: " + e.getMessage());
            } catch (DuplicateException e) {
                System.err.println("Duplicate: " + e.getMessage());
            } catch (ServiceException e) {
                System.err.println("Service: " + e.getMessage());
            }
        }
    }
}
```

**Output:**
```
User registered: alice
Validation: Username cannot be blank
Duplicate: Username already exists: admin
Validation: Username must be at least 3 characters, got: 2
```

**Explanation:** The hierarchy `ServiceException > ValidationException > NotFoundException > DuplicateException` allows callers to catch at any level. A general `catch (ServiceException e)` handles all domain errors, while specific catches handle individual failure modes.

---

## Example 6: Throw with Suppressed Exceptions

When an exception occurs during cleanup while another exception is in flight, the cleanup exception is added as suppressed.

```java
import java.io.Closeable;
import java.io.IOException;

public class SuppressedThrowExample {

    static class ManagedResource implements Closeable {
        private final String name;
        private boolean open = true;

        ManagedResource(String name) { this.name = name; }

        void use() {
            if (!open) throw new IllegalStateException(name + " is closed");
            System.out.println("Using " + name);
        }

        @Override
        public void close() throws IOException {
            open = false;
            System.out.println("Closed " + name);
            if ("database".equals(name)) {
                throw new IOException("Close failed for " + name);
            }
        }
    }

    public static void main(String[] args) {
        try (var db = new ManagedResource("database");
             var file = new ManagedResource("file")) {
            db.use();
            file.use();
            throw new RuntimeException("Processing failed");
        } catch (RuntimeException e) {
            System.err.println("Caught: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.err.println("  Suppressed: " + suppressed.getMessage());
            }
        }
    }
}
```

**Output:**
```
Using database
Using file
Closed file
Closed database
Caught: Processing failed
  Suppressed: Close failed for database
```

**Explanation:** The `try-with-resources` block attempts to close both resources. When `database.close()` throws, that exception is suppressed because `ProcessingException` is still in flight. The suppressed exception is accessible via `getSuppressed()`.
