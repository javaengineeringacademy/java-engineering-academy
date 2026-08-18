package academy.javaengineering.concurrency.practices;

public class SynchronizationExercises {

    static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Race condition without synchronization
     * TODO: Create 10 threads that each increment a shared counter 1000 times.
     *       Without synchronization, print the final value.
     *       Expected: 10000, but actual value will likely be less.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Race Condition ===");
        counter = 0;
        // TODO: Implement here
        // Hint: Use a for loop to create threads, each incrementing counter 1000 times
    }

    /**
     * Exercise 2: Synchronized method
     * TODO: Create a Counter class with a synchronized increment method.
     *       Demonstrate that synchronized access prevents race conditions.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Synchronized Method ===");
        // TODO: Implement here
        // Hint: class Counter { synchronized void increment() { counter++; } }
    }

    /**
     * Exercise 3: Synchronized block
     * TODO: Use a synchronized block to protect only the critical section.
     *       Compare the approach with synchronized method.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Synchronized Block ===");
        // TODO: Implement here
        // hint: synchronized(this) { counter++; }
    }

    /**
     * Exercise 4: Static synchronized method
     * TODO: Create a class with a static synchronized method and a static counter.
     *       Demonstrate that static synchronized locks on the Class object, not instance.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Static Synchronized ===");
        // TODO: Implement here
        // Hint: static synchronized increment() locks on ClassName.class
    }

    /**
     * Exercise 5: Deadlock prevention
     * TODO: Create a scenario with two locks that could cause deadlock.
     *       Fix it by ensuring consistent lock ordering.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Deadlock Prevention ===");
        // TODO: Implement here
        // Hint: Two threads, two locks. Always acquire locks in same order.
    }
}
