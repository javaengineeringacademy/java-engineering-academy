# LinkedHashMap

## Scope

This folder focuses exclusively on LinkedHashMap.
Examples, exercises, and quizzes in this folder cover only LinkedHashMap concepts.

## 1. Why It Exists

LinkedHashMap was introduced in Java 1.4 to provide a Map implementation that maintains either insertion order or access order. HashMap loses ordering, which is problematic when you need both key-value mapping and predictable iteration order.

## 2. What It Is

LinkedHashMap is a hash table with linked list implementation of the Map interface. It extends HashMap and maintains a doubly-linked list running through all entries. It can maintain insertion order or access order (for LRU caches).

## 3. Internal Working

```java
// LinkedHashMap extends HashMap
// Maintains doubly-linked list through entries
public class LinkedHashMap<K,V> extends HashMap<K,V> {
    transient Entry<K,V> head;  // Head of linked list
    transient Entry<K,V> tail;  // Tail of linked list
    final boolean accessOrder;   // false = insertion order, true = access order
}

// Entry extends HashMap.Node
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before;  // Previous in linked list
    Entry<K,V> after;   // Next in linked list
}
```

### Linked List Structure

```
LinkedHashMap structure:
table[] buckets:
table[0] → null
table[1] → Node("A") → Node("E") → null
table[2] → Node("B") → null
...

Doubly-linked list (insertion order):
head -> Node("A") <-> Node("B") <-> Node("C") <-> Node("D") <-> Node("E") <- tail

Iteration follows linked list, not bucket order
```

### Access Order Mode

```
// When accessOrder = true:
// get("C") moves "C" to end of linked list
// LRU cache behavior

Before get("C"):
head -> A <-> B <-> C <-> D <-> E <- tail

After get("C"):
head -> A <-> B <-> D <-> E <-> C <- tail
```

## 4. Constructors

```java
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();                      // Insertion order
LinkedHashMap<String, Integer> map = new LinkedHashMap<>(100);                   // Custom capacity
LinkedHashMap<String, Integer> map = new LinkedHashMap<>(100, 0.75f);            // Custom capacity + load factor
LinkedHashMap<String, Integer> map = new LinkedHashMap<>(100, 0.75f, true);      // Access order (LRU)
LinkedHashMap<String, Integer> map = new LinkedHashMap<>(map);                    // From map
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

### View Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `keySet()` | Set of keys (insertion/access order) | O(1) |
| `values()` | Collection of values (order) | O(1) |
| `entrySet()` | Set of entries (order) | O(1) |

### LinkedHashMap-Specific Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `removeEldestEntry(Map.Entry)` | Override for LRU behavior | O(1) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| put(K, V) | O(1) | O(1) |
| get(K) | O(1) | O(1) |
| remove(K) | O(1) | O(1) |
| containsKey(K) | O(1) | O(1) |
| containsValue(V) | O(n) | O(1) |
| size() | O(1) | O(1) |
| keySet() | O(1) | O(1) |
| values() | O(1) | O(1) |
| entrySet() | O(1) | O(1) |

## 7. Thread Safety

LinkedHashMap is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Map<String, Integer> syncMap = Collections.synchronizedMap(new LinkedHashMap<>());

// Option 2: Explicit synchronization
synchronized (linkedHashMap) {
    // Access linkedHashMap
}
```

## 8. Memory Behavior

### Memory Layout

```
LinkedHashMap object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ table reference (8 bytes)   │──────┐
│ head reference (8 bytes)    │      │
│ tail reference (8 bytes)    │      │
│ size (int, 4 bytes)         │      │
│ threshold (int, 4 bytes)    │      │
│ loadFactor (float, 4 bytes) │      │
│ accessOrder (boolean, 1B)   │      │
│ (padding 3 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Entry object: ~48 bytes
                              ├── Object header: 12 bytes
                              ├── hash (int): 4 bytes
                              ├── key reference: 8 bytes
                              ├── value reference: 8 bytes
                              ├── next reference: 8 bytes (hash chain)
                              ├── before reference: 8 bytes (linked list)
                              └── after reference: 8 bytes (linked list)
```

