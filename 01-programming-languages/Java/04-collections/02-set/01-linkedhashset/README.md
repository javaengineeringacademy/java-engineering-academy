# LinkedHashSet

## Scope

This folder focuses exclusively on LinkedHashSet.
Examples, exercises, and quizzes in this folder cover only LinkedHashSet concepts.

## 1. Why It Exists

LinkedHashSet was introduced in Java 1.4 to provide a Set implementation that maintains insertion order. HashSet loses ordering, which is problematic when you need both unique elements and predictable iteration order.

## 2. What It Is

LinkedHashSet is a hash table with linked list implementation of the Set interface. It maintains insertion order by linking all elements via a doubly-linked list. It extends HashSet but uses a LinkedHashMap internally.

## 3. Internal Working

```java
// LinkedHashSet extends HashSet
// Uses LinkedHashMap internally
public class LinkedHashSet<E> extends HashSet<E> {
    // Constructor creates LinkedHashMap
    public LinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);  // true = access order
    }
}

// LinkedHashMap maintains doubly-linked list
// Each entry has before/after pointers
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
head → Node("A") ↔ Node("B") ↔ Node("C") ↔ Node("D") ↔ Node("E") ← tail

Iteration follows linked list, not bucket order
```

### Entry Structure

```java
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before;  // Previous in linked list
    Entry<K,V> after;   // Next in linked list
}
```

## 4. Constructors

```java
LinkedHashSet<String> set = new LinkedHashSet<>();                    // Default capacity 16, load factor 0.75
LinkedHashSet<String> set = new LinkedHashSet<>(100);                 // Custom initial capacity
LinkedHashSet<String> set = new LinkedHashSet<>(100, 0.5f);           // Custom capacity + load factor
LinkedHashSet<String> set = new LinkedHashSet<>(collection);          // From collection
LinkedHashSet<String> set = new LinkedHashSet<>(Set.of("a", "b"));   // From set
```

## 5. Methods

### Set Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(1) |
| `remove(Object o)` | Removes element | O(1) |
| `contains(Object o)` | Checks membership | O(1) |
| `size()` | Element count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all | O(n) |
| `iterator()` | Returns iterator (insertion order) | O(1) |

### Bulk Operations

| Method | Description | Complexity |
|--------|-------------|------------|
| `addAll(Collection)` | Union | O(n) |
| `retainAll(Collection)` | Intersection | O(n*m) |
| `removeAll(Collection)` | Difference | O(n*m) |
| `containsAll(Collection)` | Checks all present | O(n*m) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(1) amortized | O(1) |
| remove(Object) | O(1) amortized | O(1) |
| contains(Object) | O(1) amortized | O(1) |
| size() | O(1) | O(1) |
| isEmpty() | O(1) | O(1) |
| iterator() | O(1) | O(1) |
| Iterator.next() | O(1) amortized | O(1) |

## 7. Thread Safety

LinkedHashSet is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Set<String> syncSet = Collections.synchronizedSet(new LinkedHashSet<>());

// Option 2: Explicit synchronization
synchronized (linkedHashSet) {
    // Access linkedHashSet
}
```

## 8. Memory Behavior

### Memory Layout

```
LinkedHashSet object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ map reference (8 bytes)     │──────┐
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              LinkedHashMap structure:
                              ┌────────────────────────┐
                              │ table: Node[] (8B ref)  │
                              │ head → Entry (8B ref)   │
                              │ tail → Entry (8B ref)   │
                              │ size (4 bytes)          │
                              │ threshold (4 bytes)     │
                              │ loadFactor (4 bytes)    │
                              └────────────────────────┘

Per entry:
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

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| LinkedHashSet | ~48 bytes | ~48 MB |
| HashSet | ~40 bytes | ~40 MB |
| ArrayList | ~8 bytes | ~8 MB |

## 9. Production Incidents

### Incident 1: Memory Overhead from Linked List

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** LinkedHashSet uses more memory due to linked list pointers.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows 48 bytes per element vs 40 bytes for HashSet.
**Solution:** Use HashSet if order doesn't matter.
**Prevention:** Use LinkedHashSet only when insertion order is required.

### Incident 2: Slow Iteration with Large Sets

**Problem:** Application latency spikes during iteration.
**Cause:** LinkedHashSet iteration follows linked list, not bucket order.
**Impact:** Response time increases with set size.
**Detection:** Profiling shows 99% time in linked list traversal.
**Solution:** Use HashSet if order doesn't matter.
**Prevention:** Use LinkedHashSet only when insertion order is required.

### Incident 3: Performance Degradation with High Load Factor

