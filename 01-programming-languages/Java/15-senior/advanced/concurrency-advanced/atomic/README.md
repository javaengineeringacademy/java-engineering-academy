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

## Overview

Atomic variables provide lock-free, thread-safe operations on single variables using the CPU-level Compare-And-Swap (CAS) instruction. Part of `java.util.concurrent.atomic`, they avoid the overhead and deadlock risk of `synchronized` blocks for simple read-modify-write patterns. The key types are `AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference`, `AtomicStampedReference`, and `AtomicMarkableReference`.

## Interview Questions

1. **How does CAS work at the hardware level and what happens on contention?**
   CAS executes a single CPU instruction (e.g., `CMPXCHG` on x86) that atomically compares a memory location to an expected value and swaps in a new value if they match. On contention (another thread modified the value between read and CAS), the CAS fails and the operation retries in a spin loop. Java 9+ uses `VarHandle` with `acquire/release` semantics; older versions use `Unsafe` with full fence instructions. Under high contention, this spin loop wastes CPU cycles—this is when `LongAdder` or locks are preferred.

2. **Explain the ABA problem in detail. Give a real-world scenario where it causes a bug.**
   Scenario: A lock-free stack uses CAS on the head pointer. Thread 1 reads head→A→B, prepares to CAS(head, A, B→A→C). Before CAS executes, Thread 2 pops A, pops B, pushes A back. Thread 1's CAS succeeds because head still points to A, but the stack topology changed (B was removed). Result: lost data. In practice: memory allocator free-lists can reuse freed blocks—CAS may succeed even though the block was freed and reallocated. Solution: `AtomicStampedReference` uses a version stamp incremented on every modification.

3. **When would you choose `AtomicLong` over `LongAdder` and vice versa?**
   `AtomicLong`: use when you need the exact current value at all times (sequence generators, unique IDs). Single memory location, all threads see consistent value. `LongAdder`: use for high-contention counters where approximate sum is acceptable (metrics, stats). Maintains per-thread cells; writes are local, reads sum all cells. Throughput: `LongAdder` ~10x better than `AtomicLong` under 16-thread contention. Trade-off: `sum()` is not atomic and may be stale.

4. **What is the difference between `compareAndSet`, `weakCompareAndSet`, and `getAndSet`?**
   `compareAndSet`: full CAS with happens-before ordering (acquire+release). Guarantees visibility of prior reads/writes. `weakCompareAndSet`: may fail spuriously (allows JVM to skip memory fences for optimization). Use in loops where spurious failure is handled. `getAndSet`: atomically sets new value and returns old value (equivalent to CAS loop but implemented as a single intrinsic). Java 9+ adds `getAndSet` as a `VarHandle` operation.

5. **How do you build a lock-free stack using `AtomicReference`? Explain the CAS loop pattern.**
   ```java
   public void push(T value) {
       Node<T> oldHead = head.get();
       Node<T> newHead = new Node<>(value, oldHead);
       while (!head.compareAndSet(oldHead, newHead)) {
           oldHead = head.get(); // re-read on failure
           newHead = new Node<>(value, oldHead);
       }
   }
   ```
   The CAS loop pattern: (1) read current state, (2) compute new state, (3) attempt CAS, (4) on failure, re-read and retry. This ensures linearizability—each operation appears instantaneous. The loop terminates when no concurrent modification occurred between read and CAS.

6. **Why is `AtomicStampedReference` more expensive than `AtomicReference`?**
   `AtomicStampedReference` stores both a reference AND an integer stamp (version). CAS must match both value AND stamp, requiring two memory locations atomically. Implementation uses `VarHandle` with opaque access mode or `Unsafe.compareAndSwapObject` + `Unsafe.compareAndSwapInt`. Overhead: ~2x memory, slightly slower CAS due to double comparison. Only use when ABA is a real concern (pointer-based data structures, memory allocators).

7. **What are the memory ordering guarantees of atomic variables?**
   All atomic variables provide at least `volatile` semantics: writes are visible to subsequent reads. `compareAndSet` and `getAndSet` provide acquire+release ordering. `get()` and `set()` are volatile reads/writes. Java 9+ `VarHandle` offers fine-grained control: `getAcquire()`, `setRelease()`, `getOpaque()` for relaxed ordering. On x86, CAS implies full barrier; on ARM/POWER, explicit fences may be emitted.

