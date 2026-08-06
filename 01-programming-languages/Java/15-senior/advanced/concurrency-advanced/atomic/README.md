# Atomic Variables

## Core Concepts

Atomic variables provide lock-free, thread-safe operations using the
Compare-And-Swap (CAS) CPU instruction. They are part of
`java.util.concurrent.atomic`.

### What is CAS?

CAS is an atomic CPU instruction that:
1. Reads the current value
2. Compares it with an expected value
3. Writes a new value if they match
4. Returns whether the operation succeeded

```
if (currentValue == expectedValue) {
    currentValue = newValue;
    return true;
} else {
    return false; // another thread changed it
}
```

---

## Available Atomic Types

| Type | Description |
|------|-------------|
| `AtomicInteger` | Thread-safe int operations |
| `AtomicLong` | Thread-safe long operations |
| `AtomicBoolean` | Thread-safe boolean operations |
| `AtomicReference<V>` | Thread-safe reference operations |
| `AtomicStampedReference<V>` | Prevents ABA problem with version stamps |
| `AtomicMarkableReference<V>` | Reference with boolean mark |

---

## Lock-Free Programming

### Benefits

- No lock contention or deadlock risk
- Better performance under moderate contention
- Non-blocking: threads never wait

### Drawbacks

- Spin loops waste CPU under high contention
- ABA problem (mitigated by `AtomicStampedReference`)
- Only one variable at a time (no atomic compound operations)

### When to Use

| Use Atomic When | Use Locks When |
|----------------|---------------|
| Single variable updates | Multiple variables need atomicity |
| Read-modify-write patterns | Long critical sections |
| High read frequency | Complex state transitions |
| Simple counters/flags | Conditional waiting (wait/notify) |

---

## LongAdder vs AtomicLong

`LongAdder` maintains separate cells for each thread, reducing contention:

- **AtomicLong**: single memory location, CAS retries on contention
- **LongAdder**: per-thread cells, sum aggregated on read
- Use `LongAdder` for high-contention counters (e.g., stats, metrics)
- Use `AtomicLong` when you need exact current value at all times

```java
// High contention scenario
LongAdder adder = new LongAdder();
// vs
AtomicLong counter = new AtomicLong();
```

---

## ABA Problem

Thread 1 reads value A, gets preempted.
Thread 2 changes A -> B -> A.
Thread 1 resumes, CAS succeeds (value is A again) but state may be inconsistent.

Solution: `AtomicStampedReference` adds a version stamp to detect changes.

```java
AtomicStampedReference<String> ref =
    new AtomicStampedReference<>("A", 0);
// stamp increments on each successful CAS
```

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
