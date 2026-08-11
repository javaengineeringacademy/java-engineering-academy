# Suppressed Exceptions — Memory Behavior

## Suppressed Exception Memory Layout

```
Primary Exception
┌──────────────────────────────────┐
│ Object header:           16 bytes│
│ Message:                 48 bytes│
│ Stack trace:          2,000 bytes│
│ Cause ref:                8 bytes│
│ Suppressed list ref:      8 bytes│ → points to ↓
├──────────────────────────────────┤
│ Total:                ~2,080 bytes│
└──────────────┬───────────────────┘
               │
Suppressed[] array
┌──────────────────────────────────┐
│ Array header:           16 bytes│
│ Element 0 ref:           8 bytes│ → points to ↓
│ Element 1 ref:           8 bytes│ → (if present)
├──────────────────────────────────┤
│ Total:                   32 bytes│
└──────────────┬───────────────────┘
               │
Suppressed Exception
┌──────────────────────────────────┐
│ Object header:           16 bytes│
│ Message:                 48 bytes│
│ Stack trace:          2,000 bytes│
├──────────────────────────────────┤
│ Total:                ~2,064 bytes│
└──────────────────────────────────┘
```

## Try-with-Resources Accumulation

```java
// Each close() that fails adds a suppressed exception
try (Resource1 r1 = new Resource1();   // fails on close
     Resource2 r2 = new Resource2();   // fails on close
     Resource3 r3 = new Resource3()) { // fails on close
    throw new RuntimeException("primary");
}
// Result: 1 primary + 3 suppressed = ~8.5KB total
```

## Cost Per Suppressed

| Component | Size |
|-----------|------|
| Exception object | ~56 bytes |
| Message string | ~48 bytes |
| Stack trace | ~2KB |
| **Total per suppressed** | **~2.1KB** |

## Memory-Safe Pattern

```java
// For high-frequency paths with try-with-resources
// Override fillInStackTrace on suppressed exceptions
public class LightweightResource implements Closeable {
    @Override
    public void close() {
        // Minimal exception creation
    }
}
```

## Key Insight

Each suppressed exception adds ~2KB due to its own stack trace. In nested try-with-resources with multiple failures, memory usage multiplies quickly.
