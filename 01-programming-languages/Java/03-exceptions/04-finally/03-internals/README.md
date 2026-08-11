# Finally Internals: How the Compiler Duplicates Finally Bytecode

## Why This Matters

The `finally` block is one of the most misunderstood constructs in Java. Developers assume it is a special JVM instruction — a "guaranteed execution" mechanism built into the runtime. It is not. The `finally` block is entirely a **compiler transformation**. The JVM has no concept of `finally` at the bytecode level.

This distinction matters because the compiler's approach — duplicating the finally bytecode into every exit path — creates real consequences: larger `.class` files, subtle interactions with `return` statements, and edge cases that can silently swallow exceptions.

## What Problem This Solves

Without `finally`, resource cleanup would be fragile:

```java
InputStream is = null;
try {
    is = new FileInputStream("file.txt");
    // use the stream
} catch (IOException e) {
    // handle error
    if (is != null) {
        try { is.close(); } catch (IOException ignored) {}
    }
    throw e;
}
// Normal path cleanup
if (is != null) {
    try { is.close(); } catch (IOException ignored) {}
}
```

The cleanup code is duplicated in every exit path. `finally` solves this by guaranteeing that cleanup runs regardless of how the try block exits — normally, via return, via break/continue, or via an exception.

But the guarantee comes at a cost: the compiler must duplicate the finally bytecode into every possible exit path.

## How the Compiler Handles Finally

### The Duplication Rule

When the compiler encounters a `finally` block, it copies the bytecode of the finally block into every exit point of the corresponding `try` block:

1. **Normal exit** (falling through the try block without exception).
2. **Exception exit** (an exception is thrown and caught by a catch block — the finally is inserted at the end of the catch block, before any re-throw).
3. **Return exit** (the try or catch block contains a `return` statement — the finally is inserted before the return).
4. **Break/continue exit** (the try or catch block contains a break or continue — the finally is inserted before the jump).

This means the same finally bytecode can appear **multiple times** in the compiled class file — once for each exit path.

### Why the JVM Cannot Do It Natively

The JVM's exception handling mechanism (the exception table) supports two modes:
- **Catch**: redirect to a handler.
- **Finally**: there is no "finally" mode.

The exception table can map a bytecode range to a handler, but it cannot specify "run this code after the handler completes." The JVM would need a more complex control flow mechanism to support `finally` natively, and it was simpler to leave that to the compiler.

### Bytecode Example

Given this Java code:

```java
public int demo() {
    try {
        return 1;
    } finally {
        cleanup();
    }
}
```

The compiler generates bytecode equivalent to:

```
public int demo();
    Code:
       0: iconst_1        // push 1
       1: istore_1        // store return value in local variable 1
       2: invokestatic #1 // cleanup()
       5: iload_1         // load return value
       6: ireturn         // return it
       7: astore_2        // exception handler: store exception
       8: invokestatic #1 // cleanup()
      11: aload_2         // load stored exception
      12: athrow          // re-throw
```

Notice:
- The return value is saved to a local variable before the finally block runs.
- The finally block (`cleanup()`) is inlined at both exit points.
- After the finally runs, the saved return value is restored and returned.
- If an exception occurred, it is saved, the finally runs, and the exception is re-thrown.

### The Return Value Problem

The most surprising behavior occurs when a try block returns a value and the finally block also returns:

```java
public int demo() {
    try {
        return 1;
    } finally {
        return 2; // This silently overrides the try return!
    }
}
```

This compiles and runs. The finally block's return overrides the try block's return. The try block's return value (1) is stored in a local variable, but the finally block's `return 2` immediately discards it and returns 2.

The JLS (Java Language Specification) explicitly warns against this: "A try statement with a finally block always executes the finally block as its final action... If the finally block completes abruptly for any reason, the try statement completes abruptly for the same reason."

In practice, this is almost always a bug. The compiler may emit a warning, but it is legal code.

### The Exception Swallowing Problem

If the finally block throws an exception, any exception from the try or catch block is lost:

```java
public void demo() {
    try {
        throw new RuntimeException("original");
    } finally {
        throw new RuntimeException("finally"); // replaces original!
    }
}
```

The original exception is never caught or logged. The finally block's exception propagates instead. This is because the finally bytecode is inlined after the catch handler — and when the finally block throws, it immediately jumps to the caller's exception handler, skipping the original exception entirely.

### Code Size Impact

Each duplication of the finally bytecode increases the method's size:

- A method with 3 exit paths and a 10-byte finally block adds 30 bytes of duplicated bytecode.
- Complex finally blocks (multiple statements, try-catch inside finally) can be significantly larger.
- This increases the `.class` file size and can affect JIT compilation thresholds.

In extreme cases, deeply nested try-finally structures can push a method's bytecode beyond the JVM's limit of 65535 bytes per method, causing a compilation error.

## Try-With-Resources and Finally

Java 7's try-with-resources compiles to a try-finally structure. The compiler generates:

1. The `try` body.
2. A `finally` block that calls `close()` on each resource.
3. If the original try body threw an exception and `close()` also throws, the close exception is **suppressed** (added to the original exception's suppressed exceptions list).

This is the only place where the compiler uses the suppressed exceptions mechanism as part of its finally transformation. Without try-with-resources, suppressed exceptions are not generated by the finally duplication.

## Performance Implications

1. **Happy path**: Finally bytecode duplication has no runtime cost beyond the increased method size. The JVM executes the finally code at each exit point, just as if you had written the cleanup code manually.

2. **JIT compilation**: Larger methods take longer to compile. The JIT compiler must parse and optimize more bytecode. In hot loops, this can slightly delay compilation.

3. **Instruction cache**: Duplicated bytecode means the same instructions appear at multiple offsets. The CPU instruction cache may not deduplicate them, leading to slightly higher cache pressure.

4. **Exception paths**: When an exception occurs, the finally bytecode runs in addition to the catch handler. This is not duplication — it is sequential execution. But it adds to the total cost of exception handling.

## Code Demonstration

See `FinallyInternals.java` for a programmatic demonstration of:
- The bytecode duplication visible through `javap`.
- The return value override problem.
- The exception swallowing problem.
- Measuring the performance impact of finally blocks.

## Practical Implications

1. **Keep finally blocks short.** Every statement in the finally block is duplicated across all exit paths. Longer finally blocks mean more code duplication.

2. **Never return from a finally block.** It silently overrides the try/catch return value. The compiler may warn, but the behavior is well-defined and dangerous.

3. **Never throw from a finally block** unless you intentionally want to replace any pending exception. Use try-catch inside the finally block if you must perform fallible operations.

4. **Prefer try-with-resources over manual try-finally** for resource cleanup. It handles the suppressed exception mechanism correctly and reduces boilerplate.

5. **Monitor `.class` file sizes** if you have deeply nested try-finally structures. Use `javap -c` to inspect the bytecode and confirm that the duplication is within acceptable bounds.

## Summary

| Aspect | Detail |
|--------|--------|
| JVM support for finally | None — it is a compiler transformation |
| Duplication rule | Finally bytecode is copied into every exit path |
| Return from finally | Overrides try/catch return silently |
| Throw from finally | Replaces any pending exception |
| Code size impact | Proportional to exit paths × finally block size |
| Runtime cost | Same as writing cleanup code inline |
| try-with-resources | Compiles to try-finally with suppressed exception support |

The finally block is a convenience, not a primitive. Understanding its compiler-level implementation helps you avoid subtle bugs and write more predictable resource management code.
