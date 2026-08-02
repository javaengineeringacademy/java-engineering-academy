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

        // Add at beginning
        names.addFirst("Zara");
        System.out.println("After addFirst: " + names);

        // Remove from end
        String removed = names.removeLast();
        System.out.println("Removed: " + removed);
        System.out.println("After removeLast: " + names);

        // Use as Deque (stack and queue)
        Deque<String> stack = new LinkedList<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");

        System.out.println("\nStack operations:");
        while (!stack.isEmpty()) {
            System.out.println("Pop: " + stack.pop());
        }

        // Use as Queue
        Deque<String> queue = new LinkedList<>();
        queue.offer("Customer 1");
        queue.offer("Customer 2");
        queue.offer("Customer 3");

        System.out.println("\nQueue operations:");
        while (!queue.isEmpty()) {
            System.out.println("Serve: " + queue.poll());
        }
    }
}
```

## 12. Medium Example

```java
import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListOperations {
    public static void main(String[] args) {
        // Bidirectional traversal
        LinkedList<Integer> numbers = new LinkedList<>();
        for (int i = 1; i <= 5; i++) {
            numbers.add(i);
        }

        System.out.println("Forward:");
        ListIterator<Integer> forward = numbers.listIterator();
        while (forward.hasNext()) {
            System.out.print(forward.next() + " ");
        }
        System.out.println();

        System.out.println("Backward:");
        ListIterator<Integer> backward = numbers.listIterator(numbers.size());
        while (backward.hasPrevious()) {
            System.out.print(backward.previous() + " ");
        }
        System.out.println();

        // Replace elements while iterating
        ListIterator<Integer> replaceIt = numbers.listIterator();
        while (replaceIt.hasNext()) {
            int num = replaceIt.next();
            replaceIt.set(num * 10);
        }
        System.out.println("Doubled: " + numbers);

        // Add elements while iterating
        ListIterator<Integer> addIt = numbers.listIterator();
        while (addIt.hasNext()) {
            int num = addIt.next();
            if (num == 30) {
                addIt.add(25); // Add 25 before 30
            }
        }
        System.out.println("After adding: " + numbers);

        // Reverse the list
        reverseLinkedList(numbers);
        System.out.println("Reversed: " + numbers);
    }

    static <T> void reverseLinkedList(LinkedList<T> list) {
        ListIterator<T> forward = list.listIterator();
        ListIterator<T> backward = list.listIterator(list.size());

        while (forward.nextIndex() < backward.previousIndex()) {
            T forwardElem = forward.next();
            T backwardElem = backward.previous();
            forward.set(backwardElem);
            backward.set(forwardElem);
        }
    }
}
```

## 13. Hard Example

```java
import java.util.*;

public class AdvancedLinkedList {
    public static void main(String[] args) {
        // Pattern 1: LRU Cache using LinkedList
        System.out.println("=== LRU Cache ===");
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        cache.get("A"); // Moves A to end
        cache.put("D", 4); // Evicts B (least recently used)
        System.out.println("Cache after operations: " + cache);

        // Pattern 2: Undo/Redo system
        System.out.println("\n=== Undo/Redo ===");
        UndoRedoSystem<String> undoRedo = new UndoRedoSystem<>();
        undoRedo.execute("Action 1");
        undoRedo.execute("Action 2");
        undoRedo.execute("Action 3");
        System.out.println("Undo: " + undoRedo.undo());
        System.out.println("Undo: " + undoRedo.undo());
        System.out.println("Redo: " + undoRedo.redo());
        System.out.println("Redo: " + undoRedo.redo());

        // Pattern 3: Polynomial using LinkedList
        System.out.println("\n=== Polynomial ===");
        Polynomial p1 = new Polynomial();
        p1.addTerm(3, 2); // 3x^2
        p1.addTerm(2, 1); // 2x
        p1.addTerm(1, 0); // 1
        System.out.println("P1: " + p1);
        System.out.println("P1(2) = " + p1.evaluate(2));

        // Pattern 4: Browser history
        System.out.println("\n=== Browser History ===");
        BrowserHistory history = new BrowserHistory("home.com");
        history.visit("google.com");
        history.visit("github.com");
        history.visit("stackoverflow.com");
        System.out.println("Back: " + history.back());
        System.out.println("Back: " + history.back());
        System.out.println("Forward: " + history.forward());
        System.out.println("Current: " + history.current());
    }

