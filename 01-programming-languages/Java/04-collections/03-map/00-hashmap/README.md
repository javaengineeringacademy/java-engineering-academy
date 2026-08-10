# HashMap

## Scope

This folder focuses exclusively on HashMap.
Examples, exercises, and quizzes in this folder cover only HashMap concepts.

## 1. Why It Exists

HashMap was introduced in Java 1.2 to provide a hash table implementation of the Map interface. It replaced the legacy Hashtable, offering better performance by not synchronizing every method and allowing null keys/values.

## 2. What It Is

HashMap is a hash table implementation of the Map interface. It stores key-value pairs, provides O(1) average-time for get/put operations, and allows one null key and multiple null values.

## 3. Internal Working

```java
// HashMap uses an array of Node buckets
transient Node<K,V>[] table;
transient int size;
int threshold;
final float loadFactor;
```

### Hash Table Structure

```
HashMap object:
┌──────────────────────────────┐
│ table → Node[] (reference)   │──────┐
│ size (int)                   │      │
│ threshold (int)              │      │
│ loadFactor (float)           │      ▼
└──────────────────────────────┘      Node[] bucket array:
                                      ┌────────────────────────┐
                                      │ [0] → null             │
                                      │ [1] → Node → Node → .. │
                                      │ [2] → null             │
                                      │ [3] → Node             │
                                      │ ...                    │
                                      └────────────────────────┘
```

### Node Structure

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

### Hash Code and Bucket Index

```java
// Bucket index calculation
int hash = hash(key.hashCode());
int index = hash & (n - 1);  // n = table.length

// Hash function (Java 8+)
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### Collision Resolution

```
Bucket with collision:
table[1] → Node("A", hash=5) → Node("E", hash=5) → null

Both "A" and "E" hash to same bucket (index 1)
Stored as linked list in bucket
```

### Treeification (Java 8+)

```
Bucket with 8+ entries:
table[1] → TreeNode → TreeNode → ... (red-black tree)

When bucket has 8+ entries, converts to tree for O(log n) lookup
When bucket has 6 or fewer entries, converts back to linked list
```

### Resize Operation

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1;  // double capacity
    }
    // ...
}
```

## 4. Constructors

```java
HashMap<String, Integer> map = new HashMap<>();                      // Default capacity 16, load factor 0.75
HashMap<String, Integer> map = new HashMap<>(100);                   // Custom initial capacity
HashMap<String, Integer> map = new HashMap<>(100, 0.5f);             // Custom capacity + load factor
HashMap<String, Integer> map = new HashMap<>(map);                    // From map
HashMap<String, Integer> map = new HashMap<>(Map.of("a", 1, "b", 2)); // From map literal
```

## 5. Methods

### Map Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `put(K key, V value)` | Associates key with value | O(1) amortized |
| `get(Object key)` | Returns value for key | O(1) amortized |
| `remove(Object key)` | Removes key-value pair | O(1) amortized |
| `containsKey(Object key)` | Checks if key exists | O(1) amortized |
| `containsValue(Object value)` | Checks if value exists | O(n) |
| `size()` | Returns entry count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all entries | O(n) |

### View Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `keySet()` | Set of all keys | O(1) |
| `values()` | Collection of all values | O(1) |
| `entrySet()` | Set of Map.Entry<K,V> | O(1) |

### Default Methods (Java 8+)

| Method | Description | Complexity |
|--------|-------------|------------|
| `getOrDefault(key, default)` | Returns value or default | O(1) |
| `putIfAbsent(key, value)` | Puts only if key absent | O(1) |
| `remove(key, value)` | Removes only if matches | O(1) |
| `compute(key, mappingFunction)` | Computes new value | O(1) |
| `computeIfAbsent(key, mappingFunction)` | Computes if key absent | O(1) |
| `computeIfPresent(key, remappingFunction)` | Computes if key present | O(1) |
| `merge(key, value, remappingFunction)` | Merges values | O(1) |
| `forEach(action)` | Iterates over entries | O(n) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| put(K, V) | O(1) amortized | O(1) |
| get(K) | O(1) amortized | O(1) |
| remove(K) | O(1) amortized | O(1) |
| containsKey(K) | O(1) amortized | O(1) |
| containsValue(V) | O(n) | O(1) |
| size() | O(1) | O(1) |
| isEmpty() | O(1) | O(1) |
| keySet() | O(1) | O(1) |
| values() | O(1) | O(1) |
| entrySet() | O(1) | O(1) |
| forEach(action) | O(n) | O(1) |

## 7. Thread Safety

