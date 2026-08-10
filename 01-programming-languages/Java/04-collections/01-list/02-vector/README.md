# Vector

## Scope

This folder focuses exclusively on Vector.
Examples, exercises, and quizzes in this folder cover only Vector concepts.

## 1. Why It Exists

Vector was introduced in Java 1.0 as a synchronized, resizable array. It was the original dynamic array before ArrayList. In Java 1.2, ArrayList was introduced as a faster, non-synchronized alternative.

## 2. What It Is

Vector is a legacy synchronized implementation of the List interface. It uses a dynamic array internally, similar to ArrayList, but every method is synchronized for thread safety.

## 3. Internal Working

```java
// Vector uses Object[] internally
protected Object[] elementData;
protected int elementCount;

// When adding and array is full:
// 1. New capacity = old capacity * 2 (doubles capacity)
// 2. Arrays.copyOf() copies all elements
```

### Growth Factor

| Initial | After 10 adds | After 100 adds | After 1000 adds |
|---------|---------------|----------------|-----------------|
| 10 | 20 | 1280 | 1,024,000 |

Vector doubles capacity (2x) vs ArrayList 1.5x. This wastes more memory.

## 4. Constructors

```java
Vector<String> v = new Vector<>();              // Default capacity 10
Vector<String> v = new Vector<>(50);            // Custom initial capacity
Vector<String> v = new Vector<>(50, 10);        // Custom capacity + increment
Vector<String> v = new Vector<>(collection);     // From collection
```

## 5. Methods

### Standard List Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(1) amortized |
| `add(int index, E e)` | Inserts at index | O(n) |
| `get(int index)` | Returns element | O(1) |
| `set(int index, E e)` | Replaces element | O(1) |
| `remove(int index)` | Removes by index | O(n) |
| `remove(Object o)` | Removes by value | O(n) |
| `contains(Object o)` | Checks membership | O(n) |
| `indexOf(Object o)` | Finds index | O(n) |
| `size()` | Element count | O(1) |
| `capacity()` | Current capacity | O(1) |

### Legacy Methods (Avoid)

| Method | Description |
|--------|-------------|
| `addElement(E e)` | Use add() instead |
| `elementAt(int index)` | Use get() instead |
| `firstElement()` | Use get(0) instead |
| `lastElement()` | Use get(size()-1) instead |
| `insertElementAt(E e, int index)` | Use add(index, e) instead |
| `removeElementAt(int index)` | Use remove(index) instead |
| `setElementAt(E e, int index)` | Use set(index, e) instead |

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
| size() | O(1) | O(1) |

Note: All operations have additional synchronized lock overhead.

## 7. Thread Safety

Vector is synchronized, meaning every method acquires a monitor lock:

```java
// Vector.add() is synchronized
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}

// Even size() is synchronized
public synchronized int size() {
    return elementCount;
}
```

### Problem: Compound Operations Not Atomic

```java
// NOT thread-safe even with Vector!
if (!vector.contains(element)) {
    vector.add(element);  // Another thread may add between contains() and add()
}
```

## 8. Memory Behavior

### Memory Layout

