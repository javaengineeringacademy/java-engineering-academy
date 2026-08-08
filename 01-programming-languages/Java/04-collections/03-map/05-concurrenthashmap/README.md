# ConcurrentHashMap

## 1. Why It Exists

ConcurrentHashMap was introduced in Java 5 to solve the performance problem of Hashtable's coarse-grained locking. It provides a thread-safe hash table with fine-grained locking, allowing concurrent reads and writes without blocking the entire map.

## 2. What It Is

ConcurrentHashMap is a thread-safe hash table implementation of the ConcurrentMap interface. It uses segment locking (Java 7) or CAS + synchronized on bins (Java 8+) for fine-grained concurrency. It does not allow null keys or values.

## 3. Internal Working

### Java 7: Segment Locking

```java
// ConcurrentHashMap uses Segment array
final Segment<K,V>[] segments;

// Each segment is a mini-HashMap with its own lock
static final class Segment<K,V> extends ReentrantLock {
    transient volatile HashEntry<K,V>[] table;
    transient int count;
}
```

### Java 8+: CAS + synchronized

```java
// ConcurrentHashMap uses Node[] table
transient volatile Node<K,V>[] table;

// CAS for empty slots
// synchronized on bucket head for collision chains
```

### Lock Striping

```
Java 7: Segment Locking
┌─────────────────────────────────────────┐
│ Segment[0] (lock 0)                     │
│ ├── Entry[] table                       │
│ └── ReentrantLock                       │
│                                         │
│ Segment[1] (lock 1)                     │
│ ├── Entry[] table                       │
│ └── ReentrantLock                       │
│ ...                                     │
│ Segment[15] (lock 15)                   │
│ ├── Entry[] table                       │
│ └── ReentrantLock                       │
└─────────────────────────────────────────┘

Java 8+: CAS + synchronized
┌─────────────────────────────────────────┐
│ Node[] table                            │
│ ├── [0] → null                          │
│ ├── [1] → Node (CAS for empty)          │
│ ├── [2] → synchronized bucket           │
│ └── ...                                 │
└─────────────────────────────────────────┘
```

## 4. Constructors

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();                      // Default capacity 16
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100);                   // Custom initial capacity
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100, 0.75f, 16);       // Custom capacity + load factor + concurrency level
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(map);                    // From map
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(Map.of("a", 1));       // From map literal
```

## 5. Methods

### Map Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `put(K key, V value)` | Associates key with value | O(1) |
| `get(Object key)` | Returns value for key | O(1) |
| `remove(Object key)` | Removes key-value pair | O(1) |
| `containsKey(Object key)` | Checks if key exists | O(1) |
| `containsValue(Object value)` | Checks if value exists | O(n) |
| `size()` | Returns entry count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all entries | O(n) |

### ConcurrentMap Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `putIfAbsent(K key, V value)` | Puts only if key absent | O(1) |
| `remove(Object key, Object value)` | Removes only if matches | O(1) |
| `replace(K key, V oldValue, V newValue)` | Replaces only if matches | O(1) |
| `replace(K key, V value)` | Replaces value for key | O(1) |
| `compute(K key, mappingFunction)` | Computes new value | O(1) |
| `computeIfAbsent(K key, mappingFunction)` | Computes if key absent | O(1) |
| `computeIfPresent(K key, remappingFunction)` | Computes if key present | O(1) |
| `merge(K key, V value, remappingFunction)` | Merges values | O(1) |

### Bulk Operations (Java 8+)

| Method | Description | Complexity |
|--------|-------------|------------|
| `forEach(action)` | Iterates over entries | O(n) |
| `reduce(limiter, transformer, combiner)` | Reduces entries | O(n) |
| `search(limiter, searchFunction)` | Searches entries | O(n) |
| `forEachKey(action)` | Iterates over keys | O(n) |
| `forEachValue(action)` | Iterates over values | O(n) |
| `forEachEntry(action)` | Iterates over entries | O(n) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| put(K, V) | O(1) | O(1) |
| get(K) | O(1) | O(1) |
| remove(K) | O(1) | O(1) |
| containsKey(K) | O(1) | O(1) |
| containsValue(V) | O(n) | O(1) |
| size() | O(n) | O(1) |
| isEmpty() | O(1) | O(1) |
| putIfAbsent(K, V) | O(1) | O(1) |
| replace(K, V) | O(1) | O(1) |
| compute(K, fn) | O(1) | O(1) |
| merge(K, V, fn) | O(1) | O(1) |
| forEach(action) | O(n) | O(1) |

## 7. Thread Safety

ConcurrentHashMap is thread-safe:

```java
// Java 7: Segment locking
// 16 segments by default, each with its own lock
// Different segments can be accessed concurrently

