# LinkedList

## 1. Why It Exists

Before LinkedList, developers who needed frequent insertions and deletions in the middle of a list had to use ArrayList (O(n) shifts) or implement their own linked list from scratch. LinkedList provides a standard, tested doubly-linked list implementation with O(1) insert/remove at known positions.

## 2. What It Is

LinkedList is a doubly-linked list implementation of the List and Deque interfaces. Each element is a Node containing the element reference plus pointers to the previous and next nodes.

## 3. Internal Working

```
Node structure:
┌─────────────────────────────┐
│ E item                      │
│ Node<E> next  ──────────────→ next node
│ Node<E> prev  ──────────────→ previous node
└─────────────────────────────┘

LinkedList object:
┌─────────────────────────────┐
│ int size                    │
│ Node<E> first ──────────────→ Node("A")
│ Node<E> last  ──────────────→ Node("C")
└─────────────────────────────┘

Traversal: first → Node("A") → Node("B") → Node("C") → null
```

### addFirst() Operation

```java
void linkFirst(E e) {
    final Node<E> f = first;
    final Node<E> newNode = new Node<>(null, e, f);
    first = newNode;
    if (f == null)
        last = newNode;
    else
        f.prev = newNode;
    size++;
    modCount++;
}
```

### addLast() Operation