### Memory Comparison

| Type | Per-Entry | 1M Entries |
|------|-----------|------------|
| LinkedHashMap | ~48 bytes | ~48 MB |
| HashMap | ~32 bytes | ~32 MB |
| TreeMap | ~56 bytes | ~56 MB |

## 9. Production Incidents

### Incident 1: Memory Leak from Unbounded LRU Cache

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** LRU cache without size limit.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows growing map.
**Solution:** Override removeEldestEntry() to limit size.
**Prevention:** Always limit cache size.

### Incident 2: Slow Performance with Access Order

**Problem:** Application slows under high access.
**Cause:** Access order mode updates linked list on every get().
**Impact:** Response time increases with map size.
**Detection:** Profiling shows 99% time in linked list updates.
**Solution:** Use insertion order if access order not needed.
**Prevention:** Use access order only for LRU caches.

### Incident 3: Stale Data in Access Order Mode

**Problem:** Old entries not evicted from cache.
**Cause:** Access order mode moves accessed entries to end.
**Impact:** Cache not effective.
**Detection:** Monitoring shows old entries not evicted.
**Solution:** Override removeEldestEntry() correctly.
**Prevention:** Test LRU behavior thoroughly.

## 10. Engineering Decision Framework

### When Should I Use This?
- Insertion order matters
- Access order (LRU cache) needed
- Both HashMap speed and order needed
- Building LRU caches

### When Should I NOT Use This?
- **Order doesn't matter**: Use HashMap (simpler, less memory)
- **Sorted keys needed**: Use TreeMap
- **Memory is constrained**: Use HashMap (lower memory)
- **Thread safety needed**: Use ConcurrentHashMap

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| HashMap | Order doesn't matter | Faster, no ordering |
| TreeMap | Sorted keys needed | O(log n), sorted |
| ConcurrentHashLRUMap | Thread-safe LRU cache | Higher overhead |
| Caffeine | Production-grade caching | Feature-rich, faster |
| Guava Cache | Production-grade caching | Google library |

### What Trade-offs Am I Making?
- **Order vs Memory**: Medium memory for insertion/order access
- **Order vs Speed**: Slightly slower than HashMap
- **Immutability vs Flexibility**: Mutable vs Map.of() (immutable)
- **Thread Safety**: Not thread-safe by default

### What Would I Choose in Production?
> Use LinkedHashMap for insertion order — it's almost as fast as HashMap. Use HashMap if order doesn't matter — it's faster and uses less memory. For LRU caches, set accessOrder=true and override removeEldestEntry().

### Common Code Review Comments
- "Why are you using LinkedHashMap? HashMap is faster if you don't need order."
- "This LinkedHashMap is for LRU cache — make sure to override removeEldestEntry()."
- "Consider using Map.of() if this map is immutable."
- "This map is being iterated concurrently — use Collections.synchronizedMap()."

### Common Production Mistakes

> Notice: LinkedHashMap is slightly slower than HashMap — don't use it if order doesn't matter.

> Notice: LinkedHashMap memory overhead is higher than HashMap — each entry has a linked list node.

> Notice: LinkedHashMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: LinkedHashMap(accessOrder=true) moves entries on access — this affects iteration order.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory overhead | Heap dump | Check for linked list pointers |
| Slow performance | Profiling | Check linked list updates |
| Order not maintained | Debug logging | Check accessOrder flag |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] Using LinkedHashMap for insertion/access order
- [ ] Not using when order doesn't matter (use HashMap)
- [ ] Considering memory overhead (48 vs 32 bytes)
- [ ] LRU cache size limited via removeEldestEntry()
- [ ] Thread safety handled
- [ ] accessOrder flag set correctly

