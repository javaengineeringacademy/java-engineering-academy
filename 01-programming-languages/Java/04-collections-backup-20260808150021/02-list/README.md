# List Interface

## 1. Introduction

The `List` interface is an ordered collection (also known as a sequence) that allows duplicate elements. It extends the `Collection` interface and provides additional operations for positional access, search, iteration, and range-view. List is one of the most fundamental interfaces in the Java Collections Framework.

List implementations store elements in a sequence, where each element has an integer index (starting from 0). This allows random access to any element by its index, as well as efficient iteration from beginning to end.

The most common List implementations are:
- **ArrayList**: Dynamic array (fast random access, slow insertions)
- **LinkedList**: Doubly-linked list (fast insertions, slow random access)
- **Vector**: Legacy synchronized array (avoid in modern code)
- **CopyOnWriteArrayList**: Thread-safe for read-heavy scenarios

## 2. Learning Objectives

- Understand the List interface and its contract
- Learn the difference between List implementations
- Master List operations: add, get, set, remove, subList
- Understand index-based access and its performance implications
- Learn about List iterators (ListIterator)
- Compare ArrayList vs LinkedList for different scenarios
- Understand thread-safety options for List

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming (interfaces, inheritance)
- Module 03: Generics basics
- Basic understanding of arrays

## 4. Why This Concept Exists

Before List, developers had to:
1. Use arrays: Fixed size, manual management
2. Use Vector: Synchronized, slow
3. Implement their own data structures: Error-prone, inefficient

List provides:
1. **Dynamic sizing**: Grows and shrinks as needed
2. **Indexed access**: O(1) for ArrayList
3. **Rich API**: add, remove, search, sort, subList
4. **Multiple implementations**: Choose the right one for your needs
5. **Integration**: Works with all Collection APIs

## 5. Problem Statement

Consider building a to-do list application:
- Add tasks at any position
- Remove tasks by position
- Get task by position
- Reorder tasks
- Search for tasks

Without List, you'd need to:
- Use arrays and manage resizing manually
- Or implement your own linked list

With List, you simply choose ArrayList or LinkedList based on your needs.

## 6. Theory

### List Interface Contract

1. **Ordered**: Elements have a defined order (insertion order or index-based)
2. **Indexed**: Each element has an integer index (0-based)
3. **Duplicates**: Allows duplicate elements
4. **Null elements**: Allows multiple null elements

### List Operations

| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| add(E) | O(1)* | O(1) | Append to end |
| add(int, E) | O(n) | O(n)** | Insert at index |
| get(int) | O(1) | O(n) | Random access |
| set(int, E) | O(1) | O(n) | Replace at index |
| remove(int) | O(n) | O(n)** | Remove by index |
| remove(Object) | O(n) | O(n) | Remove by value |
| contains(Object) | O(n) | O(n) | Linear search |
| indexOf(Object) | O(n) | O(n) | Linear search |
| size() | O(1) | O(1) | Field access |
| subList(int, int) | O(1) | O(n) | View (ArrayList), traversal (LinkedList) |

*Amortized O(1) for ArrayList (occasional resize is O(n))
**O(n) to find the node, then O(1) to insert/remove

## 7. Internal Working

### ArrayList Internal Structure

```java
// ArrayList uses a dynamic array
private transient Object[] elementData;
private int size;

// When adding and array is full:
// 1. Create new array with 1.5x capacity
// 2. Copy all elements using Arrays.copyOf()
// 3. Replace old array reference
```

### LinkedList Internal Structure

```java
// LinkedList uses a doubly-linked list
transient int size = 0;
transient Node<E> first;
transient Node<E> last;

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
```

### ListIterator

ListIterator extends Iterator for bidirectional traversal:
- `hasPrevious()`: Check if previous element exists
- `previous()`: Get previous element
- `nextIndex()`: Get index of next element
- `previousIndex()`: Get index of previous element
- `set(E)`: Replace current element
- `add(E)`: Insert element at current position

## 8. JVM Perspective

### Memory Allocation

```java
List<String> list = new ArrayList<>();
// ArrayList object: ~32 bytes
// Backing array: 10 references × 8 bytes = 80 bytes (default capacity)

List<String> linkedList = new LinkedList<>();
// LinkedList object: ~32 bytes
// Each Node: ~40 bytes (item + next + prev + object header)
```

### JIT Optimization

The JIT compiler optimizes List operations:
- Inline `get()` and `set()` for ArrayList
- Optimize bounds checking
- Devirtualize calls when concrete type is known

## 9. Memory Representation

```
ArrayList<String> list:
┌───────────────────────────────┐
│ ArrayList object              │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ elementData ──────────────────┐
│ size = 3 (4 bytes)            │
└───────────────────────────────┘
                                │
                                ▼
                         Object[] elementData
                         ┌────────────────────┐
                         │ [0] → "Hello"      │
                         │ [1] → "World"      │
                         │ [2] → "Java"       │
                         │ [3] → null         │
                         └────────────────────┘

LinkedList<String> list:
┌───────────────────────────────┐
│ LinkedList object             │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ size = 3 (4 bytes)            │
│ first ──────────────────────────┐
│ last ───────────────────────────┼──┐
└───────────────────────────────┘  │  │
                                   ▼  │
                          Node "Hello" │
                          ┌────────────┐
                          │ prev=null  │
                          │ item="Hello"│
                          │ next ──────────→ Node "World"
                          └────────────┘
```

