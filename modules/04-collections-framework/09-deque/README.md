# Deque Interface

## Introduction

Deque (Double-Ended Queue) is a collection that supports insertion and removal at both ends. It can function as both a queue (FIFO) and a stack (LIFO).

## Learning Objectives

- Understand the Deque interface and its methods
- Use Deque as a queue or stack
- Learn ArrayDeque vs LinkedList implementations
- Know when to use Deque over Stack or Queue

## Prerequisites

- Queue Interface
- Stack concepts

## Why This Matters

Deque is more versatile than Stack and Queue, and ArrayDeque provides better performance than both LinkedList and Stack.

## Syntax

```java
// Creating Deque
Deque<E> deque = new ArrayDeque<>();  // Recommended
Deque<E> deque = new LinkedList<>();

// Queue operations (FIFO)
deque.offer(element);  // Add to tail
deque.poll();          // Remove from head
deque.peek();          // View head

// Stack operations (LIFO)
deque.push(element);   // Add to head
deque.pop();           // Remove from head
deque.peek();          // View head

// Double-ended operations
deque.offerFirst(element);  // Add to head
deque.offerLast(element);   // Add to tail
deque.pollFirst();          // Remove from head
deque.pollLast();           // Remove from tail
deque.peekFirst();          // View head
deque.peekLast();           // View tail
```

## Examples

```java
// Example 1: Deque as Stack
Deque<String> stack = new ArrayDeque<>();
stack.push("Bottom");
stack.push("Middle");
stack.push("Top");

System.out.println(stack.pop());   // Top
System.out.println(stack.peek());  // Middle

// Example 2: Deque as Queue
Deque<String> queue = new ArrayDeque<>();
queue.offer("First");
queue.offer("Second");
queue.offer("Third");

System.out.println(queue.poll());  // First
System.out.println(queue.peek());  // Second

// Example 3: Sliding window maximum
public int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> deque = new ArrayDeque<>();
    int[] result = new int[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
        while (!deque.isEmpty() && deque.peekLast() < nums[i]) {
            deque.pollLast();
        }
        deque.offerLast(nums[i]);

        if (deque.peekFirst() == nums[i - k + 1]) {
            deque.pollFirst();
        }

        if (i >= k - 1) {
            result[i - k + 1] = deque.peekFirst();
        }
    }

    return result;
}
```

## Exercises

1. Implement a palindrome checker using Deque.
2. Create a browser history simulator with back/forward using Deque.
3. Implement a sliding window maximum algorithm using Deque.

## Interview Questions

- Why is ArrayDeque preferred over Stack?
- What is the time complexity of addFirst() and addLast()?
- How would you implement a queue using two stacks?

## Common Pitfalls

- Using LinkedList when ArrayDeque would be more efficient
- Not checking for empty deque before pop/poll operations
- Confusing push/pop with addFirst/removeFirst

## Best Practices

- Use ArrayDeque for queue/stack implementations
- Prefer offer/poll/peek for queue operations
- Prefer push/pop for stack operations
- Check size() or isEmpty() before operations

## Real World Applications

- Implementing stacks and queues
- Browser navigation history
- Undo/redo functionality
- Sliding window algorithms
- Palindrome checking

## References

- [Deque Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html)
- [ArrayDeque Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html)

## Summary

In this topic, you learned about the Deque interface and its versatility as both a queue and stack. ArrayDeque is the preferred implementation for most use cases. Practice with the exercises before learning about Set.
