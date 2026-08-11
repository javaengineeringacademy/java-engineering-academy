# 09 - Finally Block (Part 2)
**Previous:** [Part 1](README.md)

## When to Use finally

### Use finally for non-Closeable cleanup

```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    processTransaction();
} finally {
    lock.unlock();
}
```

### Use finally for legacy code

Pre-Java 7 codebases that don't use TWR should continue using `finally` for resource cleanup until modernized.

### Use finally when cleanup spans multiple scopes

```java
void processRequest() {
    boolean started = startTimer();
    try {
        handleRequest();
    } finally {
        if (started) {
            stopTimer();
        }
    }
}
```

### Do NOT use finally when TWR suffices

For `AutoCloseable` resources, TWR is superior in every way. Using `finally` instead is legacy.

## Common Pitfalls

### 1. Return in finally

```java
// BAD — returns 2, not 1
int value() {
    try { return 1; } finally { return 2; }
}
```

**Fix:** Remove `return` from `finally`.

### 2. Exception in finally masking try exception

```java
// BAD — original exception lost
void process() {
    try {
        riskyOperation();
    } finally {
        cleanup(); // if this throws, riskyOperation's exception is lost
    }
}
```

**Fix:** Wrap `finally` body in try-catch:

```java
void process() {
    try {
        riskyOperation();
    } finally {
        try {
            cleanup();
        } catch (Exception e) {
            log.error("Cleanup failed", e);
        }
    }
}
```

### 3. Missing finally after catch

```java
// BAD — if close() throws, original exception is lost
try {
    FileInputStream fis = new FileInputStream("data.txt");
    // ... work ...
    fis.close();
} catch (IOException e) {
    // handle
}
```

**Fix:** Use TWR or move `close()` to `finally`.

### 4. Return value mutation

```java
// BAD — mutates then returns from finally
String build() {
    StringBuilder sb = new StringBuilder();
    try {
        sb.append("hello");
        return sb.toString();
    } finally {
        sb.append(" world"); // mutates, but return value is already set
    }
}
// Returns "hello", NOT "hello world"
// The return value is captured before finally runs
```

The `sb` object is mutated, but the reference to `"hello"` (immutable String) was already captured for return. This is confusing and must be avoided.

## Production Patterns

### Transaction rollback pattern

```java
public void transfer(Account from, Account to, BigDecimal amount) {
    Connection conn = null;
    try {
        conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        deduct(from, amount);
        credit(to, amount);
        conn.commit();
    } catch (SQLException e) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) { e.addSuppressed(ex); }
        }
        throw new TransferException("Transfer failed", e);
    } finally {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ex) { /* log */ }
        }
    }
}
```

### Thread-local cleanup

```java
void handleRequest() {
    currentUser.set(extractUser());
    try {
        processRequest();
    } finally {
        currentUser.remove(); // prevent memory leak
    }
}
```

### Timer / metric pattern

```java
void trackMetric(String operation) {
    long start = System.nanoTime();
    try {
        executeOperation(operation);
    } finally {
        long duration = System.nanoTime() - start;
        metrics.record(operation, duration);
    }
}
```

### Resource acquisition failure recovery

```java
void safeAcquire() {
    Resource resource = null;
    try {
        resource = acquireResource();
        useResource(resource);
    } catch (AcquisitionException e) {
        log.warn("Failed to acquire", e);
    } finally {
        if (resource != null) {
            try { resource.release(); } catch (Exception e) { /* log */ }
        }
    }
}
```

## Summary

- `finally` guarantees execution after `try` regardless of exit path
- Always run before `return`, `break`, `continue`, or exception propagation
- **Never** put `return` in `finally` — it overrides the try return value
- `finally` exceptions mask `try` exceptions (unlike TWR suppression)
- Prefer try-with-resources for `AutoCloseable` resources
- Use `finally` for non-closeable cleanup: locks, thread-locals, timers, metrics

---

**Next:** [10 - Multi-Catch](../10-multi-catch/README.md) — Handling multiple exception types in a single catch block.