```
Vector object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ elementData reference (8B)  │──────┐
│ elementCount (int, 4B)      │      │
│ capacityIncrement (int, 4B) │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] elementData
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| Vector | ~8 bytes + lock | ~8 MB + lock |
| ArrayList | ~8 bytes | ~8 MB |

## 9. Production Incidents

### Incident 1: Performance Degradation Under Load

**Problem:** Application slows from 10ms to 500ms under concurrent load.
**Cause:** Vector's coarse-grained locking causing contention.
**Impact:** Service degraded, user experience poor.
**Detection:** Thread dump shows threads waiting on Vector's monitor.
**Solution:** Switch to ArrayList with explicit synchronization or CopyOnWriteArrayList.
**Prevention:** Use modern concurrent collections instead of Vector.

### Incident 2: Wasted Memory from 2x Growth

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Vector's 2x growth factor wasting memory.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows 50% unused capacity.
**Solution:** Switch to ArrayList with 1.5x growth factor.
**Prevention:** Use ArrayList for better memory efficiency.

### Incident 3: Legacy Code Maintained Vector

**Problem:** New developers confused by Vector in codebase.
**Cause:** Legacy code still using Vector instead of ArrayList.
**Impact:** Developer confusion, maintenance overhead.
**Detection:** Code review shows Vector usage.
**Solution:** Migrate to ArrayList or CopyOnWriteArrayList.
**Prevention:** Establish coding standards against Vector.

## 10. Engineering Decision Framework

### When Should I Use This?
- Maintaining legacy code that already uses Vector
- Required by external library or API
- Simple synchronized list needed (but prefer alternatives)

### When Should I NOT Use This?
- **Writing new code**: Use ArrayList with explicit synchronization
- **Performance matters**: Synchronized overhead is too high
- **Concurrent access needed**: Use CopyOnWriteArrayList or synchronizedList
- **High contention**: Use ConcurrentHashMap.newKeySet() or Collections.synchronizedList()

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| ArrayList | General purpose, no synchronization needed | Faster, no thread safety |
| CopyOnWriteArrayList | Read-heavy concurrent access | Higher memory for writes |
| Collections.synchronizedList() | When you need synchronization on ArrayList | Simple wrapper |
| ConcurrentHashMap.newKeySet() | Set-like behavior with thread safety | Better concurrency |

### What Trade-offs Am I Making?
- **Thread Safety**: Synchronized but slow vs fast but unsafe (ArrayList)
- **Memory**: 2x growth factor vs 1.5x (ArrayList)
- **Legacy vs Modern**: Legacy code vs modern alternatives
- **Performance**: Synchronized overhead vs fine-grained locking

### What Would I Choose in Production?
> Never use Vector in new code. If you're maintaining legacy code, plan migration to ArrayList + Collections.synchronizedList() or CopyOnWriteArrayList.

### Common Code Review Comments
- "Why are you using Vector? Use ArrayList + Collections.synchronizedList() instead."
- "Vector is legacy — plan migration to ArrayList."
- "This Vector should be a CopyOnWriteArrayList for read-heavy workloads."
- "Vector Enumeration is legacy — use Iterator instead."

### Common Production Mistakes

> Notice: Vector is not deprecated but strongly discouraged — use ArrayList + Collections.synchronizedList() instead.

> Notice: Vector.toString() is synchronized — it can cause contention in concurrent code.

> Notice: Vector.grow() doubles the size — ArrayList grows by 50%, which is more memory-efficient.

> Notice: Vector is legacy — it was part of Java 1.0, before the Collections Framework.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow performance | Profiling (JFR, VisualVM) | Check for Vector contention |
| ConcurrentModificationException | Thread dump | Find which thread modifying |
| Memory leak | Heap dump | Check for unused Vector references |
| Legacy code confusion | Code review | Migrate to ArrayList |

## 12. Code Review Checklist

- [ ] Not using Vector in new code
- [ ] Migrating legacy Vector to ArrayList
- [ ] Using CopyOnWriteArrayList for concurrent access
- [ ] Not using legacy methods (addElement, elementAt, etc.)
- [ ] Considering thread safety requirements
- [ ] Checking for compound operation atomicity
- [ ] Performance testing under concurrent load

## 13. Architecture Considerations

### Where Vector Fits in System Design

| Layer | Use Case | Why Vector |
|-------|----------|------------|
| Legacy API | Backward-compatible interfaces | Required by old APIs |
| Service Layer | Simple synchronized list | Synchronized methods |
| Migration | Interim during refactoring | Drop-in replacement for old code |

### Integration Patterns

```
Legacy Client → Vector → Legacy Service → Vector → Legacy Client
                    ↓
            Vector → Migration Bridge → ArrayList
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K elements | Vector works but prefer ArrayList |
| 10K - 100K elements | Migrate to ArrayList + synchronized |
| 100K - 1M elements | Use CopyOnWriteArrayList or ConcurrentHashMap |
| > 1M elements | Consider database or external storage |

