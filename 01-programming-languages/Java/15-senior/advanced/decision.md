# Advanced Java: Decision Guide

## When to Use Advanced Concurrency Patterns

### CompletableFuture vs Blocking I/O

| Criteria | CompletableFuture | Blocking I/O |
|----------|------------------|--------------|
| Thread usage | Non-blocking, few threads | One thread per operation |
| Scalability | High (thousands of ops) | Limited by thread pool |
| Code complexity | Higher (composition chains) | Lower (sequential) |
| Debugging | Harder (async stack traces) | Easier (linear flow) |

**Use CompletableFuture when:** You need to orchestrate multiple independent async operations (e.g., fan-out to multiple services, aggregate results).

**Use blocking I/O when:** Operations are sequential and latency is acceptable, or when using virtual threads.

### Virtual Threads vs Platform Threads

| Criteria | Virtual Threads | Platform Threads |
|----------|----------------|-----------------|
| Memory per thread | ~1KB | ~1MB |
| Max threads | Millions | Thousands |
| Blocking cost | Cheap (carrier thread freed) | Expensive (thread held) |
| CPU-bound work | Same performance | Same performance |

**Use virtual threads when:** Tasks involve I/O blocking (HTTP calls, database queries, file I/O).

**Use platform threads when:** Tasks are CPU-bound or require thread-local state with specific affinity.

### Lock-Free vs Lock-Based

| Criteria | Lock-Free | Lock-Based |
|----------|-----------|------------|
| Throughput | Higher under contention | Lower under contention |
| Complexity | Very high (CAS loops) | Moderate (synchronized) |
| Fairness | No guarantees | Can be fair (ReentrantLock) |
| ABA problem | Possible | Not applicable |

**Use lock-free when:** You need maximum throughput on simple atomic operations (counters, flags, queues).

**Use lock-based when:** You need complex critical sections or fairness guarantees.

## Architecture Decision Records (ADR) Template

```markdown
# ADR-[number]: [title]

## Status
[Proposed | Accepted | Deprecated | Superseded by ADR-xxx]

## Context
[What is the issue that we're seeing that motivates this decision?]

## Decision
[What is the change that we're proposing and/or doing?]

## Consequences
[What becomes easier or more difficult to do because of this change?]

## Alternatives Considered
[What other options were evaluated?]
```

## Trade-off: Data Access Patterns

| Pattern | Consistency | Latency | Complexity | Best For |
|---------|-------------|---------|------------|----------|
| Synchronous REST | Strong | High | Low | Simple CRUD |
| Event-driven | Eventual | Low | High | High throughput |
| CQRS | Eventual (read) | Low | High | Read-heavy systems |
| Saga | Eventual | Medium | High | Distributed transactions |

## Further Reading

- *Java Concurrency in Practice* by Brian Goetz
- *Effective Java* by Joshua Bloch (Chapter 8: Lambdas and Streams)
- [Virtual Threads JEP](https://openjdk.org/jeps/444)
- [Structured Concurrency JEP](https://openjdk.org/jeps/462)
