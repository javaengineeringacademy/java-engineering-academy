# 06 - The throws Declaration

## Scope

This topic covers Java's `throws` keyword in method declarations — the mechanism for declaring that a method may throw certain exceptions. You will learn what `throws` is, its syntax, the distinction between checked and unchecked exceptions in `throws`, exception translation patterns, and production best practices.

## Why It Exists

Java's checked exception system requires methods to declare the exceptions they might throw. This declaration serves as a contract between the method and its callers. Without `throws`, a caller has no way to know which exceptions to prepare for without reading the implementation.

```java
// Without throws — caller has no idea what can go wrong
public String readFile(String path) {
    // ... throws IOException? FileNotFoundException? SecurityException?
}

// With throws — explicit contract
public String readFile(String path) throws IOException {
    // caller knows: handle IOException or declare it
}
```

## What Is throws

`throws` is a keyword in the method declaration that specifies which exception types the method might propagate to its caller. It is part of the method signature, not a statement.

### Basic Syntax

```
accessModifier returnType methodName(params) throws ExceptionType1, ExceptionType2 {
    // body
}
```

```java
// Single checked exception
public void open(String path) throws FileNotFoundException { }

// Multiple checked exceptions
public void transfer(Account a, Account b) throws InsufficientFundsException, IOException { }

// No throws clause (only unchecked exceptions possible)
public void validate(String input) { }
```

## When to Use throws

| Exception Type | Must declare `throws`? | Should declare `throws`? |
|----------------|----------------------|------------------------|
| Checked (IOException, etc.) | Yes — compiler enforced | Yes |
| Unchecked (RuntimeException) | No | Sometimes, for documentation |
| Error (OutOfMemoryError) | No | No — JVM error, not recoverable |

## Checked vs Unchecked in throws

### Checked Exceptions — Required

```java
public String readFirstLine(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    try {
        return reader.readLine();
    } finally {
        reader.close();
    }
}
```

### Unchecked Exceptions — Optional

```java
public void configure(String settings) throws IllegalArgumentException {
    if (settings == null) {
        throw new IllegalArgumentException("Settings cannot be null");
    }
}
```

### Caller Experience Difference

```java
// Checked — compiler forces handling
try {
    fileProcessor.readFirstLine("data.txt");
} catch (IOException e) {
    // MUST handle
}

// Unchecked — compiler does not force handling
fileProcessor.processData("data.txt"); // compiles without try-catch
```

## throws with Generic Types

Generic methods can declare throws, but exception types cannot be parameterized.

```java
public <T> T deserialize(String json, Class<T> type) throws JsonSyntaxException {
    return gson.fromJson(json, type);
}
```

Due to type erasure, you cannot catch a generic exception type.

## Exception Translation

Exception translation catches a low-level exception and rethrows it as a higher-level exception appropriate for the current layer.

```
┌─────────────┐     throws IOException      ┌─────────────┐
│  Low Level  │ ──────────────────────────► │  Mid Level  │
│  (IO layer) │                             │  (Service)  │
└─────────────┘                             └──────┬──────┘
                                                   │
                                            throws ServiceException
                                                   │
                                                   ▼
                                             ┌─────────────┐
                                             │  High Level │
                                             │  (Handler)  │
                                             └─────────────┘
```

```java
// Low-level — throws IOException
public String sendRequest(String url) throws IOException {
    // network call
}

// Mid-level — translates to domain exception
public OrderStatus getStatus(String orderId) {
    try {
        return sendRequest("/orders/" + orderId);
    } catch (IOException e) {
        throw new OrderException("Failed to get status", e);
    }
}
```

| Benefit | Explanation |
|---------|-------------|
| **Abstraction** | Caller does not need to know about IO details |
| **Cohesion** | Each layer handles its own exception types |
| **Stability** | Low-level changes do not propagate to high-level callers |
| **Testability** | Testing service layer does not require mocking IO |

## Common Patterns

### Declare and Propagate
```java
public void saveUser(User user) throws ValidationException, DatabaseException {
    validate(user);
    database.insert(user);
}
```

### Catch, Wrap, and Rethrow
```java
public UserDTO getUser(long id) {
    try {
        User user = repository.findById(id);
        return toDTO(user);
    } catch (SQLException e) {
        throw new ServiceException("User not found: " + id, e);
    }
}
```

