# 03 - RuntimeException

## Scope

This topic covers `RuntimeException`, the unchecked exception hierarchy in Java. You will learn what makes `RuntimeException` different from checked exceptions, the most common subtypes, when to use them, and how to handle them in production code.

## Why It Exists

Java distinguishes between checked and unchecked exceptions. Checked exceptions represent recoverable conditions — things the caller can reasonably handle. `RuntimeException` and its subtypes represent **programming bugs**: situations where the code has a flaw that should be fixed, not caught and silently ignored.

The design rationale is practical. If every array access, every null dereference, and every arithmetic error required a `try-catch` block, Java code would be unreadable. By making these exceptions unchecked, the compiler does not force you to handle them. This keeps normal code clean while still allowing you to catch them when recovery is possible.

The Java language designers decided that certain failures are so common and so clearly the result of programming errors that requiring `throws` declarations for them would add noise without value. A `NullPointerException` almost always indicates a bug. An `ArrayIndexOutOfBoundsException` almost always indicates a logic error. Forcing every method that could encounter these to declare them in its signature would clutter the API without helping callers recover.

## What Is RuntimeException

`RuntimeException` extends `Exception` directly. Unlike checked exceptions, the compiler does not require you to declare or catch them. They are **unchecked**.

```java
public class RuntimeException extends Exception {
    // ...
}
```

A `RuntimeException` typically indicates a bug in the program logic. It is not something you catch to recover from — it is something you fix. The key distinction is that checked exceptions represent conditions that a well-written application should anticipate and recover from, while unchecked exceptions represent conditions that reflect bugs in the program.

In practice, this means that when you see a `RuntimeException` in your logs, your first reaction should be to fix the code that caused it, not to add a `try-catch` block around it.

## Characteristics

| Property               | Value                                      |
|------------------------|--------------------------------------------|
| Hierarchy              | `RuntimeException extends Exception`       |
| Checked/Unchecked      | Unchecked                                  |
| Compile-time enforced  | No — no `throws` clause required           |
| Represents             | Programming bugs, not recoverable errors   |
| Typical cause          | Null references, bad arguments, state bugs |
| Typical action         | Fix the code, do not catch                 |
| Can be caught          | Yes, but usually should not be             |
| Can be chained         | Yes, via `initCause()` or constructor      |
| Custom subtypes        | Encouraged for domain-specific bugs        |

## Common RuntimeException Subtypes

| Exception                      | When It Occurs                                        |
|--------------------------------|-------------------------------------------------------|
| `NullPointerException`         | Dereferencing a `null` reference                      |
| `IndexOutOfBoundsException`    | Accessing an array or list with an invalid index      |
| `ArrayIndexOutOfBoundsException` | Array index is negative or beyond array length     |
| `StringIndexOutOfBoundsException` | String index is out of range                      |
| `IllegalArgumentException`    | Method receives an inappropriate argument             |
| `IllegalStateException`        | Method called at the wrong time (object in wrong state) |
| `ArithmeticException`         | Illegal arithmetic operation (e.g., division by zero) |
| `ClassCastException`          | Casting an object to an incompatible type             |
| `ConcurrentModificationException` | Collection modified while being iterated           |
| `UnsupportedOperationException`  | Operation not supported by the implementation       |
| `NumberFormatException`       | String cannot be parsed to a numeric type             |

**Note:** `StackOverflowError` and `OutOfMemoryError` are `Error` subclasses, not `RuntimeException`. They represent JVM-level failures, not application-level programming bugs. Never catch `Error` types — they indicate unrecoverable failures.

## RuntimeException vs Error

It is important to understand the difference between `RuntimeException` and `Error`. Both are unchecked, but they represent different kinds of failures.

| Property               | RuntimeException                          | Error                                      |
|------------------------|-------------------------------------------|--------------------------------------------|
| Hierarchy              | `RuntimeException extends Exception`      | `Error extends Throwable`                  |
| Represents             | Programming bugs in application code      | JVM-level failures                         |
| Examples               | `NullPointerException`, `IllegalStateException` | `OutOfMemoryError`, `StackOverflowError` |
| Action                 | Fix the code                              | Usually cannot be recovered from           |
| Catchable              | Sometimes, for recovery                  | Almost never                               |

