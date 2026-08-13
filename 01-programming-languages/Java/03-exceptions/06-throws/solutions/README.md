# Solutions: The throws Declaration

---

## Solution 1: Add throws Declaration

The method uses `FileReader` and `BufferedReader.readLine()`, both of which throw `IOException`. The method must declare `throws IOException`, and the caller must handle it.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exercise1 {

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
            String line = readFirstLine("data.txt");
            System.out.println("Line: " + line);
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
}
```

**Key points:**
- `throws IOException` is added to the method signature
- The generic `catch (Exception e)` is removed
- The caller in `main` catches `IOException` explicitly
- `finally` ensures the reader is closed

---

## Solution 2: Exception Translation

The `ServiceException` wraps `SQLException` while preserving the cause. The service method catches the low-level exception and rethrows the domain exception.

```java
import java.sql.SQLException;

public class Exercise2 {

    static class ServiceException extends RuntimeException {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class UserRepository {
        User findById(long id) throws SQLException {
            if (id <= 0) {
                throw new SQLException("Invalid ID: " + id);
            }
            return new User(id, "User-" + id);
        }
    }

    static class User {
        final long id;
        final String name;
        User(long id, String name) { this.id = id; this.name = name; }
        @Override
        public String toString() { return "User{id=" + id + ", name='" + name + "'}"; }
    }

    static class UserService {
        private final UserRepository repository = new UserRepository();

        User findUser(long id) {
            try {
                return repository.findById(id);
            } catch (SQLException e) {
                throw new ServiceException("Failed to find user: " + id, e);
            }
        }
    }

