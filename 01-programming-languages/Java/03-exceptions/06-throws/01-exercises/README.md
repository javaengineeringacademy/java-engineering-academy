# Exercises: The throws Declaration

Work through these exercises in order. Each builds on the previous one.

---

## Exercise 1: Add throws Declaration to a Method

A method reads a file but does not declare the checked exception it throws. Fix the method signature.

**Problem:** The following code does not compile. Add the correct `throws` declaration.

```java
import java.io.BufferedReader;
import java.io.FileReader;

public class Exercise1 {

    static String readFirstLine(String path) {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String line = readFirstLine("data.txt");
        System.out.println("Line: " + line);
    }
}
```

**Requirements:**
1. Add the appropriate `throws` clause to `readFirstLine`
2. The method must compile without wrapping the exception in a generic catch
3. Remove the existing `catch (Exception e)` block — let the exception propagate

**Hints:**
- `FileReader` constructor throws a checked exception
- `BufferedReader.readLine()` throws a checked exception
- Both are subtypes of the same exception class
- The caller in `main` must handle or propagate the exception

---

## Exercise 2: Exception Translation

Translate a low-level exception to a domain exception while preserving the cause.

**Problem:** Write a `UserService` class with a method `findUser(long id)` that calls a repository method throwing `SQLException`. Catch the `SQLException` and wrap it in a custom `ServiceException` (unchecked).

```java
import java.sql.SQLException;

public class Exercise2 {

    static class ServiceException extends RuntimeException {
        // TODO: Add constructor that accepts message and cause
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
            // TODO: Call repository.findById(id)
            // TODO: Catch SQLException and throw ServiceException
            // TODO: Preserve the original cause
            return null;
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

**Expected output:**
```
User{id=1, name='User-1'}
User{id=2, name='User-2'}
Service error: Failed to find user: -1
Root cause: Invalid ID: -1
```

**Hints:**
- `ServiceException` needs a constructor `ServiceException(String message, Throwable cause)`
- Use `throw new ServiceException("Failed to find user: " + id, e)` to preserve the cause
- Do not declare `throws` on `findUser` — it throws an unchecked exception

---

## Exercise 3: Design a Method Chain with throws

Build a processing pipeline where each stage handles its own exceptions.

**Problem:** Implement three methods that form a processing chain. The first method reads input (may throw `IOException`), the second validates (may throw `IllegalArgumentException`), and the third produces output (may throw `IOException`). Use exception translation to present a single exception type to the caller.

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

    // TODO: Implement this method
    // It should:
    // 1. Call readInput(source)
    // 2. Pass result to validate()
    // 3. Pass result to writeOutput(destination)
    // 4. Catch IOException and translate to PipelineException
    // 5. Let IllegalArgumentException propagate naturally
    static String process(String source, String destination) throws PipelineException {
        return null; // TODO
    }

    public static void main(String[] args) {
        // Happy path
        try {
            String result = process("input.txt", "output.txt");
            System.out.println("Success: " + result);
        } catch (PipelineException e) {
            System.out.println("Pipeline error: " + e.getMessage());
        }

        // Bad source
        try {
            String result = process("", "output.txt");
            System.out.println("Success: " + result);
        } catch (PipelineException e) {
            System.out.println("Pipeline error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        // Bad destination
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

**Expected output:**
```
Success: written:input.txt->output.txt
Pipeline error: Failed to process pipeline
Cause: Source cannot be empty
Pipeline error: Failed to process pipeline
Cause: Destination cannot be null
```

**Hints:**
- `process` should declare `throws PipelineException`
- Catch `IOException` from `readInput` and `writeOutput`, wrap in `PipelineException`
- Let `IllegalArgumentException` from `validate` propagate without catching
- Preserve the original cause when wrapping

---

## Exercise 4: Create a Service Layer with throws

Design a service layer with proper `throws` declarations across multiple methods.

**Problem:** Implement a `ConfigService` with three methods. Each method has different failure modes that must be declared.

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

    // TODO: Implement these methods with proper throws declarations

    // Method 1: Read a config file
    // - Throws ConfigNotFoundException if the file path is null
    // - Throws ConfigParseException if the file content is invalid
    // - Returns the parsed config as a String
    static String loadConfig(String path) throws ConfigException {
        return null; // TODO
    }

    // Method 2: Validate config
    // - Throws ConfigException if config is null
    // - Returns true if valid, false otherwise
    // - This method does NOT throw checked exceptions
    static boolean validateConfig(String config) {
        return false; // TODO
    }

    // Method 3: Load and validate
    // - Calls loadConfig then validateConfig
    // - If loadConfig throws, propagate it
    // - If validateConfig returns false, throw ConfigException
    // - Returns the config if both succeed
    static String loadAndValidate(String path) throws ConfigException {
        return null; // TODO
    }

    public static void main(String[] args) {
        // Test loadConfig with null
        try {
            loadConfig(null);
        } catch (ConfigNotFoundException e) {
            System.out.println("Not found: " + e.getMessage());
        } catch (ConfigException e) {
            System.out.println("Config error: " + e.getMessage());
        }

        // Test loadConfig with valid path
        try {
            String config = loadConfig("app.properties");
            System.out.println("Loaded: " + config);
        } catch (ConfigException e) {
            System.out.println("Config error: " + e.getMessage());
        }

        // Test loadAndValidate
        try {
            String config = loadAndValidate("app.properties");
            System.out.println("Valid config: " + config);
        } catch (ConfigException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}
```

**Hints:**
- `loadConfig` should declare `throws ConfigException` (the base type covers both subtypes)
- `validateConfig` should not declare any `throws` clause
- `loadAndValidate` should declare `throws ConfigException`
- Use `throw new ConfigNotFoundException(path)` for null path
- Use `throw new ConfigParseException(path, cause)` for parse failures

---

## Exercise 5: Handle throws Across Layers

Implement a three-layer architecture where each layer handles exceptions according to its responsibility.

**Problem:** Implement Controller, Service, and Repository layers. The Repository throws `IOException`. The Service translates it. The Controller handles it.

```java
import java.io.IOException;

public class Exercise5 {

    static class ServiceException extends RuntimeException {
        ServiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    // Repository layer — throws checked exception
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

    // Service layer — translates to domain exception
    static class DataService {
        private final DataRepository repository = new DataRepository();

        // TODO: Implement this method
        // - Call repository.fetchData(key)
        // - Catch IOException
        // - Throw ServiceException with the original cause
        String getData(String key) {
            return null; // TODO
        }
    }

    // Controller layer — handles domain exception
    static class DataController {
        private final DataService service = new DataService();

        // TODO: Implement this method
        // - Call service.getData(key)
        // - Catch ServiceException
        // - Return error message string
        // - On success, return the data
        String handleRequest(String key) {
            return null; // TODO
        }
    }

    public static void main(String[] args) {
        DataController controller = new DataController();

        // Happy path
        System.out.println(controller.handleRequest("user-1"));

        // Null key
        System.out.println(controller.handleRequest(null));

        // Error key
        System.out.println(controller.handleRequest("error"));

        // Unknown key
        System.out.println(controller.handleRequest("unknown"));
    }
}
```

**Expected output:**
```
data-value-for-user-1
Error: Failed to fetch data
Error: Failed to fetch data
data-value-for-unknown
```

**Hints:**
- `DataService.getData` catches `IOException` and throws `ServiceException`
- `DataController.handleRequest` catches `ServiceException` and returns an error string
- `ServiceException` must have a constructor accepting `(String, Throwable)`
- The Repository does not catch exceptions — it lets them propagate
