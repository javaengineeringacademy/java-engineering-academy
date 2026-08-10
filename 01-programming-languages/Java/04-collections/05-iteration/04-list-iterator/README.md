# ListIterator Pattern

## 1. Scope

This folder covers the `ListIterator` interface for bidirectional iteration and modification of List collections in Java.
Examples and exercises demonstrate forward/backward traversal, add(), set(), and index-based positioning.

## 2. Why It Exists

Iterator only goes forward. If you need to traverse backwards, insert elements during iteration, or replace the current element, Iterator falls short:

```java
// Iterator can only go forward
Iterator<String> it = list.iterator();
// No previous() method
// No add() method
// No set() method
```

ListIterator extends Iterator with:
- `previous()` — go backwards
- `add(E)` — insert at current position
- `set(E)` — replace last returned element
- `nextIndex()` / `previousIndex()` — get current position

This makes it the only Iterator variant that can fully modify a List during traversal.

## 3. What It Is

`ListIterator<E>` extends `Iterator<E>` and provides bidirectional traversal for List collections. It has a cursor positioned between elements, allowing you to move forward and backward, and to add, set, or remove elements.

```java
public interface ListIterator<E> extends Iterator<E> {
    boolean hasNext();
    E next();
    int nextIndex();
    boolean hasPrevious();
    E previous();
    int previousIndex();
    void set(E e);      // Replace last returned element
    void add(E e);       // Insert at current position
    void remove();       // Remove last returned element
}
```

## 4. Internal Working

### Cursor position for bidirectional traversal

```
Collection: [A] [B] [C] [D]

Initial cursor (at beginning):
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
      ↑
    cursor (before first element)
    nextIndex() = 0, previousIndex() = -1

After next() returns A:
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
          ↑
    cursor (between A and B)
    nextIndex() = 1, previousIndex() = 0

After previous() returns B:
    ┌───┬───┬───┬───┬───┐
    │   │ A │ B │ C │ D │
    └───┴───┴───┴───┴───┘
        ↑
    cursor (between A and B)
    nextIndex() = 1, previousIndex() = 0
```

### ArrayList ListIterator implementation

```java
private class ListItr extends Itr implements ListIterator<E> {
    ListItr(int index) {
        cursor = index;
    }

    public boolean hasPrevious() {
        return cursor != 0;
    }

    public E previous() {
        checkForComodification();
        int i = cursor - 1;
        Object[] elementData = ArrayList.this.elementData;
        cursor = lastRet = i;
        return (E) elementData[i];
    }

    public void set(E e) {
        checkForComodification();
        ArrayList.this.set(lastRet, e);
    }

    public void add(E e) {
        checkForComodification();
        ArrayList.this.add(cursor, e);
        cursor++;
        lastRet = -1;
        expectedModCount = modCount;
    }
}
```

## 5. Constructors / Usage

### Create from List
```java
List<String> list = new ArrayList<>(List.of("A", "B", "C"));
ListIterator<String> lit = list.listIterator();
```

### Create from specific index
```java
ListIterator<String> lit = list.listIterator(2); // Start at index 2
```

### Forward traversal
```java
while (lit.hasNext()) {
    System.out.println(lit.next());
}
```

### Backward traversal
```java
// First, move to end
while (lit.hasNext()) lit.next();
// Now go backwards
while (lit.hasPrevious()) {
    System.out.println(lit.previous());
}
```

### Replace elements during iteration
```java
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String s = lit.next();
    lit.set(s.toUpperCase()); // Replace in-place
}
```

### Insert elements during iteration
```java
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String s = lit.next();
    if (s.equals("B")) {
        lit.add("X"); // Insert X before B
    }
}
// list is now [A, X, B, C]
```

### Mixed operations
```java
ListIterator<Integer> lit = list.listIterator();
while (lit.hasNext()) {
    int val = lit.next();
    if (val < 0) {
        lit.set(Math.abs(val));  // Replace negative with positive
    }
    if (val == 0) {
        lit.add(100);            // Insert 100 after 0
    }
}
```

## 6. Methods

| Method | Description | Throws |
|--------|-------------|--------|
| `hasNext()` | More elements forward? | — |
| `next()` | Return next element | `NoSuchElementException` |
| `nextIndex()` | Index of next element | — |
| `hasPrevious()` | More elements backward? | — |
| `previous()` | Return previous element | `NoSuchElementException` |
| `previousIndex()` | Index of previous element | — |
| `set(E e)` | Replace last returned element | `IllegalStateException` if no last return |
| `add(E e)` | Insert at cursor position | — |
| `remove()` | Remove last returned element | `IllegalStateException` if no last return |

## 7. Complexity Table

| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| listIterator() | O(1) | O(1) | Creates iterator object |
| next() | O(1) | O(1) | Increment cursor |
| previous() | O(1) | O(1) | Decrement cursor |
| set(E) | O(1) | O(1) | Replace element at lastRet |
| add(E) | O(n) | O(1) | ArrayList shifts elements |
| remove() | O(n) | O(1) | ArrayList shifts elements |
| nextIndex() | O(1) | O(1) | Return cursor |
| previousIndex() | O(1) | O(1) | Return cursor - 1 |

