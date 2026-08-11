# Examples: The throws Declaration (Part 1)

> Examples 1–4. See [Part 2](README-Part2.md) for Examples 5–6.

---

## Example 1: Checked Exception Declaration

A method that performs I/O must declare `throws IOException` because the caller cannot perform the operation without risking an IO failure. The compiler enforces this contract.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckedDeclaration {

    static String readFirstLine(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    public static void main(String[] args) {
        try {
            String line = readFirstLine("config.txt");
            System.out.println("First line: " + line);
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Failed to read file: config.txt (No such file or directory)
```

**Explanation:** `readFirstLine` declares `throws IOException` because `FileReader` and `readLine` both throw checked `IOException`. The caller must either catch it or propagate it further. Without the `throws` declaration, the code will not compile.

---

## Example 2: Multiple Exceptions in throws

A method may encounter different failure modes. Each checked exception type should be listed separately in the `throws` clause so callers can handle them independently.

```java
import java.io.IOException;
import java.sql.SQLException;

public class MultipleExceptions {

    static class OrderException extends Exception {
        OrderException(String msg) { super(msg); }
    }

    static class PaymentException extends Exception {
        PaymentException(String msg) { super(msg); }
    }

    static void processOrder(String orderId, String paymentMethod)
            throws OrderException, PaymentException {
        if (orderId == null || orderId.isEmpty()) {
            throw new OrderException("Order ID is required");
        }
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new PaymentException("Payment method is required");
        }
        System.out.println("Order " + orderId + " processed via " + paymentMethod);
    }

    public static void main(String[] args) {
        try {
            processOrder("", "credit-card");
        } catch (OrderException e) {
            System.out.println("Order error: " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("Payment error: " + e.getMessage());
        }

        try {
            processOrder("ORD-1", "");
        } catch (OrderException e) {
            System.out.println("Order error: " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("Payment error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Order error: Order ID is required
Payment error: Payment method is required
```

**Explanation:** Declaring two separate exceptions lets the caller catch each one independently. This is superior to declaring a single `throws Exception` because the caller can provide targeted recovery for each failure mode.

---

## Example 3: Exception Translation Pattern

Exception translation catches a low-level exception and rethrows it as a domain-specific exception. The original cause is preserved. This keeps each layer decoupled from implementation details of the layer below.

```java
import java.io.IOException;
import java.sql.SQLException;

public class ExceptionTranslation {

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class NotFoundException extends RuntimeException {
        NotFoundException(long id) {
            super("Not found: " + id);
        }
    }

    static String queryDatabase(long id) throws SQLException {
        throw new SQLException("Connection refused to host db-primary:5432");
    }

    static String getOrder(long orderId) {
        try {
            return queryDatabase(orderId);
        } catch (SQLException e) {
            throw new DataAccessException(
                "Failed to query order: " + orderId, e);
        }
    }

    public static void main(String[] args) {
        try {
            String result = getOrder(42);
            System.out.println("Result: " + result);
        } catch (DataAccessException e) {
            System.out.println("Service error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Service error: Failed to query order: 42
Root cause: Connection refused to host db-primary:5432
```

**Explanation:** The service layer catches `SQLException` (a low-level implementation detail) and wraps it in `DataAccessException` (a domain concept). The caller sees only domain exceptions and does not need to know about SQL, JDBC, or database drivers. The original `SQLException` is preserved as the cause for debugging.

---

## Example 4: Builder Pattern with throws

A builder can validate its state in the `build()` method. If validation fails, the builder throws an exception. The `throws` declaration on `build()` tells callers what can go wrong during construction.

```java
public class BuilderWithThrows {

    static class InvalidConfigException extends Exception {
        InvalidConfigException(String msg) { super(msg); }
    }

    static class AppConfig {
        final String host;
        final int port;
        final String protocol;

        AppConfig(String host, int port, String protocol) {
            this.host = host;
            this.port = port;
            this.protocol = protocol;
        }

        @Override
        public String toString() {
            return protocol + "://" + host + ":" + port;
        }
    }

    static class AppConfigBuilder {
        private String host;
        private Integer port;
        private String protocol = "https";

        AppConfigBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        AppConfigBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        AppConfigBuilder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        AppConfig build() throws InvalidConfigException {
            if (host == null || host.isEmpty()) {
                throw new InvalidConfigException("Host is required");
            }
            if (port == null || port < 1 || port > 65535) {
                throw new InvalidConfigException(
                    "Port must be between 1 and 65535, got: " + port);
            }
            if (!"http".equals(protocol) && !"https".equals(protocol)) {
                throw new InvalidConfigException(
                    "Protocol must be http or https, got: " + protocol);
            }
            return new AppConfig(host, port, protocol);
        }
    }

    public static void main(String[] args) {
        // Valid configuration
        try {
            AppConfig config = new AppConfigBuilder()
                .setHost("api.example.com")
                .setPort(443)
                .build();
            System.out.println("Created: " + config);
        } catch (InvalidConfigException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Missing host
        try {
            AppConfig config = new AppConfigBuilder()
                .setPort(8080)
                .build();
            System.out.println("Created: " + config);
        } catch (InvalidConfigException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Invalid port
        try {
            AppConfig config = new AppConfigBuilder()
                .setHost("localhost")
                .setPort(-1)
                .build();
            System.out.println("Created: " + config);
        } catch (InvalidConfigException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Created: https://api.example.com:443
Error: Host is required
Error: Port must be between 1 and 65535, got: -1
```

**Explanation:** The `build()` method declares `throws InvalidConfigException` so callers know they must handle configuration errors. The builder accumulates state and validates everything at construction time, producing a valid object or a clear error. The `throws` declaration makes the failure modes part of the API contract.

---

*See also: [Decision Guide](../decision.md) | [Part 2: Examples 5–6](README-Part2.md) | [Solutions](../02-solutions/README.md)*
