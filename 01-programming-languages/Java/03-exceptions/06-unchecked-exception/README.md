# 06 - Unchecked Exceptions

## How This Differs from 04-RuntimeException

| 04-runtime-exception | 06-unchecked-exception (this topic) |
|---------------------|--------------------------------------|
| The `RuntimeException` **class** | The unchecked exception **category** |
| Inheritance, API, common subclasses | Compiler behavior, design philosophy |
| "What is this class?" | "When and why should I use this?" |

## 1. Scope

This topic covers unchecked exceptions in Java — exceptions that are **not**
checked at compile time. You will learn what unchecked exceptions are, when they
occur, and how they differ from checked exceptions. The material explains the
design rationale behind unchecked exceptions and provides practical guidance for
handling them in production code.

**Learning Objectives**

- Understand what unchecked exceptions are and how they differ from checked
  exceptions.
- Know the common unchecked exception subtypes in the Java API.
- Recognize programming errors that produce unchecked exceptions.
- Apply best practices for handling unchecked exceptions in production.

---

## 2. Why It Exists

Java introduced the checked/unchecked distinction to enforce a design rule: **if
a caller can reasonably recover from an exception, make it checked; if not, make
it unchecked**. Unchecked exceptions represent programming bugs — situations
where the code itself is wrong, not where an external condition prevents
execution.

Unchecked exceptions exist because:

1. **Not all errors are recoverable.** A `NullPointerException` or
   `ArrayIndexOutOfBoundsException` signals a defect in the code, not an
   external failure. Forcing every method to declare or catch these would add
   boilerplate without improving reliability.
2. **Performance.** Checked exceptions require runtime stack trace generation
   even when no exception occurs (on some JVMs). Unchecked exceptions avoid this
   overhead.
3. **Flexibility.** Library designers can change internal implementations
   without breaking client code that would otherwise need to catch newly declared
   checked exceptions.
4. **Simplicity.** Code that uses unchecked exceptions reads more cleanly
   because it is not cluttered with try/catch blocks for errors that should
   never happen.

---

## 3. What Are Unchecked Exceptions

An **unchecked exception** is any exception that extends `RuntimeException` or
`Error`. The Java compiler does **not** require you to catch or declare them.

```
          Throwable
          ├── Error  (unchecked)
          │     ├── OutOfMemoryError
          │     ├── StackOverflowError
          │     └── ...
          └── Exception
                ├── RuntimeException  (unchecked)
                │     ├── NullPointerException
                │     ├── IllegalArgumentException
                │     └── ...
                └── IOException  (checked)
                      ├── FileNotFoundException
                      └── ...
```

Because they extend `RuntimeException`, they bypass the compile-time checking
that checked exceptions enforce. You can throw an unchecked exception from any
method without adding a `throws` clause.

---

## 4. Characteristics

| Characteristic              | Unchecked Exception                          |
|-----------------------------|----------------------------------------------|
| Compile-time checking       | None — compiler does not enforce handling     |
| Superclass                  | `RuntimeException` or `Error`                 |
| Typical cause               | Programming bug or JVM error                 |
| Should be caught?           | Generally **no** — fix the bug instead        |
| Declared in `throws` clause| Optional (often omitted)                     |
| Performance impact          | Lower — no mandatory stack trace generation   |
| Examples                    | `NullPointerException`, `ArrayIndexOutOfBounds` |

---

## 5. Subtypes Overview

The unchecked exception hierarchy contains the same classes as `RuntimeException`
and `Error`. From the unchecked perspective the important subtypes are:

### 5.1 RuntimeException Subtypes

