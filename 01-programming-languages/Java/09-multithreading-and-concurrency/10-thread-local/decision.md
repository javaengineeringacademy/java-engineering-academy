# ThreadLocal Decision Guide

## ThreadLocal vs Passing Parameters

| Aspect | ThreadLocal | Passing Parameters |
|--------|-------------|-------------------|
| Thread safety | Automatic | Manual |
| API cleanliness | No parameter clutter | Parameters in signatures |
| Memory | Per-thread overhead | Minimal |
| Cleanup | Must call remove() | Automatic |

## When to Use ThreadLocal

| Use Case | Example |
|----------|---------|
| User context in web apps | User ID, roles |
| SimpleDateFormat | Non-thread-safe formatter |
| Database connections | Per-thread connection |
| Transaction context | Current transaction |

## Cleanup Rules

```java
try {
    threadLocal.set(value);
    // use value
} finally {
    threadLocal.remove(); // ALWAYS do this
}
```
