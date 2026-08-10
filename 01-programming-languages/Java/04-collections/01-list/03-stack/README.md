# Stack

## Scope

This folder focuses exclusively on Stack.
Examples, exercises, and quizzes in this folder cover only Stack concepts.

## 1. Why It Exists

Stack was introduced in Java 1.0 as a LIFO (Last-In-First-Out) data structure. It extends Vector, inheriting all its methods plus push/pop/peek operations. In Java 6, Deque was introduced as a better alternative.

## 2. What It Is

Stack is a legacy LIFO data structure that extends Vector. It provides push(), pop(), and peek() operations on top of Vector's List methods.

## 3. Internal Working

```
Stack extends Vector:
┌─────────────────────────────┐
│ Vector methods (add, get)   │
│ + push(E e)                 │
│ + pop()                     │
│ + peek()                    │
│ + empty()                   │
│ + search(Object o)          │
└─────────────────────────────┘

Internal storage: Object[] (from Vector)
```

### push() Operation

```java
public E push(E item) {
    addElement(item);  // Vector method
    return item;
}
```

### pop() Operation

```java
public synchronized E pop() {
    E obj;
    int len = size();
    obj = peek();  // Gets last element
    removeElementAt(len - 1);  // Vector method
    return obj;
}
```

## 4. Constructors

```java
Stack<String> stack = new Stack<>();              // Empty stack
Stack<String> stack = new Stack<>();              // Default capacity
```

## 5. Methods

### Stack-Specific Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `push(E e)` | Pushes element onto stack | O(1) amortized |
| `pop()` | Removes and returns top element | O(1) |
| `peek()` | Returns top element without removing | O(1) |
| `empty()` | Checks if stack is empty | O(1) |
| `search(Object o)` | Returns 1-based position from top | O(n) |

### Inherited Vector Methods

| Method | Description |
|--------|-------------|
| `add(E e)` | Adds to end (top of stack) |
| `get(int index)` | Returns element at index |
| `size()` | Returns element count |
| `isEmpty()` | Checks if empty |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| push(E) | O(1) amortized | O(1) |
| pop() | O(1) | O(1) |
| peek() | O(1) | O(1) |
| search(Object) | O(n) | O(1) |
| empty() | O(1) | O(1) |
| size() | O(1) | O(1) |
| contains(Object) | O(n) | O(1) |

## 7. Thread Safety

Stack is synchronized (inherits from Vector):

```java
// push() is synchronized (from Vector)
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}

// pop() is synchronized
public synchronized E pop() {
    // ...
}
```

### Problem: Compound Operations Not Atomic

```java
// NOT thread-safe even with Stack!
if (!stack.empty() && stack.peek().equals(target)) {
    stack.pop();  // Another thread may pop between empty() and peek()
}
```

## 8. Memory Behavior

### Memory Layout

```
Stack object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ elementData reference (8B)  │──────┐
│ elementCount (int, 4B)      │      │
│ capacityIncrement (int, 4B) │      │
└─────────────────────────────┘      │
                                     ▼
                              Object[] elementData
```

### Memory Comparison

| Type | Per-Element | 1M Elements |
|------|-------------|-------------|
| Stack | ~8 bytes + lock | ~8 MB + lock |
| ArrayDeque | ~8 bytes | ~8 MB |

## 9. Production Incidents

### Incident 1: Performance Degradation Under Load

**Problem:** Application slows from 10ms to 500ms under concurrent load.
**Cause:** Stack's synchronized methods causing contention.
**Impact:** Service degraded, user experience poor.
**Detection:** Thread dump shows threads waiting on Stack's monitor.
**Solution:** Switch to ArrayDeque (non-synchronized).
**Prevention:** Use ArrayDeque for LIFO operations.

### Incident 2: Wasted Memory from 2x Growth

**Problem:** Application crashes with OutOfMemoryError.
**Cause:** Stack inherits Vector's 2x growth factor.
**Impact:** Application crash, data loss.
**Detection:** Heap dump shows 50% unused capacity.
**Solution:** Switch to ArrayDeque with 2x growth factor.
**Prevention:** Use ArrayDeque for better memory efficiency.

### Incident 3: Legacy Code Maintained Stack

**Problem:** New developers confused by Stack in codebase.
**Cause:** Legacy code still using Stack instead of ArrayDeque.
**Impact:** Developer confusion, maintenance overhead.
**Detection:** Code review shows Stack usage.
**Solution:** Migrate to ArrayDeque.
**Prevention:** Establish coding standards against Stack.

## 10. Engineering Decision Framework

### When Should I Use This?
- Maintaining legacy code that already uses Stack
- Required by external library or API
- Simple LIFO needed (but prefer alternatives)

### When Should I NOT Use This?
- **Writing new code**: Use ArrayDeque (no synchronization overhead)
- **LIFO queue**: Use ArrayDeque.push() and pop()
- **Performance matters**: Synchronized overhead is too high
- **Thread safety**: Use explicit synchronization with ArrayList

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| ArrayDeque | General purpose LIFO (recommended) | Faster, no legacy overhead |
| LinkedList | LIFO with List operations | Higher memory, slower |
| Collections.synchronizedList() | When you need synchronization on ArrayList | Simple wrapper |
| ConcurrentHashMap | Thread-safe with fine-grained locking | Better concurrency |

### What Trade-offs Am I Making?
- **Thread Safety**: Synchronized but slow vs fast but unsafe (ArrayDeque)
- **Legacy vs Modern**: Legacy code vs modern alternatives
- **Memory**: Medium memory vs low memory (ArrayDeque)
- **Performance**: Synchronized overhead vs no overhead

### What Would I Choose in Production?
> Never use Stack in new code. Use ArrayDeque for LIFO operations. If you're maintaining legacy code, plan migration to ArrayDeque.