| Exception                      | Typical Cause                                    |
|--------------------------------|--------------------------------------------------|
| `NullPointerException`        | Dereferencing a null reference                   |
| `ArrayIndexOutOfBoundsException` | Accessing an array with an invalid index      |
| `StringIndexOutOfBoundsException` | Accessing a String with an invalid index    |
| `IndexOutOfBoundsException`   | Accessing a list, string, or array with bad index|
| `IllegalArgumentException`    | Method received an illegal argument              |
| `IllegalStateException`       | Method invoked in wrong object state             |
| `NumberFormatException`       | String cannot be parsed to a number              |
| `ArithmeticException`         | Illegal arithmetic (e.g. division by zero)      |
| `ClassCastException`          | Invalid type cast at runtime                     |
| `ConcurrentModificationException` | Collection modified during iteration        |
| `UnsupportedOperationException` | Operation not supported by collection         |
| `EmptyStackException`         | Pop from an empty stack                          |
| `NoSuchElementException`     | Accessing an element that does not exist         |

### 5.2 Error Subtypes

| Error                          | Typical Cause                                    |
|--------------------------------|--------------------------------------------------|
| `OutOfMemoryError`            | JVM cannot allocate more memory                  |
| `StackOverflowError`          | Recursion too deep                               |
| `NoClassDefFoundError`        | Classpath issue at class loading                 |
| `ExceptionInInitializerError` | Static initializer failed                        |

> **Note:** `Error` subtypes are also unchecked but represent JVM-level
> failures rather than application bugs. They are rarely caught.

---

## 6. When to Use Unchecked Exceptions

Use unchecked exceptions for conditions that reflect **programming errors**:

1. **Invariant violations** — The object's state is corrupt.
   ```java
   public void withdraw(double amount) {
       if (amount > this.balance) {
           throw new IllegalArgumentException("Insufficient funds");
       }
       this.balance -= amount;
   }
   ```

2. **Null references** — A required value is missing.
   ```java
   public String getDisplayName(User user) {
       if (user == null) {
           throw new NullPointerException("User must not be null");
       }
       return user.getName();
   }
   ```

3. **Index out of range** — Accessing a collection with an invalid index.
   ```java
   String getMiddle(String text) {
       if (text == null || text.length() < 3) {
           throw new IllegalArgumentException("Text too short");
       }
       return text.substring(1, text.length() - 1);
   }
   ```

4. **Illegal arguments** — Method received values outside its contract.
   ```java
   public void setAge(int age) {
       if (age < 0 || age > 150) {
           throw new IllegalArgumentException("Invalid age: " + age);
       }
       this.age = age;
   }
   ```

5. **Illegal state** — Method called on an object that is not ready.
   ```java
   public byte[] readAll() {
       if (!isOpen()) {
           throw new IllegalStateException("Connection is closed");
       }
       // read bytes...
   }
   ```

---

## 7. Checked vs Unchecked Comparison

| Aspect                       | Checked Exception            | Unchecked Exception           |
|------------------------------|------------------------------|-------------------------------|
| Compile-time enforcement     | Yes — must catch or declare  | No — compiler ignores         |
| Superclass                   | `Exception` (not Runtime)    | `RuntimeException` or `Error` |
| Intended for                 | Recoverable external failures| Programming bugs              |
| When to catch                | At point of recovery         | Generally should not be caught|
| Declaring in `throws`        | Mandatory                    | Optional                      |
| Example                      | `IOException`, `SQLException`| `NullPointerException`        |
| Design philosophy            | Liskov Substitution Principle| Fail-fast / fix-the-bug       |
| Impact on API surface        | Adds to method signature     | No signature change           |

---

## 8. Common Pitfalls

### 8.1 Catching Unchecked Exceptions Silently

```java
try {
    processOrder(order);
} catch (RuntimeException e) {
    // Swallowing the exception hides bugs
}
```

**Better:** Let the exception propagate or log it meaningfully.

### 8.2 Using Unchecked Exceptions for Flow Control

```java
try {
    return list.get(index);
} catch (IndexOutOfBoundsException e) {
    return defaultValue;
}
```

**Better:** Check the index before accessing.

```java
if (index >= 0 && index < list.size()) {
    return list.get(index);
}
return defaultValue;
```

### 8.3 Overly Broad `catch` Clauses