**Problem:** Application slows under high load.
**Cause:** High load factor causes more collisions, degrading performance.
**Impact:** Service degraded, user experience poor.
**Detection:** Profiling shows high collision rate.
**Solution:** Use default load factor (0.75) or tune for specific use case.
**Prevention:** Monitor load factor and resize appropriately.

## 10. Engineering Decision Framework

### Use LinkedHashSet when:
- Insertion order matters
- Fastest lookup required (O(1))
- Unique elements required
- Both HashSet speed and order needed

### Avoid LinkedHashSet when:
- Order doesn't matter (use HashSet)
- Sorted elements needed (use TreeSet)
- Memory is constrained (use HashSet)
- Thread safety needed (use ConcurrentSkipListSet)

### When NOT to Use LinkedHashSet
- **Memory**: Extra linked list overhead
- **No order needed**: Use HashSet (simpler)
- **Sorted order**: Use TreeSet

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| HashSet | Order doesn't matter |
| TreeSet | Sorted elements needed |
| EnumSet | Enum constants |
| ConcurrentSkipListSet | Thread-safe sorted set |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory overhead | Heap dump | Check for linked list pointers |
| Slow iteration | Profiling (JFR, VisualVM) | Check linked list traversal |
| Order not maintained | Debug logging | Check insertion order |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] Using LinkedHashSet for insertion order
- [ ] Not using when order doesn't matter (use HashSet)
- [ ] Considering memory overhead (48 bytes vs 40 bytes)
- [ ] Thread safety handled for concurrent access
- [ ] Not using for sorted data (use TreeSet)
- [ ] Initial capacity set for known-size sets
- [ ] Load factor appropriate for use case

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded collections |
| Hash collision DoS | Service degradation | Use randomized hash functions |
| Mutable key manipulation | Data corruption | Use immutable objects |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.4 | LinkedHashSet introduced | Insertion-ordered HashSet |
| Java 8 | Treeification | O(log n) for collision chains with 8+ entries |
| Java 9 | Set.of() factory | Immutable set alternatives |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| LinkedHashSet | 1.4 | Stable |
| Treeification | 8.0 | Stable |
| Set.of() | 9.0 | Stable |
| Stream support | 8.0 | Stable |

## 16. Best Practices

1. **Use when order matters**: LinkedHashSet maintains insertion order
2. **Set initial capacity**: Avoid resizing for known sizes
3. **Use default load factor**: 0.75 balances time/space
4. **Use HashSet if order doesn't matter**: More memory efficient
5. **Use TreeSet if sorted needed**: Better for sorted elements
6. **Override hashCode/equals**: For consistent behavior

## 17. Common Mistakes

1. **Using when order doesn't matter**: HashSet is more efficient
2. **Ignoring memory overhead**: 48 bytes vs 40 bytes per element
3. **Not setting initial capacity**: Wastes time resizing
4. **Using for sorted data**: Use TreeSet instead
5. **Using mutable objects as elements**: Hash changes, element lost

## 18. Common Myths

### Myth 1: LinkedHashSet is always faster than HashSet
**Reality:** Same O(1) operations, but more memory overhead.

### Myth 2: LinkedHashSet maintains sorted order
**Reality:** Maintains insertion order, not sorted order. Use TreeSet for sorted.

### Myth 3: LinkedHashSet is thread-safe
**Reality:** Not thread-safe. Use Collections.synchronizedSet().

### Myth 4: LinkedHashSet allows multiple null elements
**Reality:** Allows at most one null element.

## 19. One-Minute Revision

- Hash table with linked list for insertion order
- O(1) add/remove/contains operations
- Maintains insertion order for iteration
- More memory overhead than HashSet (48 vs 40 bytes)
- Best when both HashSet speed and insertion order needed
- Not thread-safe, use concurrent collections

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| HashSet | Unordered variant |
| LinkedHashMap | Internal implementation |
| TreeSet | Sorted variant |
| Insertion order | Key feature |
| hashCode/equals | Contract for elements |

## 21. Interview Questions

1. **How does LinkedHashSet maintain insertion order?** — Uses LinkedHashMap with doubly-linked list connecting entries.

2. **What is the difference between LinkedHashSet and HashSet?** — LinkedHashSet maintains insertion order, HashSet does not.

3. **What is the time complexity of LinkedHashSet operations?** — O(1) amortized for add/remove/contains.

4. **Does LinkedHashSet allow null elements?** — Yes, at most one null element.

5. **When should you use LinkedHashSet over HashSet?** — When insertion order matters.

6. **Is LinkedHashSet thread-safe?** — No. Use Collections.synchronizedSet() or explicit synchronization.

## 22. References

- [Oracle Java Documentation - LinkedHashSet](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashSet.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 13: Override hashCode judiciously](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
