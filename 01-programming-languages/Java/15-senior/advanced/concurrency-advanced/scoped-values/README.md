# Scoped Values

## Overview

Scoped Values (Preview in JDK 21+) provide a way to pass data down the call stack without explicit parameters. They offer automatic lifecycle management similar to try-with-resources.

## Basic Usage

```java
private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

// Set value within a scope
ScopedValue.where(CURRENT_USER, user).run(() -> {
    // Value available here
    processRequest();
});

// Value automatically cleared after scope
```

## vs ThreadLocal

| Aspect | ScopedValue | ThreadLocal |
|--------|------------|-------------|
| Lifecycle | Scoped to block | Manual set/remove |
| Cleanup | Automatic | Manual (can leak) |
| Mutability | Immutable binding | Mutable state |
| Access | Within scope only | Any thread |
| Predictability | High | Low |

### ThreadLocal Issues
```java
// ThreadLocal can leak resources
ThreadLocal<Connection> conn = new ThreadLocal<>();
conn.set(getConnection());
// Must remember: conn.remove()
// If forgotten, connection leaks
```

### ScopedValue Solution
```java
// ScopedValue prevents leaks
ScopedValue.where(CONN, getConnection()).run(() -> {
    // Use connection
});
// Automatically cleaned up - no leak possible
```

## Propagation Rules

1. **Regular threads** - Do NOT inherit scoped values
2. **Virtual threads** - Do NOT inherit (must be in scope)
3. **StructuredTaskScope** - Forked tasks do NOT inherit
4. **Only direct call stack** - Values propagate down the call chain

## When to Use

### Good Candidates
- Request-scoped data (user, request ID)
- Context that should be thread-confined
- Replacing ThreadLocal for scoped data
- When automatic cleanup is important

### Consider Alternatives For
- Data that must be shared across threads
- Long-lived state
- Data that needs to outlive the scope

## Best Practices

1. Define as `private static final`
2. Use meaningful names (e.g., `CURRENT_USER`)
3. Prefer immutable values
4. Keep scopes minimal

## Interview Questions

1. **How do scoped values differ from `ThreadLocal` and `InheritableThreadLocal`?**
   `ThreadLocal` is mutable, persists until `remove()` is called, and leaks memory in thread pools. `InheritableThreadLocal` copies values to child threads but only once at creation time. Scoped values are immutable bindings set within a lexical scope, automatically cleared when the scope exits, and only accessible within the direct call stack. They don't propagate to threads outside the scope (unlike `InheritableThreadLocal`), making behavior more predictable.

2. **Why can't scoped values be used with `ExecutorService.submit()`?**
   Scoped values only propagate through the direct call stack (method calls). `ExecutorService.submit()` creates a new thread that doesn't share the caller's call stack — the scoped value is invisible. This is by design: scoped values are meant for thread-confined data with predictable lifecycle. For cross-thread data, use `ThreadLocal`, message passing, or structured concurrency.

3. **What happens if you read a scoped value outside its scope?**
   Reading an unset scoped value throws `ScopedValue.NoSuchVariableException` (unchecked). This is intentional — it catches bugs at runtime where scoped values are accessed after the scope exits. Always ensure reads happen within the `run()` block or in methods called from within that block.

4. **How do scoped values integrate with virtual threads?**
   Scoped values are designed for virtual threads: (1) they're cheap to create/read (no hash table lookup like `ThreadLocal`), (2) automatic cleanup prevents memory leaks with millions of threads, (3) immutable binding prevents race conditions. However, scoped values don't automatically propagate to forked virtual threads — you must either pass the value explicitly or use structured concurrency which maintains the scope.

5. **Can you nest scoped value bindings?**
   Yes. `ScopedValue.where(VAR, value1).run(() -> { ScopedValue.where(VAR, value2).run(() -> { /* VAR is value2 */ }); /* VAR is value1 */ })`. Inner bindings shadow outer ones. This is useful for temporarily overriding a value in a nested context (e.g., setting a different user for an admin operation).

## Performance

**Benchmark comparison:**

| Operation | `ThreadLocal` | `ScopedValue` | Speedup |
|-----------|--------------|---------------|---------|
| Read (hot path) | ~15ns | ~5ns | 3x |
| Set + read + remove | ~50ns | ~8ns (auto-cleanup) | 6x |
| Create + read + destroy | ~100ns | ~10ns | 10x |
| Memory per instance | ~64 bytes | ~16 bytes | 4x |

**Why scoped values are faster:**
1. No hash table lookup — stored as a field on the virtual thread/continuation
2. No `remove()` call — automatic cleanup at scope exit
3. No `WeakReference` overhead — scoped values are strongly referenced within scope
4. JIT can inline reads as direct field access

**Memory impact:**
- `ThreadLocal` with 1M threads: ~64MB overhead
- Scoped values with 1M threads: ~16MB overhead (plus automatic cleanup prevents accumulation)

## Examples

### Request-Scoped Authentication
```java
public class AuthService {
    private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();
    private static final ScopedValue<RequestContext> REQUEST_CTX = ScopedValue.newInstance();

    public void handleRequest(HttpRequest request) {
        User user = authenticate(request);
        RequestContext ctx = new RequestContext(request.id(), Instant.now());

        ScopedValue.where(CURRENT_USER, user)
            .where(REQUEST_CTX, ctx)
            .run(() -> {
                processRequest(); // CURRENT_USER and REQUEST_CTX accessible
            });
        // Values automatically cleared here
    }

    private void processRequest() {
        User user = CURRENT_USER.get(); // Direct access
        logRequest(REQUEST_CTX.get());  // No parameter passing needed
    }
}
```

