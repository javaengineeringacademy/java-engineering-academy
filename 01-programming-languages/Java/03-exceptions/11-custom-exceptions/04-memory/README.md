# Custom Exceptions — Memory Behavior

## Instance Size

A custom exception's memory footprint includes:

```
ValidationException instance:
┌─────────────────────────────────┐
│ Object header           16 bytes│
│ Throwable cause ref      8 bytes│
│ Message String ref       8 bytes│
│ StackTraceElement[] ref  8 bytes│
│ Suppressed exceptions    8 bytes│
│ fieldName (String ref)   8 bytes│ ← your field
├─────────────────────────────────┤
│ Total:                  56 bytes│ (minimum, no message)
└─────────────────────────────────┘
```

## With Message + Stack Trace

```
With message "invalid email":
  Object header:       16 bytes
  Message:            ~48 bytes (String object + char[])
  fieldName:          ~48 bytes (String object + char[])
  Stack trace:     1,000-5,000 bytes (50 frames × ~80 bytes each)
  Cause chain:       ~500 bytes (if present)
  ─────────────────────────────
  Total:          1,600-5,600 bytes per exception instance
```

## Comparison

| Exception Type | Without Message | With Message | With Full Trace |
|---------------|----------------|--------------|-----------------|
| NPE (JDK) | ~56 bytes | ~104 bytes | ~1-5KB |
| Your custom | ~64 bytes | ~112 bytes | ~1-5KB |

The extra 8 bytes is your `fieldName` field. Everything else is identical.

## Object Allocation

```java
throw new ValidationException("email", "bad format");
// 1. Allocate ValidationException on heap: ~56 bytes
// 2. Allocate "email" String: ~48 bytes
// 3. Allocate "bad format" String: ~48 bytes
// 4. fillInStackTrace(): ~1-5KB
// Total: ~1.5-5.5KB per throw
```

## Key Insight

Custom exceptions add only the size of your custom fields (~8 bytes per field). The rest of the memory cost is identical to any JDK exception.
