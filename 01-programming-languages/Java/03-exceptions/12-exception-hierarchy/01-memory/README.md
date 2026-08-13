# Exception Hierarchy — Memory Behavior

## Class Metadata Size

Each exception class in the hierarchy loads metadata into the JVM:

```
Class metadata per exception class:
  Class object:           ~200 bytes
  Method table:           ~50 bytes
  Constant pool:         ~100 bytes
  Field descriptors:      ~30 bytes
  ──────────────────────────────
  Total per class:       ~380 bytes
```

For a typical exception hierarchy (Throwable → Exception → RuntimeException → NPE):
```
4 classes × ~380 bytes = ~1,520 bytes of metadata
```

## Exception Instance Size

Same as any object hierarchy:

```
NullPointerException instance:
  Object header:       16 bytes
  Throwable fields:    24 bytes (cause, message, trace, suppressed)
  RuntimeException:     0 bytes (no extra fields)
  NullPointerException: 0 bytes (no extra fields)
  ─────────────────────────────
  Total:               40 bytes minimum
```

## Hierarchy Depth Cost

| Depth | instanceof Cost | Memory |
|-------|----------------|--------|
| 1 (Throwable) | 1 comparison | 0 |
| 2 (Exception) | 2 comparisons | 0 |
| 3 (RuntimeException) | 3 comparisons | 0 |
| 4 (NPE) | 4 comparisons | 0 |

Deeper hierarchies cost more CPU (more comparisons) but no extra memory.

## Key Insight

The exception hierarchy's memory cost is in class metadata (loaded once per class) and instance fields (inherited from parents). The hierarchy depth adds CPU cost for `instanceof` but no memory cost.
