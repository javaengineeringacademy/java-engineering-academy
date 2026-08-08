# TreeMap

## 1. Why It Exists

TreeMap was introduced in Java 1.2 to provide a Map implementation that maintains keys in sorted order. HashMap does not maintain ordering, which is problematic when you need both key-value mapping and sorted key iteration.

## 2. What It Is

TreeMap is a NavigableMap implementation based on a Red-Black tree. It maintains keys in natural order or by a provided Comparator. All basic operations (get, put, remove) are O(log n).

## 3. Internal Working

```java
// TreeMap uses a Red-Black tree
private final Comparator<? super K> comparator;
private transient Entry<K,V> root;

static final class Entry<K,V> implements Map.Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
}
```

### Red-Black Tree Properties

```
TreeMap internal structure:
                    Node(D, value)
                   /              \
              Node(B, value)      Node(F, value)
             /       \           /       \
        Node(A) Node(C)    Node(E)    Node(G)

Properties:
- Root is black
- All leaves are null and black
- Red nodes have black children
- All paths from root to leaves have same number of black nodes
- Height: O(log n)
```

### Tree Rotation

```
Left rotation on Node(B):
Before:        After:
    D              B
   / \            / \
  B   F    ->    A   D
 / \               / \
A   C             C   F
```

## 4. Constructors

```java
TreeMap<String, Integer> map = new TreeMap<>();                         // Natural order
TreeMap<String, Integer> map = new TreeMap<>(Comparator.reverseOrder()); // Custom comparator
TreeMap<String, Integer> map = new TreeMap<>(map);                       // From map
TreeMap<String, Integer> map = new TreeMap<>(Map.of("a", 1, "b", 2));  // From map literal
```

## 5. Methods

### Map Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `put(K key, V value)` | Associates key with value | O(log n) |
| `get(Object key)` | Returns value for key | O(log n) |
| `remove(Object key)` | Removes key-value pair | O(log n) |
| `containsKey(Object key)` | Checks if key exists | O(log n) |
| `containsValue(Object value)` | Checks if value exists | O(n) |
| `size()` | Returns entry count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all entries | O(n) |

### NavigableMap Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `firstKey()` | Returns lowest key | O(log n) |
| `lastKey()` | Returns highest key | O(log n) |
| `lowerKey(K key)` | Greatest key less than key | O(log n) |
| `higherKey(K key)` | Least key greater than key | O(log n) |
| `floorKey(K key)` | Greatest key <= key | O(log n) |
| `ceilingKey(K key)` | Least key >= key | O(log n) |
| `headMap(K to)` | Keys less than to | O(log n) |
| `tailMap(K from)` | Keys >= from | O(log n) |
| `subMap(K from, K to)` | Keys in range [from, to) | O(log n) |
| `pollFirstEntry()` | Removes and returns lowest | O(log n) |
| `pollLastEntry()` | Removes and returns highest | O(log n) |

### View Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `keySet()` | Sorted set of keys | O(1) |
| `values()` | Collection of values | O(1) |
| `entrySet()` | Sorted set of entries | O(1) |
| `descendingMap()` | Reverse order map | O(1) |
| `navigableKeySet()` | Navigable set of keys | O(1) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| put(K, V) | O(log n) | O(1) |
| get(K) | O(log n) | O(1) |
| remove(K) | O(log n) | O(1) |
| containsKey(K) | O(log n) | O(1) |
| containsValue(V) | O(n) | O(1) |
| size() | O(1) | O(1) |
| firstKey() | O(log n) | O(1) |
| lastKey() | O(log n) | O(1) |
| lowerKey(K) | O(log n) | O(1) |
| higherKey(K) | O(log n) | O(1) |
| headMap(K) | O(log n) | O(1) |
| tailMap(K) | O(log n) | O(1) |
| subMap(K, K) | O(log n) | O(1) |

## 7. Thread Safety

TreeMap is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
SortedMap<String, Integer> syncMap = Collections.synchronizedSortedMap(new TreeMap<>());

// Option 2: Explicit synchronization
synchronized (treeMap) {
    // Access treeMap
}

// Option 3: ConcurrentSkipListMap for concurrent sorted map
ConcurrentSkipListMap<String, Integer> concurrentMap = new ConcurrentSkipListMap<>();
```

## 8. Memory Behavior

### Memory Layout

```
TreeMap object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ root reference (8 bytes)    │──────┐
│ comparator (8 bytes)        │      │
│ size (int, 4 bytes)         │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Entry structure:
                              ┌────────────────────────┐
                              │ key reference (8 bytes) │
                              │ value reference (8B)    │
                              │ left reference (8B)     │
                              │ right reference (8B)    │
                              │ parent reference (8B)   │
                              │ color (boolean, 1B)     │
                              │ (padding 7 bytes)       │
                              └────────────────────────┘

