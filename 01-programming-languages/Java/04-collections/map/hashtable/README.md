# Hashtable

## 1. Why It Exists

Hashtable was introduced in Java 1.0 as a synchronized hash table implementation. It was the original key-value store before HashMap. In Java 1.2, HashMap was introduced as a faster, non-synchronized alternative.

## 2. What It Is

Hashtable is a legacy synchronized implementation of the Map interface. It uses a hash table internally, similar to HashMap, but every method is synchronized for thread safety. It does not allow null keys or values.

## 3. Internal Working

```java
// Hashtable uses Entry[] internally
private transient Entry<?,?>[] table;
private int count;
private int threshold;
private float loadFactor;

// Entry structure
private static class Entry<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Entry<K,V> next;
}
```

### Growth Factor

| Initial | After 10 adds | After 100 adds | After 1000 adds |
|---------|---------------|----------------|-----------------|
| 11 | 23 | 281 | 4,447 |

Hashtable doubles capacity + 1 (2x + 1) vs HashMap 2x.

## 4. Constructors

```java
Hashtable<String, Integer> table = new Hashtable<>();                    // Default capacity 11, load factor 0.75
Hashtable<String, Integer> table = new Hashtable<>(100);                 // Custom initial capacity
Hashtable<String, Integer> table = new Hashtable<>(100, 0.5f);           // Custom capacity + load factor
Hashtable<String, Integer> table = new Hashtable<>(map);                  // From map
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
| `contains(Object value)` | Alias for containsValue | O(n) |
| `size()` | Returns entry count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all entries | O(n) |

### Legacy Methods (Avoid)

| Method | Description |
|--------|-------------|
| `keys()` | Returns Enumeration of keys |
| `elements()` | Returns Enumeration of values |
| `putAll(Map)` | Copies all mappings |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| put(K, V) | O(1) | O(1) |
| get(K) | O(1) | O(1) |
| remove(K) | O(1) | O(1) |
| containsKey(K) | O(1) | O(1) |
| containsValue(V) | O(n) | O(1) |
| size() | O(1) | O(1) |
| keys() | O(1) | O(1) |
| elements() | O(1) | O(1) |

Note: All operations have additional synchronized lock overhead.

## 7. Thread Safety

Hashtable is synchronized:

```java
// Hashtable.add() is synchronized
public synchronized V put(K key, V value) {
    // Make sure the value is not null
    if (value == null) {
        throw new NullPointerException();
    }

    // Makes sure the key is not already in the hashtable.
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    // ...
}
```

### Problem: Compound Operations Not Atomic

```java
// NOT thread-safe even with Hashtable!
if (!table.containsKey(key)) {
    table.put(key, value);  // Another thread may put between containsKey() and put()
}
```

## 8. Memory Behavior

### Memory Layout

```
Hashtable object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ table reference (8 bytes)   │──────┐
│ count (int, 4 bytes)        │      │
│ threshold (int, 4 bytes)    │      │
│ loadFactor (float, 4 bytes) │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Entry[] table

