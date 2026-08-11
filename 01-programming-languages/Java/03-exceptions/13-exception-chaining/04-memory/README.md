# Exception Chaining — Memory Behavior

## Chained Exception Memory Layout

Each exception in a chain allocates separately on the heap:

```
Primary Exception (DataLoadException)
┌──────────────────────────────┐
│ Object header:       16 bytes│
│ Message:             48 bytes│
│ Stack trace:      2,000 bytes│ (50 frames)
│ Cause ref:            8 bytes│ → points to ↓
├──────────────────────────────┤
│ Total:            ~2,072 bytes│
└──────────────┬───────────────┘
               │
Cause Exception (DatabaseException)
┌──────────────────────────────┐
│ Object header:       16 bytes│
│ Message:             48 bytes│
│ Stack trace:      2,000 bytes│ (separate trace!)
│ Cause ref:            8 bytes│ → null
├──────────────────────────────┤
│ Total:            ~2,072 bytes│
└──────────────────────────────┘

Total for chain: ~4,144 bytes
```

## Double Stack Trace Cost

```java
try {
    throw new DatabaseException("connection refused");
} catch (DatabaseException e) {
    throw new DataLoadException("load failed", e);
    // Both exceptions have separate stack traces!
    // Primary: fillInStackTrace() → 2KB
    // Cause:   fillInStackTrace() → 2KB
    // Total:   4KB just for traces
}
```

## Chain Depth Cost

| Chain Length | Memory |
|-------------|--------|
| 1 (no cause) | ~2KB |
| 2 (one cause) | ~4KB |
| 3 (two causes) | ~6KB |
| N | ~2N KB |

## Mitigation

```java
// Lazy stack trace for cause (override fillInStackTrace)
public class LightweightChainedException extends Exception {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // skip trace for this exception
    }
}
```

## Key Insight

Exception chaining doubles the memory cost per level because each exception has its own stack trace. Keep chains shallow in memory-sensitive paths.
