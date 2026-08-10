# ArrayList Decision Guide

## Decision Tree

```
Need a dynamic array?
├── Need random access by index? → ArrayList (default)
├── Need frequent insert/remove in middle? → LinkedList (but measure first)
├── Need thread safety?
│   ├── Read-heavy → CopyOnWriteArrayList
│   └── General → Collections.synchronizedList()
├── Need fixed size? → List.of() or Arrays.asList()
└── Need primitives? → Consider Eclipse Collections or Trove
```

## Comparison Matrix

| Feature | ArrayList | LinkedList | CopyOnWriteArrayList |
|---------|-----------|------------|---------------------|
| get(i) | O(1) | O(n) | O(1) |
| add(end) | O(1) amortized | O(1) | O(n) |
| add(mid) | O(n) | O(1) | O(n) |
| remove(mid) | O(n) | O(1) | O(n) |
| Memory | Low | High | Very High |
| Cache locality | Excellent | Poor | Excellent |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose list | ArrayList | Fastest for most operations |
| Random access | ArrayList | O(1) get() |
| Insert/remove in middle | LinkedList | O(1) at known position |
| Read-heavy concurrent | CopyOnWriteArrayList | No locks on writes |
| Immutable | List.of() | Thread-safe, no modification |

## Production Recommendations

> **Default to ArrayList** — it's the fastest for most operations and has the smallest memory footprint.

> **Pre-size with initialCapacity** — if you know the approximate size, avoid resizing overhead.

> **Use removeIf() instead of Iterator.remove()** — cleaner code, same performance.

> **Avoid LinkedList** — it's rarely faster in practice due to cache locality.

## Engineering Trade-offs

| Trade-off | ArrayList | Alternative |
|-----------|-----------|-------------|
| Random access vs Insert | Fast get, slow insert | LinkedList: slow get, fast insert |
| Memory vs Thread-safety | Low memory, no safety | CopyOnWriteArrayList: high memory, safe |
| Flexibility vs Immutability | Mutable | List.of(): immutable |
| Simplicity vs Performance | Simple | Collections.synchronizedList(): simple wrapper |

## Common Code Review Comments

- "Why are you using LinkedList? ArrayList is faster for most use cases."
- "This could be a List.of() if it's immutable."
- "Consider using removeIf() instead of Iterator.remove()."
- "Pre-size the ArrayList if you know the approximate size."

## Common Production Mistakes

> Notice: ArrayList.remove() in a for-loop skips elements. Use Iterator.remove() or list.removeIf().

> Notice: ArrayList capacity vs size — pre-sizing avoids resizing overhead for large lists.

> Notice: ArrayList is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: ArrayList.indexOf() is O(n) — use a HashSet for frequent membership checks.
