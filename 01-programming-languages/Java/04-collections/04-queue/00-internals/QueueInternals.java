package academy.javaengineering.collections.queue.internals;

import java.util.*;
import java.util.concurrent.*;

public class QueueInternals {

    public static void main(String[] args) {
        System.out.println("=== Queue Interface Internals ===\n");

        // 1. Queue operations
        System.out.println("--- Queue Operations ---");
        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println("offer() adds to tail");
        System.out.println("poll() removes from head");
        System.out.println("peek() views head");
        System.out.println("Queue: " + queue);

        // 2. PriorityQueue heap structure
        System.out.println("\n--- PriorityQueue Binary Heap ---");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(5);
        pq.offer(1);
        pq.offer(3);
        pq.offer(2);
        System.out.println("Min-heap: smallest at head");
        System.out.println("Extract order: " + pq.poll() + ", " + pq.poll() + ", " + pq.poll());

        // 3. ArrayDeque as Queue
        System.out.println("\n--- ArrayDeque as Queue ---");
        Deque<String> deque = new ArrayDeque<>();
        deque.offer("A");
        deque.offer("B");
        deque.offer("C");
        System.out.println("ArrayDeque: circular array");
        System.out.println("Faster than LinkedList for queue ops");

        // 4. BlockingQueue operations
        System.out.println("\n--- BlockingQueue ---");
        BlockingQueue<String> blocking = new ArrayBlockingQueue<>(5);
        System.out.println("put() blocks if full");
        System.out.println("take() blocks if empty");
        System.out.println("Capacity: " + blocking.remaining());

        // 5. Stack as LIFO Queue
        System.out.println("\n--- Stack (LIFO) ---");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Bottom");
        stack.push("Top");
        System.out.println("push() to top");
        System.out.println("pop() from top");
        System.out.println("peek() view top");
        System.out.println("Stack: " + stack);
    }
}
