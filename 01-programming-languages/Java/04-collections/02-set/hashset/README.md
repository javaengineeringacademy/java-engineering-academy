# HashSet

## 1. Why It Exists

Before HashSet, developers had to use Vector or Hashtable to store unique elements, or implement their own hash-based set. HashSet provides a standard, fast implementation for storing unique elements with O(1) add/remove/contains operations.

## 2. What It Is

HashSet is a hash table implementation of the Set interface. It uses a HashMap internally where all values are a shared PRESENT object. It provides O(1) add/remove/contains operations but does not maintain any ordering.

## 3. Internal Working

```java
// HashSet uses HashMap internally
private transient HashMap<E, Object> map;

// Shared dummy value for all entries
private static final Object PRESENT = new Object();

// add() calls map.put()
public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}

// contains() calls map.containsKey()
public boolean contains(Object o) {
    return map.containsKey(o);
}
```

### Hash Table Structure

```
HashSet object:
┌─────────────────────────────┐
│ HashMap map reference       │──────┐
└─────────────────────────────┘      │
                                     ▼
                              HashMap structure:
                              ┌────────────────────────┐
                              │ table: Node[] buckets   │
                              │ size: int               │
                              │ threshold: int          │
                              │ loadFactor: float       │
                              └────────────────────────┘

Bucket structure:
table[0] → null
table[1] → Node("A") → Node("E") → null   (collision chain)
table[2] → Node("B") → null
table[3] → null
table[4] → Node("C") → Node("D") → null   (collision chain)
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

## 4. Constructors

```java
HashSet<String> set = new HashSet<>();                    // Default capacity 16, load factor 0.75
HashSet<String> set = new HashSet<>(100);                 // Custom initial capacity
HashSet<String> set = new HashSet<>(100, 0.5f);           // Custom capacity + load factor
HashSet<String> set = new HashSet<>(collection);          // From collection
HashSet<String> set = new HashSet<>(Set.of("a", "b"));   // From set
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
| `iterator()` | Returns iterator | O(1) |

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

HashSet is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());

// Option 2: Explicit synchronization
synchronized (hashSet) {
    // Access hashSet
}

// Option 3: ConcurrentSkipListSet for sorted concurrent set
Set<String> concurrentSet = new ConcurrentSkipListSet<>();
```

## 8. Memory Behavior

### Memory Layout

```
HashSet object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ map reference (8 bytes)     │──────┐
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              HashMap structure:
                              ┌────────────────────────┐
                              │ table: Node[] (8B ref)  │
                              │ size (4 bytes)          │
                              │ threshold (4 bytes)     │
                              │ loadFactor (4 bytes)    │
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

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| HashSet | ~40 bytes | ~40 MB |
| ArrayList | ~8 bytes | ~8 MB |
| TreeSet | ~48 bytes | ~48 MB |

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
**Cause:** Mutable objects used as HashSet elements, hash changes after insertion.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows duplicate entries with different hashes.
**Solution:** Use immutable objects as elements, or override hashCode/equals correctly.
**Prevention:** Use immutable objects, test hashCode/equals contract.

### Incident 3: Poor Performance from Bad hashCode()

**Problem:** Application latency spikes, CPU at 100%.
**Cause:** Badly implemented hashCode() causing many collisions.
**Impact:** Response time increases linearly with set size.
**Detection:** Profiling shows 99% time in hash bucket traversal.
**Solution:** Fix hashCode() implementation to distribute evenly.
**Prevention:** Test hashCode() distribution, use well-known implementations.

## 10. Engineering Decision Framework

### Use HashSet when:
- Fastest lookup is required (O(1))
- No ordering needed
- Memory is not a constraint
- Unique elements required

### Avoid HashSet when:
- Sorted elements needed (use TreeSet)
- Insertion order matters (use LinkedHashSet)
- Memory is constrained (use ArrayList if duplicates allowed)
- Thread safety needed (use ConcurrentSkipListSet)