8. **How does `DoubleAdder`/`LongAdder` internally distribute contention?**
   `LongAdder` uses a `Cell[]` array. Each thread computes its index via `ThreadLocalRandom.current().nextInt()` and writes to that cell. On `sum()`, all cells are aggregated. Cells are `@Contended` to avoid false sharing. If contention causes cell creation, the array grows (cells CAS'd from null to allocated). `Striped64` is the base class providing the cell infrastructure. No global lock—writes are always to thread-local cells.

## Pitfalls

```java
// BAD: Compound atomicity mistake
// AtomicInteger can't atomically check-then-act
AtomicInteger count = new AtomicInteger(0);
// NOT atomic:
if (count.get() < 10) {
    count.incrementAndGet(); // race condition!
}

// GOOD: Use compareAndSet loop
int current;
do {
    current = count.get();
    if (current >= 10) return false;
} while (!count.compareAndSet(current, current + 1));
```

- **CAS retry storms**: Under extreme contention, CAS loops spin indefinitely. Use `LongAdder` or locks instead.
- **Ignoring ABA**: Lock-free data structures with `AtomicReference` are vulnerable. Use `AtomicStampedReference`.
- **AtomicReference with mutable objects**: The reference is atomic, not the object's fields. Use `AtomicReferenceFieldUpdater` or make fields volatile.
- **Using `getAndSet` in tight loops**: `getAndSet` allocates internally on some JVM versions. Prefer `compareAndSet` loops for maximum performance.

## Performance

| Operation | `AtomicLong` | `LongAdder` | `synchronized` |
|-----------|-------------|-------------|----------------|
| Single-thread increment | ~5 ns | ~7 ns | ~10 ns |
| 16-thread increment | ~50 ns | ~5 ns | ~200 ns |
| 16-thread read (sum) | ~5 ns | ~20 ns | ~10 ns |
| Throughput (16 threads) | ~50M ops/s | ~500M ops/s | ~10M ops/s |

- CAS is ~2-5x faster than `synchronized` under low contention
- `LongAdder` is ~10x faster than `AtomicLong` under high contention
- `AtomicStampedReference` CAS is ~2x slower than plain `AtomicReference`

## Examples

```java
// Lock-free bounded queue (simplified Michael-Scott queue)
public class LockFreeQueue<T> {
    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public void enqueue(T value) {
        Node<T> newTail = new Node<>(value, null);
        while (true) {
            Node<T> currentTail = tail.get();
            Node<T> next = currentTail.next.get();
            if (currentTail == tail.get()) {
                if (next == null) {
                    if (currentTail.next.compareAndSet(null, newTail)) {
                        tail.compareAndSet(currentTail, newTail);
                        return;
                    }
                } else {
                    tail.compareAndSet(currentTail, next);
                }
            }
        }
    }
}

// CAS-based lock-free counter with conditional update
public class BoundedCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    private static final int MAX = 100;

    public boolean incrementIfBelowMax() {
        int current;
        do {
            current = count.get();
            if (current >= MAX) return false;
        } while (!count.compareAndSet(current, current + 1));
        return true;
    }
}
```

## Internal Working

- **CAS instruction**: On x86, `CMPXCHG` is a single instruction that atomically compares `RAX` with a memory operand and swaps if equal. On ARM, `LDREX/STREX` (load-exclusive/store-exclusive) provides the same semantics.
- **JVM intrinsics**: `AtomicInteger.compareAndSet` is mapped to `Unsafe.compareAndSwapInt`, which the JIT compiler replaces with a single `CMPXCHG` instruction. No method call overhead.
- **Memory fences**: Java 9+ uses `VarHandle` with `acquire/release` semantics instead of full fences. On x86, `CMPXCHG` already implies a full barrier; on ARM, explicit `DMB` barriers are emitted.
- **Spin strategy**: HotSpot uses adaptive spinning (`ObjectSynchronizer`), spinning a few hundred iterations before parking. Thread-local polling reduces bus traffic.
- **`LongAdder` internals**: `Striped64` base class. `Cell[]` array with `@Contended` padding. Each thread hashes to a cell. `sum()` iterates cells non-atomically. Cells allocated lazily on contention.

## Why This Concept Exists

`ynchronized` blocks cause thread contention, context switching, and potential deadlocks. Atomic variables provide lock-free alternatives for single-variable operations:

1. **No deadlocks**: Lock-free algorithms cannot deadlock because no threads hold locks.
2. **No context switches**: Threads spin rather than block, avoiding expensive OS context switches.
3. **Better throughput**: Under low-to-moderate contention, CAS is 2-5x faster than locks.
4. **Composability**: Atomic variables can be combined with other lock-free structures (queues, stacks, counters).
5. **Scalability**: `LongAdder` scales linearly with cores for counter workloads.

Atomic variables are the building blocks of lock-free concurrent data structures, enabling high-performance concurrent programming without the complexity of lock ordering and deadlock avoidance.

## References

- [Oracle: java.util.concurrent.atomic](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/package-summary.html)
- [OpenJDK: VarHandle (Java 9+)](https://openjdk.org/jeps/193)
- [Doug Lea: A Scalable Concurrent LongAdder](http://gee.cs.oswego.edu/dl/papers/04/ecoop04-blobs.pdf)
- [Art of Multiprocessor Programming (Herlihy & Shavit)](https://www.amazon.com/Art-Multiprocessor-Programming-Revised-Reprint/dp/0123705916)
- [Mechanical Sympathy: CAS vs Locks](https://mechanical-sympathy.blogspot.com/2012/05/cas-vs-locks.html)
