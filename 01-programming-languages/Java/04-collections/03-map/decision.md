# Map Interface Decision Guide

## Decision Tree

```
Need key-value storage?
├── Need ordering?
│   ├── Sorted keys → TreeMap
│   ├── Insertion order → LinkedHashMap
│   ├── Access order (LRU) → LinkedHashMap(accessOrder=true)
│   └── No order → HashMap (fastest)
├── Need thread safety?
│   ├── High concurrency → ConcurrentHashMap
│   ├── Legacy code → Hashtable (avoid in new code)
│   └── Simple wrapper → Collections.synchronizedMap()
├── Need weak references? → WeakHashMap
├── Need enum keys? → EnumMap (fastest)
└── Need null key/values?
    ├── One null key → HashMap
    ├── No null keys → ConcurrentHashMap
    └── No null values → ConcurrentHashMap
```

## Comparison Matrix

| Implementation | Order | Null Keys | Thread-Safe | Performance | Use Case |
|---------------|-------|-----------|-------------|-------------|----------|
| HashMap | None | One key | No | O(1) | General-purpose |
| LinkedHashMap | Insertion/Access | One key | No | O(1) | LRU cache, ordered |
| TreeMap | Sorted | No | No | O(log n) | Sorted keys |
| Hashtable | None | No | Yes (all) | O(1) | Legacy code |
| ConcurrentHashMap | None | No | Yes (CAS) | O(1) | High concurrency |
| WeakHashMap | None | One key | No | O(1) | Caching |
| EnumMap | Enum order | No | No | O(1) bit-vector | Enum keys |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose map | HashMap | Fastest, no ordering |
| Sorted keys | TreeMap | Natural/custom ordering |
| Insertion order | LinkedHashMap | Maintains order |
| LRU cache | LinkedHashMap(accessOrder=true) | Built-in access-order |
| Thread-safe | ConcurrentHashMap | High concurrency |
| Legacy code | Hashtable | Don't use in new code |
| Weak references | WeakHashMap | GC-friendly caching |
| Enum keys | EnumMap | Bit-vector, fastest |
| Immutable | Map.of() | Thread-safe, no modification |

## Production Recommendations

> **Default to HashMap** unless you need ordering or thread safety. It's the fastest and most memory-efficient.

> **Use ConcurrentHashMap for concurrent access** — never use Collections.synchronizedMap() in production (poor performance under contention).

> **Use LinkedHashMap for LRU caches** — set accessOrder=true and override removeEldestEntry().

> **Avoid Hashtable** — it's legacy and synchronized with performance overhead. Use ConcurrentHashMap instead.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Speed vs Ordering | HashMap (fast, no order) | TreeMap (sorted, O(log n)) |
| Immutability vs Flexibility | Map.of() (immutable) | HashMap (mutable) |
| Thread-safety vs Performance | ConcurrentHashMap (safe, overhead) | HashMap (fast, no safety) |
| Memory vs GC-friendliness | HashMap (compact) | WeakHashMap (GC-friendly, higher overhead) |
| Generality vs Specialization | HashMap (general) | EnumMap (enum-specific, fastest) |

## Common Code Review Comments

- "Why are you using Hashtable? Use ConcurrentHashMap instead."
- "This map is being accessed concurrently — use ConcurrentHashMap."
- "Consider using Map.of() if this map is immutable."
- "This should be an EnumMap — you're using enum values as keys."

## Common Production Mistakes

> Notice: HashMap doesn't maintain order — if you need insertion order, use LinkedHashMap.

> Notice: TreeMap requires keys to be Comparable or you must provide a Comparator — otherwise you get ClassCastException at runtime.

> Notice: ConcurrentHashMap doesn't allow null keys or values — use Optional instead.

> Notice: WeakHashMap keys are weakly referenced — they can be GC'd at any time. Don't use it as a cache unless you understand the implications.