```java
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

### remove() Operation

```java
E unlink(Node<E> x) {
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```

## 4. Constructors

```java
LinkedList<String> list = new LinkedList<>();           // Empty list
LinkedList<String> list = new LinkedList<>(collection);  // From collection
LinkedList<String> list = new LinkedList<>(List.of("a", "b", "c")); // From list
```

## 5. Methods

### List Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds to end | O(1) |
| `add(int index, E e)` | Inserts at index | O(n) |
| `addFirst(E e)` | Adds to beginning | O(1) |
| `addLast(E e)` | Adds to end | O(1) |
| `get(int index)` | Returns element | O(n) |
| `getFirst()` | Returns first element | O(1) |
| `getLast()` | Returns last element | O(1) |
| `remove(int index)` | Removes by index | O(n) |
| `remove(Object o)` | Removes by value | O(n) |
| `removeFirst()` | Removes first | O(1) |
| `removeLast()` | Removes last | O(1) |
| `set(int index, E e)` | Replaces element | O(n) |
| `indexOf(Object o)` | Finds index | O(n) |
| `contains(Object o)` | Checks membership | O(n) |
| `size()` | Element count | O(1) |

### Deque Methods

| Method | Throws Exception | Returns Special Value |
|--------|-----------------|----------------------|
| Insert first | `addFirst(e)` | `offerFirst(e)` |
| Insert last | `addLast(e)` | `offerLast(e)` |
| Remove first | `removeFirst()` | `pollFirst()` |
| Remove last | `removeLast()` | `pollLast()` |
| Peek first | `getFirst()` | `peekFirst()` |
| Peek last | `getLast()` | `peekLast()` |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(1) | O(1) |
| add(int, E) | O(n) | O(1) |
| get(int) | O(n) | O(1) |
| remove(int) | O(n) | O(1) |
| remove(Object) | O(n) | O(1) |
| contains(Object) | O(n) | O(1) |
| indexOf(Object) | O(n) | O(1) |
| size() | O(1) | O(1) |
| Iterator.next() | O(1) | O(1) |

## 7. Thread Safety

LinkedList is NOT thread-safe. For concurrent access:

```java
// Option 1: Synchronized wrapper
List<E> syncList = Collections.synchronizedList(new LinkedList<>());

// Option 2: CopyOnWriteArrayList (read-heavy)
List<E> copyOnWrite = new CopyOnWriteArrayList<>();

// Option 3: Explicit synchronization
synchronized (linkedList) {
    // Access linkedList
}
```

## 8. Memory Behavior

### Per-Element Overhead

```
Node object: ~48 bytes
├── Object header: 12 bytes
├── item reference: 8 bytes
├── next reference: 8 bytes
├── prev reference: 8 bytes
└── Padding: 4 bytes (to 8-byte boundary)

Total per element: ~48 bytes vs ~8 bytes (ArrayList reference)
```

### Memory Layout

```
LinkedList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ size (int, 4 bytes)         │
│ first → Node (8 bytes)      │──────┐
│ last → Node (8 bytes)       │──┐   │
└─────────────────────────────┘  │   │
                                 │   ▼
                          Node "A"    Node "B"
                          ┌────────────┐  ┌────────────┐
                          │ item → "A" │  │ item → "B" │
                          │ next ──────────→ null      │
                          │ prev = null│  │ prev ──────────→ Node A
                          └────────────┘  └────────────┘
```

### Memory Comparison

| List Type | Per-Element | 1M Elements |
|-----------|-------------|-------------|
| ArrayList | ~8 bytes | ~8 MB |
| LinkedList | ~48 bytes | ~48 MB |

## 9. Production Incidents

### Incident 1: Memory Leak with Large Lists

**Problem:** Application crashes with OutOfMemoryError after hours of operation.
**Cause:** LinkedList with millions of elements, each node consuming 48 bytes.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows 48 bytes per element vs expected 8 bytes.
**Solution:** Switch to ArrayList for better memory efficiency.
**Prevention:** Use ArrayList unless mid-list insert/remove is critical.

### Incident 2: Stack Overflow from Recursive Traversal

**Problem:** StackOverflowError when traversing large LinkedList.
**Cause:** Recursive traversal allocating stack frame per node.
**Impact:** Process crashes at ~10K nodes.
**Detection:** Core dump shows 10K+ recursive frames.
**Solution:** Convert to iterative with while loop.
**Prevention:** Prefer iterative for unbounded recursion.

### Incident 3: Slow Performance in Hot Loop

**Problem:** Application latency spikes, CPU at 100%.
**Cause:** LinkedList.get() called in tight loop, O(n) per call.
**Impact:** Response time increases linearly with list size.
**Detection:** Profiling shows 99% time in LinkedList node traversal.
**Solution:** Switch to ArrayList for random access, or use Iterator.
**Prevention:** Avoid indexed access on LinkedList, use Iterator.

## 10. Engineering Decision Framework

### Use LinkedList when:
- Frequent insertions/removals at known positions
- Implementing queue/deque operations
- Memory is not a constraint
- Elements are large (node overhead is small relative to element)

### Avoid LinkedList when:
- Random access by index is frequent
- Memory is constrained
- Cache performance matters
- Iteration speed is critical

### When NOT to Use LinkedList
- **Random access**: ArrayList is faster for get(i)
- **Iteration**: ArrayList cache-friendly, LinkedList scattered nodes
- **Memory**: Each node has prev+next pointers overhead
- **Need Deque**: Use ArrayDeque (faster than LinkedList)

### Alternatives

| Alternative | When to Use |
|-------------|-------------|
| ArrayList | Random access, memory efficiency |
| ArrayDeque | Queue/deque operations (faster than LinkedList) |
| CopyOnWriteArrayList | Read-heavy concurrent access |

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ConcurrentModificationException | Thread dump + stack trace | Find which thread modifying, use concurrent collection |
| Slow get() performance | Profiling (JFR, VisualVM) | Switch to ArrayList or use Iterator |
| Memory leak | Heap dump (jmap, MAT) | Check for unused references |
| Infinite loop in traversal | Debug logging | Check next/prev pointers |

## 12. Code Review Checklist

- [ ] Using LinkedList for right reason (not default choice)
- [ ] Not using indexed access (get/set) in hot loops
- [ ] Using Iterator for safe removal during traversal
- [ ] Considering ArrayDeque for queue/deque operations
- [ ] Memory constraints considered
- [ ] Thread safety handled for concurrent access
- [ ] Not using as default List (ArrayList is usually better)

## 13. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size, use bounded collections |
| ConcurrentModificationException | Service degradation | Use concurrent collections |
| Null pointer | Application crash | Null checks, use Optional |

## 14. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | LinkedList introduced | Standard linked list implementation |
| Java 6 | Deque interface added | LinkedList implements Deque |
| Java 8 | Stream support | Can use with streams |
| Java 21 | SequencedCollection | getFirst()/getLast() added |

## 15. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| LinkedList | 1.2 | Stable |
| Deque interface | 6.0 | Stable |
| Stream support | 8.0 | Stable |
| SequencedCollection | 21 | Stable |

## 16. Best Practices

1. **Use as Deque**: LinkedList is faster as Queue/Deque than as List
2. **Prefer ArrayDeque**: For FIFO/LIFO operations
3. **Use Iterator**: For safe removal during traversal
4. **Avoid indexed access**: O(n) per call
5. **Consider memory**: 6x more memory than ArrayList per element
6. **Pre-allocate if possible**: Not directly supported, but can add elements in order

## 17. Common Mistakes

1. **Using as default List**: ArrayList is usually better
2. **Calling get() in loop**: O(n) per call, use Iterator instead
3. **Ignoring memory overhead**: 48 bytes per element vs 8 bytes
4. **Using for random access**: LinkedList has O(n) indexed access
5. **Not considering ArrayDeque**: For queue/deque operations

## 18. Common Myths

### Myth 1: LinkedList is always faster for insert/remove
**Reality:** Only O(1) at known positions. Finding position is O(n).

### Myth 2: LinkedList uses less memory
**Reality:** Uses 6x more memory due to node overhead.

### Myth 3: LinkedList is better for large collections
**Reality:** Worse due to poor cache locality and memory overhead.

### Myth 4: LinkedList is thread-safe
**Reality:** Not thread-safe, use concurrent collections.

## 19. One-Minute Revision

- Doubly-linked list implementation of List and Deque
- O(1) add/remove at ends, O(n) for indexed access
- 48 bytes per element vs 8 bytes for ArrayList
- Best as Queue/Deque, not as general-purpose List
- Not thread-safe, use concurrent collections
- Prefer ArrayDeque for queue/deque operations

## 20. Related Topics

| Topic | Relationship |
|-------|-------------|
| ArrayList | Alternative List implementation |
| ArrayDeque | Faster Queue/Deque implementation |
| Deque | LinkedList implements Deque |
| Queue | LinkedList implements Queue |
| Iterator | Used for safe traversal |

## 21. Interview Questions

1. **What is the time complexity of LinkedList.get(index)?** — O(n). Must traverse from head or tail.

2. **How does LinkedList differ from ArrayList?** — LinkedList: O(1) insert/remove, O(n) get. ArrayList: O(1) get, O(n) insert/remove.

3. **When should you use LinkedList over ArrayList?** — Frequent insertions/removals at known positions, or implementing Queue/Deque.

4. **What is the memory overhead of LinkedList?** — ~48 bytes per element (node) vs ~8 bytes for ArrayList.

5. **How does LinkedList implement Deque?** — By maintaining first/last pointers and supporting add/remove at both ends.

6. **Is LinkedList thread-safe?** — No. Use Collections.synchronizedList() or CopyOnWriteArrayList for concurrent access.

## 22. References

- [Oracle Java Documentation - LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 54: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