## 13. Architecture Considerations

### Where LinkedHashMap Fits in System Design

| Layer | Use Case | Why LinkedHashMap |
|-------|----------|-------------------|
| Service Layer | LRU cache implementation | Access order mode |
| API Gateway | Recent request tracking | Insertion order |
| Caching | In-memory cache with eviction | removeEldestEntry() |
| Session Store | Session ordering | Insertion/access order |
| UI Layer | Recent items list | Insertion order for display |

### Integration Patterns

```
Client → API Gateway → LinkedHashMap → Service → LinkedHashMap → Client
                    ↓
            LinkedHashMap → LRU Cache Manager → LinkedHashMap
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K entries | LinkedHashMap is optimal |
| 10K - 100K entries | LinkedHashMap with proper sizing |
| 100K - 1M entries | Consider Caffeine or Guava Cache |
| > 1M entries | Consider Redis or external cache |

### When to Replace LinkedHashMap in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| Order doesn't matter | HashMap | Less memory overhead |
| Sorted keys | TreeMap | O(log n) sorted operations |
| Thread-safe ordered | Collections.synchronizedMap() | Concurrent access |
| Production caching | Caffeine/Guava | Feature-rich, thread-safe |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Limit cache size |
| Unbounded growth | DoS | Override removeEldestEntry() |
| Mutable key manipulation | Data corruption | Use immutable objects |

## 15. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.4 | LinkedHashMap introduced | Insertion/access ordered map |
| Java 8 | Stream support | Stream processing |
| Java 9 | Map.of() factory | Immutable map alternatives |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| LinkedHashMap | 1.4 | Stable |
| Stream support | 8.0 | Stable |
| Map.of() | 9.0 | Stable |

## 17. Best Practices

1. Use insertion order for predictable iteration
2. Use access order for LRU caches
3. Override removeEldestEntry() to limit size
4. Set initial capacity for known sizes
5. Consider memory overhead vs HashMap

## 18. Common Mistakes

1. Using when order doesn't matter (use HashMap)
2. Not limiting LRU cache size
3. Ignoring memory overhead (48 vs 32 bytes)
4. Using for sorted data (use TreeMap)
5. Using mutable objects as keys

## 19. Common Myths

### Myth 1: LinkedHashMap is always slower than HashMap
**Reality:** Same O(1) operations, but more memory overhead.

### Myth 2: LinkedHashMap maintains sorted order
**Reality:** Maintains insertion or access order, not sorted order. Use TreeMap for sorted.

### Myth 3: LinkedHashMap is thread-safe
**Reality:** Not thread-safe. Use ConcurrentHashMap or synchronized wrapper.

### Myth 4: LinkedHashMap allows multiple null keys
**Reality:** Allows at most one null key.

## 20. One-Minute Revision

- Hash table with linked list for insertion/access order
- O(1) for get/put/remove
- Maintains insertion order or access order (LRU)
- More memory overhead than HashMap (48 vs 32 bytes)
- Best for insertion order or LRU caches
- Not thread-safe, use concurrent collections

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| HashMap | Unordered variant |
| LinkedHashSet | Set variant (uses LinkedHashMap) |
| LRU Cache | Access order mode use case |
| Insertion order | Key feature |
| hashCode/equals | Contract for keys |

## 22. Interview Questions

1. **How does LinkedHashMap maintain insertion order?** — Uses doubly-linked list connecting all entries.

2. **What is access order mode?** — When accessOrder=true, get() moves entry to end of linked list (LRU behavior).

3. **How do you implement LRU cache with LinkedHashMap?** — Override removeEldestEntry() to return true when size exceeds limit.

4. **What is the time complexity of LinkedHashMap operations?** — O(1) for get/put/remove.

5. **Is LinkedHashMap thread-safe?** — No. Use ConcurrentHashMap or synchronized wrapper.

## 23. References

- [Oracle Java Documentation - LinkedHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
