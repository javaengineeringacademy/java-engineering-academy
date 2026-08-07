import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 1: Visibility Fix
 * Fix the broken visibility example
 */
public class VisibilityExercise {

    // BROKEN: This has a visibility issue
    static class BrokenVisibility {
        private boolean running = true;

        public void start() {
            new Thread(() -> {
                while (running) {
                    // May loop forever!
                }
                System.out.println("Worker stopped");
            }).start();
        }

        public void stop() {
            running = false;
        }
    }

    // TODO: Fix this class using the appropriate synchronization mechanism
    // Options: volatile, synchronized, AtomicInteger
    static class FixedVisibility {
        // Add your fix here

        public void start() {
            new Thread(() -> {
                while (true) { // TODO: Replace with correct condition
                    // Worker loop
                }
                System.out.println("Worker stopped");
            }).start();
        }

        public void stop() {
            // TODO: Implement stop
        }
    }

    /**
     * Exercise 2: Thread-Safe Counter
     * Implement using three different approaches
     */
    static class SynchronizedCounter {
        private int count = 0;

        // TODO: Implement thread-safe increment using synchronized
        public void increment() {
            // Your code here
        }

        public int getCount() {
            return count;
        }
    }

    static class VolatileCounter {
        private volatile int count = 0;

        // TODO: Explain why volatile alone is NOT sufficient for count++
        // What happens during count++?

        public void increment() {
            count++; // This is NOT thread-safe even with volatile!
        }

        public int getCount() {
            return count;
        }
    }

    static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        // TODO: Implement thread-safe increment using AtomicInteger
        public void increment() {
            // Your code here
        }

        public int getCount() {
            return count.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Memory Model Exercises ===\n");

        // Exercise 1: Fix visibility
        System.out.println("--- Exercise 1: Visibility Fix ---");
        System.out.println("TODO: Fix the BrokenVisibility class");
        System.out.println("Test by calling start(), sleeping, then stop()");
        System.out.println("The worker thread should stop within reasonable time\n");

        // Exercise 2: Thread-safe counter
        System.out.println("--- Exercise 2: Thread-Safe Counter ---");
        testCounter("Synchronized", new SynchronizedCounter());
        testCounter("Volatile (broken)", new VolatileCounter());
        testCounter("Atomic", new AtomicCounter());

        System.out.println("\n=== End of Exercises ===");
    }

    interface Counter {
        void increment();
        int getCount();
    }

    private static void testCounter(String name, Object counterObj) throws InterruptedException {
        Counter counter = null;
        if (counterObj instanceof SynchronizedCounter) {
            counter = new Counter() {
                public void increment() { ((SynchronizedCounter) counterObj).increment(); }
                public int getCount() { return ((SynchronizedCounter) counterObj).getCount(); }
            };
        } else if (counterObj instanceof VolatileCounter) {
            counter = new Counter() {
                public void increment() { ((VolatileCounter) counterObj).increment(); }
                public int getCount() { return ((VolatileCounter) counterObj).getCount(); }
            };
        } else if (counterObj instanceof AtomicCounter) {
            counter = new Counter() {
                public void increment() { ((AtomicCounter) counterObj).increment(); }
                public int getCount() { return ((AtomicCounter) counterObj).getCount(); }
            };
        }

        Thread t1 = new Thread(() -> { for (int i = 0; i < 100000; i++) counter.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 100000; i++) counter.increment(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.printf("%-25s Expected: 200000, Actual: %d%n", name, counter.getCount());
    }
}