Per entry:
Entry object: ~32 bytes
├── Object header: 12 bytes
├── hash (int): 4 bytes
├── key reference: 8 bytes
├── value reference: 8 bytes
└── next reference: 8 bytes
```

### Memory Comparison

| Type | Per-Entry | 1M Entries |
|------|-----------|------------|
| Hashtable | ~32 bytes + lock | ~32 MB + lock |
| HashMap | ~32 bytes | ~32 MB |

## 9. Production Incidents

### Incident 1: Performance Degradation Under Load

**Problem:** Application slows from 10ms to 500ms under concurrent load.
**Cause:** Hashtable's coarse-grained locking causing contention.
**Impact:** Service degraded, user experience poor.
**Detection:** Thread dump shows threads waiting on Hashtable's monitor.
**Solution:** Switch to ConcurrentHashMap.
**Prevention:** Use modern concurrent collections.

### Incident 2: Wasted Memory from 2x + 1 Growth

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Hashtable's 2x + 1 growth factor wasting memory.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows 50% unused capacity.
**Solution:** Switch to HashMap with 2x growth factor.
**Prevention:** Use HashMap for better memory efficiency.

### Incident 3: Legacy Code Maintained Hashtable

**Problem:** New developers confused by Hashtable in codebase.
**Cause:** Legacy code still using Hashtable instead of HashMap.
**Impact:** Developer confusion, maintenance overhead.
**Detection:** Code review shows Hashtable usage.
**Solution:** Migrate to HashMap or ConcurrentHashMap.
**Prevention:** Establish coding standards against Hashtable.

## 10. Engineering Decision Framework

### Use Hashtable when:
- Maintaining legacy code that already uses Hashtable
- Required by external library or API
- Simple synchronized map needed (but prefer alternatives)

### Avoid Hashtable when:
- Writing new code (use HashMap)
- Performance matters (synchronized overhead)
- Concurrent access needed (use ConcurrentHashMap)
- Null keys/values needed (use HashMap)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| HashMap | General purpose, no synchronization |
| ConcurrentHashMap | Thread-safe with fine-grained locking |
| Collections.synchronizedMap() | When you need synchronization on HashMap |
| EnumMap | Enum keys |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow performance | Profiling (JFR, VisualVM) | Check for Hashtable contention |
| ConcurrentModificationException | Thread dump | Find which thread modifying |
| Memory leak | Heap dump | Check for unused Hashtable references |
| Legacy code confusion | Code review | Migrate to HashMap |

## 12. Code Review Checklist

- [ ] Not using Hashtable in new code
- [ ] Migrating legacy Hashtable to HashMap
- [ ] Using ConcurrentHashMap for concurrent access
- [ ] Not using legacy methods (keys(), elements())
- [ ] Considering thread safety requirements
- [ ] Checking for compound operation atomicity
- [ ] Performance testing under concurrent load

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max capacity |
| Deadlock from synchronization | Service hang | Use fine-grained locking |
| Legacy code vulnerabilities | Security risk | Migrate to modern collections |
| Null injection | NullPointerException | Validate inputs |

## 14. Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Hashtable introduced | Use as synchronized map |
| Java 1.2 | HashMap introduced | Migrate to HashMap |
| Java 1.2 | Collections.synchronizedMap() | Wrap HashMap if needed |
| Java 5 | ConcurrentHashMap | Use for concurrent access |
| Java 5 | Generics added | Add type parameters |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Hashtable | 1.0 | Legacy (avoid) |
| HashMap | 1.2 | Recommended |
| ConcurrentHashMap | 5.0 | Recommended |

## 16. Best Practices

1. **Avoid in new code**: Use HashMap or ConcurrentHashMap
2. **Migrate existing**: Replace Hashtable with HashMap
3. **Use legacy methods**: Avoid keys(), elements(), contains()
4. **Consider thread safety**: Hashtable's synchronization is coarse-grained
5. **Monitor performance**: Hashtable adds overhead even in single-threaded code
6. **Use modern alternatives**: ConcurrentHashMap for concurrent access

## 17. Common Mistakes

1. **Using Hashtable as default**: HashMap is faster and more memory efficient
2. **Thinking Hashtable is thread-safe for compound operations**: Contains-then-put is not atomic
3. **Using legacy methods**: keys(), elements(), contains() are obsolete
4. **Ignoring synchronization overhead**: Hashtable is slower than HashMap even in single-threaded code
5. **Not migrating**: Legacy Hashtable code should be updated

## 18. Common Myths

### Myth 1: Hashtable is always thread-safe
**Reality:** Individual methods are synchronized, but compound operations are not atomic.

### Myth 2: Hashtable is better than HashMap
**Reality:** HashMap is faster and more memory efficient for most use cases.

### Myth 3: Hashtable is deprecated
**Reality:** Not deprecated, but discouraged in favor of HashMap/ConcurrentHashMap.

### Myth 4: Hashtable allows null keys/values
**Reality:** Hashtable throws NullPointerException for null keys or values.

## 19. One-Minute Revision

- Legacy synchronized hash table (Java 1.0)
- Every method synchronized, causing overhead
- 2x + 1 growth factor wastes memory
- No null keys or values allowed
- Avoid in new code, use HashMap or ConcurrentHashMap
- Use ConcurrentHashMap for concurrent access

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| HashMap | Modern alternative, non-synchronized |
| ConcurrentHashMap | Thread-safe alternative |
| Collections.synchronizedMap() | Wraps HashMap with synchronization |
| Legacy code | Often contains Hashtable, should migrate |
| Enumeration | Legacy iteration (use Iterator instead) |

## 21. Interview Questions

1. **What is the difference between Hashtable and HashMap?** — Hashtable is synchronized, HashMap is not. Hashtable does not allow null, HashMap allows one null key.

2. **Is Hashtable thread-safe?** — Yes, individual methods are synchronized. But compound operations are not atomic.

3. **When should you use Hashtable?** — Almost never in new code. Only in legacy code that already uses Hashtable.

4. **What are the legacy methods in Hashtable?** — keys(), elements(), contains().

5. **What is the growth factor of Hashtable?** — 2x + 1 (doubles capacity plus one). HashMap uses 2x.

6. **How do you make HashMap thread-safe?** — Use Collections.synchronizedMap() or ConcurrentHashMap.

## 22. References

- [Oracle Java Documentation - Hashtable](https://docs.oracle.com/javase/8/docs/api/java/util/Hashtable.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 54: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