## 8. Thread Safety

ListIterator is NOT thread-safe:

```java
// UNSAFE — two threads sharing one ListIterator
ListIterator<String> lit = list.listIterator();
// Thread 1:
lit.next();
lit.set("X"); // Modifies collection
// Thread 2:
lit.next(); // May see inconsistent state

// SAFE — each thread gets its own ListIterator
ListIterator<String> lit1 = list.listIterator();
ListIterator<String> lit2 = list.listIterator();
```

For concurrent bidirectional iteration:
- Use `CopyOnWriteArrayList` (snapshot Iterator, but add/set throw UnsupportedOperationException)
- Use external synchronization

## 9. Memory Behavior

```
ListIterator object (ArrayList):
┌────────────────────────────────┐
│ Object header (12 bytes)       │
│ cursor (int, 4 bytes)          │
│ lastRet (int, 4 bytes)         │
│ expectedModCount (int, 4 bytes)│
│ ArrayList reference (8 bytes)  │
│ (padding 4 bytes)              │
└────────────────────────────────┘
Total: ~36 bytes per ListIterator

For LinkedList, each step of traversal
follows node.next/node.prev pointers.
```

| Collection | Iterator Size | Notes |
|------------|--------------|-------|
| ArrayList | ~36 bytes | Same as Iterator |
| LinkedList | ~36 bytes | Same as Iterator |

## 10. Production Incidents

### Incident 1: add() During Forward Traversal Caused Index Shift

**Problem:** Code inserts elements during forward iteration but the inserted element is never visited.
**Cause:** `add()` inserts before the cursor. After `add()`, the cursor moves past the new element, so it is skipped in the next `next()` call.
```java
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String s = lit.next();
    lit.add("NEW"); // NEW is added before cursor, skipped
}
```
**Impact:** Elements never processed.
**Solution:** Call `previous()` after `add()` to visit the new element.
**Prevention:** Understand cursor positioning — `add()` inserts before cursor.

### Incident 2: set() Called Without Previous next()/previous()

**Problem:** `IllegalStateException` in production data migration.
**Cause:** Called `lit.set(newValue)` without first calling `next()` or `previous()`.
**Impact:** Migration job fails.
**Solution:** Ensure `next()` or `previous()` is called before `set()`.
**Prevention:** Follow the pattern: `next()` then `set()` immediately after.

### Incident 3: ConcurrentModificationException from Multiple ListIterators

**Problem:** Two ListIterators on the same ArrayList — one calls `add()`, the other calls `next()`.
**Cause:** Both iterators have different `expectedModCount` values. When one modifies the list, the other detects the mismatch.
**Impact:** Service crash.
**Solution:** Use only one ListIterator at a time, or use CopyOnWriteArrayList.
**Prevention:** Don't use multiple Iterators on the same List concurrently.

## 11. Engineering Decision Framework

### When Should I Use This?
- You need bidirectional traversal (forward and backward)
- You need to insert elements during iteration
- You need to replace elements during iteration
- You need to know the current index during traversal

### When Should I NOT Use This?
- Simple forward traversal (use enhanced for)
- You only need removal (use Iterator)
- You don't need to modify the list during traversal (use enhanced for)
- You are working with a Set or Map (ListIterator is List-specific)

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| Iterator | Forward-only, removal only | Cannot go backward |
| Enhanced for | Simple read-only traversal | No modification |
| for loop with index | Random access by index | No insertion during iteration |
| Stream API | Functional transformations | No modification |
| removeIf() | Conditional removal | No insertion |

### What Trade-offs Am I Making?
- **Flexibility**: Full bidirectional modification vs simplicity of enhanced for
- **Verbosity**: More code than enhanced for
- **Performance**: add() on ArrayList is O(n) due to shifting
- **Scope**: Only works on Lists, not Sets or Maps

### What Would I Choose in Production?
> Use ListIterator when you need to insert or replace elements during iteration, or when you need bidirectional traversal. For simple forward traversal, prefer enhanced for. For conditional removal, prefer removeIf().

### Code Review Comments
- "Use removeIf() instead of ListIterator.remove() — cleaner."
- "This ListIterator.add() inserts before the cursor — be aware of cursor position."
- "You don't need bidirectional traversal here — use Iterator instead."
- "set() must be called after next() or previous() — not before."

## 12. Performance

| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| next() | O(1) | O(1) | Cursor movement |
| previous() | O(1) | O(1) | Cursor movement |
| set(E) | O(1) | O(1) | Direct replacement |
| add(E) | O(n) | O(1) | ArrayList shifts elements |
| remove() | O(n) | O(1) | ArrayList shifts elements |

JIT optimizations:
- `set()` is inlined — just an array write
- `next()`/`previous()` are simple cursor increments
- `add()` on ArrayList triggers `System.arraycopy`, which is SIMD-optimized

