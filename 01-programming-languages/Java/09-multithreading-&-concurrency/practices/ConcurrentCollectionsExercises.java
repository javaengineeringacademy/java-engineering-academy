package academy.javaengineering.concurrency.practices;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentCollectionsExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: ConcurrentHashMap putIfAbsent
     * TODO: Use ConcurrentHashMap's putIfAbsent() method to safely initialize values.
     *       Demonstrate that it only puts if the key is not already present.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: ConcurrentHashMap putIfAbsent ===");
        // TODO: Implement here
        // Hint: map.putIfAbsent(key, value) returns null if key was new, else existing value
    }

    /**
     * Exercise 2: ConcurrentHashMap compute
     * TODO: Use compute() and computeIfAbsent() to atomically update map values.
     *       Implement a word counter using ConcurrentHashMap.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: ConcurrentHashMap compute ===");
        // TODO: Implement here
        // Hint: map.compute(key, (k, v) -> v == null ? 1 : v + 1)
    }

    /**
     * Exercise 3: ConcurrentLinkedQueue
     * TODO: Use ConcurrentLinkedQueue for a thread-safe queue without blocking.
     *       Add and remove elements from multiple threads.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: ConcurrentLinkedQueue ===");
        // TODO: Implement here
        // Hint: queue.offer(e) to add, queue.poll() to remove (returns null if empty)
    }

    /**
     * Exercise 4: BlockingQueue put/take
     * TODO: Use LinkedBlockingQueue with put() and take() for producer-consumer.
     *       put() blocks when full, take() blocks when empty.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: BlockingQueue put/take ===");
        // TODO: Implement here
        // Hint: BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
    }

    /**
     * Exercise 5: ArrayBlockingQueue bounded
     * TODO: Use ArrayBlockingQueue with a fixed capacity.
     *       Demonstrate that put() blocks when the queue is full.
     *       Use offer() with timeout for non-blocking behavior.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: ArrayBlockingQueue ===");
        // TODO: Implement here
        // Hint: ArrayBlockingQueue has fixed capacity set at construction
    }
}
