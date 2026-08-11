# Examples: The throws Declaration

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

## Example 5: Method Chain Exception Handling

When methods are chained, each method in the chain must either handle exceptions or declare them. The `throws` clause propagates up the call stack until caught.

```java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MethodChain {

    static class ProcessingException extends Exception {
        ProcessingException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    static String readSource(String path) throws IOException {
        if (path == null) {
            throw new IOException("Path cannot be null");
        }
        if (!path.endsWith(".txt")) {
            throw new IOException("Only .txt files supported: " + path);
        }
        return "content-from-" + path;
    }

    static List<String> parseLines(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be empty");
        }
        List<String> lines = new ArrayList<>();
        for (String s : data.split("-")) {
            lines.add(s);
        }
        return lines;
    }

    static String transform(String line) {
        return line.toUpperCase();
    }

    static String processFile(String path) throws ProcessingException {
        try {
            String data = readSource(path);
            List<String> lines = parseLines(data);
            StringBuilder result = new StringBuilder();
            for (String line : lines) {
                result.append(transform(line)).append(" ");
            }
            return result.toString().trim();
        } catch (IOException e) {
            throw new ProcessingException(
                "Failed to process file: " + path, e);
        }
    }

    public static void main(String[] args) {
        // Happy path
        try {
            String result = processFile("data.txt");
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // IOException path
        try {
            String result = processFile("data.csv");
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        // Null path
        try {
            String result = processFile(null);
            System.out.println("Result: " + result);
        } catch (ProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Result: CONTENT FROM DATA TXT
Error: Failed to process file: data.csv
Cause: Only .txt files supported: data.csv
Error: Failed to process file: null
Cause: Path cannot be null
```

**Explanation:** `processFile` handles `IOException` from `readSource` by translating it to `ProcessingException`. The unchecked `IllegalArgumentException` from `parseLines` propagates naturally without being declared. This demonstrates the standard pattern: checked exceptions are caught and translated at the layer boundary, while unchecked exceptions pass through.

---

## Example 6: throws with Interface and Implementation

An interface declares `throws` to establish a contract. Implementations must honor that contract — they can declare fewer exceptions but not more.

```java
import java.io.IOException;

public class InterfaceThrows {

    interface DataSource {
        String read() throws IOException;
        void write(String data) throws IOException;
    }

    static class FileDataSource implements DataSource {
        private final String basePath;

        FileDataSource(String basePath) {
            this.basePath = basePath;
        }

        @Override
        public String read() throws IOException {
            throw new IOException("Simulated read failure from " + basePath);
        }

        @Override
        public void write(String data) throws IOException {
            throw new IOException("Simulated write failure to " + basePath);
        }
    }

    static class StaticDataSource implements DataSource {
        private final String data;

        StaticDataSource(String data) {
            this.data = data;
        }

        @Override
        public String read() {
            return data;
        }

        @Override
        public void write(String data) {
            // no-op for static source
        }
    }

    static void processSource(DataSource source) {
        try {
            String content = source.read();
            System.out.println("Read: " + content);
            source.write("processed-" + content);
            System.out.println("Write complete");
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("--- FileDataSource ---");
        processSource(new FileDataSource("/tmp/data"));

        System.out.println("--- StaticDataSource ---");
        processSource(new StaticDataSource("hardcoded-value"));
    }
}
```

**Output:**
```
--- FileDataSource ---
IO error: Simulated read failure from /tmp/data
--- StaticDataSource ---
Read: hardcoded-value
Write complete
```

**Explanation:** The `DataSource` interface declares `throws IOException`. `FileDataSource` honors the contract by declaring it. `StaticDataSource` does not need to declare it because its methods never throw `IOException` — this is allowed. The consumer `processSource` handles the checked exception, demonstrating how interface contracts propagate through implementations.
