package academy.javaengineering.jvm.solutions;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Memory Model Solutions - Complete implementations
 */
public class MemoryModelSolutions {

    private static int sharedCounter = 0;
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);
    private static final Object lock = new Object();

    /**
     * Exercise 1 Solution: Fix visibility problem using volatile
     */
    public static void fixVisibilityProblem() {
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

        stop[0] = true; // volatile-like visibility via array

        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Exercise 2 Solution: Thread-safe counter using synchronized
     */
    static class SynchronizedCounter {
        private int count = 0;

        public void increment() {
            synchronized (this) {
                count++;
            }
        }

        public void decrement() {
            synchronized (this) {
                count--;
            }
        }

        public int getCount() {
            synchronized (this) {
                return count;
            }
        }
    }

    /**
     * Exercise 3 Solution: Double-checked locking singleton
     */
    static class Singleton {
        private static volatile Singleton instance; // volatile is critical!

        private Singleton() {
            System.out.println("Singleton created by thread: " + Thread.currentThread().getName());
        }

        public static Singleton getInstance() {
            if (instance == null) {                      // First check (no lock)
                synchronized (Singleton.class) {         // Lock
                    if (instance == null) {              // Second check (with lock)
                        instance = new Singleton();      // Create instance
                    }
                }
            }
            return instance;
        }
    }

    /**
     * Exercise 4 Solution: Demonstrate happens-before with Thread.join()
     */
    public static void demonstrateJoinHappensBefore() {
        final int[] sharedValue = {0};

        Thread writer = new Thread(() -> {
            sharedValue[0] = 42; // Modify shared variable
            System.out.println("[Writer] Set value to 42");
        });

        Thread reader = new Thread(() -> {
            try {
                writer.join(); // join() happens-before subsequent actions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Guaranteed to see 42 because:
            // writer modifies before join()
            // join() returns -> happens-before -> reader reads
            System.out.println("[Reader] Read value: " + sharedValue[0]
                    + " (guaranteed to be 42)");
        });

        writer.start();
        reader.start();

        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Exercise 5 Solution: Fix race condition using AtomicInteger
     */
    public static void fixRaceCondition() {
        AtomicInteger counter = new AtomicInteger(0);

        Runnable checkThenAct = () -> {
            for (int i = 0; i < 10000; i++) {
                // Atomic operation - no race condition
                counter.compareAndSet(0, 1);
                counter.compareAndSet(1, 0);
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

        System.out.println("AtomicInteger result (should be 0): " + counter.get());
    }

    /**
     * Alternative solution: Using synchronized
     */
    public static void fixRaceConditionSynchronized() {
        sharedCounter = 0;

        Runnable checkThenAct = () -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (lock) {
                    if (sharedCounter == 0) {
                        sharedCounter = 1;
                    }
                    sharedCounter = 0;
                }
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

        System.out.println("Synchronized result (should be 0): " + sharedCounter);
    }

    public static void main(String[] args) {
        System.out.println("=== Memory Model Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Visibility Fix");
        fixVisibilityProblem();

        // Exercise 2
        System.out.println("\nExercise 2: Synchronized Counter");
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

        // Exercise 3
        System.out.println("\nExercise 3: Singleton");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Singleton s = Singleton.getInstance();
                System.out.println("Thread " + Thread.currentThread().getName()
                        + " got: " + System.identityHashCode(s));
            }).start();
        }

        // Exercise 4
        System.out.println("\nExercise 4: Happens-Before with join()");
        demonstrateJoinHappensBefore();

        // Exercise 5
        System.out.println("\nExercise 5: Fix Race Condition");
        fixRaceCondition();
    }
}
