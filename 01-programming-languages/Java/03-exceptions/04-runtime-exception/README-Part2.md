# 04 - Runtime Exception (Part 2)
**Previous:** [Part 1](README.md)

## Production Patterns

### Custom RuntimeException Subtypes

Define domain-specific unchecked exceptions for your application:

```java
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
```

Custom subtypes make it easy to distinguish between different kinds of programming errors in logs and stack traces.

### Precondition Validation

Use `IllegalArgumentException` to enforce method contracts:

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Age must be between 0 and 150, got: " + age);
    }
    this.age = age;
}
```

This pattern makes the method contract explicit and fails fast when it is violated.

### State Validation

Use `IllegalStateException` when an object is in the wrong state:

```java
public void close() {
    if (closed) {
        throw new IllegalStateException("Already closed");
    }
    // close resources
    closed = true;
}
```

This prevents operations that would produce incorrect results or corrupt state.

### Global Exception Handler

For unhandled `RuntimeException`, register a default handler:

```java
Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
    log.error("Uncaught exception in thread " + thread.getName(), throwable);
});
```

This provides a safety net for exceptions that escape the normal error handling flow.

### Defensive Programming with Checks

Use explicit checks instead of relying on exceptions:

```java
// Instead of catching NullPointerException:
if (map.containsKey(key)) {
    Object value = map.get(key);
    process(value);
}

// Instead of catching IndexOutOfBoundsException:
if (index >= 0 && index < list.size()) {
    Object item = list.get(index);
    process(item);
}
```

### Exception Translation

Wrap low-level unchecked exceptions in domain-specific exceptions:

```java
public User findUser(String id) {
    try {
        return database.query(id);
    } catch (RuntimeException e) {
        throw new UserNotFoundException("User not found: " + id, e);
    }
}
```

This preserves the original cause while providing a more meaningful exception type.

## Common Subtypes in Detail

### NullPointerException

Thrown when an application attempts to use `null` in a case where an object is required. This is the most common `RuntimeException` in Java.

```java
String name = null;
int length = name.length(); // throws NullPointerException
```

Modern Java provides `Objects.requireNonNull()` to check for null and throw `NullPointerException` with a clear message:

```java
public void process(String value) {
    Objects.requireNonNull(value, "value must not be null");
    // ...
}
```

### IllegalArgumentException

Thrown to indicate that a method has been passed an illegal or inappropriate argument. This is the standard exception for enforcing method parameter contracts.

```java
public void setPort(int port) {
    if (port < 1 || port > 65535) {
        throw new IllegalArgumentException("Port must be between 1 and 65535, got: " + port);
    }
    this.port = port;
}
```

### IllegalStateException

Thrown to indicate that a method has been invoked at an illegal or inappropriate time. The object is not in the appropriate state for the requested operation.

```java
public void start() {
    if (running) {
        throw new IllegalStateException("Already running");
    }
    running = true;
}
```

### IndexOutOfBoundsException

Thrown to indicate that an index of some sort is out of range. Subclasses include `ArrayIndexOutOfBoundsException` and `StringIndexOutOfBoundsException`.

```java
List<String> items = List.of("a", "b", "c");
String item = items.get(5); // throws IndexOutOfBoundsException
```

### ClassCastException

Thrown to indicate that the code has attempted to cast an object to a subclass of which it is not an instance.

```java
Object obj = "Hello";
Integer num = (Integer) obj; // throws ClassCastException
```

Modern Java provides `instanceof` checks and pattern matching to avoid this:

```java
if (obj instanceof String s) {
    // use s directly
}
```

### ArithmeticException

Thrown when an illegal arithmetic operation has occurred. The most common case is integer division by zero.

```java
int result = 10 / 0; // throws ArithmeticException
```

### ConcurrentModificationException

Thrown when a method detects that an object has been modified concurrently with a method that is not synchronized. This commonly happens when modifying a collection during iteration.

```java
List<String> items = new ArrayList<>(List.of("a", "b", "c"));
for (String item : items) {
    if ("b".equals(item)) {
        items.remove(item); // throws ConcurrentModificationException
    }
}
```

### UnsupportedOperationException

Thrown to indicate that the requested operation is not supported. This is common in unmodifiable collections or incomplete implementations.

```java
List<String> list = List.of("a", "b", "c");
list.add("d"); // throws UnsupportedOperationException
```

### NumberFormatException

Thrown to indicate that the application has attempted to convert a string to one of the numeric types, but that the string does not have the appropriate format.

```java
int value = Integer.parseInt("abc"); // throws NumberFormatException
```

## Summary

- `RuntimeException` is unchecked — the compiler does not force you to catch it.
- It represents programming bugs, not recoverable conditions.
- Common subtypes: `NullPointerException`, `IllegalArgumentException`, `IllegalStateException`, `IndexOutOfBoundsException`, `ClassCastException`, `ArithmeticException`.
- Use specific subtypes with descriptive messages.
- Do not use `RuntimeException` for control flow.
- Let bugs propagate to a global handler rather than catching them broadly.
- Create custom subtypes for domain-specific programming errors.
- Use explicit precondition and state checks instead of catching exceptions.
