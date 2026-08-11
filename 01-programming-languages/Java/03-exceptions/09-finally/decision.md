# Decision Guide: When to Use Finally

## Use finally when:

1. **Non-Closeable resources** — `Lock.lock()`/`unlock()`, `ThreadLocal` removal, `Timer` stop
2. **Legacy code** — Pre-Java 7 codebases without TWR support
3. **Multi-scope cleanup** — Cleanup spans try and catch blocks
4. **Metrics and timing** — Record duration regardless of success/failure
5. **Flag/state reset** — Restore invariants that aren't tied to AutoCloseable

## Do NOT use finally when:

1. **`AutoCloseable` resources** — Use try-with-resources instead
2. **`return` in finally** — Always a bug; compiler warns but compiles
3. **Cleanup that can throw** — Wrap in try-catch inside finally to avoid masking exceptions
4. **Redundant cleanup after TWR** — TWR already closes the resource

## Decision Tree

```
Does the resource implement AutoCloseable?
├── Yes → Use try-with-resources
└── No
    ├── Does it need cleanup?
    │   ├── Yes → Use finally
    │   └── No → No special handling needed
    └── Is cleanup tied to success/failure?
        ├── Yes → Finally with conditional logic
        └── No → Always-finally (e.g., timer stop)
```

## Common Patterns

### Pattern 1: Lock management
```java
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

### Pattern 2: Thread-local cleanup
```java
context.set(value);
try {
    process();
} finally {
    context.remove();
}
```

### Pattern 3: Timer / metrics
```java
long start = System.nanoTime();
try {
    execute();
} finally {
    recordDuration(System.nanoTime() - start);
}
```

### Pattern 4: Legacy resource cleanup
```java
Connection conn = null;
try {
    conn = getConnection();
    // work
} finally {
    if (conn != null) {
        try { conn.close(); } catch (SQLException ignored) {}
    }
}
```

## Anti-patterns to Avoid

1. **Empty finally** — Wasted code, remove it
2. **Return in finally** — Overrides try return, always a bug
3. **Throw in finally without catch** — Masks try exception
4. **Using finally for AutoCloseable** — Use TWR instead
5. **Duplicating cleanup in catch and finally** — Put it in finally only

## Java Version Considerations

- Java 5+: `finally` available (core exception handling)
- Java 7+: Try-with-resources for AutoCloseable (prefer over finally)
- Java 9+: TWR with effectively final variables (even more TWR cases)

## Performance Notes

- `finally` compiles to duplicated bytecode at every exit point
- No runtime overhead compared to equivalent try-catch chains
- The JVM does not optimize away `finally` — it always runs