An `Error` indicates a serious problem that a reasonable application should not try to catch. A `RuntimeException` indicates a problem that is the result of a programming mistake and should be fixed.

## The Inheritance Hierarchy

Understanding where `RuntimeException` sits in the exception hierarchy helps you make design decisions:

```
Throwable
├── Error (unchecked)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception
    ├── RuntimeException (unchecked)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   ├── IllegalStateException
    │   ├── IndexOutOfBoundsException
    │   ├── ClassCastException
    │   ├── ArithmeticException
    │   ├── ConcurrentModificationException
    │   ├── UnsupportedOperationException
    │   ├── NumberFormatException
    │   └── ...
    ├── IOException (checked)
    ├── SQLException (checked)
    └── ...
```

The key insight is that `RuntimeException` and `Error` are both unchecked. The difference is in what they represent: `RuntimeException` is a bug in your code, while `Error` is a problem with the JVM or environment.

## RuntimeException Contract

The contract is identical to `Exception`:

- You can throw it with `throw new RuntimeException(...)`.
- You can catch it with `try-catch`.
- You can declare it in a `throws` clause (though this is uncommon for unchecked exceptions).
- You can chain it using `initCause()`.
- You can subclass it to create domain-specific unchecked exceptions.

```java
public void process(String value) {
    if (value == null) {
        throw new NullPointerException("value must not be null");
    }
    // ...
}
```

The contract also implies that if you throw a `RuntimeException`, you are asserting that the caller made a programming error. The exception should never occur if the caller uses the API correctly.

## When to Use RuntimeException

Use `RuntimeException` (or a custom subtype) when the failure is a **programming bug** — something the caller should prevent, not handle.

Use checked exceptions when the failure is **recoverable** — something the caller can reasonably do something about.

| Scenario                                        | Exception Type       |
|-------------------------------------------------|----------------------|
| Null argument passed to a method                 | `IllegalArgumentException` |
| Method called on an object in invalid state      | `IllegalStateException` |
| Invalid index in array/list access               | `IndexOutOfBoundsException` |
| Division by zero in integer arithmetic           | `ArithmeticException` |
| File not found                                  | `FileNotFoundException` (checked) |
| Network connection fails                        | `IOException` (checked) |

Custom `RuntimeException` subtypes are useful for domain-specific programming errors:

```java
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(double balance, double amount) {
        super("Insufficient funds: balance=" + balance + ", requested=" + amount);
    }
}
```

## Common Pitfalls

### Catching RuntimeException Broadly

```java
// Bad: swallows all bugs
try {
    doSomething();
} catch (RuntimeException e) {
    log.warn("Something went wrong", e);
}
```

This hides bugs. Catch specific subtypes, or let the exception propagate to a global handler. Broadly catching `RuntimeException` makes debugging extremely difficult because the exception disappears from the normal error flow.

### Using RuntimeException for Control Flow

```java
// Bad: using exceptions as logic
try {
    int value = map.get(key);
    process(value);
} catch (NullPointerException e) {
    // use default
}
```

Check for null explicitly. Exceptions are expensive to create and throw, and using them for control flow makes intent unclear. The cost of creating an exception includes filling in the stack trace, which is a significant performance hit in tight loops.

### Throwing RuntimeException Without a Message

```java
// Bad: no context
throw new RuntimeException();

// Good: descriptive message
throw new RuntimeException("Failed to process order " + orderId);
```

Always include a message that helps diagnose the problem. The message should describe what went wrong, including any relevant context such as parameter values, object state, or identifiers.

### Catching and Swallowing

```java
// Bad: swallowing the exception
try {
    riskyOperation();
} catch (RuntimeException e) {
    // silently ignored
}
```

If you catch an exception, at minimum log it. Ideally, either handle it properly or rethrow it.

---
**Continue:** [Part 2](README-Part2.md)
