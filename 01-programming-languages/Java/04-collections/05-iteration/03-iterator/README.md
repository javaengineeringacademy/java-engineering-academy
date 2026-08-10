# Iterator Pattern

## 1. Scope

This folder covers the `Iterator` interface for traversing and selectively removing elements from collections in Java.
Examples and exercises demonstrate hasNext(), next(), remove(), and fail-fast behavior.

## 2. Why It Exists

Before Iterator, modifying a collection during traversal required index gymnastics:

```java
for (int i = 0; i < list.size(); i++) {
    if (shouldRemove(list.get(i))) {
        list.remove(i);
        i--; // Remember to adjust index
    }
}
```

This is error-prone (forgetting `i--` skips elements) and does not work at all for Sets. Iterator solves this by providing a standard interface for "walk through the collection and optionally remove the current element."

## 3. What It Is

`Iterator<E>` is an interface in `java.util` that provides a unified way to traverse any collection. It has three methods:

```java
public interface Iterator<E> {
    boolean hasNext();  // Are there more elements?
    E next();           // Get the next element
    default void remove();  // Remove the last returned element
}
```

Key traits:
- Forward-only traversal
- One-directional cursor between elements
- Supports safe removal via `remove()`
- Fail-fast: throws `ConcurrentModificationException` if collection is modified outside the Iterator

## 4. Internal Working

### How ArrayList Iterator works

```java
// ArrayList's iterator() method
public Iterator<E> iterator() {
    return new Itr();
}

private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned (-1 if none)
    int expectedModCount = modCount;  // fail-fast tracking

    public boolean hasNext() {
        return cursor != size;
    }

    public E next() {
        checkForComodification();
        int i = cursor;
        Object[] elementData = ArrayList.this.elementData;
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        checkForComodification();
        Itr.remove();
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

### Cursor position between elements

```
Collection: [A] [B] [C] [D]

Initial state:
              cursor
                ↓
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
      -1   0   1   2   3
           ↑
         lastRet = -1

After next() returns A:
              cursor
                    ↓
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
      -1   0   1   2   3
           ↑
         lastRet = 0

After next() returns B:
              cursor
                        ↓
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
      -1   0   1   2   3
                ↑
              lastRet = 1
```

### Fail-fast mechanism

```
modCount incremented ──► Iterator checks ──► Mismatch ──► CME thrown
(outside Iterator)       (on next/remove)     (modCount != expectedModCount)
```

## 5. Constructors / Usage

### Basic iteration
```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    System.out.println(s);
}
```

### Safe removal during iteration
```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.startsWith("X")) {
        it.remove(); // Removes "X" elements safely
    }
}
```

### Iterating a Set
```java
Iterator<Integer> it = set.iterator();
while (it.hasNext()) {
    int n = it.next();
    if (n % 2 == 0) {
        it.remove(); // Remove even numbers
    }
}
```

### Using Iterator with while
```java
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    process(it.next());
}
```

### Converting to list (Java 9+)
```java
Iterator<String> it = list.iterator();
List<String> remaining = new ArrayList<>();
it.forEachRemaining(remaining::add);
```

## 6. Methods

| Method | Description | Throws |
|--------|-------------|--------|
| `hasNext()` | Returns true if more elements exist | — |
| `next()` | Returns next element | `NoSuchElementException` if no more |
| `remove()` | Removes last element returned by `next()` | `IllegalStateException` if called before next() or twice without next() |
| `forEachRemaining(Consumer)` | Iterates remaining elements | — (Java 8+) |

## 7. Complexity Table

| Operation | ArrayList | LinkedList | HashSet | Time |
|-----------|-----------|------------|---------|------|
| iterator() | O(1) | O(1) | O(1) | Constant |
| hasNext() | O(1) | O(1) | O(1) | Constant |
| next() | O(1) | O(1) | O(1) amortized | Constant |
| remove() | O(n) | O(1) | O(1) | Depends on collection |
| forEachRemaining() | O(n) | O(n) | O(n) | Linear |

## 8. Thread Safety

Iterator is NOT thread-safe:

```java
// UNSAFE — two threads sharing one Iterator
Iterator<String> it = list.iterator();
// Thread 1:
String s1 = it.next();
// Thread 2:
String s2 = it.next(); // Race condition

