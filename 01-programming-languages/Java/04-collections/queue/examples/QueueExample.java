package queue.examples;

import java.util.*;

public class QueueExample {

    public static void main(String[] args) {
        example1_BasicQueueOperations();
        example2_QueueMethods();
        example3_QueueImplementations();
        example4_QueueProcessing();
        example5_QueueWithDeque();
    }

    static void example1_BasicQueueOperations() {
        System.out.println("=== Example 1: Basic Queue Operations ===");
        Queue<String> queue = new LinkedList<>();
        queue.offer("Java");
        queue.offer("Python");
        queue.offer("C++");
        System.out.println("Queue: " + queue);
        System.out.println("Peek: " + queue.peek());
        System.out.println("Poll: " + queue.poll());
        System.out.println("After poll: " + queue);
    }

    static void example2_QueueMethods() {
        System.out.println("\n=== Example 2: Queue Methods ===");
        Queue<Integer> queue = new LinkedList<>();
        System.out.println("offer(10): " + queue.offer(10));
        System.out.println("offer(20): " + queue.offer(20));
        System.out.println("element(): " + queue.element());
        System.out.println("size(): " + queue.size());
        System.out.println("contains(10): " + queue.contains(10));
    }

    static void example3_QueueImplementations() {
        System.out.println("\n=== Example 3: Different Queue Implementations ===");
        Queue<Integer> linkedList = new LinkedList<>();
        Queue<Integer> arrayDeque = new ArrayDeque<>();
        Queue<Integer> priorityQueue = new PriorityQueue<>();
        for (int i : Arrays.asList(5, 1, 3, 2, 4)) {
            linkedList.offer(i);
            arrayDeque.offer(i);
            priorityQueue.offer(i);
        }
        System.out.println("LinkedList (FIFO): " + linkedList);
        System.out.println("ArrayDeque (FIFO): " + arrayDeque);
        System.out.println("PriorityQueue (sorted): " + priorityQueue);
    }

    static void example4_QueueProcessing() {
        System.out.println("\n=== Example 4: Queue Processing ===");
        Queue<String> taskQueue = new LinkedList<>(Arrays.asList("Task1", "Task2", "Task3"));
        while (!taskQueue.isEmpty()) {
            String task = taskQueue.poll();
            System.out.println("Processing: " + task);
        }
        System.out.println("Queue empty: " + taskQueue.isEmpty());
    }

    static void example5_QueueWithDeque() {
        System.out.println("\n=== Example 5: Queue using Deque ===");
        Deque<String> deque = new ArrayDeque<>();
        deque.offer("First");
        deque.offer("Second");
        deque.offer("Third");
        System.out.println("Queue via Deque: " + deque);
        System.out.println("Poll: " + deque.poll());
        System.out.println("After poll: " + deque);
    }
}