### When NOT to Use HashSet
- **Sorted elements**: Use TreeSet
- **Insertion order**: Use LinkedHashSet
- **Thread safety**: Use Collections.synchronizedSet() or ConcurrentHashMap.newKeySet()
- **Need get**: HashSet doesn't support get(). Use HashMap

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| LinkedHashSet | Insertion order matters |
| TreeSet | Sorted elements needed |
| EnumSet | Enum constants |
| ConcurrentSkipListSet | Thread-safe sorted set |
| ArrayList | Duplicates allowed, indexed access |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow performance | Profiling (JFR, VisualVM) | Check for hash collisions |
| Memory leak | Heap dump (jmap, MAT) | Check for mutable keys |
| Null pointer | Debug logging | Check hashCode/equals |
| ConcurrentModificationException | Thread dump | Use concurrent collection |

## 12. Code Review Checklist

- [ ] Using immutable objects as elements
- [ ] hashCode/equals properly implemented
- [ ] Not using mutable objects as elements
- [ ] Considering LinkedHashSet for insertion order
- [ ] Considering TreeSet for sorted elements
- [ ] Thread safety handled for concurrent access
- [ ] Initial capacity set for known-size sets

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Hash collision DoS | Service degradation | Use randomized hash functions, rate limiting |
| Mutable key manipulation | Data corruption | Use immutable objects |
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded collections |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | HashSet introduced | Standard hash-based set |
| Java 8 | Treeification | O(log n) for collision chains with 8+ entries |
| Java 9 | Set.of() factory | Immutable set alternatives |
| Java 10 | Copy-on-write improvements | Better concurrency |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| HashSet | 1.2 | Stable |
| Treeification | 8.0 | Stable |
| Set.of() | 9.0 | Stable |
| Stream support | 8.0 | Stable |

## 16. Best Practices

1. **Use immutable objects**: Prevents hash changes after insertion
2. **Override hashCode/equals**: Correctly for consistent behavior
3. **Set initial capacity**: Avoid resizing for known sizes
4. **Use LinkedHashSet**: If insertion order matters
5. **Use TreeSet**: If sorted elements needed
6. **Monitor load factor**: Default 0.75 balances time/space

## 17. Common Mistakes

1. **Using mutable objects as elements**: Hash changes, element lost
2. **Not overriding hashCode/equals**: Breaks Set behavior
3. **Ignoring null behavior**: HashSet allows one null
4. **Not setting initial capacity**: Wastes time resizing
5. **Using for sorted data**: Use TreeSet instead

## 18. Common Myths

### Myth 1: HashSet maintains insertion order
**Reality:** HashSet does not maintain any order. Use LinkedHashSet for insertion order.

### Myth 2: HashSet allows multiple null elements
**Reality:** HashSet allows at most one null element.

### Myth 3: HashSet is always O(1)
**Reality:** Amortized O(1, but resizing is O(n). Treeification is O(log n).

### Myth 4: HashSet is thread-safe
**Reality:** Not thread-safe. Use Collections.synchronizedSet() or ConcurrentSkipListSet().

## 19. One-Minute Revision

- Hash table implementation of Set interface
- O(1) add/remove/contains operations
- No ordering, allows one null element
- Uses HashMap internally with PRESENT dummy value
- Not thread-safe, use concurrent collections
- Best for fastest lookup when order doesn't matter

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| HashMap | Internal implementation |
| LinkedHashSet | Insertion-ordered variant |
| TreeSet | Sorted variant |
| EnumSet | Enum-specific variant |
| hashCode/equals | Contract for elements |

## 21. Interview Questions

1. **How does HashSet work internally?** — Uses HashMap with dummy PRESENT value. add() calls map.put().

2. **What is the time complexity of HashSet operations?** — O(1) amortized for add/remove/contains.

3. **Does HashSet maintain insertion order?** — No. Use LinkedHashSet for insertion order.

4. **How many null elements can HashSet have?** — At most one null element.

5. **What is treeification in HashSet?** — When bucket has 8+ entries, converts to tree for O(log n) lookup.

6. **Is HashSet thread-safe?** — No. Use Collections.synchronizedSet() or ConcurrentSkipListSet().

## 22. References

- [Oracle Java Documentation - HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 13: Override hashCode judiciously](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
