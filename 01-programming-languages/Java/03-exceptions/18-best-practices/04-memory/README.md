# Best Practices — Memory Behavior

## The Real Memory Cost of Bad Practices

```
Bad Practice                          Memory Cost
─────────────────────────────────────────────────
Empty catch block                     0 (but hides bugs)
catch (Exception e) {}               0 (but catches everything)
throwing in loop                      O(n × stack_trace_size)
String concatenation in message      O(message_length)
e.printStackTrace()                  O(stack_trace_size) to stderr
```

## Stack Trace: The Hidden Killer

```java
// This single line allocates ~2-5KB
throw new RuntimeException("error");

// This allocates ~2-5KB per iteration
for (int i = 0; i < 1000000; i++) {
    try {
        riskyOperation();
    } catch (Exception e) {
        // Each log statement allocates a string representation
        System.err.println(e);  // ~100 bytes String allocation
    }
}
```

## Memory-Safe Best Practices

### 1. Pre-allocate Common Exceptions

```java
// BAD
throw new IllegalArgumentException("invalid id");

// BETTER (if thrown frequently)
private static final IllegalArgumentException INVALID_ID = 
    new IllegalArgumentException("invalid id");
throw INVALID_ID;  // reuse same instance
```

### 2. Lazy Message Construction

```java
// BAD: Message constructed even if not thrown
throw new Exception("User " + user.getName() + " not found");

// BETTER: Use Supplier (Java 8+)
// Only constructs message if exception is actually created
```

### 3. Override fillInStackTrace

```java
public class LightweightException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // skip stack trace — saves ~2-5KB
    }
}
```

## Profiling Exceptions

```
Java Flight Recorder events:
- jdk.Exception  — every exception thrown
- jdk.Thrown     — exception object allocation

Use to find:
- Hot exception paths
- Unnecessary exception creation
- Memory-hungry patterns
```

## Key Insight

Every best practice about exceptions is also a memory practice. The biggest wins come from avoiding unnecessary exception creation and minimizing stack trace overhead.
