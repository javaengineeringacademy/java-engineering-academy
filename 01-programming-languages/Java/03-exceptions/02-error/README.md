# 02 - Error in Java

## Scope

This topic covers Java's `Error` class and its subclasses — the unrecoverable failure conditions that signal catastrophic JVM or system-level problems. You will learn what Errors are, why they exist, when they occur, and how to handle them responsibly in production systems.

## Why It Exists

Not all problems are recoverable. When the JVM runs out of memory, when a class file is corrupted, or when the call stack overflows, there is no reasonable way for application code to recover. The `Error` class exists to represent these terminal conditions. It separates "things your code might reasonably handle" (`Exception`) from "things that indicate the environment itself is broken" (`Error`).

Without this distinction, developers would be tempted to catch catastrophic failures and attempt recovery — making the situation worse. The `Error` hierarchy is a contract between the JVM and your application: when you see an `Error`, something fundamental has gone wrong.

## Design Rationale

The `Error` class was introduced in JDK 1.0 alongside `Exception` as a direct subclass of `Throwable`. The original design intent was clear: Errors represent serious problems that a reasonable application should not try to catch.

The key design decisions:

- **Unchecked by default.** Errors do not require `try-catch` blocks or `throws` declarations. This forces developers to confront the reality that these conditions are not expected normal flow.
- **Fatal by nature.** Most Error subclasses indicate conditions from which recovery is impossible or meaningless. Catching an `OutOfMemoryError` and trying to continue is almost always wrong.
- **JVM responsibility.** Many Errors are thrown by the JVM itself, not by application code. The JVM detects corruption, exhaustion, and linkage failures and throws the appropriate Error subclass.
- **Separation of concerns.** By separating Errors from Exceptions, the language design makes a clear statement: Exceptions are for conditions your code should handle; Errors are for conditions the environment imposes on your code.

## What Is Error

`java.lang.Error` is a class that extends `Throwable`. It represents a serious problem that a normal application should not try to catch. Errors are unchecked — the compiler does not require you to declare them in a `throws` clause or handle them in a `try-catch` block.

Errors are thrown by the JVM when it detects conditions that it cannot recover from. They can also be thrown explicitly by application code (for example, `AssertionError`), but this is the exception rather than the rule.

```java
public class Error extends Throwable {
    public Error() {
        super();
    }

    public Error(String message) {
        super(message);
    }

    public Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Error(Throwable cause) {
        super(cause);
    }
}
```

## Characteristics

| Characteristic | Value |
|----------------|-------|
| Type | Unchecked |
| Extends | `Throwable` |
| Required handling | None (should not be caught) |
| Typical source | JVM, runtime system |
| Recovery possible | Almost never |
| Catchable | Technically yes, practically no |
| Examples | `OutOfMemoryError`, `StackOverflowError`, `NoClassDefFoundError` |

## Error Hierarchy

```
                    ┌───────────────────┐
                    │      Throwable     │
                    └─────────┬─────────┘
                              │
                    ┌─────────┴─────────┐
                    │       Error        │
                    └─────────┬─────────┘
                              │
          ┌───────────────────┼───────────────────┐
          │                   │                   │
┌─────────┴─────────┐ ┌──────┴───────┐ ┌─────────┴─────────┐
│ VirtualMachineError│ │ LinkageError │ │   AssertionError  │
└─────────┬─────────┘ └──────┬───────┘ └───────────────────┘
          │                   │
    ┌─────┴─────┐     ┌──────┴───────┐
    │           │     │              │
┌───┴───┐ ┌────┴───┐ ┌┴─────────────┐ ┌──────────────┐
│OutOfMem│ │StackOvr│ │NoClassDef    │ │ClassFormat   │
│oryError│ │flowErr │ │FoundError    │ │Error         │
└────────┘ └────────┘ └──────────────┘ └──────────────┘
```

## Error Subtypes

### VirtualMachineError

`VirtualMachineError` is the abstract superclass for errors that indicate the JVM has broken its contract or exhausted resources.

```java
public abstract class VirtualMachineError extends Error { }
```

**OutOfMemoryError**

The JVM has exhausted available memory and cannot allocate more objects. This is the most common Error encountered in production.

```java
List<byte[]> memoryHog = new ArrayList<>();
while (true) {
    memoryHog.add(new byte[1024 * 1024]); // 1MB chunks
}
// Eventually: java.lang.OutOfMemoryError: Java heap space
```

OOM can occur in different memory regions:
- `Java heap space` — heap exhaustion
- `Metaspace` — class metadata exhaustion (JDK 8+)
- `GC overhead limit exceeded` — GC is too slow relative to reclamation
- `unable to create new native thread` — OS cannot provide more threads

**StackOverflowError**

The call stack has exceeded its maximum depth. This typically indicates infinite recursion.

```java
public static void recurse() {
    recurse(); // No base case
}
// Eventually: java.lang.StackOverflowError
```

### LinkageError

`LinkageError` indicates a class has dependency issues during linking. Subclasses include:

**NoClassDefFoundError**

The JVM tried to load a class definition but could not find it at runtime. This is different from `ClassNotFoundException` (which is checked and thrown by explicit class loading).