```java
try {
    riskyOperation();
} catch (Exception e) { // Catches checked AND unchecked
    handleError(e);
}
```

This catches `RuntimeException` subtypes you might want to let propagate.

### 8.4 Declaring Unchecked Exceptions in `throws` Clauses

```java
public void process() throws IllegalArgumentException { // Unnecessary
    // ...
}
```

While technically valid, this clutters the API and is unusual for unchecked
exceptions. Only do this if you want to document a specific unchecked exception
that callers might want to handle.

### 8.5 Catching `Error`

```java
try {
    compute();
} catch (OutOfMemoryError e) {
    // Usually cannot recover
}
```

JVM-level errors like `OutOfMemoryError` should generally not be caught.

---

## 9. Production Patterns

### 9.1 Global Uncaught Exception Handler

For threads that are not directly managed, set a global handler:

```java
Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
    logger.error("Uncaught exception in thread {}: {}",
        thread.getName(), throwable.getMessage(), throwable);
    // Optionally restart the thread or shut down gracefully
});
```

### 9.2 Logging Unchecked Exceptions

Use a logging framework to record the full stack trace:

```java
public void handleRequest(Request request) {
    try {
        processRequest(request);
    } catch (RuntimeException e) {
        logger.error("Failed to process request {}: {}",
            request.getId(), e.getMessage(), e);
        throw e; // Re-throw after logging
    }
}
```

### 9.3 Defensive Programming

Validate inputs at method boundaries to fail fast:

```java
public Order createOrder(List<Item> items) {
    Objects.requireNonNull(items, "items must not be null");
    if (items.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
    }
    // proceed
}
```

### 9.4 Using `assert` for Internal Invariants

```java
public void process(Queue queue) {
    assert !queue.isEmpty() : "Queue must not be empty at this point";
    // proceed
}
```

> **Tip:** Enable assertions with `-ea` in development and testing. They are
> disabled by default in production.

### 9.5 Custom Unchecked Exception Hierarchy

For domain-specific errors, create a base unchecked exception:

```java
public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

Then create specific subtypes:

```java
public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(double balance, double amount) {
        super("INSUFFICIENT_FUNDS",
            "Balance " + balance + " is less than requested " + amount);
    }
}
```

---

## 10. Summary

| Concept               | Key Point                                          |
|-----------------------|----------------------------------------------------|
| Unchecked exception   | Extends `RuntimeException`; no compile-time check  |
| When to throw         | Programming bugs, invariant violations             |
| When to catch         | Generally should not — fix the bug instead         |
| Checked vs Unchecked  | Checked = recoverable; Unchecked = bug             |
| Production handling   | Global handler, logging, defensive validation      |
| Custom hierarchy      | Extend `RuntimeException` for domain-specific bugs |

---

## 11. Exercises

See the companion files for hands-on practice:

- **Examples:** `examples/UncheckedExceptionExample.java`
- **Exercises:** `exercises/UncheckedExceptionExercises.java`
- **Solutions:** `solutions/UncheckedExceptionSolutions.java`
- **Reference:** `references.md`
- **Decision Guide:** `decision.md`
- **Quiz:** `quiz.md`

---

## Summary

| Concept | Key Point |
|---------|-----------|
| Unchecked Exception | Extends RuntimeException or Error; no compile-time checking |
| When to Throw | Programming bugs, invariant violations, null references, illegal arguments |
| When to Catch | Generally should not; fix the bug instead |
| Checked vs Unchecked | Checked = recoverable external failures; Unchecked = programming bugs |
| Production Handling | Global uncaught handler, logging, defensive programming |
| Common Pitfalls | Silent catching, flow control, broad catch clauses, declaring in throws |
| Subtypes | RuntimeException subtypes (NPE, IAE, etc.) and Error subtypes (OOM, SOOE, etc.) |

## 12. Next Steps

Proceed to the next topic to learn about **custom exceptions** — creating your
own exception classes for domain-specific error handling.
