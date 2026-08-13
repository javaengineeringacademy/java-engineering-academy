# Production Patterns — Memory Behavior

## Exception Object Lifecycle

Production exception handling directly impacts memory:

```
throw new ServiceException("payment failed")
  → Heap allocation: ~500 bytes (object + message)
  → Stack trace: ~2-5KB (fillInStackTrace)
  → Suppressed: ~500 bytes each (if present)
  → GC: collected after catch block completes
```

## Common Production Memory Issues

### 1. Exception Logging Creates Copies

```java
// BAD: Each log creates a new String representation
log.error("Error: " + exception.toString());  // allocates String

// BETTER: Use parameterized logging
log.error("Error: {}", exception.getMessage());  // lazy evaluation
```

### 2. Exception in Tight Loop

```java
// BAD: Creates 1M exceptions/sec in error path
for (int i = 0; i < 1_000_000; i++) {
    try {
        process(i);
    } catch (Exception e) {
        log.error("Failed", e);  // 1M stack traces allocated
    }
}
```

### 3. Suppressed Exception Accumulation

```java
// try-with-resources can accumulate suppressed exceptions
// Each adds another object + stack trace to memory
```

## Memory-Safe Patterns

```java
// Pattern 1: Use exception pooling for high-frequency errors
private static final ValidationException INVALID_INPUT = 
    new ValidationException("invalid input");

// Pattern 2: Lazy stack trace
// Override fillInStackTrace() for performance-critical paths

// Pattern 3: Exception batching
// Collect multiple errors, throw one exception with all details
```

## Monitoring Memory

```
JVM flags to monitor exception memory:
-XX:+PrintGCDetails          // GC pauses from exception allocation
-XX:+UnlockDiagnosticVMOptions
-XX:+LogCompilation          // JIT handling of exception paths
```

## Key Insight

In production, the memory cost of exceptions is dominated by `fillInStackTrace()`. For high-throughput systems, minimize exception creation or override stack trace generation.
