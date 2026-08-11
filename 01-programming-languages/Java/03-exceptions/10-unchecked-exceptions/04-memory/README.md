# Unchecked Exceptions — Memory Behavior

## Runtime Memory Profile

Unchecked exceptions have the same memory profile as checked exceptions:

| Component | Size | Notes |
|-----------|------|-------|
| Exception object | 16-32 bytes header | Standard object header |
| Message string | 40+ bytes | If message provided |
| Stack trace | 1-10KB | `fillInStackTrace()` cost |
| Cause chain | Per-cause | Each cause has its own stack trace |
| Exception table | 6 bytes/handler | Static, in class file |

## Common Subtypes

```
RuntimeException subtypes (typical sizes):
  NullPointerException:     ~16 bytes (no message)
  IllegalArgumentException: ~80 bytes (with message)
  IllegalStateException:    ~80 bytes (with message)
  ArrayIndexOutOfBoundsException: ~64 bytes (with index message)
```

## Stack Trace Cost

```java
// This is where most memory goes
RuntimeException e = new RuntimeException("error");
// fillInStackTrace() walks the entire stack
// Each stack frame contributes ~80-100 bytes to the trace
// 50-frame stack = ~4-5KB just for the trace
```

## Suppressed Exception Cost

```java
try (Resource r = new Resource()) {
    throw new RuntimeException("primary");
} catch (RuntimeException e) {
    // Suppressed exception adds another object + stack trace
    // Memory: +16 bytes header + 40 bytes message + 1-10KB trace
}
```

## Key Insight

Unchecked exceptions don't save memory compared to checked. The real memory cost is `fillInStackTrace()`, which is the same regardless of exception type.
