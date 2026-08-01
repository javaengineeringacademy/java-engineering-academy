# Queue Interface

## Introduction

Queue is a collection designed for holding elements prior to processing. It follows FIFO (First In, First Out) ordering.

## Learning Objectives

- Understand the Queue interface and its methods
- Learn the difference between throw and return-value methods
- Understand Queue implementations
- Know when to use Queue vs other collections

## Prerequisites

- List Interface
- Basic data structure concepts

## Why This Matters

Queues are essential for many algorithms and systems, including task scheduling, breadth-first search, and event handling.

## Syntax

```java
// Queue interface methods
Queue<E> queue = new LinkedList<>();

// Throw exception on failure
queue.add(element);    // Add to tail
queue.remove();        // Remove from head
queue.element();       // View head

// Return special value on failure
queue.offer(element);  // Add to tail (returns false if full)
queue.poll();          // Remove from head (returns null if empty)
queue.peek();          // View head (returns null if empty)
```

## Examples

```java
// Example 1: Basic Queue operations
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
queue.offer("Third");

System.out.println(queue.peek());  // First
System.out.println(queue.poll());  // First
System.out.println(queue.poll());  // Second
System.out.println(queue.size());  // 1

// Example 2: BFS using Queue
public void bfs(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        System.out.println(node.value);

        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
}

// Example 3: Task scheduler
public class TaskScheduler {
    private Queue<Task> taskQueue = new LinkedList<>();

    public void addTask(Task task) {
        taskQueue.offer(task);
    }

    public void processTasks() {
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            task.execute();
        }
    }
}
```

## Exercises

1. Implement a printer spooler using a Queue.
2. Write a method that uses Queue to perform level-order traversal of a binary tree.
3. Create a customer service system using Queue with priorities.

## Interview Questions

- What is the difference between offer() and add()?
- When would you use a PriorityQueue over a regular Queue?
- How would you implement a stack using two queues?

## Common Pitfalls

- Confusing throw and return-value methods
- Using Queue when you need LIFO (use Deque for stack)
- Not handling null elements properly

## Best Practices

- Use offer(), poll(), and peek() for safer operations
- Consider PriorityQueue for priority-based processing
- Use ArrayDeque for better performance than LinkedList

## Real World Applications

- Print spooling
- Message queues
- Task scheduling
- BFS algorithms
- Customer service systems

## References

- [Queue Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/interfaces/queue.html)

## Summary

In this topic, you learned about the Queue interface and its FIFO operations. Queues are fundamental for many algorithms and systems. Practice with the exercises before learning about PriorityQueue.