## 10. Syntax

```java
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

// ============================================
// CREATION
// ============================================
List<E> list = new ArrayList<>();           // Empty ArrayList
List<E> list = new ArrayList<>(100);       // With initial capacity
List<E> list = new ArrayList<>(collection); // From collection
List<E> list = new LinkedList<>();          // Empty LinkedList
List<E> list = List.of("a", "b", "c");     // Immutable (Java 9+)
List<E> list = new ArrayList<>(List.of("a", "b")); // Mutable copy

// ============================================
// ADDING ELEMENTS
// ============================================
list.add(element);              // Append to end
list.add(index, element);       // Insert at index
list.addAll(collection);        // Add all from collection
list.addAll(index, collection); // Add all at index

// ============================================
// ACCESSING ELEMENTS
// ============================================
E element = list.get(index);           // O(1) for ArrayList
list.set(index, element);              // Replace at index
int index = list.indexOf(element);     // First occurrence
int lastIndex = list.lastIndexOf(element); // Last occurrence

// ============================================
// REMOVING ELEMENTS
// ============================================
E removed = list.remove(index);        // Remove by index
boolean success = list.remove(object); // Remove by value
list.removeIf(predicate);              // Conditional removal
list.clear();                          // Remove all

// ============================================
// SEARCHING
// ============================================
boolean has = list.contains(element);  // O(n)
int index = list.indexOf(element);     // O(n)
boolean empty = list.isEmpty();        // O(1)
int size = list.size();                // O(1)

// ============================================
// SUBLIST (view, not copy)
// ============================================
List<E> sub = list.subList(fromIndex, toIndex); // [from, to)
sub.set(0, newValue); // Modifies original list!
list.subList(0, 3).clear(); // Removes from original

// ============================================
// SORTING
// ============================================
Collections.sort(list);                    // Natural order
list.sort(Comparator.naturalOrder());     // Natural order
list.sort(Comparator.reverseOrder());     // Reverse order
list.sort(Comparator.comparing(String::length)); // Custom

// ============================================
// CONVERSIONS
// ============================================
Object[] array = list.toArray();
String[] array = list.toArray(new String[0]);
List<String> copy = new ArrayList<>(list);

// ============================================
// ITERATION
// ============================================
// Enhanced for loop
for (E element : list) {
    System.out.println(element);
}

// Iterator
Iterator<E> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator (bidirectional)
ListIterator<E> lit = list.listIterator();
while (lit.hasNext()) {
    System.out.println(lit.nextIndex() + ": " + lit.next());
}

// forEach with lambda
list.forEach(System.out::println);

// Stream
list.stream().filter(e -> ...).forEach(System.out::println);

// ============================================
// THREAD SAFETY
// ============================================
List<E> syncList = Collections.synchronizedList(new ArrayList<>());
List<E> copyOnWrite = new java.util.concurrent.CopyOnWriteArrayList<>();

// ============================================
// IMMUTABLE VIEWS
// ============================================
List<E> unmodifiable = Collections.unmodifiableList(list);
List<E> immutable = List.copyOf(list); // Truly immutable
```

## 11. Easy Example

```java
import java.util.*;

public class ListBasics {
    public static void main(String[] args) {
        // Create and populate
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate allowed

        System.out.println("List: " + fruits);
        System.out.println("Size: " + fruits.size());

        // Access by index
        System.out.println("First: " + fruits.get(0));
        System.out.println("Last: " + fruits.get(fruits.size() - 1));

        // Search
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Index of Banana: " + fruits.indexOf("Banana"));

        // Remove
        fruits.remove("Banana");
        fruits.remove(0);
        System.out.println("After removal: " + fruits);

        // Add at index
        fruits.add(0, "Mango");
        System.out.println("After add: " + fruits);

        // Sort
        fruits.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + fruits);

        // Iterate
        System.out.print("Iterating: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class ListOperations {
    public static void main(String[] args) {
        // Remove duplicates while preserving order
        List<Integer> withDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        List<Integer> withoutDuplicates = new ArrayList<>();
        for (Integer num : withDuplicates) {
            if (!withoutDuplicates.contains(num)) {
                withoutDuplicates.add(num);
            }
        }
        System.out.println("Without duplicates: " + withoutDuplicates);

        // Rotate list
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(numbers, 2);
        System.out.println("Rotated: " + numbers);

        // Chunk list
        List<List<Integer>> chunks = chunk(List.of(1, 2, 3, 4, 5, 6, 7), 3);
        System.out.println("Chunks: " + chunks);

        // Interleave lists
        List<String> list1 = List.of("A", "B", "C");
        List<String> list2 = List.of("1", "2", "3");
        List<String> interleaved = interleave(list1, list2);
        System.out.println("Interleaved: " + interleaved);

        // Find common elements
        List<String> l1 = List.of("A", "B", "C", "D");
        List<String> l2 = List.of("C", "D", "E", "F");
        Set<String> common = new HashSet<>(l1);
        common.retainAll(l2);
        System.out.println("Common: " + common);
    }

    static <T> List<List<T>> chunk(List<T> list, int size) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return chunks;

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

```