### Catch and Handle
```java
public void sendNotification(String message) {
    try {
        emailService.send(message);
    } catch (MailException e) {
        log.warn("Email failed, using SMS fallback", e);
        smsService.send(message);
    }
}
```

### Exception Hierarchy in throws
```java
// Declare the base type — covers all subtypes
public void process() throws IOException { }
```

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `throws` keyword introduced with checked exceptions |
| JDK 5 | Varargs affected throws with generic arrays |
| JDK 7 | Multi-catch improved throws handling |
| JDK 7 | Precise rethrow refined throws semantics |

## Common Mistakes

### Declaring Only Unchecked Exceptions
```java
// UNNECESSARY
public void validate(String input) throws IllegalArgumentException { }

// BETTER — just throw it
public void validate(String input) {
    if (input == null) throw new IllegalArgumentException("Input is null");
}
```

### Throwing Too Broadly
```java
// BAD — caller has no idea what can go wrong
public void process(String input) throws Exception { }

// BETTER — specific exceptions
public void process(String input) throws IOException, DataException { }
```

### Swallowing Checked Exceptions
```java
// BAD — loses checked status
public User getUser(long id) {
    try {
        return repository.findById(id);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

// BETTER — domain-specific unchecked exception
public User getUser(long id) {
    try {
        return repository.findById(id);
    } catch (SQLException e) {
        throw new DataAccessException("Failed to get user", e);
    }
}
```

### Ignoring InterruptedException
```java
// BAD
try { Thread.sleep(1000); } catch (InterruptedException e) { }

// BETTER — restore interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new ServiceException("Interrupted", e);
}
```

## Best Practices

1. **Declare only checked exceptions** — Do not declare unchecked exceptions unless documenting custom ones.
2. **Use the most specific type** — Prefer `FileNotFoundException` over `IOException`.
3. **Translate at layer boundaries** — Catch low-level, rethrow as domain exceptions.
4. **Do not declare `throws Exception`** — It defeats the purpose of checked exceptions.
5. **Document with `@throws`** — Every declared checked exception should be Javadoc'd.
6. **Consider interface stability** — Do not add checked exceptions to published interfaces.

## Production Patterns

### Layered Architecture
```java
// Controller — handles all exceptions
@RestController
public class OrderController {
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (DataAccessException e) {
            return ResponseEntity.status(500).body("Database error");
        }
    }
}

// Service — translates exceptions
@Service
public class OrderService {
    public OrderDTO getOrder(long id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            throw new DataAccessException("Query failed", e);
        }
    }
}
```

### The Unrecoverable Exception Pattern
```java
public Configuration(String path) {
    try {
        props.load(new FileInputStream(path));
    } catch (FileNotFoundException e) {
        throw new IllegalStateException("Config not found: " + path, e);
    } catch (IOException e) {
        throw new IllegalStateException("Invalid config: " + path, e);
    }
}
```

### The Fallback Pattern
```java
public String getData(String key) {
    try {
        return primarySource.get(key);
    } catch (IOException e) {
        log.warn("Primary source failed, using fallback", e);
        try {
            return fallbackSource.get(key);
        } catch (IOException fallbackFailed) {
            throw new ServiceException("Both sources failed", e);
        }
    }
}
```

## Summary

| Concept | Key Point |
|---------|-----------|
| throws Declaration | Declares which exceptions a method may propagate |
| Checked Exceptions | Must be declared — compiler enforced |
| Unchecked Exceptions | Optional to declare — rarely useful |
| Exception Translation | Catch low-level, rethrow as domain exceptions |
| Common Mistakes | Throwing Exception, swallowing exceptions, declaring unchecked |
| Best Practices | Declare specific types, translate at boundaries, document with Javadoc |
| Interface Stability | Do not add checked exceptions to published interfaces |

## Key Takeaways

- `throws` is a declaration, not a statement — part of the method signature
- Checked exceptions must be declared; unchecked exceptions should not be
- Exception translation at layer boundaries keeps each layer decoupled
- Never declare `throws Exception` or `throws Throwable` in production code
- Removing exceptions from `throws` is binary compatible; adding them is not
- Always document checked exceptions with `@throws` in Javadoc

---

**Previous:** [05 - The throw Keyword](../05-throw/README.md) | **Next:** [07 - Try-with-Resources](../07-try-with-resources/README.md)
