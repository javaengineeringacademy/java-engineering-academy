# LinkedList

## Introduction

LinkedList is a doubly-linked list implementation of the List and Deque interfaces. It provides efficient insertion and deletion at both ends.

## Learning Objectives

- Create and use LinkedList
- Understand LinkedList's performance characteristics
- Learn when to use LinkedList over ArrayList
- Use LinkedList as a Queue or Deque

## Prerequisites

- ArrayList
- List Interface
- Basic linked data structure concepts

## Why This Matters

LinkedList excels when you need frequent insertions and deletions, especially at the beginning of the list, or when you need a Queue/Deque implementation.

## Syntax

```java
// Creating LinkedList
LinkedList<E> list = new LinkedList<>();           // Empty
LinkedList<E> list = new LinkedList<>(collection); // From another collection

// Queue/Deque operations (LinkedList implements both)
queue.add(element);        // Add to tail
queue.offer(element);      // Add to tail (returns false if full)
queue.peek();              // View head (null if empty)
queue.poll();              // Remove and return head (null if empty)
queue.remove();            // Remove and return head (throws if empty)
```

## Examples

```java
// Example 1: Basic LinkedList
LinkedList<String> names = new LinkedList<>();
names.add("Alice");
names.addFirst("Bob");      // Add to beginning
names.addLast("Charlie");   // Add to end

System.out.println(names);  // [Bob, Alice, Charlie]
System.out.println(names.getFirst());  // Bob
System.out.println(names.getLast());   // Charlie

// Example 2: Using as Queue
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
queue.offer("Third");

System.out.println(queue.poll());  // First
System.out.println(queue.peek());  // Second

// Example 3: Using as Deque (Stack)
Deque<String> stack = new LinkedList<>();
stack.push("Bottom");
stack.push("Middle");
stack.push("Top");

System.out.println(stack.pop());  // Top
System.out.println(stack.peek()); // Middle

// Example 4: LinkedList vs ArrayList performance
LinkedList<Integer> linkedList = new LinkedList<>();
ArrayList<Integer> arrayList = new ArrayList<>();

// Insertion at beginning: LinkedList wins
long start = System.nanoTime();
linkedList.addFirst(1);
long linkedTime = System.nanoTime() - start;

start = System.nanoTime();
arrayList.add(0, 1);
long arrayTime = System.nanoTime() - start;
```

## Exercises

1. Implement a queue using LinkedList to simulate a printer spooler.
2. Create a method that reverses a LinkedList.
3. Use LinkedList as a stack to check for balanced parentheses.

## Interview Questions

- What is the difference between ArrayList and LinkedList?
- When would you choose LinkedList over ArrayList?
- How does LinkedList implement both List and Deque?

## Common Pitfalls

- Using LinkedList for random access (O(n) vs O(1) for ArrayList)
- Not understanding the overhead of node objects
- Confusing LinkedList's remove() with removeFirst()/removeLast()

## Best Practices

- Use LinkedList when frequent insertions/deletions at ends
- Use ArrayList for random access and iteration
- Consider ArrayDeque over LinkedList for stack/queue implementations
- Set initial capacity for better performance

## Real World Applications

- Implementing undo/redo functionality
- Browser history navigation
- Music playlist management
- Task scheduling queues

## References

- [LinkedList Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/implementations/linkedlist.html)

## Summary

In this topic, you learned about LinkedList and its advantages for frequent insertions and deletions. It also implements Deque for stack and queue operations. Practice with the exercises before learning about Vector.
