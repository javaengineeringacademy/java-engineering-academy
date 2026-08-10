# Collection Interface Decision Guide

## Decision Tree

```
Do you need a group of objects?
├── Yes
│   ├── Need key-value pairs? → Map (not Collection)
│   ├── Need unique elements? → Set
│   ├── Need ordered, indexed access? → List
│   ├── Need FIFO/LIFO operations? → Queue/Deque
│   └── Need just iteration? → Collection (use Iterable directly)
└── No → Use individual objects
```

## Comparison Matrix

| Interface | Order | Duplicates | Indexed | Thread-Safe | Null Elements |
|-----------|-------|------------|---------|-------------|---------------|
| List | Yes | Yes | Yes | No (use implementations) | Yes |
| Set | Depends on impl | No | No | No (use implementations) | Depends on impl |
| Queue | FIFO | Yes | No | No (use implementations) | Depends on impl |
| Deque | FIFO/LIFO | Yes | No | No (use implementations) | Depends on impl |
| Map | Depends on impl | No (keys) | No (use key) | No (use implementations) | Depends on impl |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose collection | ArrayList | Fastest for most operations |
| Unique elements | HashSet | O(1) lookup, no ordering |
| Key-value storage | HashMap | O(1) lookup, no ordering |
| Sorted elements | TreeSet/TreeMap | O(log n) operations, sorted |
| Thread-safe list | CopyOnWriteArrayList | Read-heavy workloads |
| Thread-safe map | ConcurrentHashMap | High concurrency |
| Immutable collection | List.of()/Set.of()/Map.of() | Thread-safe, no modification |
| Queue operations | ArrayDeque | Faster than LinkedList |

## Production Recommendations

> **Default to ArrayList** unless you have a specific reason to use something else. It's the most tested, most optimized, and most understood collection in Java.

> **Use immutable collections** (List.of(), Set.of(), Map.of()) for constants and configuration. They're thread-safe and prevent accidental modification.

> **Avoid Vector and Stack** — they're legacy and synchronized with performance overhead. Use ArrayList and ArrayDeque instead.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Performance vs Safety | Unsynchronized (faster) | Synchronized (safer) |
| Memory vs Speed | Compact (ArrayList) | Fast insert/remove (LinkedList) |
| Ordering vs Performance | Sorted (TreeMap, O(log n)) | Unordered (HashMap, O(1)) |
| Immutability vs Flexibility | Immutable (thread-safe) | Mutable (more flexible) |
| Generality vs Specialization | General-purpose (Collection) | Specialized (ConcurrentHashMap) |

## Common Code Review Comments

- "Why are you using Vector? Use ArrayList or ConcurrentHashMap instead."
- "This collection should be immutable — use List.of() or Collections.unmodifiableList()."
- "Consider using removeIf() instead of Iterator.remove() for cleaner code."
- "This could be a Set if you don't need duplicates."

## Common Production Mistakes

> Notice: Collections.sort() modifies the original list. Use list.stream().sorted().collect() if you need a sorted copy.

> Notice: isEmpty() is faster than size() == 0 for some implementations (like LinkedList).

> Notice: hashCode() and equals() contract — if you override one, override both. Broken contracts cause HashMap/HashSet bugs.
