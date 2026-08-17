package academy.javaengineering.concurrency.lifecycle;

/**
 * Practices - Exercises for Thread Lifecycle.
 * Complete each exercise by filling in the TODO sections.
 */
public class Practices {

    private static final Object lock = new Object();

    /**
     * Exercise 1: State Transitions
     * Create a thread and print its state at each lifecycle stage:
     * NEW → RUNNABLE → (TIMED_WAITING) → RUNNABLE → TERMINATED
     *
     * TODO: Print thread state at each transition point.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: State Transitions");
        System.out.println("=============================");

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // TODO: Print state at NEW
        // TODO: Start thread, print state at RUNNABLE
        // TODO: Wait briefly, print state at TIMED_WAITING
        // TODO: Join thread, print state at TERMINATED

        System.out.println();
    }

    /**
     * Exercise 2: BLOCKED State
     * Create two threads that both need the same lock.
     * Thread 1 holds the lock for 1 second.
     * Thread 2 tries to acquire the lock (will be BLOCKED).
     * Print Thread 2's state while it's blocked.
     *
     * TODO: Demonstrate BLOCKED state.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: BLOCKED State");
        System.out.println("=========================");

        // TODO: Create thread 1 that holds lock for 1 second
        // TODO: Create thread 2 that tries to acquire the same lock
        // TODO: Print thread 2's state while blocked
        // TODO: Wait for both threads to complete

        System.out.println();
    }

    /**
     * Exercise 3: wait/notify Pattern
     * Implement a simple signal: Thread A waits for a signal,
     * Thread B sends the signal after 500ms. Thread A should
     * print "Signal received!" when notified.
     *
     * TODO: Implement wait/notify coordination.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: wait/notify Signal");
        System.out.println("==============================");

        // TODO: Create Thread A that waits on lock
        // TODO: Create Thread B that notifies after 500ms
        // TODO: Thread A prints "Signal received!"
        // TODO: Join both threads

        System.out.println();
    }

    /**
     * Exercise 4: Interrupt During sleep()
     * Create a thread that sleeps for 10 seconds.
     * After 500ms, interrupt it. The thread should catch
     * the InterruptedException and print "Woke up early!"
     *
     * TODO: Implement interrupt handling during sleep.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Interrupt During sleep()");
        System.out.println("=====================================");

        // TODO: Create a thread that sleeps for 10 seconds
        // TODO: After 500ms, interrupt the thread
        // TODO: Thread catches exception and prints "Woke up early!"
        // TODO: Join the thread

        System.out.println();
    }

    /**
     * Exercise 5: Daemon Thread Lifecycle
     * Create a daemon thread that prints a message every 300ms.
     * The main thread sleeps for 1 second then prints "Main done".
     * The daemon should stop automatically.
     *
     * TODO: Implement daemon thread lifecycle.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Daemon Thread Lifecycle");
        System.out.println("====================================");

        // TODO: Create a daemon thread with infinite loop
        // TODO: Thread prints message every 300ms
        // TODO: Main thread sleeps 1 second
        // TODO: Main prints "Main done"
        // TODO: Daemon stops automatically

        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
