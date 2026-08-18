package academy.javaengineering.concurrency.practices;

public class WaitNotifyExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Basic wait/notify
     * TODO: Create a shared object. Thread A calls wait() on it.
     *       Thread B calls notify() after 1 second.
     *       Thread A should print "Woken up!" after being notified.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Basic wait/notify ===");
        // TODO: Implement here
        // Hint: Use an Object as monitor, call wait()/notify() inside synchronized block
    }

    /**
     * Exercise 2: Producer-Consumer with wait/notify
     * TODO: Implement a simple producer-consumer pattern.
     *       Producer adds items to a shared buffer, consumer removes them.
     *       Use wait()/notifyAll() to coordinate.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Producer-Consumer ===");
        // TODO: Implement here
        // Hint: Shared buffer with capacity 1, producer waits when full, consumer waits when empty
    }

    /**
     * Exercise 3: notifyAll() vs notify()
     * TODO: Demonstrate that notify() wakes only one thread, while notifyAll() wakes all.
     *       Create 3 waiting threads and use notifyAll().
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: notifyAll() Demo ===");
        // TODO: Implement here
        // Hint: 3 threads waiting on same object, main calls notifyAll()
    }

    /**
     * Exercise 4: Spurious wakeup prevention
     * TODO: Implement wait() inside a while loop (not if) to handle spurious wakeups.
     *       The condition should be checked after waking up.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Spurious Wakeup Prevention ===");
        // TODO: Implement here
        // Hint: while(!condition) { wait(); }
    }

    /**
     * Exercise 5: Bounded buffer producer-consumer
     * TODO: Implement a bounded buffer (size 5) with producer and consumer.
     *       Producer should block when buffer is full, consumer when empty.
     *       Use wait()/notifyAll().
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Bounded Buffer ===");
        // TODO: Implement here
        // Hint: Array-based buffer with count, putIndex, getIndex
    }
}
