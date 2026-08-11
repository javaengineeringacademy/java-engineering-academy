# JVM Internals: Checked Exception Enforcement

## Scope

This topic explains how the JVM and compiler enforce checked exceptions at the bytecode level. It covers exception tables in class files, bytecode verification, and runtime exception dispatch.

## Why It Exists

Understanding the internals of exception handling explains why checked exceptions have zero runtime cost in the happy path and how the JVM efficiently handles exceptions when they occur.

## Design Rationale

Java's checked exception enforcement is split between two layers:
1. **Compiler** - verifies that all checked exceptions are caught or declared
2. **JVM** - enforces exception table contracts at runtime via bytecode verification

This separation means checked exceptions are a compile-time construct. At the bytecode level, there is no difference between checked and unchecked exceptions. Both use the same exception table mechanism.

## How Checked Exceptions Are Enforced at Compile Time

The Java compiler performs static analysis on every method:

1. For each throw statement, it records which exception types can be thrown
2. For each method call, it records which exception types the called method declares
3. It builds an implicit "exception propagation" graph
4. It verifies that every checked exception is either caught in a try-catch or declared in the throws clause

```java
// Compiler verifies this:
void read() throws IOException {        // declares IOException
    FileInputStream fis = new FileInputStream("file.txt"); // may throw IOException
    // compiler allows: IOException is declared
}

void bad() {
    FileInputStream fis = new FileInputStream("file.txt");
    // compiler error: unreported exception IOException
}
```

Unchecked exceptions (RuntimeException, Error) skip this verification. The compiler does not require them to be caught or declared.

## Bytecode Verification of Exception Tables

After compilation, the JVM verifier checks:

1. Every exception table entry points to a valid bytecode offset
2. The catch type is a valid class in the constant pool
3. The try range is within method bounds
4. The handler range does not overlap incorrectly with other handlers

If verification fails, the class is rejected with a `VerifyError`.

## Exception Table in the Class File

Each method with try-catch blocks has an `exception_table` in its Code attribute. Each entry has:

| Field | Description |
|---|---|
| `start_pc` | Inclusive start of try block (bytecode offset) |
| `end_pc` | Exclusive end of try block (bytecode offset) |
| `handler_pc` | Bytecode offset of catch handler |
| `catch_type` | Index into constant pool for exception class (0 = finally) |

Example bytecode for `try { read(); } catch (IOException e) { handle(); }`:

```
Exception table:
    from    to  target type
        0     4     7   Class java/io/IOException
```

This means: if an IOException occurs between offsets 0 and 4, jump to offset 7.

## How the JVM Dispatches Exceptions

When `athrow` is executed or an implicit exception occurs:

1. The JVM looks up the current bytecode offset in the exception table
2. It searches for a matching handler (first match wins)
3. A handler matches if:
   - The offset is within [start_pc, end_pc)
   - The thrown exception is an instance of (or subclass of) the catch_type class
4. If no handler is found, the current frame is popped and the search repeats in the caller's frame
5. If no handler is found in any frame, the thread terminates

```
Exception dispatch pseudocode:

for each frame in stack (most recent first):
    for each entry in frame.exception_table:
        if offset in [start_pc, end_pc) and exception instanceof catch_type:
            set PC to handler_pc
            push exception onto operand stack
            return
    pop frame
// no handler found - thread dies
```

## Performance Characteristics

### Happy Path

Zero cost. Exception tables are only consulted when an exception is thrown. Normal control flow does not touch the exception table.

### Exception Path

Throwing an exception is expensive:

1. `Throwable.fillInStackTrace()` walks the entire call stack - O(depth)
2. Stack trace is stored as a `StackTraceElement[]` array
3. Exception table lookup is linear (small tables) or O(n) for methods with many handlers
4. Stack unwinding pops frames until a handler is found

### Optimization Techniques

- **JIT compilation**: HotSpot can optimize exception dispatch using "compiled exception maps" that are faster than the interpreted table lookup
- **Stack trace lazy filling**: Some JVM implementations delay stack trace filling until `getStackTrace()` is called (not standard behavior)
- **Fast throw**: JVM can reuse a single exception instance for common patterns (e.g., `NullPointerException` at the same bytecode location)

### Cost Breakdown

| Operation | Cost |
|---|---|
| Normal flow (no exception) | Zero |
| throw new Exception() | ~1-10 microseconds (depends on stack depth) |
| fillInStackTrace() | ~1-5 microseconds (depends on depth) |
| Exception table lookup | ~100 nanoseconds |
| Stack unwinding | Proportional to frames popped |

## Summary

- Checked exceptions are a compile-time construct; bytecode has no checked/unchecked distinction.
- Exception tables in class files map try ranges to handler offsets and catch types.
- The JVM searches exception tables linearly when an exception occurs.
- Happy path has zero overhead; exception path is expensive due to stack trace filling and unwinding.
- JIT compilers optimize exception dispatch for hot paths.
