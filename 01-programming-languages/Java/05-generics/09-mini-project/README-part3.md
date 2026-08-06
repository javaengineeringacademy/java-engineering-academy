# 09 - Mini Project: Type-Safe Collection Framework (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
```

---

## Performance

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| add(E) | O(1) amortized | O(1) |
| get(int) | O(1) | O(n) |
| set(int, E) | O(1) | O(n) |
| remove(int) | O(n) | O(n) |
| contains(E) | O(n) | O(n) |
| Memory | Compact | Node overhead |

### When to Use Each

- **ArrayList**: Random access, large datasets, memory efficiency
- **LinkedList**: Frequent insertions/deletions, no random access needed

---

## Best Practices

1. **Use `@SuppressWarnings("unchecked")`** — When casting from Object[]
2. **Check array bounds** — Always validate index parameters
3. **Null safety** — Use Objects.equals() for comparisons
4. **Iterator consistency** — Throw ConcurrentModificationException when needed
5. **Capacity management** — Grow arrays geometrically (2x)

---

## Common Mistakes

### 1. Forgetting to Check Bounds

```java
// BAD
public E get(int index) {
    return (E) elements[index];  // No bounds check
}

// GOOD
public E get(int index) {
    checkIndex(index);
    return (E) elements[index];
}
```

### 2. Not Clearing References

```java
// BAD
public E remove(int index) {
    E old = (E) elements[index];
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    size--;
    return old;  // elements[size] still references removed element
}

// GOOD
public E remove(int index) {
    E old = (E) elements[index];
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    elements[--size] = null;  // Clear reference for GC
    return old;
}
```

---

## Pitfalls

### 1. Type Erasure

```java
// These are the same at runtime
SimpleList<String> strings = new ArrayList<>();
SimpleList<Integer> integers = new ArrayList<>();
System.out.println(strings.getClass() == integers.getClass()); // true
```

### 2. Generic Arrays

```java
// ILLEGAL
// SimpleList<String>[] arrays = new SimpleList<String>[10];

// WORKAROUND
@SuppressWarnings("unchecked")
SimpleList<String>[] arrays = (SimpleList<String>[]) new SimpleList[10];
```

---

## Debugging Tips

### 1. Check Type Safety

```java
// If you get ClassCastException, check your casts
// The cast should be safe if generics are used correctly
```

### 2. Use IDE Debugger

- Set breakpoints in generic methods
- Inspect type parameters at runtime
- Watch for ClassCastException origins

### 3. Test with Different Types

```java
// Test with String, Integer, and custom types
SimpleList<String> strings = new ArrayList<>();
SimpleList<Integer> integers = new ArrayList<>();
SimpleList<User> users = new ArrayList<>();
```

---

## Comparison Table

| Feature | Our Framework | Java Collections |
|---------|---------------|------------------|
| Type safety | Compile time | Compile time |
| Iterator support | Yes | Yes |
| Random access | ArrayList only | ArrayList, Vector |
| Thread safety | No | Some (CopyOnWriteArrayList) |
| Performance | Basic | Optimized |

---

## Decision Tree

```
Do you need random access?
├── Yes → Use ArrayList
└── No → Do you need frequent insertions/deletions?
    ├── Yes → Use LinkedList
    └── No → Consider ArrayList for simplicity
```

---

## Interview Questions

### Q1: Why use generics in a collection framework?

**A:** Generics provide compile-time type safety, eliminating ClassCastException and the need for explicit casting. They also make code more readable and maintainable.

### Q2: How does type erasure affect our implementation?

**A:** After compilation, type parameters are erased. We must use Object[] for storage and insert casts when retrieving elements. Bridge methods maintain polymorphism.

### Q3: What is the PECS principle and how did we apply it?

**A:** Producer Extends, Consumer Super. We used `? extends T` for reading from collections (producers) and `? super T` for writing to collections (consumers).

### Q4: How would you add thread safety to this framework?

**A:** Options include synchronized wrappers, CopyOnWriteArrayList pattern, or using java.util.concurrent collections.

### Q5: What are the trade-offs between ArrayList and LinkedList?

**A:** ArrayList provides O(1) random access but O(n) insertions/deletions. LinkedList provides O(1) insertions/deletions but O(n) random access. ArrayList is generally preferred.

---

## Exercises

### Exercise 1: Add SubList Support

Add a `subList(int fromIndex, int toIndex)` method that returns a view of the list.

### Exercise 2: Implement forEach

Add a `forEach(Consumer<? super E> action)` method to SimpleList.

### Exercise 3: Add Stream Support

Add a `stream()` method that returns a Stream<E>.

---

## Assignments

### Assignment 1: Complete the Framework

Finish implementing all methods in ArrayList and LinkedList.

### Assignment 2: Add More Utility Methods

Add these methods to Collections:
1. `unmodifiableList` — returns read-only view
2. `synchronizedList` — returns thread-safe wrapper
3. `singletonList` — returns list with single element

### Assignment 3: Write Unit Tests

Write comprehensive unit tests for all classes.

---

## Mini Project

### Type-Safe Collection Framework

**Deliverables:**

1. `SimpleList<E>` interface
2. `ArrayList<E>` implementation
3. `LinkedList<E>` implementation
4. `SimpleIterator<E>` interface
5. `Collections` utility class
6. Unit tests for all classes

**Requirements:**

1. All classes must be type-safe
2. Use bounded types where appropriate
3. Apply PECS principle in utility methods
4. Handle type erasure gracefully
5. Follow Google Java Style

---

## Summary

This mini project demonstrates:

1. **Generic interfaces** — SimpleList, SimpleIterator
2. **Generic classes** — ArrayList, LinkedList
3. **Bounded types** — `<T extends Comparable<T>>` in Collections.sort
4. **Wildcards** — `SimpleList<? extends T>` in utility methods
5. **PECS principle** — Producer Extends, Consumer Super
6. **Type erasure** — Object[] storage with casts
7. **Iterator pattern** — Type-safe iteration

By building this framework, you have applied all concepts from the Generics module in a practical, real-world scenario.

---

## References

- [Java Collections Framework](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/collections/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Design Patterns - Iterator](https://www.baeldung.com/java-iterator)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
