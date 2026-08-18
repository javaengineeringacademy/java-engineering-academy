package academy.javaengineering.concurrency.communication.volatile;

/**
 * Demonstrates that volatile does NOT guarantee ATOMICITY.
 *
 * Even with volatile, compound operations like i++ are NOT thread-safe.
 * The operation i++ is actually three operations:
 * 1. Read current value
 * 2. Increment value
 * 3. Write new value
 *
 * Another thread can interfere between these steps!
 */
public class AtomicityProblem {

    // volatile ensures visibility but NOT atomicity
    private volatile int counter = 0;
    private final int INCREMENTS_PER_THREAD = 1_000_000;
    private final int NUM_THREADS = 4;

    public void increment() {
        // This is NOT atomic! It's: read -> increment -> write
        counter++;  // Race condition still exists!
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Volatile Atomicity Problem ===");
        System.out.println("Demonstrating that volatile does NOT make i++ thread-safe.");
        System.out.println();

        AtomicityProblem problem = new AtomicityProblem();

        Thread[] threads = new Thread[problem.NUM_THREADS];

        // Create and start threads
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < problem.NUM_THREADS; i++) {
            final int threadNum = i + 1;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < problem.INCREMENTS_PER_THREAD; j++) {
                    problem.increment();
                }
                System.out.println("[Thread " + threadNum + "] Completed " +
                    problem.INCREMENTS_PER_THREAD + " increments");
            }, "Worker-" + threadNum);
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            t.join();
        }

        long endTime = System.currentTimeMillis();

        int expected = problem.NUM_THREADS * problem.INCREMENTS_PER_THREAD;
        int actual = problem.getCounter();
        int lost = expected - actual;

        System.out.println();
        System.out.println("=== Results ===");
        System.out.println("Expected counter value: " + expected);
        System.out.println("Actual counter value:   " + actual);
        System.out.println("Lost increments:       " + lost);
        System.out.println("Time taken:            " + (endTime - startTime) + "ms");
        System.out.println();

        if (lost > 0) {
            System.out.println("=== Race Condition Detected! ===");
            System.out.println(lost + " increments were lost due to race conditions.");
            System.out.println();
            System.out.println("Why? The i++ operation is actually three steps:");
            System.out.println("  Step 1: temp = counter     (read)");
            System.out.println("  Step 2: temp = temp + 1    (increment)");
            System.out.println("  Step 3: counter = temp     (write)");
            System.out.println();
            System.out.println("Thread A might read counter=5, Thread B also reads counter=5.");
            System.out.println("Both compute 6, both write 6. One increment is LOST!");
            System.out.println();
            System.out.println("The fix: Use AtomicInteger or synchronized instead.");
        } else {
            System.out.println("No race condition detected (lucky timing!).");
            System.out.println("Run again - the race condition is non-deterministic.");
            System.out.println();
            System.out.println("The fix: Use AtomicInteger or synchronized instead.");
        }
    }
}
