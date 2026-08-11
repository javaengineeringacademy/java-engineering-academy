# 06 - Exception Hierarchy in Java

## 1. Scope

This topic explores the complete Java exception hierarchy — the class structure that
underpins every `try-catch-finally` block, every `throws` declaration, and every
runtime failure in the JVM. Understanding the hierarchy is essential for writing
robust, maintainable Java applications.

## 2. Why It Exists

Java's exception hierarchy serves several critical purposes:

- **Classification**: Separates recoverable errors (exceptions) from unrecoverable
  failures (errors).
- **Contract enforcement**: Compiler uses checked vs unchecked distinction to enforce
  error-handling contracts at compile time.
- **Dispatch**: JVM uses the hierarchy to find the most specific matching `catch`
  handler at runtime.
- **Polymorphism**: Catching `Exception` catches all exceptions; catching `Throwable`
  catches everything.
- **Design clarity**: Custom exceptions can be designed to fit into a logical tree
  that mirrors the application's failure modes.

## 3. Design Rationale

The hierarchy was designed by James Gosling and the Java team with these goals:

1. **Separation of concerns**: `Error` vs `Exception` separates JVM-level failures
   from application-level failures.
2. **Safety by default**: Checked exceptions force developers to handle or declare
   recoverable errors, reducing unhandled failure paths.
3. **Extensibility**: The hierarchy is open — developers can extend it with custom
   exception classes at any level.
4. **Backwards compatibility**: New exception types can be added without breaking
   existing catch blocks that catch higher in the tree.

## 4. What Is the Exception Hierarchy

### 4.1 The Root: `java.lang.Throwable`

`Throwable` is the base class for all error and exception objects in Java. Every
object that can be thrown or caught must extend `Throwable`. It provides:

- `getMessage()` — human-readable description
- `getCause()` — the underlying cause (another `Throwable`)
- `printStackTrace()` — full stack trace to `stderr`
- `getStackTrace()` — programmatic access to stack frames
- `addSuppressed()` / `getSuppressed()` — for try-with-resources

### 4.2 `java.lang.Error`

`Error` represents serious problems that a reasonable application should not try to
catch. These are typically caused by the JVM itself or by resources being exhausted.

Common `Error` subclasses:

- `OutOfMemoryError` — JVM ran out of heap memory
- `StackOverflowError` — call stack exceeded maximum depth
- `VirtualMachineError` — internal JVM error
- `LinkageError` — class linkage failure
- `NoClassDefFoundError` — class not found at runtime
- `AssertionError` — failed `assert` statement

**Rule**: Do not catch `Error` unless you have a very specific recovery strategy.

### 4.3 `java.lang.Exception`

`Exception` represents conditions that a reasonable application might want to catch.
This is the main branch for application-level failures.

`Exception` has two major sub-branches:

- **Checked exceptions** — subclasses of `Exception` that are NOT `RuntimeException`.
  Must be declared in `throws` or caught in `try-catch`.
- **Unchecked exceptions** — `RuntimeException` and its subclasses. Not required to
  be caught or declared.

### 4.4 `java.lang.RuntimeException`

`RuntimeException` is the base class for unchecked exceptions. These are typically
programming bugs — things that should not happen if the code is correct.

Common `RuntimeException` subclasses:

- `NullPointerException` — dereferencing a null reference
- `ArrayIndexOutOfBoundsException` — invalid array index
- `IllegalArgumentException` — invalid method argument
- `IllegalStateException` — method called at wrong time
- `ClassCastException` — invalid type cast
- `ArithmeticException` — divide by zero, etc.
- `UnsupportedOperationException` — operation not supported
- `ConcurrentModificationException` — concurrent collection modification
- `IndexOutOfBoundsException` — generic index out of range

## 5. Complete ASCII Hierarchy Tree

```
Throwable
├── Error
│   ├── AssertionError
│   ├── LinkageError
│   │   ├── ClassFormatError
│   │   ├── NoClassDefFoundError
│   │   ├── IncompatibleClassChangeError
│   │   ├── AbstractMethodError
│   │   ├── NoSuchFieldError
│   │   └── NoSuchMethodError
│   ├── VirtualMachineError
│   │   ├── StackOverflowError
│   │   └── OutOfMemoryError
│   ├── ThreadDeath
│   ├── ExceptionInInitializerError
│   └── IOError
├── Exception
│   ├── IOException
│   │   ├── FileNotFoundException
│   │   ├── SocketException
│   │   ├── ConnectException
│   │   ├── TimeoutException
│   │   └── EOFException
│   ├── SQLException
│   ├── ClassNotFoundException
│   ├── ReflectiveOperationException
│   │   ├── NoSuchMethodException
│   │   ├── NoSuchFieldException
│   │   └── InvocationTargetException
│   ├── InterruptedException
│   ├── CloneNotSupportedException
│   ├── InstantiationException
│   ├── IllegalAccessException
│   ├── NamingException
│   ├── RoboException (custom checked)
│   ├── RuntimeException
│   │   ├── IllegalArgumentException
│   │   │   ├── NumberFormatException
│   │   │   ├── IllegalFormatException
│   │   │   └── IllegalThreadStateException
│   │   ├── IllegalStateException
│   │   ├── UnsupportedOperationException
│   │   ├── NullPointerException
│   │   ├── IndexOutOfBoundsException
│   │   │   ├── ArrayIndexOutOfBoundsException
│   │   │   └── StringIndexOutOfBoundsException
│   │   ├── ClassCastException
│   │   ├── ArithmeticException
│   │   ├── ArrayStoreException
│   │   ├── ConcurrentModificationException
│   │   ├── NoSuchElementException
│   │   ├── StackOverflowError (extends Error, not RuntimeException)
│   │   └── SecurityException
│   └── (other checked exceptions)
```

