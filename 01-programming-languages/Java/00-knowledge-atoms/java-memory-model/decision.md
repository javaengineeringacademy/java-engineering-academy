# Decision Guide: Java Memory Model

## When to Use
- When sharing mutable state between threads
- When implementing thread-safe singletons (double-checked locking)
- When designing concurrent algorithms that require memory visibility guarantees

## When NOT to Use
- Single-threaded code — JMM concerns do not apply
- When using immutable objects — safe publication is guaranteed by final fields
- When using concurrent collections — they handle visibility internally

## Trade-offs
| Mechanism | Visibility | Atomicity | Overhead | Use Case |
|-----------|-----------|-----------|----------|----------|
| volatile | Yes | No | Low | State flags, DCL |
| synchronized | Yes | Yes | Medium | Compound operations |
| AtomicInteger | Yes | Yes | Low-Medium | Counters, accumulators |
| ConcurrentHashMap | Yes | Per-operation | Medium | Thread-safe maps |

## Expert Recommendation
Use the simplest mechanism that provides the required guarantees: `volatile` for flags, `synchronized` for compound operations, `Atomic*` for counters, and immutable objects for shared data.
