package academy.javaengineering.jvm.practices;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Memory Model Exercises
 * Complete each exercise by implementing the required method.
 * Focus on happens-before, volatile, synchronized, and visibility issues.
 */
public class MemoryModelExercises {

    // Shared state for exercises
    private static int sharedCounter = 0;
    private static volatile boolean running = true;
    private static final Object lock = new Object();
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);

    /**
     * Exercise 1: Fix the visibility problem
     * The following code has a visibility bug - the worker thread
     * may never see the updated value of 'stop'. Fix it.
     *
     * WITHOUT volatile:
     *   Thread worker = new Thread(() -> {
     *       while (!stop) { /* work */ }
     *       System.out.println("Stopped");
     *   });
     *   worker.start();
     *   Thread.sleep(100);
     *   stop = true; // May never be seen!
     */
    public static void fixVisibilityProblem() {
        // TODO: Fix the visibility problem using volatile or synchronized
        final boolean[] stop = {false};

        Thread worker = new Thread(() -> {
            int count = 0;
            while (!stop[0]) {
                count++;
            }
            System.out.println("Worker stopped after " + count + " iterations");
        });

        worker.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        stop[0] = true; // TODO: Make this visible to worker thread

        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Exercise 2: Implement a thread-safe counter using synchronized
     * Create a counter class that is thread-safe using synchronized blocks.
     * The counter should support increment, decrement, and get operations.
     */
    static class SynchronizedCounter {
        private int count = 0;

        // TODO: Implement synchronized increment
        public void increment() {
            // Implement this
        }

        // TODO: Implement synchronized decrement
        public void decrement() {
            // Implement this
        }

        // TODO: Implement synchronized get
        public int getCount() {
            // Implement this
            return 0;
        }
    }

    /**
     * Exercise 3: Implement double-checked locking correctly
     * Implement a thread-safe lazy singleton using double-checked locking.
     * Remember: the instance field MUST be volatile!
     *
     * Common mistake: forgetting volatile leads to partially constructed objects
     */
    static class Singleton {
        // TODO: Add volatile modifier
        private static Singleton instance;

        private Singleton() {
            System.out.println("Singleton created");
        }

        // TODO: Implement double-checked locking
        public static Singleton getInstance() {
            // Implement this
            return null;
        }
    }

    /**
     * Exercise 4: Demonstrate happens-before with Thread.join()
     * Create two threads:
     * - Thread A: modifies a shared variable and completes
     * - Thread B: calls Thread.join() on Thread A, then reads the shared variable
     * Verify that Thread B always sees Thread A's changes.
     */
    public static void demonstrateJoinHappensBefore() {
        // TODO: Implement this exercise
        // 1. Create shared variable
        // 2. Create Thread A that modifies the variable
        // 3. Create Thread B that calls join() then reads the variable
        // 4. Start both threads
        // 5. Verify Thread B sees Thread A's changes
    }

    /**
     * Exercise 5: Fix race condition in compound operation
     * The following code has a race condition on 'checkThenAct':
     * if (sharedCounter == 0) { sharedCounter = 1; }
     * Another thread can change sharedCounter between the check and act.
     *
     * Fix this using either:
     * 1. synchronized block
     * 2. AtomicReference
     * 3. compareAndSet
     */
    public static void fixRaceCondition() {
        sharedCounter = 0;

        Runnable checkThenAct = () -> {
            for (int i = 0; i < 10000; i++) {
                // TODO: Fix the race condition
                if (sharedCounter == 0) {
                    sharedCounter = 1;
                }
                sharedCounter = 0;
            }
        };

        Thread t1 = new Thread(checkThenAct);
        Thread t2 = new Thread(checkThenAct);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value (should be 0): " + sharedCounter);
    }

    public static void main(String[] args) {
        System.out.println("=== Memory Model Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Fix Visibility Problem");
        fixVisibilityProblem();

        // Test Exercise 2
        System.out.println("\nExercise 2: SynchronizedCounter");
        SynchronizedCounter counter = new SynchronizedCounter();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Counter (should be 10000): " + counter.getCount());

        // Test Exercise 3
        System.out.println("\nExercise 3: Singleton");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Singleton s = Singleton.getInstance();
                System.out.println("Thread " + Thread.currentThread().getName()
                        + " got: " + System.identityHashCode(s));
            }).start();
        }

        // Test Exercise 4
        System.out.println("\nExercise 4: Happens-Before with join()");
        demonstrateJoinHappensBefore();

        // Test Exercise 5
        System.out.println("\nExercise 5: Fix Race Condition");
        fixRaceCondition();
    }
}
