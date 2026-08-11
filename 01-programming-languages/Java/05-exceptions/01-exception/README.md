# Exception

## Scope

This topic covers `java.lang.Exception` — the base class for all checked exceptions in Java. It explains the Exception Contract, checked vs unchecked distinction, when and when not to use generic exceptions, and production patterns for exception handling.

## Why It Exists

Java's type system needed a way to represent recoverable error conditions that callers must explicitly acknowledge. `Exception` was designed as that mechanism — a checked type that forces the compiler to verify exception handling at compile time.

## Design Rationale

Bjarne Stroustrup's C++ approach (unchecked exceptions only) led to libraries where errors were silently ignored. Java's designers — James Gosling, Bill Joy, and others — chose checked exceptions to make error handling part of the API contract. A method that can fail declares it in its signature, and callers must handle or propagate.

This was controversial from the start. Checked exceptions improve correctness but increase boilerplate. The community has since settled on a convention: checked for recoverable conditions, unchecked for programming errors.

## What Is Exception

`java.lang.Exception` is a class in the JDK (since 1.0) that extends `java.lang.Throwable`. It is the superclass for all checked exceptions in Java.

```
Throwable
├── Error (unchecked)
└── Exception
    ├── RuntimeException (unchecked)
    │   └── NullPointerException, IllegalArgumentException, ...
    └── IOException, SQLException, ... (checked)
```

An Exception represents a condition that a reasonable application might want to catch.

## Characteristics

| Property | Value |
|---|---|
| Package | `java.lang` |
| Since | JDK 1.0 |
| Extends | `Throwable` |
| Type | Checked |
| Serializable | Yes |
| Stack trace support | Yes |
| Cause chaining | Yes (since 1.4) |

## Exception Contract

### Constructors

```java
public Exception()                         // no-arg
public Exception(String message)           // message
public Exception(String message, Throwable cause)  // message + cause
public Exception(Throwable cause)          // cause only
```

### Key Methods

| Method | Description |
|---|---|
| `getMessage()` | Returns the detail message string |
| `getLocalizedMessage()` | Returns localized version (override for localization) |
| `getCause()` | Returns the cause, or null if none |
| `initCause(Throwable)` | Sets the cause (can only be called once) |
| `fillInStackTrace()` | Fills the stack trace, returns this |
| `getStackTrace()` | Returns stack trace as array of StackTraceElement |
| `setStackTrace(StackTraceElement[])` | Replaces stack trace |
| `addSuppressed(Throwable)` | Adds suppressed exceptions (try-with-resources) |
| `getSuppressed()` | Returns suppressed exceptions |
| `printStackTrace()` | Prints to stderr |
| `toString()` | Returns `getClass().getName() + ": " + getMessage()` |

### fillInStackTrace()

Returns the throwable with a completed stack trace. Called automatically in constructors. Can be overridden in performance-critical code that doesn't need stack traces (see `ThreadDeath`).

```java
public synchronized Throwable fillInStackTrace() {
    return this;
}
```

### getLocalizedMessage()

Override this to provide locale-specific messages. The default implementation simply returns `getMessage()`.

## Checked vs Unchecked

**Checked exceptions** (subclasses of Exception that are not RuntimeException):
- Must be caught or declared in `throws` clause
- Represent recoverable conditions
- Part of the method's API contract
- Enforced at compile time

**Unchecked exceptions** (RuntimeException and Error subclasses):
- Do not require explicit handling
- Represent programming errors or JVM errors
- Can be caught but don't have to be

This is the most important distinction in Java exception handling.

## Exception as API Contract

When a method declares `throws Exception`, it is part of the public contract:

```java
public void readFile(String path) throws IOException {
    // ...
}
```

Callers must handle or propagate:

```java
try {
    readFile("/tmp/data.txt");
} catch (IOException e) {
    // handle
}
```

A method that throws `Exception` (the generic type) gives callers no information about what can go wrong. This violates the principle of least surprise.

## Common Types That Extend Exception Directly

| Exception | Purpose |
|---|---|
| `IOException` | I/O failures (file, network) |
| `SQLException` | Database access errors |
| `InterruptedException` | Thread interruption |
| `ReflectiveOperationException` | Reflection failures |
| `CloneNotSupportedException` | Clone on non-Cloneable |
| `InterruptedException` | Blocking operations interrupted |
| `AWTException` | AWT errors |

These are all checked. They represent conditions where recovery or retry is reasonable.

## When to Catch Exception (Almost Never)

Catching the generic `Exception` type is almost always wrong:

```java
// Bad
try {
    doSomething();
} catch (Exception e) {
    // What kind of exception is this?
}

// Good
try {
    doSomething();
} catch (IOException e) {
    // Specific, actionable handling
}
```

Catching `Exception`:
- Masks the actual error type
- May accidentally catch unchecked exceptions
- Makes debugging harder
- Violates the checked exception contract

**The only legitimate reason**: wrapping in a framework catch-all at the top of a call stack (e.g., servlet containers, thread pool task runners).

## Common Pitfalls

### 1. Catching Generic Exception

```java
// Bad: catches everything, handles nothing specific
try {
    service.process();
} catch (Exception e) {
    log.error("error", e);
}
```

### 2. Swallowing Exceptions

```java
// Bad: silently ignores the error
try {
    service.process();
} catch (Exception e) {
    // nothing
}
```

### 3. Catching Exception to Avoid Declaring Throws

```java
// Bad: hides the real problem
public void doWork() {
    try {
        riskyOperation();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

### 4. Overly Broad throws Declaration

```java
// Bad: gives caller no useful information
public void process() throws Exception { ... }

// Good: declares what actually happens
public void process() throws IOException, ValidationException { ... }
```

### 5. Catching Throwable

```java
// Almost always wrong: catches Error too
catch (Throwable t) { ... }
```

## Production Patterns

### 1. Catch Specific, Wrap Generic

```java
try {
    remoteService.call();
} catch (RemoteException e) {
    throw new ServiceException("Failed to call remote service", e);
}
```

### 2. Exception Translation

Layered architectures translate exceptions at boundaries:

```java
// DAO layer throws SQLException
// Service layer wraps in ServiceException
try {
    dao.findById(id);
} catch (SQLException e) {
    throw new ServiceException("Data access failed", e);
}
```

### 3. Fail-Fast Validation

```java
public void setUserEmail(String email) {
    if (email == null || email.isBlank()) {
        throw new IllegalArgumentException("Email must not be blank");
    }
    this.email = email;
}
```

### 4. Custom Checked Exceptions for Business Logic

```java
public class InsufficientFundsException extends Exception {
    private final BigDecimal deficit;

    public InsufficientFundsException(BigDecimal deficit) {
        super("Insufficient funds, deficit: " + deficit);
        this.deficit = deficit;
    }
}
```

### 5. try-with-resources for Cleanup

```java
try (var conn = dataSource.getConnection();
     var stmt = conn.prepareStatement(sql)) {
    // use resources
} // automatically closed, even on exception
```

## Summary

- `Exception` is the base class for all checked exceptions in Java.
- Checked exceptions are part of the API contract — they force callers to handle errors.
- Never catch generic `Exception` unless you have a specific framework reason.
- Declare the narrowest exception type that accurately describes failure conditions.
- Use exception translation at architectural boundaries.
- Prefer `RuntimeException` for programming errors, `Exception` for recoverable conditions.