## 6. Each Level's Purpose and Contract

| Level | Class | Contract | When to Use |
|-------|-------|----------|-------------|
| Root | `Throwable` | Base of all throwables | Never catch directly; use as type in generic handlers |
| Branch | `Error` | Serious, unrecoverable | JVM errors, resource exhaustion |
| Branch | `Exception` | Recoverable errors | Application-level failures |
| Leaf | `RuntimeException` | Programming bugs | Null dereference, bad arguments |
| Leaf | Checked `Exception` | Recoverable, enforced | IO, network, parsing |
| Leaf | `Error` subclass | JVM-level | OutOfMemory, StackOverflow |

### 6.1 `Throwable` Contract

- All throwables must be `Throwable` or a subclass.
- The `getMessage()` method returns a short description.
- The `getCause()` method returns the original exception (chaining).
- The `printStackTrace()` method prints the full stack trace.

### 6.2 `Error` Contract

- Should not be caught by application code.
- Indicates a fundamental problem that cannot be recovered from.
- Typically thrown by the JVM or runtime environment.

### 6.3 `Exception` Contract

- Can be caught and handled by application code.
- Should be used for recoverable conditions.
- If checked, must be declared in `throws` clause or caught.

### 6.4 `RuntimeException` Contract

- Represents programming bugs.
- Should not be caught unless you have a specific reason.
- Should be prevented by validating inputs and checking conditions.

## 7. How the JVM Uses the Hierarchy for Exception Dispatch

When an exception is thrown, the JVM performs a search through the call stack for a
matching `catch` block:

1. The JVM walks up the call stack, starting from the current method.
2. For each method on the stack, it checks if there's a `catch` block that can
   handle the thrown exception.
3. A `catch` block matches if the exception is an instance of the catch clause's
   exception type (or any subclass of it).
4. If no matching `catch` is found, the exception propagates to the next method
   up the stack.
5. If no `catch` block is found at all, the JVM calls `Thread.uncaughtExceptionHandler`
   and the program terminates.

### Key Insight: Most Specific First

The JVM finds the **first** matching `catch` block in the call stack. This means:

```java
try {
    throw new IOException("fail");
} catch (Exception e) {        // This catches IOException too
    System.out.println("Caught Exception");
} catch (IOException e) {      // This is NEVER reached
    System.out.println("Caught IOException");
}
```

The `Exception` handler catches `IOException` because `IOException extends Exception`.
The `IOException` handler is unreachable and the compiler will reject it.

### Correct Order

```java
try {
    throw new IOException("fail");
} catch (IOException e) {      // Most specific first
    System.out.println("Caught IOException");
} catch (Exception e) {        // More general, reached only if above doesn't match
    System.out.println("Caught Exception");
}
```

## 8. Checked vs Unchecked Boundary (RuntimeException Divide)

The boundary between checked and unchecked exceptions is one of the most debated
design decisions in Java.

### 8.1 The Divide

```
Throwable
├── Error            (unchecked)
└── Exception
    ├── RuntimeException     (unchecked)
    └── (other Exception)    (checked)
```

- **Checked**: Must be caught or declared. Compiler enforces this.
- **Unchecked**: Not required to be caught. Runtime enforcement only.

### 8.2 Why the Divide?

Joshua Bloch (Effective Java, Item 58) argues:

> "Use checked exceptions for conditions from which the caller can reasonably be
> expected to recover. Use runtime exceptions to indicate programming errors."

### 8.3 When to Use Checked vs Unchecked

| Scenario | Use |
|----------|-----|
| Caller can recover (retry, fallback) | Checked |
| Programming bug (null, bad argument) | Unchecked |
| Legacy code that doesn't handle exceptions | Unchecked |
| Framework-level exception that all callers must handle | Checked |

### 8.4 Controversy

Many modern Java developers argue that the checked exception boundary is a mistake:

- Checked exceptions lead to boilerplate `try-catch` blocks.
- They can be swallowed with empty catch blocks.
- They make code harder to read.
- Modern frameworks (Spring, etc.) often use unchecked exceptions exclusively.

However, checked exceptions remain a useful safety mechanism in many codebases,
especially when the recovery path is clear.

---
**Continue:** [Part 2](README-Part2.md)
