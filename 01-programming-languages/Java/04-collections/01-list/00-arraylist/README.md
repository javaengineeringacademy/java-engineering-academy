# ArrayList

## Scope

This folder focuses exclusively on ArrayList.
Examples, exercises, and quizzes in this folder cover only ArrayList concepts.

## 1. Why It Exists

Before ArrayList, developers had to manually manage arrays: create arrays of estimated size, track current size, create new arrays and copy elements when full, and handle null values for unused slots. ArrayList solves all these problems with automatic resizing and a simplified API.

## 2. What It Is

ArrayList is a resizable array implementation of the List interface. It uses a dynamic array internally, giving O(1) random access and efficient iteration. It is the most widely used List implementation in Java.

## 3. Internal Working

```java
// ArrayList uses Object[] internally
private transient Object[] elementData;
private int size;

// Growth factor: 1.5x
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

### Growth Factor Analysis

| Initial | After 10 adds | After 100 adds | After 1000 adds |
|---------|---------------|----------------|-----------------|
| 10 | 15 | 169 | 1706 |

## 4. Constructors

```java
ArrayList<String> list = new ArrayList<>();              // Default capacity 10
ArrayList<String> list = new ArrayList<>(100);           // Custom capacity
ArrayList<String> list = new ArrayList<>(collection);    // From collection
ArrayList<String> list = new ArrayList<>(List.of("a", "b")); // From list
```

## 5. Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Appends element | O(1) amortized |
| `add(int index, E e)` | Inserts at index | O(n) |
| `get(int index)` | Returns element | O(1) |
| `set(int index, E e)` | Replaces element | O(1) |
| `remove(int index)` | Removes by index | O(n) |
| `remove(Object o)` | Removes by value | O(n) |
| `contains(Object o)` | Checks membership | O(n) |
| `indexOf(Object o)` | Finds index | O(n) |
| `size()` | Element count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all | O(n) |
| `subList(int from, int to)` | Returns view | O(1) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(1) amortized | O(1) |
| add(int, E) | O(n) | O(1) |
| get(int) | O(1) | O(1) |
| set(int, E) | O(1) | O(1) |
| remove(int) | O(n) | O(1) |
| remove(Object) | O(n) | O(1) |
| contains(Object) | O(n) | O(1) |
| indexOf(Object) | O(n) | O(1) |
| size() | O(1) | O(1) |
| Iterator.next() | O(1) | O(1) |

## 7. Thread Safety

ArrayList is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// Option 2: CopyOnWriteArrayList for read-heavy
List<String> copyOnWrite = new CopyOnWriteArrayList<>();

// Option 3: Explicit synchronization
synchronized (arrayList) {
    // Access arrayList
}
```

## 8. Memory Behavior

### Memory Layout

