# Deque

## 1. Why It Exists

Deque (double-ended queue) was introduced in Java 6 to provide a unified interface for both stack (LIFO) and queue (FIFO) operations. Before Deque, developers had to use Stack (legacy) for LIFO and Queue for FIFO, with no single data structure supporting both.

## 2. What It Is

Deque is a linear collection that supports element insertion and removal at both ends. It extends the Queue interface and can function as both a FIFO queue and a LIFO stack.

## 3. Internal Working

```java
// Deque interface defines operations for both ends
public interface Deque<E> extends Queue<E> {
    // Insert at both ends
    void addFirst(E e);
    void addLast(E e);
    boolean offerFirst(E e);
    boolean offerLast(E e);

    // Remove from both ends
    E removeFirst();
    E removeLast();
    E pollFirst();
    E pollLast();

    // Peek at both ends
    E getFirst();
    E getLast();
    E peekFirst();
    E peekLast();

    // Stack operations (LIFO)
    void push(E e);      // = addFirst
    E pop();              // = removeFirst

    // Queue operations (FIFO)
    // offer() = offerLast
    // poll() = pollFirst
    // peek() = peekFirst
}
```

### Two Implementation Options

```
ArrayDeque (recommended):
- Resizable array
- O(1) amortized for all operations
- Better cache locality
- Less memory than LinkedList

LinkedList:
- Doubly-linked list
- O(1) for all operations
- More memory per element
- Implements List interface too
```

## 4. Constructors

```java
// ArrayDeque (recommended)
Deque<String> deque = new ArrayDeque<>();                    // Default capacity 16
Deque<String> deque = new ArrayDeque<>(100);                 // Custom capacity
Deque<String> deque = new ArrayDeque<>(collection);          // From collection

// LinkedList
Deque<String> deque = new LinkedList<>();                    // Empty
Deque<String> deque = new LinkedList<>(collection);          // From collection
```

## 5. Methods

### Insertion

| Method | Throws Exception | Returns Special Value | Complexity |
|--------|-----------------|----------------------|------------|
| Insert first | `addFirst(e)` | `offerFirst(e)` | O(1) |
| Insert last | `addLast(e)` | `offerLast(e)` | O(1) |

### Removal

| Method | Throws Exception | Returns Special Value | Complexity |
|--------|-----------------|----------------------|------------|
| Remove first | `removeFirst()` | `pollFirst()` | O(1) |
| Remove last | `removeLast()` | `pollLast()` | O(1) |

### Inspection

| Method | Throws Exception | Returns Special Value | Complexity |
|--------|-----------------|----------------------|------------|
| Peek first | `getFirst()` | `peekFirst()` | O(1) |
| Peek last | `getLast()` | `peekLast()` | O(1) |

### Stack Operations (LIFO)

| Method | Description | Complexity |
|--------|-------------|------------|
| `push(E e)` | Pushes onto stack (= addFirst) | O(1) |
| `pop()` | Pops from stack (= removeFirst) | O(1) |

### Queue Operations (FIFO)

| Method | Description | Complexity |
|--------|-------------|------------|
| `offer(E e)` | Adds to tail (= offerLast) | O(1) |
| `poll()` | Removes from head (= pollFirst) | O(1) |
| `peek()` | Peeks at head (= peekFirst) | O(1) |

## 6. Complexity Table

| Operation | ArrayDeque | LinkedList |
|-----------|------------|------------|
| addFirst(E) | O(1) amortized | O(1) |
| addLast(E) | O(1) amortized | O(1) |
| removeFirst() | O(1) | O(1) |
| removeLast() | O(1) | O(1) |
| getFirst() | O(1) | O(1) |
| getLast() | O(1) | O(1) |
| push(E) | O(1) amortized | O(1) |
| pop() | O(1) | O(1) |
| size() | O(1) | O(1) |
| contains(Object) | O(n) | O(n) |
| get(int index) | O(n) | O(n) |

## 7. Thread Safety

Deque is NOT thread-safe:

```java
// Option 1: Explicit synchronization
Deque<String> deque = new ArrayDeque<>();
synchronized (deque) {
    // Access deque
}

// Option 2: ConcurrentLinkedDeque for concurrent access
Deque<String> concurrentDeque = new ConcurrentLinkedDeque<>();

// Option 3: LinkedBlockingDeque for blocking operations
Deque<String> blockingDeque = new LinkedBlockingDeque<>();
```

## 8. Memory Behavior

### ArrayDeque Memory

```
ArrayDeque object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ elements reference (8B)     │──────┐
│ head (int, 4 bytes)         │      │
│ tail (int, 4 bytes)         │      │
│ (padding 4 bytes)           │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] elements (circular array)
```

### LinkedList Memory