### When to Replace Vector in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| Any new code | ArrayList | No synchronization overhead |
| Concurrent read-heavy | CopyOnWriteArrayList | Better read performance |
| Concurrent balanced | Collections.synchronizedList() | Wrapper around ArrayList |
| High concurrency | ConcurrentHashMap.newKeySet() | Better concurrent access |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max capacity, use bounded collections |
| Deadlock from synchronization | Service hang | Use fine-grained locking |
| Legacy code vulnerabilities | Security risk | Migrate to modern collections |

## 15. Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Vector introduced | Use as dynamic array |
| Java 1.2 | ArrayList introduced | Migrate to ArrayList |
| Java 1.2 | Collections.synchronizedList() | Wrap ArrayList if needed |
| Java 5 | Generics added | Add type parameters |
| Java 5 | CopyOnWriteArrayList | Use for read-heavy concurrent access |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Vector | 1.0 | Legacy (avoid) |
| ArrayList | 1.2 | Recommended |
| CopyOnWriteArrayList | 5.0 | Recommended |

## 17. Best Practices

1. **Avoid in new code**: Use ArrayList or CopyOnWriteArrayList
2. **Migrate existing**: Replace Vector with ArrayList
3. **Use legacy methods**: Avoid addElement(), elementAt(), etc.
4. **Consider thread safety**: Vector's synchronization is coarse-grained
5. **Monitor performance**: Vector adds overhead even in single-threaded code
6. **Use modern alternatives**: CopyOnWriteArrayList for concurrent access

## 18. Common Mistakes

1. **Using Vector as default**: ArrayList is faster and more memory efficient
2. **Thinking Vector is thread-safe for compound operations**: Contains-then-add is not atomic
3. **Using legacy methods**: addElement(), elementAt(), etc. are obsolete
4. **Ignoring synchronization overhead**: Vector is slower than ArrayList even in single-threaded code
5. **Not migrating**: Legacy Vector code should be updated

## 19. Common Myths

### Myth 1: Vector is always thread-safe
**Reality:** Individual methods are synchronized, but compound operations are not atomic.

### Myth 2: Vector is better than ArrayList
**Reality:** ArrayList is faster and more memory efficient for most use cases.

### Myth 3: Vector is deprecated
**Reality:** Not deprecated, but discouraged in favor of ArrayList.

### Myth 4: Vector is better for concurrent access
**Reality:** CopyOnWriteArrayList or synchronizedList() is better.

## 20. One-Minute Revision

- Legacy synchronized dynamic array (Java 1.0)
- Every method synchronized, causing overhead
- 2x growth factor wastes memory
- Avoid in new code, use ArrayList
- Use CopyOnWriteArrayList for concurrent access
- Not deprecated but discouraged

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| ArrayList | Modern alternative, non-synchronized |
| CopyOnWriteArrayList | Thread-safe alternative |
| Collections.synchronizedList() | Wraps ArrayList with synchronization |
| Stack | Extends Vector, also legacy |
| Legacy code | Often contains Vector, should migrate |

## 22. Interview Questions

1. **What is the difference between Vector and ArrayList?** — Vector is synchronized (every method), ArrayList is not. Vector has 2x growth factor, ArrayList has 1.5x.

2. **Is Vector thread-safe?** — Yes, individual methods are synchronized. But compound operations are not atomic.

3. **When should you use Vector?** — Almost never in new code. Only in legacy code that already uses Vector.

4. **What are the legacy methods in Vector?** — addElement(), elementAt(), firstElement(), lastElement(), insertElementAt(), removeElementAt(), setElementAt().

5. **What is the growth factor of Vector?** — 2x (doubles capacity). ArrayList uses 1.5x.

6. **How do you make ArrayList thread-safe?** — Use Collections.synchronizedList() or CopyOnWriteArrayList.

## 23. References

- [Oracle Java Documentation - Vector](https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 54: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
