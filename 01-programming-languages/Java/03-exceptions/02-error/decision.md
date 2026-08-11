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