HashMap is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// Option 2: ConcurrentHashMap for concurrent access
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

// Option 3: Explicit synchronization
synchronized (hashMap) {
    // Access hashMap
}
```

## 8. Memory Behavior

### Memory Layout

```
HashMap object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ table reference (8 bytes)   │──────┐
│ size (int, 4 bytes)         │      │
│ threshold (int, 4 bytes)    │      │
│ loadFactor (float, 4 bytes) │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Node[] table:
                              ┌────────────────────────┐
                              │ [0] → null             │
                              │ [1] → Node → Node      │
                              │ [2] → null             │
                              │ [3] → Node             │
                              └────────────────────────┘

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
| HashMap | ~32 bytes | ~32 MB |
| TreeMap | ~56 bytes | ~56 MB |
| LinkedHashMap | ~40 bytes | ~40 MB |
| Hashtable | ~32 bytes | ~32 MB |

## 9. Production Incidents

### Incident 1: Hash Collision DoS Attack

**Problem:** Web endpoint slows from 10ms to 5 seconds under attack.
**Cause:** Attacker sends requests with keys that hash to same bucket.
**Impact:** DoS condition, service degraded.
**Detection:** Profiling shows 99% time in hash lookup.
**Solution:** Use ConcurrentHashMap with bounded bucket chain length.
**Prevention:** Rate limiting, randomized hash functions (SipHash).

### Incident 2: Memory Leak from Mutable Keys

**Problem:** Application crashes with OutOfMemoryError after hours.
**Cause:** Mutable objects used as HashMap keys, hash changes after insertion.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows duplicate entries with different hashes.
**Solution:** Use immutable objects as keys, or override hashCode/equals correctly.
**Prevention:** Use immutable objects, test hashCode/equals contract.

### Incident 3: Poor Performance from Bad hashCode()

**Problem:** Application latency spikes, CPU at 100%.
**Cause:** Badly implemented hashCode() causing many collisions.
**Impact:** Response time increases linearly with map size.
**Detection:** Profiling shows 99% time in hash bucket traversal.
**Solution:** Fix hashCode() implementation to distribute evenly.
**Prevention:** Test hashCode() distribution, use well-known implementations.

## 10. Engineering Decision Framework

### When Should I Use This?
- Fastest lookup required (O(1))
- No ordering needed
- One null key/value allowed
- General-purpose key-value storage
- You don't have a specific reason to use something else

### When Should I NOT Use This?
- **Sorted keys needed**: Use TreeMap
- **Insertion order matters**: Use LinkedHashMap
- **Thread safety needed**: Use ConcurrentHashMap
- **Null not allowed**: Use Hashtable or ConcurrentHashMap
- **Enum keys**: Use EnumMap (faster)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| LinkedHashMap | Insertion/access order matters | Higher memory, slightly slower |
| TreeMap | Sorted keys needed | O(log n) vs O(1) |
| Hashtable | Legacy code (avoid in new code) | Synchronized, slower |
| ConcurrentHashMap | Thread-safe access | No null keys/values, higher overhead |
| WeakHashMap | Weak references for caching | Keys can be GC'd |
| EnumMap | Enum keys | Fastest for enum keys |
| Map.of() | Immutable map | Thread-safe, no modification |

### What Trade-offs Am I Making?
- **Performance**: O(1) lookup vs O(log n) sorted
- **Memory**: Compact vs node-based overhead
- **Thread Safety**: Not thread-safe by default vs synchronized alternatives
- **Ordering**: No ordering vs sorted/insertion order

### What Would I Choose in Production?
> For most applications, HashMap is the default choice. Only switch if you need ordering (TreeMap/LinkedHashMap) or thread safety (ConcurrentHashMap). Never use Hashtable in new code.

### Common Code Review Comments
- "Why are you using Hashtable? Use ConcurrentHashMap instead."
- "This map is being accessed concurrently — use ConcurrentHashMap."
- "Consider using Map.of() if this map is immutable."
- "This should be an EnumMap — you're using enum values as keys."

### Common Production Mistakes

> Notice: HashMap doesn't maintain order — if you need insertion order, use LinkedHashMap.

> Notice: HashMap allows one null key and multiple null values — but in concurrent code, prefer Optional over null.

> Notice: HashMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: HashMap.hashCode() is called for each key — make sure your hashCode() implementation is efficient.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow performance | Profiling (JFR, VisualVM) | Check for hash collisions |
| Memory leak | Heap dump (jmap, MAT) | Check for mutable keys |
| Null pointer | Debug logging | Check hashCode/equals |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

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
