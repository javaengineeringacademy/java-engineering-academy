# CopyOnWriteArrayList

## Scope

This folder focuses exclusively on CopyOnWriteArrayList.
Examples, exercises, and quizzes in this folder cover only CopyOnWriteArrayList concepts.

## 1. Why It Exists

CopyOnWriteArrayList was introduced in Java 5 to solve the ConcurrentModificationException problem. When iterating over a regular ArrayList while another thread modifies it, the iterator throws ConcurrentModificationException. CopyOnWriteArrayList creates a new copy of the underlying array for each write operation, allowing safe iteration without synchronization.

## 2. What It Is

CopyOnWriteArrayList is a thread-safe variant of ArrayList where all mutative operations (add, set, remove) create a new copy of the underlying array. This makes it ideal for read-heavy scenarios where reads vastly outnumber writes.

## 3. Internal Working

```java
// CopyOnWriteArrayList uses Object[] internally
private transient volatile Object[] array;

// add() operation creates new array
public boolean add(E e) {
    synchronized (lock) {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        setArray(newElements);
        return true;
    }
}

// get() operation reads from current array without synchronization
public E get(int index) {
    return get(getArray(), index);
}
```

### Write Operation Flow

```
Original array: [A, B, C]
add("D") operation:
1. Create new array of size 4
2. Copy [A, B, C] to new array
3. Add "D" to new array
4. Replace array reference

New array: [A, B, C, D]
```

## 4. Constructors

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();              // Empty
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(collection);    // From collection
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("a", "b")); // From list
```

## 5. Methods

### Standard List Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element (creates copy) | O(n) |
| `add(int index, E e)` | Inserts at index (creates copy) | O(n) |
| `get(int index)` | Returns element | O(1) |
| `set(int index, E e)` | Replaces element (creates copy) | O(n) |
| `remove(int index)` | Removes by index (creates copy) | O(n) |
| `remove(Object o)` | Removes by value (creates copy) | O(n) |
| `contains(Object o)` | Checks membership | O(n) |
| `indexOf(Object o)` | Finds index | O(n) |
| `size()` | Element count | O(1) |
| `addIfAbsent(E e)` | Adds only if not present | O(n) |
| `addAllAbsent(Collection c)` | Adds elements not present | O(n*m) |

### Iterator Methods

| Method | Description |
|--------|-------------|
| `iterator()` | Returns snapshot iterator |
| `listIterator()` | Returns snapshot list iterator |
| `listIterator(int index)` | Returns snapshot from index |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(n) | O(n) |
| add(int, E) | O(n) | O(n) |
| get(int) | O(1) | O(1) |
| set(int, E) | O(n) | O(n) |
| remove(int) | O(n) | O(n) |
| remove(Object) | O(n) | O(n) |
| contains(Object) | O(n) | O(1) |
| size() | O(1) | O(1) |
| Iterator.next() | O(1) | O(1) |

## 7. Thread Safety

CopyOnWriteArrayList is thread-safe:

```java
// add() is synchronized and creates new array
public boolean add(E e) {
    synchronized (lock) {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        setArray(newElements);
        return true;
    }
}

// get() is not synchronized (reads from current array)
public E get(int index) {
    return get(getArray(), index);
}
```

### Iterator is Snapshot

```java
// Iterator sees snapshot at creation time
Iterator<String> it = list.iterator();
// Another thread adds elements here
// it still sees original elements
while (it.hasNext()) {
    System.out.println(it.next());  // Prints original elements
}
```

## 8. Memory Behavior

### Memory Layout

```
CopyOnWriteArrayList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ lock reference (8 bytes)    │
│ array reference (8 bytes)   │──────┐
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] array
```

### Memory During Write

```
Original array: [A, B, C]     →  Old array (garbage)
add("D")       : [A, B, C, D] →  New array (kept)
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| CopyOnWriteArrayList | ~8 bytes + array copy | ~8 MB + copy overhead |
| ArrayList | ~8 bytes | ~8 MB |

## 9. Production Incidents

### Incident 1: Memory Spike During Bulk Writes

**Problem:** Application crashes with OutOfMemoryError during bulk writes.
**Cause:** CopyOnWriteArrayList creates new array for each write.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows multiple array copies.
**Solution:** Use ArrayList with explicit synchronization for bulk writes.
**Prevention:** Use CopyOnWriteArrayList only for read-heavy scenarios.

### Incident 2: Stale Reads in Event Systems

**Problem:** Event listeners receive outdated events.
**Cause:** CopyOnWriteArrayList iterator sees snapshot at creation.
**Impact:** Incorrect processing, data inconsistency.
**Detection:** Logs show listeners processing old events.
**Solution:** Use ConcurrentHashMap for real-time updates.
**Prevention:** Use CopyOnWriteArrayList only when snapshot semantics are acceptable.

### Incident 3: Slow Performance Under Write Load

**Problem:** Application latency spikes during writes.
**Cause:** CopyOnWriteArrayList creates new array for each write.
**Impact:** Response time increases with write frequency.
**Detection:** Profiling shows 99% time in Arrays.copyOf().
**Solution:** Use ArrayList with explicit synchronization or concurrent collection.
**Prevention:** Use CopyOnWriteArrayList only for read-heavy scenarios.