Per entry: ~56 bytes
```

### Memory Comparison

| Type | Per-Entry | 1M Entries |
|------|-----------|------------|
| TreeMap | ~56 bytes | ~56 MB |
| HashMap | ~32 bytes | ~32 MB |
| LinkedHashMap | ~40 bytes | ~40 MB |

## 9. Production Incidents

### Incident 1: NullPointerException with null Key

**Problem:** NullPointerException when putting null key.
**Cause:** TreeMap does not allow null keys (unlike HashMap).
**Impact:** Application crash.
**Solution:** Never put null key in TreeMap.
**Prevention:** Validate inputs before adding.

### Incident 2: Slow Performance with Bad Comparator

**Problem:** Application latency spikes.
**Cause:** Comparator causes unbalanced tree.
**Impact:** Response time increases with map size.
**Solution:** Ensure comparator produces balanced comparisons.
**Prevention:** Test comparator with various inputs.

### Incident 3: Memory Leak from Mutable Keys

**Problem:** Cannot find entries after insertion.
**Cause:** Key's compareTo() depends on mutable fields.
**Impact:** Data inconsistency.
**Solution:** Use immutable objects as keys.
**Prevention:** Use immutable objects, test compareTo() contract.

## 10. Engineering Decision Framework

### Use TreeMap when:
- Sorted keys required
- Range queries needed (headMap, tailMap, subMap)
- Navigation needed (lower, higher, floor, ceiling)
- Consistent ordering required

### Avoid TreeMap when:
- O(1) operations needed (use HashMap)
- Memory is constrained (use HashMap)
- Insertion order needed (use LinkedHashMap)
- Thread safety needed (use ConcurrentSkipListMap)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| HashMap | O(1) operations, no order |
| LinkedHashMap | Insertion/access order |
| Hashtable | Legacy code (avoid) |
| ConcurrentSkipListMap | Thread-safe sorted map |
| EnumMap | Enum keys |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| NullPointerException | Debug logging | Check for null keys |
| Slow performance | Profiling | Check comparator balance |
| Elements not found | Debug logging | Check compareTo() implementation |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] Keys implement Comparable or custom Comparator provided
- [ ] Comparator handles all edge cases
- [ ] No null keys added
- [ ] Mutable objects not used as keys
- [ ] Thread safety handled
- [ ] Initial capacity not applicable (tree grows dynamically)

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size |
| DoS via bad comparator | Service degradation | Validate comparator |
| Null injection | NullPointerException | Validate inputs |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | TreeMap introduced | Sorted map |
| Java 8 | Stream support | Stream processing |
| Java 9 | Map.of() factory | Immutable map alternatives |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| TreeMap | 1.2 | Stable |
| Stream support | 8.0 | Stable |
| Map.of() | 9.0 | Stable |

## 16. Best Practices

1. Use immutable objects as keys
2. Provide consistent Comparator
3. Never add null keys
4. Use ConcurrentSkipListMap for concurrent sorted maps
5. Use entrySet() for efficient iteration

## 17. Common Mistakes

1. Adding null keys
2. Using mutable objects as keys
3. Bad Comparator implementation
4. Using when HashMap suffices
5. Not considering memory overhead

## 18. Common Myths

### Myth 1: TreeMap is always O(log n)
**Reality:** Amortized O(log n), but compareTo() can be expensive.

### Myth 2: TreeMap allows null keys
**Reality:** TreeMap throws NullPointerException for null keys.

### Myth 3: TreeMap is thread-safe
**Reality:** Not thread-safe. Use ConcurrentSkipListMap.

### Myth 4: TreeMap is slower than HashMap
**Reality:** O(log n) vs O(1), but TreeMap provides ordering.

## 19. One-Minute Revision

- Sorted map based on Red-Black tree
- O(log n) for get/put/remove
- NavigableMap methods: lower, higher, floor, ceiling
- No null keys allowed
- Keys must be Comparable or have Comparator
- Best for sorted key-value pairs with navigation

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| TreeSet | Sorted set (uses TreeMap internally) |
| HashMap | Unordered alternative |
| LinkedHashMap | Insertion-order alternative |
| Comparable/Comparator | Sorting mechanism |
| NavigableMap | Navigation interface |

## 21. Interview Questions

1. **How does TreeMap work internally?** — Red-Black tree. Keys ordered by natural ordering or Comparator.

2. **What is the time complexity of TreeMap operations?** — O(log n) for get/put/remove.

3. **Does TreeMap allow null keys?** — No. Throws NullPointerException.

4. **What is the difference between TreeMap and HashMap?** — TreeMap is sorted O(log n), HashMap is unordered O(1).

5. **When should you use TreeMap?** — When sorted keys or range queries are needed.

6. **Is TreeMap thread-safe?** — No. Use ConcurrentSkipListMap for concurrent access.

## 22. References

- [Oracle Java Documentation - TreeMap](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 12: Always override toString](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
