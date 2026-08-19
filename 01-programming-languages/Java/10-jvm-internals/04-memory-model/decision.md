# 04. Memory Model - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Debugging concurrency issues (visibility, ordering) | **Must** |
| Writing multi-threaded code with shared state | **Must** |
| Understanding happens-before relationships | **Must** |
| Working with java.util.concurrent | **Should** |
| Implementing lock-free algorithms | **Should** |
| Diagnosing memory visibility bugs | **Should** |
| Single-threaded applications | **Nice to have** |

## When This Knowledge is Essential

- **Concurrency bugs**: Memory visibility and ordering issues are the hardest bugs to diagnose
- **JMM compliance**: Understanding happens-before is required for correct concurrent code
- **Volatile and synchronized**: Knowing what memory guarantees these provide
- **java.util.concurrent internals**: Locks, atomics, and concurrent collections rely on JMM
- **Lock-free programming**: CAS operations and memory barriers require JMM knowledge

## When This Knowledge is Less Critical

- Single-threaded applications
- Code using only high-level concurrent utilities without custom synchronization
- Applications with simple thread pool patterns using CompletableFuture

## Key Decision Points

| Decision | JMM Knowledge Impact |
|----------|---------------------|
| Using volatile vs synchronized | Different memory visibility guarantees |
| Choosing between AtomicInteger and synchronized | Performance vs correctness trade-offs |
| Implementing publish-subscribe patterns | Requires happens-before understanding |
| Debugging mysterious concurrency bugs | JMM explains why "it works on my machine" |
