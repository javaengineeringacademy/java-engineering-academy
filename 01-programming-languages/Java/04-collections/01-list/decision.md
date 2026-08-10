# List Interface Decision Guide

## Decision Tree

```
Need an ordered collection?
├── Need random access by index?
│   ├── Yes → ArrayList (default)
│   └── No
│       ├── Need frequent insert/remove in middle?
│       │   ├── Yes → LinkedList (but check ArrayDeque first)
│       │   └── No → ArrayList
│       └── Need thread safety?
│           ├── Read-heavy → CopyOnWriteArrayList
│           └── General → Collections.synchronizedList()
├── Need fixed size? → List.of() or Arrays.asList()
└── Need queue/deque operations? → ArrayDeque (not List)
```

## Comparison Matrix

| Implementation | Get(i) | Add/Remove Mid | Thread-Safe | Memory | Use Case |
|---------------|--------|----------------|-------------|--------|----------|
| ArrayList | O(1) | O(n) | No | Low | General-purpose |
| LinkedList | O(n) | O(1) | No | High | Queue/deque only |
| Vector | O(1) | O(n) | Yes (all) | Medium | Legacy code |
| Stack | O(1) | O(n) | Yes (all) | Medium | Legacy LIFO |
| CopyOnWriteArrayList | O(1) | O(n) | Yes (copy) | Very High | Read-heavy |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose list | ArrayList | Fastest for most operations |
| Random access | ArrayList | O(1) get() |
| Insert/remove in middle | LinkedList | O(1) at known position |
| Thread-safe read-heavy | CopyOnWriteArrayList | No locks on reads |
| Thread-safe general | Collections.synchronizedList() | Simple wrapper |
| Fixed-size list | List.of() or Arrays.asList() | Immutable, no resize |
| Queue/deque | ArrayDeque | Faster than LinkedList |
| Legacy code | Vector/Stack | Don't use in new code |

## Production Recommendations

> **Default to ArrayList** — it's the fastest for most operations and has the smallest memory footprint.

> **Avoid LinkedList** — it's rarely faster in practice due to cache locality. Only use if you've measured and confirmed it's faster for your specific use case.

> **Use CopyOnWriteArrayList for read-heavy concurrent access** — but never for write-heavy workloads (each write copies the entire array).

> **Never use Vector or Stack in new code** — they're legacy and synchronized with performance overhead.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Random access vs Insert/remove | ArrayList (fast get, slow insert) | LinkedList (slow get, fast insert) |
| Memory vs Thread-safety | ArrayList (low memory, no safety) | CopyOnWriteArrayList (high memory, safe) |
| Flexibility vs Immutability | ArrayList (mutable) | List.of() (immutable) |
| Simplicity vs Performance | Collections.synchronizedList() (simple) | CopyOnWriteArrayList (better read perf) |
| Cache locality vs Node overhead | ArrayList (contiguous) | LinkedList (scattered nodes) |

## Common Code Review Comments

- "Why are you using LinkedList? ArrayList is faster for most use cases."
- "This could be a List.of() if it's immutable."
- "Consider using removeIf() instead of Iterator.remove() for cleaner code."
- "This list is being accessed concurrently — use CopyOnWriteArrayList or Collections.synchronizedList()."

## Common Production Mistakes

> Notice: ArrayList.remove() in a for-loop will skip elements. Always use Iterator.remove() or list.removeIf().

> Notice: ArrayList capacity vs size — pre-sizing with initialCapacity avoids resizing overhead for large lists.

> Notice: LinkedList memory overhead is significant — each node has prev+next pointers (16-24 bytes per element).

> Notice: Collections.synchronizedList() returns a wrapper — iterating must be done in a synchronized block.
