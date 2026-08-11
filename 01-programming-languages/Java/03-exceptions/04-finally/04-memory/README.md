# Finally Block Memory Implications

## How finally Affects Bytecode and Memory

The `finally` block in Java has direct consequences for bytecode size, code cache utilization, and JIT compilation. Understanding these effects helps when writing memory-conscious code.

## Bytecode Duplication

When the compiler generates bytecode, the `finally` block is inlined at every exit point of the `try` block. If the `try` block has multiple exit paths, the `finally` bytecode appears multiple times:

```java
try {
    doSomething();
    return result;
} finally {
    cleanup();
}
```

The compiler emits `cleanup()` bytecode:
1. After the `try` body (normal completion)
2. In the catch handler (if exception is rethrown)
3. At the method's return point

Each copy of the `finally` bytecode is identical. The bytecode for `cleanup()` is duplicated verbatim at each exit point.

## Code Cache Impact

The JVM stores compiled native code in the code cache. Duplicated bytecode means duplicated machine code after JIT compilation:

- Small `finally` blocks: minimal impact (a few bytes per copy)
- Large `finally` blocks: significant overhead (hundreds of bytes per copy)
- `finally` blocks in deeply nested try structures: multiplied across all exit paths

A method with 3 exit points and a 50-byte `finally` block produces 150 bytes of duplicated bytecode. The JIT compiler must compile and cache all copies.

## JIT Compilation Overhead

The JIT compiler processes each bytecode copy independently. When `finally` blocks are duplicated:

1. The C1 compiler (client) sees multiple identical bytecode sequences
2. The C2 compiler (server) must compile each copy separately
3. Inlining decisions may differ across copies due to surrounding context
4. Deoptimization of one copy does not affect others

This increases compile time and code cache pressure without improving runtime performance.

## The Bytecode Viewer

Use `javap -c` to see the duplication. For a method with `finally`:

```java
public int process() {
    try {
        return compute();
    } finally {
        cleanup();
    }
}
```

The `javap` output shows `cleanup()` bytecode appearing twice: once for the normal return path and once in the exception handler. Both paths converge at the method's return.

## Memory Comparison: finally vs. try-with-resources

### finally Pattern

```java
Connection conn = null;
try {
    conn = acquire();
    return conn.execute();
} finally {
    if (conn != null) conn.close();
}
```

Bytecode contains:
- `conn.close()` in normal path
- `conn.close()` in exception path
- Null check duplicated in both paths

### try-with-resources Pattern

```java
try (Connection conn = acquire()) {
    return conn.execute();
}
```

Bytecode contains:
- Compiler-generated `close()` call
- Suppressed exception handling
- Single exit path for resource cleanup

The try-with-resources pattern produces different bytecode structure. The compiler generates a single `close()` invocation in the generated `finally` equivalent, but adds suppressed exception logic.

## Inlining and Method Size

The JIT compiler inlines small methods. Duplicated `finally` blocks increase method size, which can prevent inlining:

- Method size threshold for inlining: typically 325 bytes (varies by JVM)
- Each copy of a `finally` block adds to this total
- A method just under the threshold may exceed it after `finally` duplication

When inlining is prevented, method call overhead increases. The JVM must perform full call setup and teardown for each invocation.

## String Concatenation in finally

String concatenation in `finally` blocks creates temporary `StringBuilder` and `String` objects:

```java
finally {
    log("cleaned up " + resource.getName());
}
```

The concatenated string allocates:
- `StringBuilder` object
- Intermediate `char[]` or `byte[]` arrays
- Final `String` object

When `finally` is duplicated across exit paths, each copy generates its own concatenation objects. This multiplies GC pressure proportionally to the number of exit paths.

## Nested try-finally Chains

Nested `try-finally` structures multiply bytecode duplication exponentially:

```java
try {
    try {
        try {
            return compute();
        } finally {
            cleanup1();
        }
    } finally {
        cleanup2();
    }
} finally {
    cleanup3();
}
```

Each outer `try-finally` wraps the inner structure. The cleanup methods are called in reverse order. The bytecode for each cleanup appears at every exit point of the inner block.

For N nested `finally` blocks, the cleanup bytecode appears at all exit points of the innermost `try`. If the innermost `try` has M exit points, the total cleanup bytecode copies is N × M.

## Code Cache Exhaustion

In applications with many classes, duplicated `finally` bytecode contributes to code cache exhaustion:

- Default code cache size: 240 MB
- Each duplicated `finally` block consumes code cache space
- Classes with large `finally` blocks in hot methods fill the cache faster

When the code cache fills, the JVM disables JIT compilation entirely, falling back to interpreted execution. This causes significant performance degradation.

## Practical Mitigation

### Extract finally Logic to Helper Methods

```java
// Before: inlined finally
try {
    return process();
} finally {
    if (connection != null) {
        connection.close();
        log("closed");
        releasePort();
    }
}

// After: extracted helper
try {
    return process();
} finally {
    closeQuietly(connection);
}

private void closeQuietly(Connection conn) {
    if (conn != null) {
        conn.close();
        log("closed");
        releasePort();
    }
}
```

The helper method is called once and compiled once. The JIT compiler inlines it if small enough, but does not duplicate the compiled code.

### Keep finally Blocks Minimal

```java
// Bad: large finally block
try {
    return compute();
} finally {
    // 50 lines of cleanup code
}

// Good: minimal finally, delegate to helper
try {
    return compute();
} finally {
    cleanup();
}
```

### Use try-with-resources for AutoCloseable

```java
// Instead of manual finally cleanup
try (var stream = Files.lines(path)) {
    return stream.collect(toList());
}
```

The compiler handles cleanup with less bytecode duplication than manual `finally`.

## Measuring Code Cache

Monitor code cache with JVM flags:

```
-XX:+PrintCodeCache         — print code cache usage
-XX:ReservedCodeCacheSize=  — set code cache size
```

A method with duplicated `finally` blocks shows higher code cache usage than the same logic extracted to helper methods.

## Summary

The `finally` block causes bytecode duplication at every exit point of the `try` block. This duplication increases code cache usage, can prevent JIT inlining, and multiplies concatenation overhead. Extracting cleanup logic to helper methods and using try-with-resources reduces these memory effects.