// SAFE — each thread gets its own Iterator
Iterator<String> it1 = list.iterator();
Iterator<String> it2 = list.iterator();
```

Fail-fast does NOT make it thread-safe. It only detects structural modification, not concurrent access. For concurrent iteration:
- `CopyOnWriteArrayList` (snapshot Iterator)
- `ConcurrentHashMap` (weakly consistent Iterator)
- External synchronization

## 9. Memory Behavior

```
Iterator object (ArrayList):
┌────────────────────────────────┐
│ Object header (12 bytes)       │
│ cursor (int, 4 bytes)          │
│ lastRet (int, 4 bytes)         │
│ expectedModCount (int, 4 bytes)│
│ ArrayList reference (8 bytes)  │
│ (padding 4 bytes)              │
└────────────────────────────────┘
Total: ~36 bytes per Iterator
```

| Collection | Iterator Size | Notes |
|------------|--------------|-------|
| ArrayList | ~36 bytes | Stores cursor, lastRet, expectedModCount |
| LinkedList | ~36 bytes | Stores cursor node reference |
| HashSet | ~36 bytes | Stores internal bucket cursor |
| TreeMap | ~48 bytes | Stores stack for tree traversal |

## 10. Production Incidents

### Incident 1: ConcurrentModificationException in E-Commerce Cart

**Problem:** Shopping cart page throws exception for some users.
**Cause:** Enhanced for loop over cart items while a background thread updated item quantities.
```java
for (CartItem item : cart.getItems()) {
    total += item.getPrice() * item.getQuantity();
    // Background thread modifies cart here
}
```
**Impact:** Users see error page, revenue loss.
**Solution:** Copied cart items to a local list before iterating.
**Prevention:** Iterate over a snapshot when concurrent modification is possible.

### Incident 2: Iterator.remove() Called Without next()

**Problem:** Batch cleanup job crashes intermittently.
**Cause:** Code called `it.remove()` after `it.hasNext()` returned false:
```java
while (it.hasNext()) {
    String s = it.next();
    if (shouldRemove(s)) lastToRemove = s;
}
if (lastToRemove != null) it.remove(); // IllegalStateException
```
**Impact:** Cleanup job fails, data not cleaned.
**Solution:** Track the element and remove it inside the loop.
**Prevention:** Always call remove() immediately after next(), never after the loop ends.

### Incident 3: Memory Leak from Long-Lived Iterator

**Problem:** Application heap grows steadily over hours.
**Cause:** An Iterator was stored in a field of a long-lived object. The Iterator holds a reference to the entire collection, preventing GC even after the collection was cleared.
**Impact:** OutOfMemoryError after 6 hours.
**Solution:** Iterator scope limited to the method, not stored in fields.
**Prevention:** Never store Iterators as fields. Create them locally and use immediately.

## 11. Engineering Decision Framework

### When Should I Use This?
- You need to remove elements during iteration
- You need fine-grained control over traversal (skip, pause, resume)
- You are implementing a custom collection
- You need to iterate a collection that does not support indexed access (Set)

### When Should I NOT Use This?
- Simple forward traversal without removal (use enhanced for)
- You need the element's index (use for loop)
- You need bidirectional traversal (use ListIterator)
- You are in a lambda or stream pipeline (use forEach or Stream)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| Enhanced for | Simple traversal, no removal | Cannot remove |
| removeIf() | Conditional removal | No control during traversal |
| ListIterator | Need add/set/bidirectional | Lists only |
| Stream API | Functional transformations | No removal |

### What Trade-offs Am I Making?
- **Control**: Full control over traversal vs simplicity of enhanced for
- **Safety**: Fail-fast detects bugs early but can surprise in concurrent code
- **Verbosity**: More code than enhanced for or streams

### What Would I Choose in Production?
> Use Iterator when you need to remove elements during traversal. For simple read-only iteration, use enhanced for. For conditional removal, `removeIf()` is cleaner than manual Iterator.remove().

### Code Review Comments
- "Use `removeIf()` instead of manual Iterator.remove() — cleaner and less error-prone."
- "This Iterator could leak memory if stored as a field — scope it to the method."
- "You're calling remove() without calling next() first — that will throw IllegalStateException."
- "Consider using CopyOnWriteArrayList if concurrent modification is possible."

## 12. Performance

| Operation | Time | Why |
|-----------|------|-----|
| iterator() | O(1) | Just creates a small object |
| next() | O(1) amortized | Increment cursor |
| hasNext() | O(1) | Simple comparison |
| remove() on ArrayList | O(n) | Must shift elements |
| remove() on LinkedList | O(1) | Unlink node |
| remove() on HashSet | O(1) amortized | Hash-based removal |

JIT optimizations:
- Iterator methods are small and get inlined easily
- The `checkForComodification` branch is usually predicted as not-taken
- `remove()` on ArrayList triggers `System.arraycopy`, which is SIMD-optimized

## 13. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ConcurrentModificationException | Stack trace | Identify which line triggered modCount check |
| NoSuchElementException | Stack trace | Check if hasNext() was called before next() |
| IllegalStateException on remove() | Stack trace | Ensure remove() is called right after next() |
| Iterator stuck in loop | Debugger | Check if hasNext() always returns true |
| Memory leak from Iterator | Heap dump | Check if Iterator is stored as a field |

## 14. Code Review Checklist

- [ ] Iterator.remove() called only after next()
- [ ] No collection modification outside Iterator during iteration
- [ ] Iterator not stored as a field or in a long-lived object
- [ ] hasNext() checked before next()
- [ ] Concurrent access handled (CopyOnWriteArrayList or synchronization)
- [ ] removeIf() considered as cleaner alternative

## 15. Architecture Considerations

### Where Iterator Fits in System Design

| Layer | Use Case | Why Iterator |
|-------|----------|-------------|
| Collection Framework | Standard traversal API | Universal interface for all collections |
| Custom Collections | Implementing Iterable | Required for for-each compatibility |
| Data Processing | Conditional element removal | Safe removal during traversal |
| Parsing | Token stream processing | One-pass traversal |

### Integration Patterns

```
Collection ──► iterator() ──► Iterator ──► hasNext()/next() ──► Processing
                                                  │
                                                  └── remove() (optional)