## 13. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| IllegalStateException on set() | Stack trace | Ensure next()/previous() called before set() |
| Element skipped after add() | Debugger | Check cursor position after add() |
| ConcurrentModificationException | Stack trace | Don't use multiple iterators on same list |
| NoSuchElementException on previous() | Stack trace | Check hasPrevious() before previous() |
| Wrong element replaced | Debugger | Check lastRet after next()/previous() |

## 14. Code Review Checklist

- [ ] ListIterator used only on List implementations
- [ ] set() called after next()/previous(), not before
- [ ] add() cursor position understood (inserts before cursor)
- [ ] No multiple ListIterators on same List concurrently
- [ ] remove() called only after next()/previous()
- [ ] Bidirectional traversal needed (otherwise use Iterator or enhanced for)

## 15. Architecture Considerations

### Where ListIterator Fits in System Design

| Layer | Use Case | Why ListIterator |
|-------|----------|-----------------|
| Data Processing | In-place element transformation | set() replaces during traversal |
| List Manipulation | Insert elements during iteration | add() inserts at cursor |
| Reverse Processing | Process elements backward | previous() for backward traversal |
| List Utilities | List reversal, splicing | Bidirectional control |

### Integration Patterns

```
List ──► listIterator() ──► ListIterator ──► next()/previous() ──► Processing
                                    │
                                    ├── set(E)  (replace)
                                    ├── add(E)  (insert)
                                    └── remove() (remove)
```

## 16. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| ConcurrentModificationException | Service crash | Use single ListIterator per List |
| NoSuchElementException | NPE or crash | Check hasNext()/hasPrevious() |
| Memory leak from long-lived iterator | OOM | Scope iterator to method |

## 17. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 1.2 | ListIterator introduced | Bidirectional iteration |
| Java 5 | Generics added | Type-safe iteration |
| Java 8 | Stream API | Functional alternative |
| Java 9 | List.of() returns unmodifiable | ListIterator cannot add/set/remove |

## 18. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| ListIterator | 1.2 | Stable |
| Generics | 5.0 | Stable |
| add() method | 1.2 | Stable |
| set() method | 1.2 | Stable |
| Stream API (alternative) | 8.0 | Stable |

## 19. Best Practices

1. Use ListIterator only when you need bidirectional traversal or insertion
2. Understand cursor positioning — add() inserts before cursor
3. Call set() immediately after next()/previous(), not separately
4. Don't use multiple ListIterators on the same List concurrently
5. Consider using removeIf() for conditional removal (cleaner than ListIterator.remove())
6. Use listIterator(index) to start from a specific position
7. For backward-only traversal, use previous()/hasPrevious()

## 20. Common Mistakes

1. **add() inserts before cursor**: New element is skipped in forward traversal
2. **set() without next()/previous()**: Throws IllegalStateException
3. **Using multiple ListIterators concurrently**: ConcurrentModificationException
4. **Not understanding cursor position**: Elements added in wrong order
5. **Calling remove() after add()**: Removes the wrong element
6. **Using ListIterator on non-List collections**: Compilation error

## 21. Common Myths

### Myth 1: ListIterator can go both directions simultaneously
**Reality:** It has a single cursor position. You can call next() or previous() to move it, but you cannot move in both directions in a single pass without careful ordering.

### Myth 2: ListIterator.add() inserts after the cursor
**Reality:** add(E) inserts the element before the cursor. The new element becomes the element that next() would return.

### Myth 3: ListIterator is only for ArrayList
**Reality:** It works on any List implementation — ArrayList, LinkedList, CopyOnWriteArrayList (though copyOnWriteArrayList's ListIterator does not support add/remove).

## 22. One-Minute Revision

- ListIterator extends Iterator with bidirectional traversal (previous/hasPrevious)
- Supports add(), set(), and remove() during iteration
- Cursor is positioned between elements — add() inserts before cursor
- set() replaces the last element returned by next()/previous()
- Not thread-safe — use one ListIterator per List
- Only works on List implementations (not Set or Map)
- add() on ArrayList is O(n) due to element shifting

## 23. Related Topics

| Topic | Relationship |
|-------|-------------|
| Iterator | Forward-only variant |
| Enhanced For Loop | Simpler alternative for read-only traversal |
| ArrayList | O(n) add()/remove() due to shifting |
| LinkedList | O(1) add()/remove() during iteration |
| CopyOnWriteArrayList | Snapshot ListIterator, no add/remove |

## 24. Interview Questions

1. **What is the difference between Iterator and ListIterator?** — ListIterator is bidirectional (has previous()), supports add(), set(), and works only on Lists.

2. **What does add() do in ListIterator?** — It inserts an element before the cursor position. The new element becomes the element next() would return.

3. **What does set() do?** — It replaces the element returned by the last call to next() or previous(). It must be called immediately after next()/previous().

4. **Why can't you use ListIterator on a Set?** — ListIterator requires an ordered collection with positional access (List). Sets are unordered.

5. **What happens if you call set() without calling next() first?** — Throws IllegalStateException.

## 25. References

- [Oracle Java Documentation - ListIterator](https://docs.oracle.com/javase/8/docs/api/java/util/ListIterator.html)
- [JLS - Interface ListIterator](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.8.1)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
