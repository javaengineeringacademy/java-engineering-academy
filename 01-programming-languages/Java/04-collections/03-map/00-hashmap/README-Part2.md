# HashMap (Part 2)

This is Part 2 of the HashMap documentation. See Part 1 for Internal Working, Methods, and Core Concepts.

## 12. Code Review Checklist

- [ ] Using immutable objects as keys
- [ ] hashCode/equals properly implemented
- [ ] Not using mutable objects as keys
- [ ] Initial capacity set for known-size maps
- [ ] Thread safety handled for concurrent access
- [ ] Not using HashMap when order matters
- [ ] Using entrySet() for efficient iteration

## 13. Architecture Considerations

### Where HashMap Fits in System Design

| Layer | Use Case | Why HashMap |
|-------|----------|-------------|
| API Gateway | Request parameter caching | O(1) lookup |
| Service Layer | In-memory session store | Fast get/put |
| Data Access | ResultSet mapping | O(1) key lookup |
| Caching | Local cache layer | Fastest map implementation |
| Configuration | Feature flag storage | O(1) containsKey |

### Integration Patterns

```
Client → API Gateway → HashMap → Service → HashMap → Client
                    ↓
            HashMap → Cache Manager → HashMap
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K entries | HashMap is optimal |
| 10K - 100K entries | HashMap with proper sizing |
| 100K - 1M entries | Consider ConcurrentHashMap |
| > 1M entries | Consider database or Redis |

### When to Replace HashMap in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| Sorted keys needed | TreeMap | O(log n) sorted operations |
| Insertion order | LinkedHashMap | Maintains insertion/access order |
| Thread-safe map | ConcurrentHashMap | Concurrent access |
| Enum keys | EnumMap | Faster for enum keys |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Hash collision DoS | Service degradation | Use randomized hash functions, rate limiting |
| Mutable key manipulation | Data corruption | Use immutable objects |
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded collections |
| Deserialization attack | Remote code execution | Avoid ObjectInputStream |

## 15. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | HashMap introduced | Standard hash map |
| Java 8 | Treeification | O(log n) for collision chains with 8+ entries |
| Java 8 | Stream support | Stream processing |
| Java 9 | Map.of() factory | Immutable map alternatives |
| Java 10 | Copy-on-write improvements | Better concurrency |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| HashMap | 1.2 | Stable |
| Treeification | 8.0 | Stable |
| Stream support | 8.0 | Stable |
| Map.of() | 9.0 | Stable |

## 17. Best Practices

1. Use immutable objects as keys
2. Override hashCode/equals correctly
3. Set initial capacity for known sizes
4. Use entrySet() for iteration over key-value pairs
5. Use compute/computeIfAbsent for atomic operations
6. Consider LinkedHashMap for ordered maps

## 18. Common Mistakes

1. **Using mutable objects as keys**: Hash changes, entry lost
2. **Not overriding hashCode/equals**: Breaks Map behavior
3. **Ignoring null from get()**: Returns null if key absent
4. **Not setting initial capacity**: Wastes time resizing
5. **Iterating over keySet() when entrySet() needed**: Extra lookup per entry

## 19. Common Myths

### Myth 1: HashMap is always O(1)
**Reality:** Amortized O(1), but resizing is O(n). Treeification is O(log n).

### Myth 2: HashMap allows multiple null keys
**Reality:** HashMap allows at most one null key.

### Myth 3: HashMap is thread-safe
**Reality:** Not thread-safe. Use ConcurrentHashMap for concurrent access.

### Myth 4: HashMap maintains insertion order
**Reality:** HashMap does not maintain any order. Use LinkedHashMap for insertion order.

## 20. One-Minute Revision

- Hash table implementation of Map interface
- O(1) amortized for get/put/remove
- One null key, multiple null values allowed
- Not thread-safe, use ConcurrentHashMap
- Treeification for collision chains with 8+ entries
- Use immutable objects as keys

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| LinkedHashMap | Insertion-ordered variant |
| TreeMap | Sorted variant |
| Hashtable | Legacy synchronized variant |
| ConcurrentHashMap | Thread-safe variant |
| hashCode/equals | Contract for keys |

## 22. Interview Questions

1. **How does HashMap work internally?** — Array of Node buckets. Key.hashCode() determines bucket via hash() & (n-1).

2. **What happens when two keys have same hash code?** — Stored as linked list in same bucket. Treeified to tree for 8+ entries (Java 8+).

3. **What is the load factor of HashMap?** — Default 0.75. Determines when to resize (when size > capacity * loadFactor).

4. **How does HashMap handle null keys?** — Null key has hash 0, stored in bucket 0.

5. **Is HashMap thread-safe?** — No. Use ConcurrentHashMap for concurrent access.

6. **What is the time complexity of HashMap operations?** — O(1) amortized for get/put/remove.

## 23. References

- [Oracle Java Documentation - HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 13: Override hashCode judiciously](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
