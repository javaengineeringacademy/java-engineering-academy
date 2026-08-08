# WeakHashMap

## Scope

This folder focuses exclusively on WeakHashMap.
Examples, exercises, and quizzes in this folder cover only WeakHashMap concepts.

## 1. Why It Exists

WeakHashMap was introduced in Java 1.2 to provide a Map implementation where entries can be garbage collected when their keys are no longer referenced elsewhere. This is useful for caches and metadata storage where you don't want to prevent garbage collection.

## 2. What It Is

WeakHashMap is a hash table-based Map implementation where keys are held via WeakReferences. When a key has no more strong references, it can be garbage collected, and the corresponding entry is automatically removed.

## 3. Internal Working

```java
// WeakHashMap uses WeakReference for keys
private static class Entry<K,V> extends WeakReference<Object> implements Map.Entry<K,V> {
    V value;
    int hash;
    Entry<K,V> next;
}

// When key is garbage collected:
// Entry is enqueued in ReferenceQueue
// Next access to map processes queue and removes stale entries
```

### WeakReference Behavior

```
WeakHashMap with keys: A, B, C

Key A: Strong reference exists
Key B: Strong reference exists
Key C: No strong references (only WeakReference in map)

When GC runs:
- Key C is garbage collected (no strong references)
- Entry for C is enqueued in ReferenceQueue
- Next map operation processes queue, removes stale entry

Result: Entry for C is removed from map
```

## 4. Constructors

```java
WeakHashMap<String, Integer> map = new WeakHashMap<>();                    // Default capacity 16, load factor 0.75
WeakHashMap<String, Integer> map = new WeakHashMap<>(100);                 // Custom initial capacity
WeakHashMap<String, Integer> map = new WeakHashMap<>(100, 0.5f);           // Custom capacity + load factor
WeakHashMap<String, Integer> map = new WeakHashMap<>(map);                  // From map
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
| `entrySet()` | Returns entry set | O(1) |

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
| entrySet() | O(1) | O(1) |

## 7. Thread Safety

WeakHashMap is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Map<String, Integer> syncMap = Collections.synchronizedMap(new WeakHashMap<>());

// Option 2: Explicit synchronization
synchronized (weakHashMap) {
    // Access weakHashMap
}

// Option 3: ConcurrentHashMap for concurrent access
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

## 8. Memory Behavior

### Memory Layout

```
WeakHashMap object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ table reference (8 bytes)   │──────┐
│ size (int, 4 bytes)         │      │
│ threshold (int, 4 bytes)    │      │
│ loadFactor (float, 4 bytes) │      │
│ queue reference (8 bytes)   │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Entry[] table