### Common Code Review Comments
- "Why are you using Stack? Use ArrayDeque instead."
- "Stack is legacy — plan migration to ArrayDeque."
- "This Stack should be an ArrayDeque for better performance."
- "Stack extends Vector — it has synchronized overhead."

### Common Production Mistakes

> Notice: Stack is deprecated for removal in Java 9+ — plan migration to ArrayDeque.

> Notice: Stack extends Vector — it inherits all Vector's synchronized overhead.

> Notice: Stack.toString() is synchronized — it can cause contention in concurrent code.

> Notice: Stack is legacy — it was part of Java 1.0, before the Collections Framework.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Slow performance | Profiling (JFR, VisualVM) | Check for Stack contention |
| ConcurrentModificationException | Thread dump | Find which thread modifying |
| Memory leak | Heap dump | Check for unused Stack references |
| Legacy code confusion | Code review | Migrate to ArrayDeque |

## 12. Code Review Checklist

- [ ] Not using Stack in new code
- [ ] Migrating legacy Stack to ArrayDeque
- [ ] Using ArrayDeque for LIFO operations
- [ ] Not using search() method (use contains() instead)
- [ ] Considering thread safety requirements
- [ ] Checking for compound operation atomicity
- [ ] Performance testing under concurrent load

## 13. Architecture Considerations

### Where Stack Fits in System Design

| Layer | Use Case | Why Stack |
|-------|----------|-----------|
| Expression Parser | Operator precedence | LIFO pattern |
| Undo/Redo | State management | Stack-based history |
| Function Call | Recursion simulation | Call stack pattern |
| Backtracking | Maze/algorithm solving | Push/pop state exploration |

### Integration Patterns

```
Client → API Gateway → Stack → Service → Stack → Client
                    ↓
            Stack → Undo Manager → Stack
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 10K elements | Stack works but prefer ArrayDeque |
| 10K - 100K elements | Migrate to ArrayDeque |
| 100K - 1M elements | Consider iterative approach |
| > 1M elements | Consider database or external storage |

### When to Replace Stack in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| LIFO operations | ArrayDeque | Faster, no sync overhead |
| Thread-safe stack | Collections.synchronizedList() | Better performance |
| Priority stack | PriorityQueue | Priority-based ordering |
| Bounded stack | LinkedBlockingDeque | Capacity limit |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max capacity, use bounded collections |
| Deadlock from synchronization | Service hang | Use fine-grained locking |
| Legacy code vulnerabilities | Security risk | Migrate to modern collections |

## 15. Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Stack introduced | Use as LIFO |
| Java 1.2 | Collections Framework | Migrate to Deque |
| Java 6 | ArrayDeque introduced | Use ArrayDeque for LIFO |
| Java 5 | Generics added | Add type parameters |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Stack | 1.0 | Legacy (avoid) |
| ArrayDeque | 6.0 | Recommended |

## 17. Best Practices

1. **Avoid in new code**: Use ArrayDeque for LIFO operations
2. **Migrate existing**: Replace Stack with ArrayDeque
3. **Use push()/pop()/peek()**: Standard LIFO operations
4. **Consider thread safety**: Stack's synchronization is coarse-grained
5. **Monitor performance**: Stack adds overhead even in single-threaded code
6. **Use modern alternatives**: ArrayDeque for LIFO, Deque for double-ended

## 18. Common Mistakes

1. **Using Stack as default**: ArrayDeque is faster and more memory efficient
2. **Thinking Stack is thread-safe for compound operations**: Contains-then-pop is not atomic
3. **Using search() method**: O(n) operation, use contains() or indexOf() instead
4. **Ignoring synchronization overhead**: Stack is slower than ArrayDeque even in single-threaded code
5. **Not migrating**: Legacy Stack code should be updated

## 19. Common Myths

### Myth 1: Stack is always thread-safe
**Reality:** Individual methods are synchronized, but compound operations are not atomic.

### Myth 2: Stack is better than ArrayDeque
**Reality:** ArrayDeque is faster and more memory efficient for LIFO operations.

### Myth 3: Stack is deprecated
**Reality:** Not deprecated, but discouraged in favor of ArrayDeque.

### Myth 4: Stack is better for concurrent access
**Reality:** ArrayDeque with explicit synchronization or concurrent collections is better.

## 20. One-Minute Revision

- Legacy LIFO data structure extending Vector
- Every method synchronized, causing overhead
- 2x growth factor wastes memory
- Avoid in new code, use ArrayDeque
- Not deprecated but discouraged
- Prefer ArrayDeque for LIFO operations

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| ArrayDeque | Modern alternative, non-synchronized |
| Deque | Interface that ArrayDeque implements |
| Vector | Parent class, also legacy |
| LIFO | Last-In-First-Out pattern |
| Legacy code | Often contains Stack, should migrate |

## 22. Interview Questions

1. **What is the difference between Stack and ArrayDeque?** — Stack is synchronized, ArrayDeque is not. Stack has 2x growth, ArrayDeque has 2x. ArrayDeque is faster for LIFO.

2. **Is Stack thread-safe?** — Yes, individual methods are synchronized. But compound operations are not atomic.

3. **When should you use Stack?** — Almost never in new code. Only in legacy code that already uses Stack.

4. **What are the Stack-specific methods?** — push(), pop(), peek(), empty(), search().

5. **What is the growth factor of Stack?** — 2x (doubles capacity), inherited from Vector.

6. **How do you implement LIFO in Java?** — Use ArrayDeque for LIFO operations.

## 23. References

- [Oracle Java Documentation - Stack](https://docs.oracle.com/javase/8/docs/api/java/util/Stack.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 54: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