```java
// Compile-time: class is available
// Runtime: class file missing or dependency missing
// Result: NoClassDefFoundError
```

**ClassFormatError**

The class file is malformed — corrupted during transfer, storage, or compilation.

### AssertionError

Thrown by the JVM when an `assert` statement fails. This is one of the few Errors that application code throws explicitly.

```java
int x = -1;
assert x >= 0 : "x must be non-negative";
// java.lang.AssertionError: x must be non-negative
```

### ThreadDeath

Thrown when a thread is forcibly stopped using `Thread.stop()`. This method is deprecated, and `ThreadDeath` should never be caught intentionally.

## The Error Contract

Errors inherit the same contract as `Throwable`:

| Method | Description |
|--------|-------------|
| `getMessage()` | Returns the detail message |
| `getCause()` | Returns the cause (if any) |
| `getStackTrace()` | Returns the stack trace elements |
| `toString()` | Returns the class name and message |
| `printStackTrace()` | Prints the stack trace to `System.err` |
| `fillInStackTrace()` | Captures the current stack trace |

```java
try {
    causeOome();
} catch (OutOfMemoryError e) {
    String message = e.getMessage();       // "Java heap space"
    Throwable cause = e.getCause();        // null (usually)
    StackTraceElement[] trace = e.getStackTrace();
    System.err.println(e.toString());
}
```

## When to Catch Error

**Almost never.**

The general rule is that you should not catch `Error` or any of its subclasses. If you catch an `Error`, you are almost certainly doing something wrong.

There are exactly two scenarios where catching `Error` is acceptable:

1. **In container/framework code.** Application servers and containers catch `Error` to log the failure, clean up resources, and shut down gracefully. They do not attempt to continue normal operation.

2. **When recovery is well-defined and documented.** For example, some libraries catch `OutOfMemoryError` to release cached data and retry — but only when they can guarantee that the retry will not immediately trigger another OOM.

```java
// WRONG: Catching Error to continue normal flow
try {
    riskyOperation();
} catch (OutOfMemoryError e) {
    System.out.println("OOM, continuing anyway");
    continueNormalOperation(); // This will likely fail too
}

// ACCEPTABLE: Catching Error in container code
try {
    application.start();
} catch (OutOfMemoryError e) {
    logger.fatal("Application ran out of memory", e);
    cleanupResources();
    System.exit(1);
}
```

## Error vs Exception

| Aspect | Error | Exception |
|--------|-------|-----------|
| Intent | Unrecoverable | Recoverable |
| Handling | Should not catch | Should catch |
| Source | JVM/system | Application code |
| Checked | No | Some (IOException, etc.) |
| Example | `OutOfMemoryError` | `FileNotFoundException` |
| Recovery | Not possible | Often possible |
| Contract | Fatal | Recoverable |

## Common Pitfalls

### Catching Error to Recover

The most common mistake. Catching `OutOfMemoryError` to "free memory" and continue is dangerous. The JVM was unable to allocate memory — the state of your objects may be corrupted, and retrying may make things worse.

```java
// DANGEROUS: Do not do this
try {
    allocateMemory();
} catch (OutOfMemoryError e) {
    System.gc(); // Hoping GC will help — it won't
    allocateMemory(); // Likely to fail again
}
```

### Catching NoClassDefFoundError for Classloading

Some developers catch `NoClassDefFoundError` as a way to detect missing classes during classloading. This is wrong — `NoClassDefFoundError` indicates the JVM already failed to load a class, and catching it does not fix the underlying problem. Use `ClassNotFoundException` with explicit class loading instead.

### Catching AssertionError

`AssertionError` is meant for debugging. Catching it and swallowing it silently defeats the purpose of assertions.

### Ignoring StackOverflowError

`StackOverflowError` indicates a logic error (usually infinite recursion). It should be fixed, not caught.

## Production Patterns

### Monitoring for Errors

Do not catch Errors, but do monitor for them. Use logging frameworks to capture Error events and trigger alerts.

```java
Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
    if (throwable instanceof Error) {
        logger.fatal("Fatal error in thread " + thread.getName(), throwable);
        triggerAlert(throwable);
        initiateShutdown();
    }
});
```

### Graceful Shutdown

When an Error occurs, the application should shut down gracefully rather than continue in an unknown state.

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    logger.info("Shutdown hook triggered, cleaning up...");
    releaseResources();
    closeConnections();
}));
```

### Heap Dump Analysis

For `OutOfMemoryError`, enable heap dumps on OOM for later analysis:

```
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

### Thread Dump Analysis

For `StackOverflowError`, capture thread dumps to identify the recursion pattern.

## Summary

- `Error` represents unrecoverable JVM or system failures
- Errors are unchecked and should not be caught by application code
- Common subtypes: `OutOfMemoryError`, `StackOverflowError`, `NoClassDefFoundError`
- Catch Errors only in container/framework code for graceful shutdown
- Monitor for Errors but do not attempt recovery
- Use heap dumps and thread dumps to diagnose root causes
- The `Error` hierarchy exists to prevent developers from attempting impossible recovery