```

## 16. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| ConcurrentModificationException | Service crash | Use concurrent collections |
| NoSuchElementException | NPE or crash | Always check hasNext() first |
| Memory leak from long-lived Iterator | OOM | Scope Iterator to method |

## 17. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | Iterator introduced | Standard collection traversal |
| Java 5 | Generics added | Type-safe iteration |
| Java 8 | forEachRemaining() added | Bulk operation |
| Java 8 | Stream API | Functional alternative |
| Java 9 | List.of() returns unmodifiable | Iterator cannot remove |

## 18. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Iterator interface | 1.2 | Stable |
| remove() method | 1.2 | Stable |
| Generics | 5.0 | Stable |
| forEachRemaining() | 8.0 | Stable |
| fail-fast behavior | 1.2 | Stable (documented) |

## 19. Best Practices

1. Use `removeIf()` instead of manual Iterator.remove() when possible
2. Always call `next()` before `remove()`
3. Never store Iterators as fields in long-lived objects
4. Use CopyOnWriteArrayList for concurrent iteration without CME
5. Check `hasNext()` before `next()` to avoid NoSuchElementException
6. Scope Iterator to the method — don't pass it around
7. Prefer enhanced for for simple read-only traversal

## 20. Common Mistakes

1. **Calling remove() before next()**: Throws IllegalStateException
2. **Calling remove() twice without next()**: Throws IllegalStateException
3. **Modifying collection outside Iterator**: Triggers ConcurrentModificationException
4. **Storing Iterator as field**: Memory leak — holds reference to collection
5. **Forgetting hasNext() check**: NoSuchElementException on empty collection
6. **Using Iterator on CopyOnWriteArrayList and expecting removal**: remove() throws UnsupportedOperationException

## 21. Common Myths

### Myth 1: Iterator is always fail-fast
**Reality:** Only if the collection implements fail-fast. CopyOnWriteArrayList uses snapshot semantics. ConcurrentHashMap uses weakly consistent Iterators.

### Myth 2: Iterator.remove() is O(1) for all collections
**Reality:** O(1) for LinkedList and HashSet. O(n) for ArrayList because elements must be shifted.

### Myth 3: ConcurrentModificationException means the collection is corrupt
**Reality:** The collection is fine. The exception is a best-effort detection mechanism, not a guarantee. In concurrent code, you may not get the exception at all — just inconsistent data.

## 22. One-Minute Revision

- Iterator provides hasNext(), next(), and remove()
- Forward-only traversal with a cursor between elements
- Fail-fast: throws ConcurrentModificationException if collection modified outside Iterator
- Always call remove() immediately after next()
- Never store Iterators as fields — scope them to methods
- Use removeIf() for cleaner conditional removal
- Not thread-safe — use concurrent collections for multi-threaded access

## 23. Related Topics

| Topic | Relationship |
|-------|-------------|
| Enhanced For Loop | Compiler-generated Iterator usage |
| ListIterator | Bidirectional variant for Lists |
| Iterable | Interface that provides Iterator via iterator() |
| ConcurrentModificationException | Exception from fail-fast detection |
| CopyOnWriteArrayList | Provides snapshot Iterators |
| Spliterator | Parallel iteration variant |

## 24. Interview Questions

1. **What are the three methods of Iterator?** — `hasNext()`, `next()`, and `remove()`.

2. **What is fail-fast?** — The Iterator checks `modCount` against `expectedModCount`. If the collection is structurally modified outside the Iterator, it throws ConcurrentModificationException.

3. **What is the difference between Iterator and ListIterator?** — ListIterator is bidirectional (has previous()), supports add() and set(), and works only on Lists.

4. **Why does Iterator.remove() exist but not Iterator.add()?** — Iterator was designed for traversal and selective removal, not for adding elements during iteration.

5. **How do you safely iterate and remove from a collection in a multi-threaded environment?** — Use CopyOnWriteArrayList, ConcurrentHashMap, or external synchronization.

## 25. References

- [Oracle Java Documentation - Iterator](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html)
- [JLS - Interface Iterator](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.8.1)
- [Effective Java - Item 47: Know and use your collections](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
