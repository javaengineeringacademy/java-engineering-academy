# 09 - Mini Project: Type-Safe Collection Framework

## Table of Contents

1. [Introduction](#introduction)
2. [Learning Objectives](#learning-objectives)
3. [Prerequisites](#prerequisites)
4. [Why This Concept Exists](#why-this-concept-exists)
5. [Problem Statement](#problem-statement)
6. [Theory](#theory)
7. [Internal Working](#internal-working)
8. [JVM Perspective](#jvm-perspective)
9. [Memory Representation](#memory-representation)
10. [Syntax](#syntax)
11. [Easy Example](#easy-example)
12. [Medium Example](#medium-example)
13. [Hard Example](#hard-example)
14. [Enterprise Example](#enterprise-example)
15. [Performance](#performance)
16. [Best Practices](#best-practices)
17. [Common Mistakes](#common-mistakes)
18. [Pitfalls](#pitfalls)
19. [Debugging Tips](#debugging-tips)
20. [Comparison Table](#comparison-table)
21. [Decision Tree](#decision-tree)
22. [Interview Questions](#interview-questions)
23. [Exercises](#exercises)
24. [Assignments](#assignments)
25. [Mini Project](#mini-project)
26. [Summary](#summary)
27. [References](#references)

---

## Introduction

This mini project brings together all concepts from the Generics module by building a simplified, type-safe collection framework. You will implement generic interfaces, classes, and methods while applying bounded types, wildcards, and the PECS principle.

---

## Learning Objectives

By the end of this project, you will be able to:

- Apply all generic concepts learned in this module
- Design and implement generic interfaces and classes
- Use bounded types and wildcards effectively
- Implement iterator and iterable patterns
- Build a functional, type-safe API
- Handle type erasure gracefully

---

## Prerequisites

- All previous topics (01-08)
- Understanding of Iterator pattern
- Familiarity with Java Collections API
- Basic stream operations (helpful)

---

## Why This Concept Exists

### Learning by Building

Understanding generics through theory is important, but building a real framework solidifies that knowledge. This project challenges you to:

1. Apply generic concepts in a real codebase
2. Handle type erasure in practice
3. Design flexible, type-safe APIs
4. Make design decisions about generic constraints

---

## Problem Statement

Build a simplified, type-safe collection framework that includes:

1. `SimpleList<T>` interface — core collection operations
2. `ArrayList<T>` implementation — dynamic array-based list
3. `LinkedList<T>` implementation — node-based list
4. `SimpleIterator<T>` interface — iteration support
5. `Collections` utility class — static utility methods
6. Type-safe filtering, mapping, and transformation

---

## Theory

### Generic Interface Design

```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
}
```

### Iterator Pattern

```java
public interface SimpleIterator<E> {
    boolean hasNext();
    E next();
    void remove();
}
```

### Utility Methods

```java
public class Collections {
    public static <T> boolean addAll(SimpleList<? super T> dest, T... elements) { ... }
    public static <T> SimpleList<T> unmodifiable(SimpleList<T> list) { ... }
    public static <T> void swap(SimpleList<T> list, int i, int j) { ... }
}
```

---

## Internal Working

### ArrayList Implementation

```java
public class ArrayList<E> implements SimpleList<E> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
```

---

## JVM Perspective

### Type Erasure in Action

```java
ArrayList<String> strings = new ArrayList<>();
ArrayList<Integer> integers = new ArrayList<>();

// After compilation, both become ArrayList
// Type parameters erased to Object
// Casts inserted for type safety
```

### Bridge Methods

```java
// When implementing generic interfaces
public class ArrayList<E> implements SimpleList<E> {
    @Override
    public boolean add(E element) { ... }

    // Bridge method added by compiler
    public boolean add(Object element) {
        return add((E) element);
    }
}
```

---

## Memory Representation

### ArrayList Memory Layout

```
ArrayList object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ elementData: Object[] ref ──┼──→ [Object, Object, Object, ...]
│ size: int (4 bytes)         │
└─────────────────────────────┘

Object[] array:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ length: int (4 bytes)       │
│ [0]: Object reference       │
│ [1]: Object reference       │
│ [2]: Object reference       │
│ ...                         │
└─────────────────────────────┘
```

### LinkedList Memory Layout

```
LinkedList object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ first: Node ref ────────────┼──→ Node → Node → Node → null
│ size: int (4 bytes)         │
└─────────────────────────────┘

Node object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ item: Object reference      │
│ next: Node reference        │
│ prev: Node reference        │
└─────────────────────────────┘
```

---

## Syntax

### Interface Definition

```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
    SimpleIterator<E> iterator();
}
```

### Implementation

```java
public class ArrayList<E> implements SimpleList<E> {
    private Object[] elements;
    private int size;

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }
}
```

---

## Easy Example

### SimpleList Interface

```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
}

// Basic usage
SimpleList<String> list = new ArrayList<>();
list.add("hello");
list.add("world");
String first = list.get(0);  // Type-safe
```

---

## Medium Example

### ArrayList Implementation

```java
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements SimpleList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E old = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(elements[i], element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size);
        }
    }

    private class ArrayIterator implements Iterator<E> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) elements[cursor++];
        }
    }

    public static void main(String[] args) {
        SimpleList<String> list = new ArrayList<>();
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        for (String name : list) {
            System.out.println(name);
        }

        System.out.println("Size: " + list.size());  // 3
        System.out.println("Contains Bob: " + list.contains("Bob"));  // true
    }
}
```

---

## Hard Example

### LinkedList Implementation

```java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements SimpleList<E> {
    private Node<E> first;
    private Node<E> last;
    private int size;

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public boolean add(E element) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E element) {
        for (E x : this) {
            if (java.util.Objects.equals(x, element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    private Node<E> node(int index) {
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
        } else {
            x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
        }
        return x;
    }

    private E unlink(Node<E> x) {
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
        return element;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size);
        }
    }

    private class ListIterator implements Iterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;

        ListIterator() {
            next = (size > 0) ? first : null;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }
    }

    public static void main(String[] args) {
        SimpleList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        for (int num : list) {
            System.out.println(num);
        }

        list.remove(1);  // Remove element at index 1
        System.out.println("After remove: " + list.size());  // 2
    }
}
```

---

## Enterprise Example

### Collections Utility Class

```java
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Collections {

    private Collections() {}

    // PECS: Producer Extends
    @SafeVarargs
    public static <T> boolean addAll(SimpleList<? super T> dest, T... elements) {
        for (T element : elements) {
            dest.add(element);
        }
        return elements.length > 0;
    }

    // Type-safe unmodifiable wrapper
    public static <T> SimpleList<T> unmodifiable(SimpleList<T> list) {
        return new UnmodifiableSimpleList<>(list);
    }

    // Swap elements
    public static <T> void swap(SimpleList<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Find maximum
    public static <T extends Comparable<T>> T max(SimpleList<? extends T> list) {
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T current = list.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    // Filter
    public static <T> SimpleList<T> filter(
            SimpleList<? extends T> source,
            Predicate<? super T> predicate) {
        SimpleList<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // Map
    public static <T, R> SimpleList<R> map(
            SimpleList<? extends T> source,
            Function<? super T, ? extends R> mapper) {
        SimpleList<R> result = new ArrayList<>();
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    // Sort
    public static <T extends Comparable<T>> void sort(SimpleList<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    swap(list, j, j + 1);
                }
            }
        }
    }

    // Unmodifiable wrapper implementation
    private static class UnmodifiableSimpleList<E> implements SimpleList<E> {
        private final SimpleList<E> list;

        UnmodifiableSimpleList(SimpleList<E> list) {
            this.list = list;
        }

        @Override
        public boolean add(E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public boolean contains(E element) {
            return list.contains(element);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Iterator<E> iterator() {
            return list.iterator();
        }
    }

    public static void main(String[] args) {
        SimpleList<Integer> list = new ArrayList<>();
        Collections.addAll(list, 5, 3, 1, 4, 2);

        System.out.println("Original: " + listToString(list));

        Collections.sort(list);
        System.out.println("Sorted: " + listToString(list));

        SimpleList<Integer> evens = Collections.filter(list, n -> n % 2 == 0);
        System.out.println("Evens: " + listToString(evens));

        SimpleList<String> strings = Collections.map(list, n -> "Num: " + n);
        System.out.println("Mapped: " + listToString(strings));

        System.out.println("Max: " + Collections.max(list));
    }

    private static <T> String listToString(SimpleList<T> list) {
        StringBuilder sb = new StringBuilder("[");
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

- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Design Patterns - Iterator](https://www.baeldung.com/java-iterator)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
