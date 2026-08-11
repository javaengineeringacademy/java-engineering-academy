# 03 - Internals

## Scope

This topic covers the JVM internals behind Error types — how the JVM detects and throws Errors, the mechanisms that cause them, and what happens when they occur.

## Why It Exists

Understanding Error internals helps you diagnose root causes, configure the JVM appropriately, and design systems that respond correctly when Errors occur. Knowing why an `OutOfMemoryError` happened is the difference between "the JVM ran out of memory" and "a memory leak in the session cache caused heap exhaustion."

## Design Rationale

The JVM detects Error conditions through a combination of hardware signals, runtime checks, and bytecode verification. These mechanisms are layered:

1. **Bytecode verification** — Before executing a class, the JVM verifies its bytecode. Corrupted class files fail verification and throw `ClassFormatError` or `VerifyError`.
2. **Runtime checks** — The JVM performs boundary checks on arrays, stack depth checks, and memory allocation checks during execution.
3. **GC interaction** — The garbage collector reports memory exhaustion, and the JVM translates this into `OutOfMemoryError`.
4. **Thread scheduling** — Each thread has a stack with a fixed maximum depth. The JVM checks stack depth on every method invocation.

## How OutOfMemoryError Is Thrown

When the JVM attempts to allocate an object and cannot find sufficient memory:

```
1. Object allocation request arrives
2. JVM checks available heap space
3. If insufficient → trigger GC
4. After GC, if still insufficient → throw OutOfMemoryError
```

The allocation path in HotSpot:

```
SharedRuntime::allocate_new_declared_type()
  → CollectedHeap::obj_allocate()
    → CollectedHeap::allocate_from_tlab()   // Thread-local allocation
      → CollectedHeap::allocate_new_tlab()  // TLAB exhausted
        → CollectedHeap::allocate_permanent() // Slow path
          → OutOfMemoryError is thrown
```

Different OOM messages indicate different memory regions:

| Message | Region | JVM Flag |
|---------|--------|----------|
| `Java heap space` | Heap | `-Xmx` |
| `Metaspace` | Class metadata | `-XX:MaxMetaspaceSize` |
| `Compressed class space` | Compressed class pointers | `-XX:MaxMetaspaceSize` |
| `GC overhead limit exceeded` | GC overhead | `-XX:UseGCOverheadLimit` |
| `unable to create new native thread` | OS threads | `-Xss` |
| `Direct buffer memory` | NIO direct buffers | `-XX:MaxDirectMemorySize` |

## StackOverflowError Mechanism

Each Java thread has a stack with a configurable maximum depth:

```
1. Method invocation → push frame onto stack
2. JVM checks: frame size + current stack size > max stack size?
3. If yes → throw StackOverflowError
4. If no → continue with method execution
```

The stack size is configured per thread:

| Flag | Default | Description |
|------|---------|-------------|
| `-Xss` | 512KB-1MB | Thread stack size |
| `-XX:MaxJavaStackTraceDepth` | Unlimited | Max depth in stack trace |

The stack frame size depends on the method:
- Local variables
- Method parameters
- Return address
- Operand stack depth

Deep recursion with many local variables hits the limit faster than simple recursion.

## NoClassDefFoundError vs ClassNotFoundException

Two different failure modes for class loading:

**ClassNotFoundException** (checked):
```
1. Code calls Class.forName("com.example.MyClass")
2. ClassLoader searches for the class
3. Class not found → throw ClassNotFoundException
4. Caller can catch and handle
```

**NoClassDefFoundError** (unchecked):
```
1. JVM needs to load a class during execution
2. Class definition not found
3. JVM throws NoClassDefFoundError
4. Application cannot recover
```

The key difference: `ClassNotFoundException` is an explicit lookup failure. `NoClassDefFoundError` is a linkage failure — the JVM expected the class to exist (because it was referenced in bytecode) but could not find it.

Common causes of `NoClassDefFoundError`:
- Missing JAR on classpath at runtime
- Class file corrupted after compilation
- Static initializer failed (wrapped in `ExceptionInInitializerError`)
- Class loading delegated incorrectly in custom classloaders

## ClassFormatError and Verification Errors

The JVM verifies class files before execution:

**ClassFormatError:**
```
1. Class file loaded into memory
2. JVM parses the constant pool, methods, fields
3. Structural validation fails → ClassFormatError
```

Causes:
- Truncated class file
- Incorrect magic number
- Invalid constant pool entry
- Corrupted during transfer or storage

**VerifyError:**
```
1. Class file structure valid
2. JVM performs bytecode verification
3. Bytecode violates type safety → VerifyError
```

Causes:
- Bytecode manipulation gone wrong (cglib, ASM)
- Compiler bug
- Class file manipulation with incorrect stack map frames

**UnsatisfiedLinkError:**
```
1. Native method declared (native keyword)
2. JVM looks for JNI library
3. Library not found → UnsatisfiedLinkError
```

## How the JVM Handles Fatal Errors

When an Error is uncaught, the JVM follows this sequence:

```
1. Error is thrown
2. No catch clause handles it
3. Thread's uncaught exception handler invoked
4. If no handler → default handler invoked
5. Thread terminates
6. If all non-daemon threads terminate → JVM exits
```

**Shutdown Hooks:**
```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    // Cleanup code runs before JVM exits
    // Available for normal termination and some error conditions
}));
```

Shutdown hooks run in a best-effort manner. They do not run if:
- `System.halt()` is called
- The OS kills the JVM (SIGKILL)
- A native crash occurs

**Abort Mechanism:**
```
1. Fatal error detected (SIGSEGV, SIGABRT)
2. JVM signal handler invoked
3. JVM prints error report (hs_err_pid.log)
4. JVM calls abort() — terminates without shutdown hooks
```

The `hs_err_pid.log` file contains:
- JVM version and configuration
- Signal information
- Register state
- Stack trace for all threads
- Memory map
- Heap summary
- Code cache contents

## Summary

- `OutOfMemoryError` is thrown after GC fails to reclaim sufficient heap space
- `StackOverflowError` occurs when a thread's stack exceeds its configured maximum depth
- `NoClassDefFoundError` and `ClassNotFoundException` represent different class loading failures
- `ClassFormatError` and `VerifyError` occur during class file validation
- The JVM handles fatal errors by terminating threads and, eventually, the JVM itself
- Shutdown hooks provide a best-effort cleanup mechanism
- The JVM writes diagnostic reports for native crashes