## 10. Engineering Decision Framework

### Use CopyOnWriteArrayList when:
- Reads vastly outnumber writes (10:1 ratio or more)
- Iteration must not throw ConcurrentModificationException
- Snapshot semantics are acceptable
- Configuration lists that change rarely

### Avoid CopyOnWriteArrayList when:
- Writes are frequent
- Memory is constrained
- Real-time updates are required
- Large lists with frequent modifications

### When NOT to Use CopyOnWriteArrayList
- **Frequent writes**: Each write copies entire array. Use ArrayList + sync
- **Large lists**: Memory overhead from full copies
- **Real-time reads**: Iterator sees old snapshot, not live changes

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| ArrayList + Collections.synchronizedList() | Read-heavy with some writes |
| ConcurrentHashMap | Key-value concurrent access |
| Vector | Legacy code (avoid in new code) |
| ReadWriteLock + ArrayList | When you need read/write separation |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory spike | Heap dump | Check for multiple array copies |
| Stale reads | Debug logging | Check iterator snapshot timing |
| Slow writes | Profiling (JFR, VisualVM) | Check Arrays.copyOf() time |
| ConcurrentModificationException | Thread dump | Should not happen with CopyOnWriteArrayList |

## 12. Code Review Checklist

- [ ] Using CopyOnWriteArrayList for read-heavy scenarios
- [ ] Not using for write-heavy scenarios
- [ ] Understanding snapshot semantics of iterator
- [ ] Considering memory overhead of write operations
- [ ] Not using for large lists with frequent writes
- [ ] Using addIfAbsent() for conditional adds
- [ ] Monitoring memory usage under write load

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded collections |
| Stale data | Incorrect processing | Understand snapshot semantics |
| Write amplification | Performance degradation | Use only for read-heavy scenarios |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 5 | CopyOnWriteArrayList introduced | Thread-safe List for read-heavy |
| Java 8 | Stream support | Can use with streams |
| Java 9 | of() factory methods | Immutable list alternatives |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| CopyOnWriteArrayList | 5.0 | Stable |
| Stream support | 8.0 | Stable |
| of() factory methods | 9.0 | Stable |

## 16. Best Practices

1. **Read-heavy only**: Use when reads outnumber writes 10:1 or more
2. **Snapshot semantics**: Iterator sees snapshot at creation time
3. **Use addIfAbsent()**: Avoid duplicate additions
4. **Monitor memory**: Write operations create array copies
5. **Consider alternatives**: For write-heavy scenarios
6. **Use for event listeners**: Classic use case

## 17. Common Mistakes

1. **Using for write-heavy scenarios**: Creates too many array copies
2. **Ignoring snapshot semantics**: Iterator may see stale data
3. **Not monitoring memory**: Write operations create array copies
4. **Using for large lists**: Memory overhead too high
5. **Not considering alternatives**: ConcurrentHashMap may be better

## 18. Common Myths

### Myth 1: CopyOnWriteArrayList is always thread-safe
**Reality:** Yes, but compound operations may not be atomic.

### Myth 2: CopyOnWriteArrayList is fast for all operations
**Reality:** Writes are O(n) due to array copy.

### Myth 3: CopyOnWriteArrayList is better than synchronizedList
**Reality:** Depends on use case. CopyOnWriteArrayList for read-heavy, synchronizedList for balanced.

### Myth 4: CopyOnWriteArrayList uses copy-on-write for reads
**Reality:** Only writes create copies, reads are direct.

## 19. One-Minute Revision

- Thread-safe variant of ArrayList
- Writes create new array copy
- Reads are fast and non-synchronized
- Iterator sees snapshot at creation time
- Best for read-heavy scenarios (10:1 ratio or more)
- Memory overhead for write operations

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| ArrayList | Non-thread-safe alternative |
| Collections.synchronizedList() | Thread-safe wrapper with different semantics |
| ConcurrentHashMap | Thread-safe Map alternative |
| Event listeners | Classic use case for CopyOnWriteArrayList |
| Snapshot iterator | Iterator behavior |

## 21. Interview Questions

1. **How does CopyOnWriteArrayList achieve thread safety?** — By creating a new copy of the underlying array for each write operation.

2. **What is the time complexity of add() in CopyOnWriteArrayList?** — O(n) due to array copy.

3. **What is the snapshot semantics of CopyOnWriteArrayList iterator?** — Iterator sees snapshot at creation time, not real-time updates.

4. **When should you use CopyOnWriteArrayList?** — Read-heavy scenarios where reads outnumber writes 10:1 or more.

5. **What is the memory overhead of CopyOnWriteArrayList?** — Each write creates a new array copy, so memory usage doubles during writes.

6. **How does CopyOnWriteArrayList differ from Collections.synchronizedList()?** — CopyOnWriteArrayList creates copies for writes, synchronizedList uses synchronization. CopyOnWriteArrayList is better for read-heavy, synchronizedList for balanced.

## 22. References

- [Oracle Java Documentation - CopyOnWriteArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CopyOnWriteArrayList.html)
- [Java Concurrency in Practice](https://jcip.net/)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