Per entry:
Entry object: ~40 bytes
├── Object header: 12 bytes
├── referent reference (WeakRef): 8 bytes
├── value reference: 8 bytes
├── hash (int): 4 bytes
├── next reference: 8 bytes
└── (padding 4 bytes)
```

### Memory Comparison

| Type | Per-Entry | 1M Entries |
|------|-----------|------------|
| WeakHashMap | ~40 bytes | ~40 MB |
| HashMap | ~32 bytes | ~32 MB |
| LinkedHashMap | ~48 bytes | ~48 MB |

## 9. Production Incidents

### Incident 1: Unexpected Entry Removal

**Problem:** Entries disappear from map unexpectedly.
**Cause:** Keys have no strong references, garbage collected.
**Impact:** Data loss, application malfunction.
**Detection:** Map size decreases over time.
**Solution:** Use strong references for keys that must persist.
**Prevention:** Understand WeakReference behavior.

### Incident 2: Memory Leak from Value References

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Values hold strong references to large objects.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows large objects retained by values.
**Solution:** Use WeakReference for values too, or clear entries.
**Prevention:** Monitor memory usage, limit cache size.

### Incident 3: Performance Degradation from Stale Entries

**Problem:** Application slows over time.
**Cause:** Stale entries accumulate before GC processes them.
**Impact:** Response time increases with map size.
**Detection:** Profiling shows time spent in stale entry cleanup.
**Solution:** Force cleanup with get() or put() operations.
**Prevention:** Regular map access triggers cleanup.

## 10. Engineering Decision Framework

### Use WeakHashMap when:
- Cache with weak keys needed
- Metadata storage where keys can be GC'd
-防止内存泄漏 (prevent memory leaks)
- Temporary associations

### Avoid WeakHashMap when:
- Entries must persist (use HashMap)
- Thread safety needed (use ConcurrentHashMap)
- Predictable behavior required (use HashMap)
- Values are large (memory overhead)

### When NOT to Use WeakHashMap
- **Need persistence**: WeakHashMap entries may disappear
- **Large data**: Reference overhead per entry
- **No cleanup needed**: Use HashMap (simpler)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| HashMap | Entries must persist |
| ConcurrentHashMap | Thread-safe cache |
| Caffeine | Production-grade caching |
| Guava Cache | Production-grade caching |
| SoftReference cache | Memory-sensitive cache |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Unexpected removal | Debug logging | Check for strong references |
| Memory leak | Heap dump | Check value references |
| Slow performance | Profiling | Check for stale entry cleanup |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] Understanding WeakReference behavior
- [ ] Strong references maintained for critical keys
- [ ] Not used for entries that must persist
- [ ] Thread safety handled
- [ ] Memory usage monitored
- [ ] Cache size limited if needed

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Unexpected data loss | Application malfunction | Use strong references for critical data |
| Memory exhaustion | OutOfMemoryError | Limit cache size |
| Timing attacks | Security risk | Use consistent timing |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | WeakHashMap introduced | Weak reference map |
| Java 4 | ReferenceQueue improvements | Better cleanup |
| Java 5 | Generics added | Type safety |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| WeakHashMap | 1.2 | Stable |
| Generics | 5.0 | Stable |

## 16. Best Practices

1. Understand WeakReference behavior before using
2. Use strong references for critical keys
3. Limit cache size to prevent memory issues
4. Consider Caffeine or Guava for production caching
5. Monitor memory usage and entry count

## 17. Common Mistakes

1. Assuming entries persist (they don't)
2. Using for critical data storage
3. Ignoring memory overhead of values
4. Not handling unexpected removal
5. Using in concurrent code without synchronization

## 18. Common Myths

### Myth 1: WeakHashMap is always safe for caching
**Reality:** Keys can be GC'd unexpectedly, causing data loss.

### Myth 2: WeakHashMap prevents memory leaks
**Reality:** Only for keys, values can still cause leaks.

### Myth 3: WeakHashMap is thread-safe
**Reality:** Not thread-safe. Use ConcurrentHashMap.

### Myth 4: WeakHashMap is always garbage collected
**Reality:** Only when keys have no strong references.

## 19. One-Minute Revision

- Hash table with weak references for keys
- Entries removed when keys have no strong references
- Useful for caches and metadata storage
- Not thread-safe, use ConcurrentHashMap
- Not for entries that must persist
- Consider Caffeine for production caching

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| WeakReference | Key reference type |
| ReferenceQueue | Cleanup mechanism |
| HashMap | Strong reference alternative |
| Cache patterns | Use case |
| Garbage collection | Entry removal trigger |

## 21. Interview Questions

1. **How does WeakHashMap work?** — Uses WeakReference for keys. Entries removed when keys have no strong references.

2. **When are entries removed from WeakHashMap?** — When keys are garbage collected (no strong references).

3. **Is WeakHashMap thread-safe?** — No. Use ConcurrentHashMap or synchronized wrapper.

4. **What is the difference between WeakHashMap and HashMap?** — WeakHashMap uses WeakReference for keys, HashMap uses strong references.

5. **When should you use WeakHashMap?** — For caches where keys can be garbage collected.

## 22. References

- [Oracle Java Documentation - WeakHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/WeakHashMap.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 5: Prefer dependencies injection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