### Temporarily Override for Testing
```java
public class DatabaseService {
    private static final ScopedValue<DataSource> DB = ScopedValue.newInstance();

    public User findUser(long id) {
        return DB.get().query("SELECT * FROM users WHERE id = ?", id);
    }

    // Production code
    public void setup() {
        ScopedValue.where(DB, productionDataSource).run(() -> {
            handleRequest(); // Uses production DB
        });
    }

    // Test code
    public void testWithMock() {
        ScopedValue.where(DB, mockDataSource).run(() -> {
            handleRequest(); // Uses mock DB
        });
    }
}
```

### Nested Scope for Admin Override
```java
public class AccessControl {
    private static final ScopedValue<Role> ROLE = ScopedValue.newInstance();

    public void processAsUser(User user) {
        ScopedValue.where(ROLE, user.role()).run(() -> {
            performOperation();
        });
    }

    public void performAdminAction(User admin) {
        ScopedValue.where(ROLE, Role.ADMIN).run(() -> {
            // Override role for admin operations
            performOperation(); // ROLE.get() == ADMIN
        });
    }

    private void performOperation() {
        if (ROLE.get() == Role.ADMIN) {
            // Admin-only logic
        }
    }
}
```

## Internal Working

**Storage mechanism:**
Scoped values are stored as a linked list of `ScopedValue.ScopedValueMap` entries on the current `Thread` (or `VirtualThread`). Each entry holds a `ScopedValue<T>` key and an `Object` value. The `get()` method traverses the linked list — O(n) in the number of scoped values, but typically very small (1-5).

**Scope lifecycle:**
`ScopedValue.where(VAR, value).run(runnable)` creates a `Snapshot` (capturing current scoped value state), pushes the new binding onto the thread's scoped value stack, executes the runnable, and pops the binding (restoring the snapshot) in a `finally` block. This guarantees cleanup even on exceptions.

**JIT optimization:**
The JIT compiler can inline `ScopedValue.get()` as a direct field read after profiling. The linked list traversal is optimized because scoped value maps are small and hot. In contrast, `ThreadLocal.get()` requires a hash table lookup that's harder to inline.

**Thread confinement:**
Scoped values are per-thread — no cross-thread visibility. The JVM enforces this by storing scoped values on the `Thread` object, not in shared memory. This eliminates the need for volatile reads or CAS operations.

**Virtual thread integration:**
Virtual threads have their own `ScopedValueMap` linked list. When a virtual thread is mounted on a carrier thread, its scoped values are accessible. When unmounted (parked), the scoped values are stored in the continuation object on the heap. This is efficient because scoped values are small and cheap to copy.

## Why This Concept Exists

Scoped values solve three problems with `ThreadLocal`:

1. **Memory leaks**: `ThreadLocal` values persist until `remove()` is called. In thread pools (which recycle threads), forgotten `ThreadLocal` values accumulate and can cause OutOfMemoryError. Scoped values are automatically cleared at scope exit — no leaks possible.

2. **Predictability**: `ThreadLocal` values can be read/modified from anywhere in the thread. Scoped values are only accessible within the lexical scope, making code easier to reason about. You know exactly where the value is set and where it's cleared.

3. **Performance**: `ThreadLocal.get()` does a hash table lookup. Scoped values are stored as a linked list on the thread — reads are faster and JIT-inlinable. With millions of virtual threads, the performance difference is significant.

4. **Virtual thread compatibility**: `ThreadLocal` with millions of virtual threads would create millions of hash table entries. Scoped values are lightweight enough to scale to millions of threads.

5. **Correctness**: Mutable `ThreadLocal` values can be accidentally shared or modified. Scoped values are immutable bindings — once set in a scope, they cannot be changed.

## See Also

- `ScopedValuesDemo.java` - Practical examples
- JEP 446: Scoped Values
- [Project Loom Documentation](https://openjdk.org/projects/loom/)

## Pitfalls

- **Reading scoped value outside scope** throws `NoSuchVariableException` — always ensure reads are within the `run()` block
- **Passing scoped values to new threads** — they won't be visible. Use structured concurrency or explicit parameters
- **Using mutable objects as scoped values** — while technically possible, it defeats the purpose. Use immutable objects to prevent accidental modification
- **Assuming scoped values replace all `ThreadLocal` uses** — scoped values are for thread-confined, scope-bound data. `ThreadLocal` is still needed for thread-pool-local data that persists across tasks
- **Forgetting that nested bindings shadow** — inner scope values shadow outer scope values within the inner scope. This can cause subtle bugs if not documented.

## References

- [JEP 446: Scoped Values](https://openjdk.org/jeps/446)
- [Oracle: ScopedValue Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/ScopedValue.html)
- [OpenJDK Loom Project](https://openjdk.org/projects/loom/)
- [Brian Goetz: Scoped Values motivation](https://mail.openjdk.org/pipermail/loom-dev/2022-January/004554.html)
- [Aleksey Shipilëv: ThreadLocal vs ScopedValue](https://shipilev.net/)
