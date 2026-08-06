# LinkedList

## 1. Introduction

LinkedList is a doubly-linked list implementation of the `List` and `Deque` interfaces. Unlike ArrayList which uses a contiguous array, LinkedList stores each element as a separate `Node` object containing the element value and references to the next and previous nodes.

LinkedList provides O(1) insertions and deletions at both ends (when you have a reference to the node), making it ideal for queue and deque operations. However, it has O(n) random access time because there's no index-based lookup—you must traverse from the head or tail to reach a specific position.

Despite its theoretical advantages for certain operations, LinkedList is rarely the best choice in practice due to poor cache locality (nodes are scattered in memory), high memory overhead (each node needs two pointers), and the fact that ArrayList's random access pattern often wins even for insertion-heavy workloads.

## 2. Learning Objectives

- Create and use LinkedList as both List and Deque
- Understand the doubly-linked list data structure
- Learn Node-based memory allocation and traversal
- Compare LinkedList vs ArrayList for different operations
- Understand when LinkedList actually outperforms ArrayList
- Master deque operations (offerFirst, pollLast, peekFirst, etc.)
- Learn about memory overhead of linked structures
- Recognize when LinkedList is the wrong choice

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 03: Generics basics
- Understanding of the List interface
- Basic knowledge of pointers/references

## 4. Why This Concept Exists

LinkedList solves specific problems that arrays cannot handle efficiently:

1. **Frequent insertions at the beginning**: ArrayList shifts all elements O(n), LinkedList just updates pointers O(1)
2. **FIFO/LIFO operations**: As a Deque, LinkedList provides efficient queue and stack operations
3. **Unknown size with frequent modifications**: No resizing overhead, just add/remove nodes
4. **Splicing lists**: Combining or splitting lists by updating pointers

However, in modern Java, ArrayDeque is generally preferred over LinkedList for queue/deque operations, and ArrayList is preferred for most list operations. LinkedList remains useful for specific use cases like implementing undo/redo functionality or maintaining an LRU cache.

## 5. Problem Statement

Consider building a music playlist application:
- Users can add songs at the beginning or end
- Users can skip forward or backward
- Songs can be removed from any position
- The playlist must support bidirectional traversal

While ArrayList could work, it would be slow for insertions at the beginning and doesn't naturally support bidirectional traversal. LinkedList provides:
- O(1) add/remove at both ends
- Bidirectional iterator
- Natural fit for sequential access patterns

## 6. Theory

### Node Structure

Each element in LinkedList is a `Node`:

```java
private static class Node<E> {
    E item;        // The element
    Node<E> next;  // Reference to next node
    Node<E> prev;  // Reference to previous node

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

### LinkedList Fields

```java
transient int size = 0;          // Number of elements
transient Node<E> first;         // Head of the list
transient Node<E> last;          // Tail of the list
```

### Adding Elements

**Adding at the end (addLast)**:
```java
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;  // Empty list
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

**Adding at the beginning (addFirst)**:
```java
void linkFirst(E e) {
    final Node<E> f = first;
    final Node<E> newNode = new Node<>(null, e, f);
    first = newNode;
    if (f == null)
        last = newNode;  // Empty list
    else
        f.prev = newNode;
    size++;
    modCount++;
}
```

### Removing Elements

```java
E unlink(Node<E> x) {
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;  // Removing first element
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;  // Removing last element
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

## 7. Internal Working

### Memory Allocation for Nodes

Each Node is a separate object on the heap:

```
Node 1 (first):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ prev = null (8 bytes)       │
│ item → "Hello" (8 bytes)    │
│ next → Node 2 (8 bytes)     │
└─────────────────────────────┘