```
ArrayList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ elementData reference (8B)  │──────┐
│ size (int, 4 bytes)         │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] elementData
                              ┌──────────────────┐
                              │ [0] → "Hello"    │
                              │ [1] → "World"    │
                              │ [2] → null       │
                              └──────────────────┘
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| ArrayList | ~8 bytes | ~8 MB |
| LinkedList | ~48 bytes | ~48 MB |
| Vector | ~8 bytes + lock | ~8 MB + lock |

## 9. Production Incidents

### Incident 1: ConcurrentModificationException

**Problem:** Exception thrown during iteration.
**Cause:** Modifying ArrayList during enhanced for loop.
**Impact:** Application crash.
**Solution:** Use Iterator.remove() or removeIf().
**Prevention:** Don't modify during iteration.

### Incident 2: Memory Leak from Unused Capacity

**Problem:** Application uses more memory than expected.
**Cause:** ArrayList capacity larger than size after bulk removal.
**Impact:** Wasted memory.
**Solution:** Use trimToSize() after bulk operations.
**Prevention:** Monitor capacity vs size.

### Incident 3: Slow Performance from Index-Based Removal

**Problem:** Application slows during frequent removals.
**Cause:** remove(int) is O(n) due to array shifting.
**Impact:** Response time increases with list size.
**Solution:** Use LinkedList or Iterator for frequent removals.
**Prevention:** Choose right collection for use case.

## 10. Engineering Decision Framework

### Use ArrayList when:
- Random access by index needed (O(1))
- Append operations dominate
- Cache-friendly iteration important
- Simple general-purpose list needed

### Avoid ArrayList when:
- Frequent insertions/removals in middle (use LinkedList)
- Queue/deque operations needed (use ArrayDeque)
- Thread-safe access needed (use CopyOnWriteArrayList)

### When NOT to Use ArrayList
- **Frequent insertions/deletions in middle**: LinkedList or CopyOnWriteArrayList for thread safety
- **Need thread safety**: CopyOnWriteArrayList or Collections.synchronizedList()
- **Fixed size**: Use Arrays.asList() (but list is fixed-size, not resizable)
- **Memory sensitive**: Each ArrayList has backing array overhead. Use IntList for primitives

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| LinkedList | Frequent mid-list insert/remove |
| ArrayDeque | Queue/deque operations |
| CopyOnWriteArrayList | Read-heavy concurrent access |
| Vector | Legacy synchronized list |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ConcurrentModificationException | Thread dump | Use Iterator for removal |
| OutOfMemoryError | Heap dump | Check capacity vs size |
| Slow indexOf() | Profiling | Use HashSet for frequent lookups |
| IndexOutOfBoundsException | Debug logging | Check bounds before get/remove |

## 12. Code Review Checklist

- [ ] Using ArrayList for right reason (random access)
- [ ] Initial capacity set for known sizes
- [ ] Not modifying during enhanced for loop
- [ ] Using Iterator.remove() for removal during iteration
- [ ] Thread safety handled for concurrent access
- [ ] Using removeIf() for conditional removal
- [ ] trimToSize() used after bulk removals

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded list |
| Index manipulation | IndexOutOfBoundsException | Validate inputs |
| ConcurrentModification | Service degradation | Use concurrent collections |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | ArrayList introduced | Standard dynamic array |
| Java 5 | Generics added | Type safety |
| Java 8 | removeIf(), sort() | Better API |
| Java 9 | List.of() factory | Immutable alternatives |
| Java 10 | copyOf() factory | Immutable copies |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| ArrayList | 1.2 | Stable |
| Generics | 5.0 | Stable |
| removeIf() | 8.0 | Stable |
| List.of() | 9.0 | Stable |
| List.copyOf() | 10 | Stable |

## 16. Best Practices

1. Set initial capacity when size is known
2. Use enhanced for loop for iteration
3. Use Iterator for removal during iteration
4. Use removeIf() for conditional removal
5. Use trimToSize() after bulk removals
6. Consider LinkedList for frequent mid-list operations

## 17. Common Mistakes

1. **Modifying during enhanced for loop**: Causes ConcurrentModificationException
2. **Not pre-allocating capacity**: Wastes time resizing
3. **Using indexOf() in hot loops**: O(n) per call
4. **Using subList() as persistent view**: It's a live view of original
5. **Removing by index in loop**: Account for shifted indices

## 18. Common Myths

### Myth 1: ArrayList is always faster than LinkedList
**Reality:** Depends on operations. LinkedList can be faster for frequent mid-list insert/remove.

### Myth 2: ArrayList size equals capacity
**Reality:** Capacity >= size. Backing array can have more space.

### Myth 3: ArrayList is thread-safe
**Reality:** Not thread-safe. Use CopyOnWriteArrayList for concurrent access.

## 19. One-Minute Revision

- Resizable array implementation of List
- O(1) random access, O(n) insert/remove
- Default capacity 10, grows by 1.5x
- Not thread-safe, use CopyOnWriteArrayList
- Use Iterator for safe removal during iteration
- Pre-allocate capacity when size is known

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| LinkedList | Alternative List implementation |
| ArrayDeque | Queue/deque alternative |
| CopyOnWriteArrayList | Thread-safe variant |
| Vector | Legacy synchronized variant |
| Arrays | Static array utility |

## 21. Interview Questions

1. **What is the time complexity of ArrayList get()?** — O(1) for indexed access.

2. **How does ArrayList grow?** — 1.5x capacity via Arrays.copyOf().

3. **Is ArrayList thread-safe?** — No. Use Collections.synchronizedList() or CopyOnWriteArrayList.

4. **What is the difference between size() and capacity?** — size() is element count; capacity is backing array length.

5. **When should you use LinkedList over ArrayList?** — Frequent insertions/removals at known positions.

## 22. References

- [Oracle Java Documentation - ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 15: Minimize mutability](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