// Java 8+: CAS + synchronized
// CAS for empty slots
// synchronized on bucket head for collision chains
// No locking for reads
```

### Atomic Operations

```java
// putIfAbsent is atomic
map.putIfAbsent("key", "value");

// replace is atomic
map.replace("key", "oldValue", "newValue");

// compute is atomic
map.compute("key", (k, v) -> v == null ? "default" : v + 1);
```

### Weakly Consistent Iterators

```java
// Iterator is weakly consistent
// Reflects state of map at some point since iterator was created
// May (but is not guaranteed to) reflect modifications after creation
Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
    // May see concurrent modifications
}
```

## 8. Memory Behavior

### Memory Layout

```
ConcurrentHashMap object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ table reference (8 bytes)   │──────┐
│ baseCount (long, 8 bytes)   │      │
│ sizeCtl (int, 4 bytes)      │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Node[] table

Per entry:
Node object: ~32 bytes
├── Object header: 12 bytes
├── hash (int): 4 bytes
├── key reference: 8 bytes
├── value reference: 8 bytes
└── next reference: 8 bytes
```

### Memory Comparison

| Type | Per-Entry | 1M Entries |
|------|-----------|------------|
| ConcurrentHashMap | ~32 bytes | ~32 MB |
| HashMap | ~32 bytes | ~32 MB |
| Hashtable | ~32 bytes + lock | ~32 MB + lock |

## 9. Production Incidents

### Incident 1: Size() Operation is Expensive

**Problem:** Application slows when calling size().
**Cause:** ConcurrentHashMap.size() iterates all segments (Java 7) or uses CounterCell (Java 8+).
**Impact:** Response time increases with map size.
**Solution:** Use mappingCount() for approximate count, or maintain separate counter.
**Prevention:** Avoid size() in hot paths.

### Incident 2: Weakly Consistent Iterator Misses Updates

**Problem:** Iterator does not see recent updates.
**Cause:** Weakly consistent iterator reflects state at some point since creation.
**Impact:** Incorrect processing.
**Solution:** Understand iterator semantics, use forEach() for real-time view.
**Prevention:** Document iterator behavior for team.

### Incident 3: Memory Leak from Unbounded Growth

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** ConcurrentHashMap grows without bounds.
**Impact:** Application crash, data loss.
**Solution:** Implement bounded map or eviction policy.
**Prevention:** Monitor map size, implement limits.

## 10. Engineering Decision Framework

### Use ConcurrentHashMap when:
- Thread-safe map required
- High concurrency expected
- Fine-grained locking needed
- Null keys/values not needed

### Avoid ConcurrentHashMap when:
- Single-threaded (use HashMap)
- Null keys/values needed (use HashMap)
- Sorted keys needed (use ConcurrentSkipListMap)
- Strong consistency needed (use Collections.synchronizedMap() with external sync)

### When NOT to Use ConcurrentHashMap
- **Single-threaded**: Use HashMap (no concurrency overhead)
- **Need nulls**: ConcurrentHashMap rejects nulls
- **Sorted keys**: Use ConcurrentSkipListMap

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| HashMap | Single-threaded |
| Hashtable | Legacy code (avoid) |
| Collections.synchronizedMap() | Simple synchronization |
| ConcurrentSkipListMap | Sorted concurrent map |
| Caffeine | Production-grade caching |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow size() | Profiling | Use mappingCount() instead |
| Iterator misses updates | Debug logging | Understand weakly consistent semantics |
| Memory leak | Heap dump | Check for unbounded growth |
| ConcurrentModificationException | Thread dump | Should not happen with ConcurrentHashMap |

## 12. Code Review Checklist

- [ ] Using ConcurrentHashMap for thread-safe maps
- [ ] Not using null keys/values
- [ ] Using atomic operations (putIfAbsent, replace, compute)
- [ ] Understanding weakly consistent iterators
- [ ] Monitoring map size
- [ ] Using mappingCount() instead of size()
- [ ] Considering compute/computeIfAbsent for atomic updates

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, implement eviction |
| Race conditions | Data corruption | Use atomic operations |
| DoS via hash collision | Service degradation | Rate limiting |
| Null injection | NullPointerException | Validate inputs |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 5 | ConcurrentHashMap introduced | Fine-grained concurrent map |
| Java 7 | Segment locking | 16 segments by default |
| Java 8 | CAS + synchronized | Better performance, no segments |
| Java 8 | Stream support | Stream processing |
| Java 8 | Bulk operations | forEach, reduce, search |
| Java 9 | Map.of() factory | Immutable map alternatives |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| ConcurrentHashMap | 5.0 | Stable |
| CAS + synchronized | 8.0 | Stable |
| Stream support | 8.0 | Stable |
| Bulk operations | 8.0 | Stable |
| Map.of() | 9.0 | Stable |

## 16. Best Practices

1. Use putIfAbsent for atomic inserts
2. Use replace for conditional updates
3. Use compute/computeIfAbsent for atomic computations
4. Use mappingCount() instead of size()
5. Use forEach for iteration (weakly consistent)
6. Consider Caffeine for production caching
7. Monitor map size to prevent memory issues

## 17. Common Mistakes

1. **Using null keys/values**: ConcurrentHashMap does not allow null
2. **Assuming size() is O(1)**: It's O(n) in Java 7, approximate in Java 8+
3. **Assuming iterator is consistent**: It's weakly consistent
4. **Not using atomic operations**: putIfAbsent, replace, compute
5. **Ignoring memory growth**: Implement bounds

## 18. Common Myths

### Myth 1: ConcurrentHashMap is always faster than Hashtable
**Reality:** Depends on concurrency level and access patterns.

### Myth 2: ConcurrentHashMap is always faster than synchronizedMap
**Reality:** synchronizedMap may be better for low contention.

### Myth 3: ConcurrentHashMap allows null keys/values
**Reality:** ConcurrentHashMap does not allow null keys or values.

### Myth 4: ConcurrentHashMap iterators are fail-fast
**Reality:** Iterators are weakly consistent, not fail-fast.

## 19. One-Minute Revision

- Thread-safe hash table with fine-grained locking
- O(1) for get/put/remove
- No null keys/values allowed
- Weakly consistent iterators
- CAS + synchronized (Java 8+)
- Use atomic operations for thread safety

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| HashMap | Non-thread-safe alternative |
| Hashtable | Legacy synchronized alternative |
| ConcurrentSkipListMap | Sorted concurrent map |
| Atomic operations | Thread-safe updates |
| Weakly consistent iterators | Iterator behavior |

## 21. Interview Questions

1. **How does ConcurrentHashMap achieve thread safety?** — CAS for empty slots, synchronized on bucket head for collision chains.

2. **What is the time complexity of ConcurrentHashMap operations?** — O(1) for get/put/remove.

3. **Does ConcurrentHashMap allow null keys/values?** — No. Throws NullPointerException.

4. **What is the difference between ConcurrentHashMap and Hashtable?** — ConcurrentHashMap: fine-grained locking. Hashtable: coarse-grained locking.

5. **What are weakly consistent iterators?** — Reflect state at some point since iterator was created, may not reflect concurrent modifications.

6. **When should you use ConcurrentHashMap?** — When thread-safe map is required with high concurrency.

## 22. References

- [Oracle Java Documentation - ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)
- [Java Concurrency in Practice](https://jcip.net/)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
