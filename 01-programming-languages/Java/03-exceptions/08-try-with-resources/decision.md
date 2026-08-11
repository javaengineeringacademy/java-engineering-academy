# Decision Guide: When to Use Try-with-Resources

## Use try-with-resources when:

1. **Working with I/O streams** — `FileInputStream`, `BufferedReader`, `Socket`, `Channel`
2. **Database resources** — `Connection`, `Statement`, `ResultSet`
3. **Lock objects** — `Lock.lock()` / `unlock()` patterns (wrap in AutoCloseable adapter)
4. **Third-party libraries** — Any class implementing `AutoCloseable`
5. **Custom resources** — Classes you control that hold external resources

## Do NOT use try-with-resources when:

1. **Non-closeable resources** — Raw threads, sockets without close(), primitives
2. **Resources with no cleanup** — Pure computation objects
3. **Resources that outlive the scope** — Return values, long-lived singletons
4. **Manual lifecycle control** — When you need explicit ordering not covered by TWR

## Decision Tree

```
Does the resource implement AutoCloseable?
├── Yes → Use try-with-resources
└── No
    ├── Can you wrap it in an AutoCloseable adapter?
    │   ├── Yes → Use try-with-resources with wrapper
    │   └── No → Use finally block
    └── Does it need cleanup at all?
        ├── Yes → Implement close() manually in finally
        └── No → No special handling needed
```

## Exception Suppression Considerations

- If close() exceptions matter: catch and inspect suppressed exceptions
- If close() exceptions should propagate: rethrow from catch block
- If close() exceptions are unimportant: TWR handles this automatically

## Java Version Considerations

- Java 7+: Basic try-with-resources
- Java 9+: Effectively final variable support
- Java 9+: Better stack traces for suppressed exceptions

## Performance Notes

- TWR compiles to equivalent try-finally — zero runtime overhead
- Multiple resources in single try = multiple finally blocks generated
- Nested TWR = nested finally blocks (same performance)

## Common Anti-patterns

1. **Empty try body** — Just use close() directly
2. **TWR with no resources** — pointless, just use try-finally
3. **Reassigning TWR variable** — compiler error, use separate variable
4. **Catching Exception broadly** — catches both body and close exceptions

## When to Prefer finally Over TWR

```java
// When cleanup is not tied to AutoCloseable
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // not AutoCloseable
}

// When resource escapes the scope
Socket socket = createSocket();
try {
    configure(socket);
} finally {
    // Don't close — socket is returned
}
```

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| TWR over manual finally | Guaranteed close; cleaner code; suppressed exceptions handled | Cannot control close ordering for multiple resources |
| Multiple resources in single try | Concise; all close in reverse order | If close of one fails, others still close but their exceptions may be suppressed |
| TWR with wrapper adapter | Can manage non-AutoCloseable resources | Indirection; wrapper must handle edge cases correctly |
| Manual finally for non-AutoCloseable | Direct control; no wrapper needed | Boilerplate; risk of missing close; no automatic suppression |

## Common Code Review Comments

- "This resource implements `AutoCloseable` — use try-with-resources instead of manual finally."
- "You have a `finally` block that closes a `Connection` — that's what TWR is for."
- "Don't catch `Exception` in TWR — it catches both body and close exceptions; catch specific types."
- "TWR compiles to the same bytecode as try-finally — there's no performance reason to avoid it."
- "If you need to handle close() exceptions specially, inspect `getSuppressed()` on the caught exception."

## Common Production Mistakes

- **Ignoring suppressed exceptions**: TWR adds `close()` exceptions as suppressed — if you only log the primary exception, you miss the close failure that may be the real problem.
- **Not closing resources on exception paths**: Manual `finally` blocks that skip close when an exception occurs — resource leaks under error conditions.
- **Reassigning TWR variable**: `conn = null;` inside TWR — compiler error; use a separate variable outside TWR if you need to null it out.
- **Using TWR for resources that outlive the scope**: TWR closes at end of block, but the resource is needed after — leads to use-after-close bugs.

## When to Escalate

- You are managing resources that require specific close ordering not supported by TWR (e.g., dependent connections).
- A resource's close() method can fail in ways that affect business logic — the error handling strategy needs review.
- You are designing a custom `AutoCloseable` resource with complex lifecycle semantics.