    // LRU Cache using LinkedList
    static class LRUCache<K, V> {
        private final int capacity;
        private final Map<K, V> cache;
        private final LinkedList<K> accessOrder;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.accessOrder = new LinkedList<>();
        }

        public V get(K key) {
            if (!cache.containsKey(key)) return null;
            accessOrder.remove(key);
            accessOrder.addLast(key);
            return cache.get(key);
        }

        public void put(K key, V value) {
            if (cache.containsKey(key)) {
                accessOrder.remove(key);
            } else if (cache.size() >= capacity) {
                K eldest = accessOrder.removeFirst();
                cache.remove(eldest);
            }
            cache.put(key, value);
            accessOrder.addLast(key);
        }

        @Override
        public String toString() {
            return cache.toString();
        }
    }

    // Undo/Redo System
    static class UndoRedoSystem<T> {
        private final LinkedList<T> history = new LinkedList<>();
        private final LinkedList<T> redoStack = new LinkedList<>();

        public void execute(T action) {
            history.addLast(action);
            redoStack.clear();
        }

        public T undo() {
            if (history.isEmpty()) return null;
            T action = history.removeLast();
            redoStack.addLast(action);
            return action;
        }

        public T redo() {
            if (redoStack.isEmpty()) return null;
            T action = redoStack.removeLast();
            history.addLast(action);
            return action;
        }
    }

    // Polynomial
    static class Polynomial {
        private final LinkedList<int[]> terms = new LinkedList<>(); // [coefficient, exponent]

        public void addTerm(int coefficient, int exponent) {
            terms.addLast(new int[]{coefficient, exponent});
        }

        public double evaluate(double x) {
            double result = 0;
            for (int[] term : terms) {
                result += term[0] * Math.pow(x, term[1]);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < terms.size(); i++) {
                int[] term = terms.get(i);
                if (i > 0 && term[0] >= 0) sb.append(" + ");
                else if (i > 0 && term[0] < 0) sb.append(" - ");
                else if (term[0] < 0) sb.append("-");
                sb.append(Math.abs(term[0]));
                if (term[1] > 0) sb.append("x");
                if (term[1] > 1) sb.append("^").append(term[1]);
            }
            return sb.toString();
        }
    }

    // Browser History
    static class BrowserHistory {
        private final LinkedList<String> history;
        private int currentIndex;

        public BrowserHistory(String homepage) {
            history = new LinkedList<>();
            history.add(homepage);
            currentIndex = 0;
        }

        public void visit(String url) {
            while (history.size() > currentIndex + 1) {
                history.removeLast();
            }
            history.addLast(url);
            currentIndex++;
        }

        public String back() {
            if (currentIndex > 0) {
                currentIndex--;
                return history.get(currentIndex);
            }
            return history.getFirst();
        }

        public String forward() {
            if (currentIndex < history.size() - 1) {
                currentIndex++;
                return history.get(currentIndex);
            }
            return history.getLast();
        }

        public String current() {
            return history.get(currentIndex);
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatMessageSystem {
    private final LinkedList<ChatMessage> messageHistory;
    private final LinkedList<ChatMessage> undoBuffer;
    private final int maxHistory;

    public ChatMessageSystem(int maxHistory) {
        this.messageHistory = new LinkedList<>();
        this.undoBuffer = new LinkedList<>();
        this.maxHistory = maxHistory;
    }

    public void sendMessage(ChatMessage message) {
        messageHistory.addLast(message);
        undoBuffer.clear();
        if (messageHistory.size() > maxHistory) {
            messageHistory.removeFirst();
        }
    }

    public Optional<ChatMessage> undoLastMessage() {
        if (messageHistory.isEmpty()) return Optional.empty();
        ChatMessage last = messageHistory.removeLast();
        undoBuffer.addLast(last);
        return Optional.of(last);
    }

    public Optional<ChatMessage> redoMessage() {
        if (undoBuffer.isEmpty()) return Optional.empty();
        ChatMessage message = undoBuffer.removeLast();
        messageHistory.addLast(message);
        return Optional.of(message);
    }

    public List<ChatMessage> getRecentMessages(int count) {
        int size = messageHistory.size();
        return new ArrayList<>(messageHistory.subList(
            Math.max(0, size - count), size
        ));
    }

    public List<ChatMessage> getMessagesByUser(String userId) {
        List<ChatMessage> userMessages = new ArrayList<>();
        for (ChatMessage msg : messageHistory) {
            if (msg.senderId().equals(userId)) {
                userMessages.add(msg);
            }
        }
        return userMessages;
    }

    public Map<String, Long> getMessageCountByUser() {
        Map<String, Long> counts = new HashMap<>();
        for (ChatMessage msg : messageHistory) {
            counts.merge(msg.senderId(), 1L, Long::sum);
        }
        return counts;
    }

    public static void main(String[] args) {
        ChatMessageSystem chat = new ChatMessageSystem(100);

        chat.sendMessage(new ChatMessage("user1", "Hello everyone!", new Date()));
        chat.sendMessage(new ChatMessage("user2", "Hi there!", new Date()));
        chat.sendMessage(new ChatMessage("user1", "How are you?", new Date()));
        chat.sendMessage(new ChatMessage("user3", "Great, thanks!", new Date()));

        System.out.println("=== Recent Messages ===");
        chat.getRecentMessages(3).forEach(msg ->
            System.out.printf("  [%s] %s%n", msg.senderId(), msg.content())
        );

        System.out.println("\n=== Undo Last Message ===");
        chat.undoLastMessage().ifPresent(msg ->
            System.out.printf("  Undid: [%s] %s%n", msg.senderId(), msg.content())
        );

        System.out.println("\n=== Redo Message ===");
        chat.redoMessage().ifPresent(msg ->
            System.out.printf("  Redid: [%s] %s%n", msg.senderId(), msg.content())
        );

        System.out.println("\n=== Messages by User ===");
        chat.getMessagesByUser("user1").forEach(msg ->
            System.out.println("  " + msg.content())
        );

        System.out.println("\n=== Message Counts ===");
        chat.getMessageCountByUser().forEach((user, count) ->
            System.out.printf("  %s: %d messages%n", user, count)
        );
    }

    record ChatMessage(String senderId, String content, Date timestamp) {}
}
```

## 15. Performance

### Time Complexity

| Operation | Time | Notes |
|-----------|------|-------|
| add(E) | O(1) | At end |
| add(0, E) | O(1) | At beginning |
| add(index, E) | O(n) | Must traverse |
| get(index) | O(n) | Must traverse |
| set(index, E) | O(n) | Must traverse |
| remove(index) | O(n) | Must traverse |
| remove(Object) | O(n) | Search + unlink |
| contains(Object) | O(n) | Linear search |
| size() | O(1) | Field access |
| addFirst(E) | O(1) | Pointer update |
| addLast(E) | O(1) | Pointer update |
| removeFirst() | O(1) | Pointer update |
| removeLast() | O(1) | Pointer update |

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList | Winner |
|-----------|-----------|------------|--------|
| get(index) | O(1) | O(n) | ArrayList |
| add(end) | O(1)* | O(1) | Tie |
| add(beginning) | O(n) | O(1) | LinkedList |
| add(middle) | O(n) | O(n)** | ArrayList |
| remove(end) | O(1) | O(1) | Tie |
| remove(beginning) | O(n) | O(1) | LinkedList |
| remove(middle) | O(n) | O(n)** | ArrayList |
| iteration | O(n) | O(n) | ArrayList |
| memory | 4 bytes/elem | 24 bytes/elem | ArrayList |
| cache locality | Good | Poor | ArrayList |

**O(n) for LinkedList because you must traverse to find the node

### When LinkedList Wins

1. **Frequent add/remove at both ends**: Deque operations are O(1)
2. **Frequent insertions at known positions**: If you have a ListIterator at the position
3. **Splicing lists**: Combining lists by updating pointers
4. **No random access needed**: Sequential iteration only

### When ArrayList Wins (Almost Always)

1. **Random access**: O(1) vs O(n)
2. **Memory efficiency**: 4 bytes vs 24 bytes per element
3. **Cache performance**: Contiguous memory, prefetching works
4. **Iteration**: Better cache locality makes it faster even though both are O(n)
5. **Most real-world use cases**: Random access is more common than insertions at beginning

## 16. Best Practices

1. **Prefer ArrayList**: For most use cases, ArrayList is faster
2. **Use ArrayDeque**: For queue/deque operations (faster than LinkedList)
3. **Use ListIterator**: For bidirectional traversal or insertions during iteration
4. **Avoid get(index)**: In loops, use iterator instead
5. **Set initial capacity**: LinkedList doesn't have this issue, but ArrayList does
6. **Use descendingIterator()**: For reverse traversal
7. **Consider memory**: LinkedList uses 6x more memory per element
8. **Thread safety**: LinkedList is not thread-safe; use CopyOnWriteArrayList

## 17. Common Mistakes

```java
// Mistake 1: Using LinkedList as a general-purpose List
// Bad - O(n) random access
LinkedList<String> list = new LinkedList<>();
String element = list.get(5); // O(n) - must traverse

// Good - Use ArrayList for random access
ArrayList<String> list = new ArrayList<>();
String element = list.get(5); // O(1) - direct access

// Mistake 2: Using LinkedList for queue operations
// Bad - slower than ArrayDeque
Queue<String> queue = new LinkedList<>();

// Good - faster than LinkedList
Queue<String> queue = new ArrayDeque<>();

// Mistake 3: Using get() in a loop
// Bad - O(n^2) for LinkedList
for (int i = 0; i < list.size(); i++) {
    process(list.get(i)); // Each get is O(n)
}

// Good - O(n) with iterator
for (String s : list) {
    process(s);
}

// Mistake 4: Not using ListIterator for bidirectional traversal
// Bad - inefficient
for (int i = list.size() - 1; i >= 0; i--) {
    process(list.get(i)); // Each get is O(n)
}

// Good - efficient
ListIterator<String> it = list.listIterator(list.size());
while (it.hasPrevious()) {
    process(it.previous());
}

// Mistake 5: Using addLast() when add() suffices
// Same thing, but add() is clearer
list.addLast("element"); // Confusing
list.add("element");     // Clearer
```

## 18. Pitfalls

### Memory Overhead
- Each node requires 24 bytes of extra memory (prev + next pointers)
- For 1 million elements: ~24 MB overhead vs ~0 MB for ArrayList
- Nodes are scattered in memory, causing cache misses

### Random Access Performance
- get(index) is O(n), not O(1)
- Using get() in a loop makes it O(n^2)
- Always use iterator for LinkedList traversal

### Thread Safety
- LinkedList is NOT thread-safe
- Concurrent access can corrupt the linked structure
- Use Collections.synchronizedList() or CopyOnWriteArrayList

### SubList Views
- subList() returns a view, not a copy
- Changes to the view affect the original list
- The view holds a reference to the original list

### Null Elements
- LinkedList allows null elements
- This can cause issues with some algorithms
- Consider filtering nulls explicitly

## 19. Debugging Tips

1. **Print the list**: Use System.out.println() to see the list contents
2. **Use debugger**: Inspect first, last, and node references
3. **Check size**: Verify size() matches expected count
4. **Use assertions**: Verify linked structure integrity
5. **Profile memory**: Use JProfiler to check node allocation
6. **Track modifications**: Monitor modCount for concurrent modification issues
7. **Visualize**: Draw the linked structure for complex operations

## 20. Comparison Table

| Feature | LinkedList | ArrayList | ArrayDeque |
|---------|------------|-----------|------------|
| Backing structure | Doubly-linked list | Dynamic array | Circular array |
| Random access | O(n) | O(1) | O(n) |
| Add/remove begin | O(1) | O(n) | O(1) |
| Add/remove end | O(1) | O(1)* | O(1) |
| Add/remove middle | O(n) | O(n) | O(n) |
| Memory per element | 24 bytes | 4 bytes | 8 bytes |
| Cache locality | Poor | Good | Good |
| Implements | List, Deque | List | Deque |
| Best for | Deque ops | Random access | Queue/Deque |

## 21. Decision Tree

```
Need a List?
├── Yes → Need random access by index?
│   ├── Yes → ArrayList
│   └── No → Need frequent insertions at beginning?
│       ├── Yes → LinkedList (but consider ArrayDeque)
│       └── No → ArrayList (usually still better)
├── No → Need a Queue?
│   └── Use ArrayDeque (not LinkedList)
├── Need a Deque?
│   └── Use ArrayDeque (not LinkedList)
└── Need a Stack?
    └── Use ArrayDeque (not Stack class)
```

## 22. Interview Questions

### Q1: When would you use LinkedList over ArrayList?
**A**: Rarely. LinkedList is better for frequent insertions/removals at both ends (Deque operations), or when you need to splice lists. For most use cases, ArrayList is superior due to cache locality and O(1) random access.

### Q2: What is the time complexity of get(index) in LinkedList?
**A**: O(n). LinkedList must traverse from head or tail to reach the index. The optimization starts from whichever end is closer, but worst case is still O(n).

### Q3: Why is LinkedList rarely used in practice?
**A**: Poor cache locality (nodes scattered in memory), high memory overhead (24 bytes per element), O(n) random access, and ArrayList's superior performance in most benchmarks. ArrayDeque is also faster for queue/deque operations.

### Q4: What is the difference between LinkedList and ArrayDeque?
**A**: ArrayDeque uses a circular array (better cache locality, less memory), while LinkedList uses nodes with pointers. ArrayDeque is faster for most operations except mid-list insertions with a ListIterator.

### Q5: How does LinkedList implement both List and Deque?
**A**: LinkedList extends AbstractSequentialList (which implements List) and implements Deque interface. It provides both index-based access (via traversal) and deque operations (addFirst, addLast, etc.).

### Q6: What happens when you remove elements from LinkedList during iteration?
**A**: Same as ArrayList—ConcurrentModificationException if you modify the list structure. Use Iterator.remove() or ListIterator.remove() for safe removal.

### Q7: What is the memory overhead of LinkedList vs ArrayList?
**A**: LinkedList: ~24 bytes per element (node object + 2 pointers). ArrayList: ~4 bytes per element (reference only). For 1 million elements: LinkedList uses ~24 MB more memory.

## 23. Exercises

### Exercise 1: Queue Implementation
Implement a queue using LinkedList:
- Enqueue and dequeue operations
- Peek at front element
- Check if empty
- Get size

### Exercise 2: Stack Implementation
Implement a stack using LinkedList:
- Push and pop operations
- Peek at top element
- Check if empty
- Get size

### Exercise 3: Bidirectional List
Create a bidirectional list that supports:
- Add/remove at both ends
- Forward and backward iteration
- Get element at index
- Reverse the list

### Exercise 4: LRU Cache
Implement an LRU (Least Recently Used) cache using LinkedList and HashMap:
- put(key, value) - Add or update
- get(key) - Retrieve and mark as recently used
- Evict least recently used when full

## 24. Assignments

### Assignment 1: Music Playlist
Build a music playlist application using LinkedList:
- Add songs at beginning or end
- Skip forward/backward
- Remove songs from any position
- Shuffle playlist
- Repeat functionality

### Assignment 2: Browser History
Implement browser history functionality:
- Visit new URLs
- Go back/forward
- Clear history
- Show recent history
- Bookmark functionality

### Assignment 3: Undo/Redo System
Build a generic undo/redo system:
- Execute actions
- Undo last action
- Redo last undone action
- Clear history
- Limit history size

## 25. Mini Project

### Text Editor with Undo/Redo

Build a simple text editor using LinkedList:

```java
// Features:
// 1. Insert/delete text
// 2. Undo/redo operations
// 3. Cursor movement (left/right)
// 4. Copy/paste functionality
// 5. Search and replace
// 6. Save/load to file
```

**Requirements:**
- Use LinkedList for text buffer (characters as nodes)
- Use another LinkedList for undo/redo history
- Support cursor position tracking
- Handle large files efficiently

## 26. Summary

LinkedList is a doubly-linked list implementation with specific use cases:

- **Internal structure**: Doubly-linked list of Node objects
- **Performance**: O(1) add/remove at ends, O(n) random access
- **Memory**: High overhead (24 bytes per element)
- **Best for**: Deque operations, frequent insertions at both ends, list splicing
- **Avoid for**: General-purpose list usage, random access, memory-sensitive applications
- **Alternative**: ArrayDeque is faster for queue/deque operations

## 27. References

### Official Documentation
- [LinkedList JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)
- [Deque Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html)

### Books
- *Effective Java* by Joshua Bloch
- *Java Performance* by Scott Oaks

### Online Resources
- [Baeldung LinkedList Guide](https://www.baeldung.com/java-linkedlist)
- [GeeksforGeeks LinkedList](https://www.geeksforgeeks.org/linked-list-set-1-introduction/)
- [OpenJDK LinkedList Source](https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/LinkedList.java)

### Related Topics
- [ArrayList](../03-arraylist/README.md)
- [Deque](../09-deque/README.md)
- [Iterator](../24-iterator/README.md)
