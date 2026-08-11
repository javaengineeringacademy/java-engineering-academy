# Decision Tree: Error Subtypes

## Decision Flow

```
An Error is thrown
    │
    ├── Is it OutOfMemoryError?
    │       │
    │       ├── Yes → Is it heap space?
    │       │         ├── Yes → Enable heap dump, analyze memory usage
    │       │         └── No → Check Metaspace, native memory config
    │       │
    │       └── Is it GC overhead limit exceeded?
    │                 ├── Yes → Application is spending >98% time in GC
    │                 └── No → Check memory pool configuration
    │
    ├── Is it StackOverflowError?
    │       │
    │       ├── Yes → Review recursion logic
    │       │         ├── Add base case
    │       │         └── Increase stack size (-Xss) as temporary fix
    │       │
    │       └── No → Check thread configuration
    │
    ├── Is it NoClassDefFoundError?
    │       │
    │       ├── Yes → Is class file present at runtime?
    │       │         ├── No → Fix classpath, include dependency
    │       │         └── Yes → Check for class initialization failure
    │       │
    │       └── No → Check linkage configuration
    │
    ├── Is it ClassFormatError?
    │       │
    │       ├── Yes → Class file is corrupted
    │       │         ├── Recompile the source
    │       │         └── Verify file transfer integrity
    │       │
    │       └── No → Check class loading mechanism
    │
    ├── Is it AssertionError?
    │       │
    │       ├── Yes → Assertion failed in code
    │       │         ├── Fix the logic error
    │       │         └── Enable assertions (-ea flag) if not running
    │       │
    │       └── No → Review assertion usage
    │
    └── Any other Error?
            │
            ├── Log the error with full context
            ├── Capture thread dump
            ├── Trigger graceful shutdown
            └── Alert operations team
```

## When to Let Propagate

| Error Type | Action |
|------------|--------|
| `OutOfMemoryError` | Let propagate, enable heap dump, shutdown |
| `StackOverflowError` | Let propagate, fix recursion |
| `NoClassDefFoundError` | Let propagate, fix classpath |
| `ClassFormatError` | Let propagate, recompile/redeploy |
| `AssertionError` | Let propagate, fix logic |
| `ThreadDeath` | Let propagate, never catch |
| `VirtualMachineError` | Let propagate, shutdown |
| `LinkageError` | Let propagate, fix dependencies |
| `ExceptionInInitializerError` | Let propagate, check static initializer |

## When Catching Is Acceptable

| Scenario | Reason |
|----------|--------|
| Container/framework shutdown | Log and clean up before exit |
| OOM with cache eviction | Release cached data, retry once |
| Thread pool exhaustion | Reject task, maintain pool |
| Class loading in plugin systems | Graceful degradation, skip plugin |

## Key Principle

When in doubt, let it propagate. The cost of swallowing an Error is almost always higher than the cost of a clean restart.

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Letting Error propagate | JVM state is visible; clean restart | Service downtime; ungraceful shutdown |
| Catching OOM for cache eviction | Saves the request; graceful degradation | Risk of continued operation in compromised JVM state |
| Catching ExceptionInInitializerError | Application can skip faulty module | Partial initialization may cause undefined behavior |
| Wrapping Error in domain exception | Cleaner API for callers | Hides JVM-level failure; caller may try to recover from fatal condition |

## Common Code Review Comments

- "Never catch `OutOfMemoryError` unless you're releasing a cache — and even then, only once."
- "This is an `Error`, not an `Exception` — let it propagate and fix the root cause."
- "Don't wrap `StackOverflowError` in a domain exception — fix the recursion."
- "If you catch `Error`, you must document why — and your exception handler must be extremely careful."
- "Catching `AssertionError` silently hides logic bugs — enable assertions in CI instead."

## Common Production Mistakes

- **Catching OutOfMemoryError and continuing**: The JVM has failed internal invariants. Catching it without releasing significant memory guarantees a second, worse OOM. Always shut down after OOM unless you are a managed cache.
- **Swallowing StackOverflowError**: Hides infinite recursion bugs. The stack is corrupted; continuing execution is undefined behavior.
- **Catching LinkageError**: Class loading failures mean the classpath is broken. Catching and continuing leads to `NoClassDefFoundError` at unpredictable later points.
- **Not enabling assertions in production**: `AssertionError` is thrown by `assert` statements — if assertions are disabled (default), logic bugs go undetected until they cause data corruption.

## When to Escalate

- You are implementing a custom class loader or module system and need to handle `LinkageError` / `NoClassDefFoundError`.
- A production system keeps hitting `OutOfMemoryError` — this requires architect-level heap analysis and potentially redesigning memory usage patterns.
- You are building a plugin system that must handle `ExceptionInInitializerError` — the initialization semantics need architectural review.
- You need to decide whether a condition is an `Error` or a checked `Exception` — this affects the entire system's error handling contract.