    public static void main(String[] args) {
        UserService service = new UserService();

        System.out.println(service.findUser(1));
        System.out.println(service.findUser(2));

        try {
            service.findUser(-1);
        } catch (ServiceException e) {
            System.out.println("Service error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
    }
}
```

**Key points:**
- `ServiceException` extends `RuntimeException` — no `throws` needed
- The constructor accepts `(String, Throwable)` to preserve the cause
- `findUser` catches `SQLException` and translates it
- The caller sees only `ServiceException`, never `SQLException`

---

## Solution 3: Method Chain with throws

The processing chain catches `IOException` at the boundary and translates it to `PipelineException`. `IllegalArgumentException` from validation passes through naturally.

```java
import java.io.IOException;

public class Exercise3 {

    static class PipelineException extends Exception {
        PipelineException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    static String readInput(String source) throws IOException {
        if (source == null || source.isEmpty()) {
            throw new IOException("Source cannot be empty");
        }
        return "raw-data-from-" + source;
    }

    static String validate(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null");
        }
        if (!data.startsWith("raw-data")) {
            throw new IllegalArgumentException("Invalid data format: " + data);
        }
        return data.replace("raw-data-", "");
    }

    static String writeOutput(String data, String destination) throws IOException {
        if (destination == null) {
            throw new IOException("Destination cannot be null");
        }
        return "written:" + data + "->" + destination;
    }

    static String process(String source, String destination) throws PipelineException {
        try {
            String data = readInput(source);
            String validated = validate(data);
            return writeOutput(validated, destination);
        } catch (IOException e) {
            throw new PipelineException("Failed to process pipeline", e);
        }
    }

    public static void main(String[] args) {
        try {
            String result = process("input.txt", "output.txt");
            System.out.println("Success: " + result);
        } catch (PipelineException e) {
            System.out.println("Pipeline error: " + e.getMessage());
        }

        try {
            String result = process("", "output.txt");
            System.out.println("Success: " + result);
        } catch (PipelineException e) {
            System.out.println("Pipeline error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        try {
            String result = process("input.txt", null);
            System.out.println("Success: " + result);
        } catch (PipelineException e) {
            System.out.println("Pipeline error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Key points:**
- Single `catch (IOException e)` handles both `readInput` and `writeOutput`
- `IllegalArgumentException` is not caught — it propagates as-is
- `process` declares `throws PipelineException` to signal translated failures
- The cause chain is preserved for debugging

---

## Solution 4: Service Layer with throws

The `ConfigService` uses a checked exception hierarchy. `loadConfig` declares the base type `ConfigException`, covering both subtypes. The caller can catch the base type or specific subtypes.

```java
import java.io.IOException;

public class Exercise4 {

    static class ConfigException extends Exception {
        ConfigException(String msg) { super(msg); }
        ConfigException(String msg, Throwable cause) { super(msg, cause); }
    }

    static class ConfigNotFoundException extends ConfigException {
        ConfigNotFoundException(String path) {
            super("Configuration not found: " + path);
        }
    }

    static class ConfigParseException extends ConfigException {
        ConfigParseException(String path, Throwable cause) {
            super("Failed to parse configuration: " + path, cause);
        }
    }

    static String loadConfig(String path) throws ConfigException {
        if (path == null) {
            throw new ConfigNotFoundException(path);
        }
        if (path.endsWith(".invalid")) {
            throw new ConfigParseException(path,
                new IOException("Malformed config file"));
        }
        return "server.port=8080;server.host=localhost";
    }

    static boolean validateConfig(String config) {
        if (config == null) {
            throw new IllegalArgumentException("Config is null");
        }
        return config.contains("server.port") && config.contains("server.host");
    }

    static String loadAndValidate(String path) throws ConfigException {
        String config = loadConfig(path);
        if (!validateConfig(config)) {
            throw new ConfigException("Invalid configuration: missing required keys");
        }
        return config;
    }

    public static void main(String[] args) {
        try {
            loadConfig(null);
        } catch (ConfigNotFoundException e) {
            System.out.println("Not found: " + e.getMessage());
        } catch (ConfigException e) {
            System.out.println("Config error: " + e.getMessage());
        }

        try {
            String config = loadConfig("app.properties");
            System.out.println("Loaded: " + config);
        } catch (ConfigException e) {
            System.out.println("Config error: " + e.getMessage());
        }

        try {
            String config = loadAndValidate("app.properties");
            System.out.println("Valid config: " + config);
        } catch (ConfigException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}
```

**Key points:**
- `ConfigNotFoundException` and `ConfigParseException` extend `ConfigException`
- `loadConfig` declares `throws ConfigException` (the base type)
- Callers can catch `ConfigException` for all failures or specific subtypes for targeted handling
- `validateConfig` does not declare `throws` — it uses unchecked exceptions for programming errors

---

## Solution 5: Handle throws Across Layers

Each layer has a clear responsibility: Repository throws checked exceptions, Service translates them, Controller handles them.

```java
import java.io.IOException;

public class Exercise5 {

    static class ServiceException extends RuntimeException {
        ServiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    static class DataRepository {
        String fetchData(String key) throws IOException {
            if (key == null) {
                throw new IOException("Key cannot be null");
            }
            if ("error".equals(key)) {
                throw new IOException("Connection timeout to database");
            }
            return "data-value-for-" + key;
        }
    }

    static class DataService {
        private final DataRepository repository = new DataRepository();

        String getData(String key) {
            try {
                return repository.fetchData(key);
            } catch (IOException e) {
                throw new ServiceException("Failed to fetch data", e);
            }
        }
    }

    static class DataController {
        private final DataService service = new DataService();

        String handleRequest(String key) {
            try {
                return service.getData(key);
            } catch (ServiceException e) {
                return "Error: " + e.getMessage();
            }
        }
    }

    public static void main(String[] args) {
        DataController controller = new DataController();

        System.out.println(controller.handleRequest("user-1"));
        System.out.println(controller.handleRequest(null));
        System.out.println(controller.handleRequest("error"));
        System.out.println(controller.handleRequest("unknown"));
    }
}
```

**Key points:**
- **Repository**: Declares `throws IOException` — does not catch it
- **Service**: Catches `IOException`, translates to `ServiceException` (unchecked)
- **Controller**: Catches `ServiceException`, returns user-friendly error string
- Exception propagation stops at the Controller — the caller never sees `IOException` or `ServiceException`
- Each layer is decoupled from the layers below it

---

## Comparison: Layer Responsibilities

| Layer | Exception Role | Declares throws? | Catches? |
|---|---|---|---|
| Repository | Produces checked exceptions | Yes (`IOException`) | No |
| Service | Translates to domain exceptions | No (unchecked) | Yes (`IOException`) |
| Controller | Handles domain exceptions | No | Yes (`ServiceException`) |

---

## Key Patterns Across All Solutions

1. **Checked exceptions are declared** — the compiler enforces the contract
2. **Unchecked exceptions are not declared** — they pass through naturally
3. **Exception translation preserves the cause** — always use the `(message, cause)` constructor
4. **Layer boundaries are where translation happens** — not inside utility methods
5. **Specific types are preferred over generic** — `throws IOException` over `throws Exception`