```
LinkedList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ size (int, 4 bytes)         │
│ first → Node (8 bytes)      │──────┐
│ last → Node (8 bytes)       │──┐   │
└─────────────────────────────┘  │   │
                                 │   ▼
                          Node objects (~48 bytes each)
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| ArrayDeque | ~8 bytes | ~8 MB |
| LinkedList | ~48 bytes | ~48 MB |

## 9. Production Incidents

### Incident 1: Memory Leak from Unbounded Growth

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Deque grows unbounded without processing.
**Impact:** Application crash, data loss.
**Solution:** Implement bounded deque or backpressure.
**Prevention:** Monitor queue size, implement limits.

### Incident 2: Slow Contains Operation

**Problem:** Application slows when checking if element exists.
**Cause:** Deque.contains() is O(n).
**Impact:** Response time increases with deque size.
**Solution:** Use HashSet alongside Deque for fast membership testing.
**Prevention:** Use appropriate data structure for each operation.

### Incident 3: ConcurrentModificationException

**Problem:** Exception thrown during iteration.
**Cause:** Modifying deque during iteration.
**Impact:** Application crash.
**Solution:** Use Iterator.remove() or removeIf().
**Prevention:** Don't modify during iteration.

## 10. Engineering Decision Framework

### Use Deque when:
- FIFO queue operations needed
- LIFO stack operations needed
- Both queue and stack operations needed
- Add/remove from both ends

### Avoid Deque when:
- Random access by index needed (use ArrayList)
- Priority processing needed (use PriorityQueue)
- Sorted elements needed (use TreeSet)
- Thread safety needed (use ConcurrentLinkedDeque)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| ArrayDeque | Recommended for queue/deque |
| LinkedList | When List interface also needed |
| ConcurrentLinkedDeque | Thread-safe deque |
| LinkedBlockingDeque | Blocking deque operations |
| Stack | Legacy LIFO (avoid) |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Memory leak | Heap dump | Check deque size growth |
| Slow contains | Profiling | Use HashSet for membership testing |
| ConcurrentModificationException | Thread dump | Use Iterator.remove() |
| Empty deque exception | Debug logging | Check isEmpty() before operations |

## 12. Code Review Checklist

- [ ] ArrayDeque preferred over LinkedList for queue/deque
- [ ] offer/poll/peek used for graceful failure handling
- [ ] isEmpty() checked before remove/element operations
- [ ] Thread safety handled
- [ ] Not used for random access (use ArrayList)
- [ ] Bounded deque implemented if needed

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded deque |
| Unbounded growth | DoS | Implement backpressure |
| ConcurrentModification | Service degradation | Use concurrent collections |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 6 | Deque interface introduced | Unified queue/stack interface |
| Java 6 | ArrayDeque introduced | Recommended queue/deque |
| Java 8 | Stream support | Stream processing |
| Java 21 | SequencedCollection | getFirst()/getLast() added |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Deque | 6.0 | Stable |
| ArrayDeque | 6.0 | Stable |
| Stream support | 8.0 | Stable |

## 16. Best Practices

1. Use ArrayDeque over LinkedList for queue/deque
2. Use offer/poll/peek for graceful failure
3. Check isEmpty() before remove/element
4. Use as stack instead of Stack class
5. Consider bounded deque for production

## 17. Common Mistakes

1. Using LinkedList over ArrayDeque for queue/deque
2. Using Stack class instead of ArrayDeque
3. Not checking isEmpty() before operations
4. Using add/remove (throw exceptions) instead of offer/poll (return special values)
5. Using for random access (use ArrayList)

## 18. Common Myths

### Myth 1: LinkedList is better for Deque
**Reality:** ArrayDeque is faster and uses less memory.

### Myth 2: Deque is only for queues
**Reality:** Deque supports both FIFO (queue) and LIFO (stack) operations.

### Myth 3: Deque is thread-safe
**Reality:** Not thread-safe. Use ConcurrentLinkedDeque.

### Myth 4: Stack class is better for LIFO
**Reality:** ArrayDeque is better for LIFO. Stack is legacy.

## 19. One-Minute Revision

- Double-ended queue supporting FIFO and LIFO
- ArrayDeque: recommended implementation (faster, less memory)
- O(1) for add/remove at both ends
- Not thread-safe, use ConcurrentLinkedDeque
- Replace Stack class with ArrayDeque
- Replace Queue with Deque when both ends needed

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| ArrayDeque | Recommended implementation |
| LinkedList | Alternative implementation |
| Queue | Parent interface |
| Stack | Legacy LIFO (replace with ArrayDeque) |
| ConcurrentLinkedDeque | Thread-safe variant |

## 21. Interview Questions

1. **What is the difference between ArrayDeque and LinkedList?** — ArrayDeque: faster, less memory (circular array). LinkedList: more memory (nodes), implements List.

2. **How do you implement a stack using Deque?** — Use push()/pop() methods (addFirst/removeFirst).

3. **How do you implement a queue using Deque?** — Use offer()/poll() methods (offerLast/pollFirst).

4. **Is Deque thread-safe?** — No. Use ConcurrentLinkedDeque for concurrent access.

5. **When should you use Deque over Queue?** — When you need operations on both ends.

## 22. References

- [Oracle Java Documentation - Deque](https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html)
- [Oracle Java Documentation - ArrayDeque](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