Node 2:
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ prev → Node 1 (8 bytes)     │
│ item → "World" (8 bytes)    │
│ next → null (8 bytes)       │
└─────────────────────────────┘
```

### Traversal for get(index)

To get element at index 5:
1. Start at first node
2. Follow next references 5 times
3. Each step is a pointer dereference (not cache-friendly)

```java
Node<E> node(int index) {
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

### The node() Optimization

LinkedList optimizes by starting from whichever end is closer:
- If index < size/2, start from head
- If index >= size/2, start from tail
- Still O(n) worst case, but reduces average traversal

## 8. JVM Perspective

### Memory Allocation

```java
LinkedList<String> list = new LinkedList<>();
// JVM allocates:
// - LinkedList object header: 12 bytes
// - size field: 4 bytes
// - first reference: 8 bytes
// - last reference: 8 bytes
// Total LinkedList object: ~32 bytes

// Each node:
// - Node object header: 12 bytes
// - prev reference: 8 bytes
// - item reference: 8 bytes
// - next reference: 8 bytes
// Total per node: ~36 bytes (rounded to 40 with alignment)
```

### Cache Performance

LinkedList has poor cache performance because:
1. Nodes are scattered across the heap
2. Traversal requires following pointers (cache misses)
3. No spatial locality like arrays

### JIT Optimization

The JIT compiler can optimize LinkedList operations:
- Inline node access methods
- Optimize the node() method's bidirectional traversal
- But cannot eliminate pointer chasing overhead

## 9. Memory Representation

```
LinkedList<String> list = new LinkedList<>();
list.add("Hello");
list.add("World");
list.add("Java");

Memory layout:
┌───────────────────────────────┐
│ LinkedList object             │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ size = 3 (4 bytes)            │
│ first ──────────────────────────┐
│ last ───────────────────────────┼──┐
└───────────────────────────────┘  │  │
                                   │  │
                                   ▼  │
                          Node "Hello" │
                          ┌────────────┐
                          │ prev=null  │
                          │ item="Hello"│
                          │ next ──────────→ Node "World"
                          └────────────┘
                                   │
                                   ▼
                          Node "World"
                          ┌────────────┐
                          │ prev ──────────→ Node "Hello"
                          │ item="World"│
                          │ next ──────────→ Node "Java"
                          └────────────┘
                                   │
                                   ▼
                          Node "Java"
                          ┌────────────┐
                          │ prev ──────────→ Node "World"
                          │ item="Java" │
                          │ next=null  │
                          └────────────┘

Total memory:
- LinkedList object: ~32 bytes
- 3 Node objects: 3 × 40 = 120 bytes
- 3 String objects: ~150 bytes (varies)
- Total: ~302 bytes (vs ~180 for ArrayList)
```

## 10. Syntax

```java
import java.util.LinkedList;
import java.util.Deque;
import java.util.List;

// ============================================
// CREATION
// ============================================
LinkedList<String> list = new LinkedList<>();
LinkedList<String> fromCollection = new LinkedList<>(List.of("A", "B", "C"));
Deque<String> deque = new LinkedList<>(); // As Deque
List<String> asList = new LinkedList<>(); // As List

// ============================================
// LIST OPERATIONS
// ============================================
list.add("element");              // Add to end
list.add(0, "element");          // Add at index
list.addFirst("element");        // Add to beginning
list.addLast("element");         // Add to end
list.get(0);                     // O(n) - must traverse
list.getFirst();                 // O(1) - head access
list.getLast();                  // O(1) - tail access
list.set(0, "new");              // O(n) - must traverse
list.remove(0);                  // O(n) - must traverse
list.removeFirst();              // O(1) - head removal
list.removeLast();               // O(1) - tail removal

// ============================================
// DEQUE OPERATIONS
// ============================================
deque.offer("element");          // Add to end (returns boolean)
deque.offerFirst("element");     // Add to beginning
deque.offerLast("element");      // Add to end
deque.poll();                    // Remove from head (returns null if empty)
deque.pollFirst();               // Remove from head
deque.pollLast();                // Remove from tail
deque.peek();                    // View head (returns null if empty)
deque.peekFirst();               // View head
deque.peekLast();                // View tail
deque.push("element");           // Add to beginning (stack)
deque.pop();                     // Remove from beginning (stack)

// ============================================
// QUEUE OPERATIONS
// ============================================
queue.offer("element");          // Add to tail
queue.add("element");            // Add to tail (throws if full)
queue.poll();                    // Remove from head
queue.remove();                  // Remove from head (throws if empty)
queue.peek();                    // View head
queue.element();                 // View head (throws if empty)

// ============================================
// COMMON OPERATIONS
// ============================================
list.size();                     // O(1)
list.isEmpty();                  // O(1)
list.contains("element");        // O(n)
list.indexOf("element");         // O(n)
list.lastIndexOf("element");     // O(n)
list.clear();                    // O(n)
list.clone();                    // Shallow copy
list.toArray();                  // Convert to array
list.addAll(collection);         // Add all
list.removeAll(collection);      // Remove all matching
list.retainAll(collection);      // Keep only matching

// ============================================
// ITERATION
// ============================================
// Enhanced for loop
for (String s : list) {
    System.out.println(s);
}

// Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    System.out.println(lit.next());
}
while (lit.hasPrevious()) {
    System.out.println(lit.previous());
}

// Descending iterator
Iterator<String> desc = list.descendingIterator();
while (desc.hasNext()) {
    System.out.println(desc.next());
}

// forEach
list.forEach(System.out::println);
```

## 11. Easy Example

```java
import java.util.LinkedList;
import java.util.Deque;

public class LinkedListBasics {
    public static void main(String[] args) {
        // Basic list operations
        LinkedList<String> names = new LinkedList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        System.out.println("List: " + names);
        System.out.println("First: " + names.getFirst());
        System.out.println("Last: " + names.getLast());

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

