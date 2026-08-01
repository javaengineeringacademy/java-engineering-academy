# PriorityQueue

## Introduction

PriorityQueue is a Queue implementation that orders elements based on their natural ordering or a custom comparator.

## Learning Objectives

- Create and use PriorityQueue
- Understand priority ordering
- Use custom comparators with PriorityQueue
- Know when to use PriorityQueue vs regular Queue

## Prerequisites

- Queue Interface
- Comparable and Comparator interfaces

## Why This Matters

PriorityQueue is essential for implementing priority-based processing, such as task schedulers, event systems, and algorithms like Dijkstra's.

## Syntax

```java
// Creating PriorityQueue
PriorityQueue<E> pq = new PriorityQueue<>();              // Natural ordering
PriorityQueue<E> pq = new PriorityQueue<>(comparator);    // Custom ordering
PriorityQueue<E> pq = new PriorityQueue<>(initialCapacity); // With capacity
PriorityQueue<E> pq = new PriorityQueue<>(collection);    // From collection

// Standard Queue operations
pq.offer(element);
pq.poll();
pq.peek();
pq.size();
```

## Examples

```java
// Example 1: Basic PriorityQueue
PriorityQueue<Integer> numbers = new PriorityQueue<>();
numbers.offer(5);
numbers.offer(1);
numbers.offer(3);

System.out.println(numbers.poll());  // 1 (smallest first)
System.out.println(numbers.poll());  // 3
System.out.println(numbers.poll());  // 5

// Example 2: PriorityQueue with custom comparator
PriorityQueue<String> names = new PriorityQueue<>(Comparator.reverseOrder());
names.offer("Alice");
names.offer("Bob");
names.offer("Charlie");

System.out.println(names.poll());  // Charlie (largest first)
System.out.println(names.poll());  // Bob
System.out.println(names.poll());  // Alice

// Example 3: Task scheduler with priorities
class Task {
    String name;
    int priority;

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
}

PriorityQueue<Task> taskQueue = new PriorityQueue<>(
    Comparator.comparingInt(t -> t.priority)
);

taskQueue.offer(new Task("Low priority", 1));
taskQueue.offer(new Task("High priority", 10));
taskQueue.offer(new Task("Medium priority", 5));

System.out.println(taskQueue.poll().name);  // High priority
```

## Exercises

1. Create a PriorityQueue that orders integers in descending order.
2. Implement a top-k elements finder using PriorityQueue.
3. Create a task scheduler that processes high-priority tasks first.

## Interview Questions

- What is the time complexity of offer() and poll()?
- How does PriorityQueue differ from a sorted List?
- When would you use PriorityQueue over a TreeMap?

## Common Pitfalls

- Assuming PriorityQueue is sorted (it's a heap)
- Not providing a comparator when natural ordering isn't suitable
- Using remove() which is O(n) instead of poll()

## Best Practices

- Use appropriate initial capacity for better performance
- Provide a comparator when natural ordering isn't suitable
- Consider using for top-k problems

## Real World Applications

- Task scheduling with priorities
- Event-driven systems
- Dijkstra's algorithm
- Top-k element problems
- Job scheduling

## References

- [PriorityQueue Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Heap Data Structure](https://en.wikipedia.org/wiki/Heap_(data_structure))

## Summary

In this topic, you learned about PriorityQueue and its ordering capabilities. It's essential for priority-based processing and many algorithms. Practice with the exercises before learning about Deque.
