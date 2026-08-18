package academy.javaengineering.concurrency.practices;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: AtomicInteger basics
     * TODO: Use AtomicInteger to safely increment a counter from multiple threads.
     *       Demonstrate get(), incrementAndGet(), addAndGet().
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: AtomicInteger Basics ===");
        // TODO: Implement here
        // Hint: AtomicInteger counter = new AtomicInteger(0);
    }

    /**
     * Exercise 2: AtomicInteger compareAndSet
     * TODO: Use compareAndSet() for lock-free updates.
     *       Update the value only if it matches expected value.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: AtomicInteger compareAndSet ===");
        // TODO: Implement here
        // Hint: boolean updated = counter.compareAndSet(expected, newValue);
    }

    /**
     * Exercise 3: AtomicReference
     * TODO: Use AtomicReference to safely share an object between threads.
     *       Update the reference atomically using compareAndSet().
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: AtomicReference ===");
        // TODO: Implement here
        // Hint: AtomicReference<String> ref = new AtomicReference<>("initial");
    }

    /**
     * Exercise 4: AtomicBoolean
     * TODO: Use AtomicBoolean as a thread-safe flag.
     *       One thread sets it to true, another checks it.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: AtomicBoolean ===");
        // TODO: Implement here
        // Hint: AtomicBoolean flag = new AtomicBoolean(false);
    }

    /**
     * Exercise 5: AtomicLong accumulator
     * TODO: Use AtomicLong with getAndUpdate() to implement a simple accumulator.
     *       Accumulate values from multiple threads.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: AtomicLong Accumulator ===");
        // TODO: Implement here
        // Hint: accumulator.getAndUpdate(value -> value + increment)
    }
}
