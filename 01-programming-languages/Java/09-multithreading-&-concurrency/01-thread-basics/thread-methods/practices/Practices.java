package academy.javaengineering.concurrency.basics.methods;

/**
 * Practices - 5 exercises for Thread methods
 *
 * EXERCISE 1: Create a thread using start() and verify it runs in a different thread.
 *
 * EXERCISE 2: Use join() to wait for multiple threads to complete and print results in order.
 *
 * EXERCISE 3: Implement a thread that can be interrupted gracefully using proper interrupt handling.
 *
 * EXERCISE 4: Demonstrate the difference between interrupted() and isInterrupted().
 *
 * EXERCISE 5: Create a daemon thread that runs a background task.
 */
public class Practices {

    /**
     * EXERCISE 1:
     * Create a thread using start() and verify it runs in a different thread.
     * Print both current thread name and the new thread name.
     */
    public static void exercise1() {
        System.out.println("=== EXERCISE 1: start() vs run() ===");
        // TODO: Create a Thread, call start(), print thread names
        // Expected: Two different thread names printed
        System.out.println();
    }

    /**
     * EXERCISE 2:
     * Create 3 threads that each sleep for different durations (1s, 2s, 3s).
     * Use join() to wait for all to complete, then print "All done".
     */
    public static void exercise2() {
        System.out.println("=== EXERCISE 2: join() with Multiple Threads ===");
        // TODO: Create 3 threads, start them, join all, print "All done"
        System.out.println();
    }

    /**
     * EXERCISE 3:
     * Create a worker thread that runs a loop.
     * The main thread should interrupt it after 1 second.
     * The worker should handle the interrupt gracefully and print "Stopping gracefully".
     */
    public static void exercise3() {
        System.out.println("=== EXERCISE 3: Proper Interrupt Handling ===");
        // TODO: Create interruptible worker, interrupt from main
        System.out.println();
    }

    /**
     * EXERCISE 4:
     * Demonstrate that interrupted() clears the flag while isInterrupted() does not.
     * Interrupt a thread and show the difference.
     */
    public static void exercise4() {
        System.out.println("=== EXERCISE 4: interrupted() vs isInterrupted() ===");
        // TODO: Show flag behavior differences
        System.out.println();
    }

    /**
     * EXERCISE 5:
     * Create a daemon thread that prints "Background work" every 500ms.
     * Main thread should sleep for 2 seconds then exit.
     * Daemon should stop automatically when main exits.
     */
    public static void exercise5() {
        System.out.println("=== EXERCISE 5: Daemon Thread ===");
        // TODO: Create daemon thread, let main exit
        System.out.println();
    }

    public static void main(String[] args) {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
