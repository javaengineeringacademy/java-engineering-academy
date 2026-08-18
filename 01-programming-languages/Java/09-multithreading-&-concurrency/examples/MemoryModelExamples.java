package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;

public class MemoryModelExamples {

    private static int x = 0;
    private static int y = 0;
    private static int a = 0;
    private static int b = 0;
    private static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        example1_VisibilityProblem();
        example2_VolatileVisibility();
        example3_HappensBefore();
        example4_Reordering();
        example5_MemoryBarrier();
    }

    // Example 1: Visibility problem without volatile
    static void example1_VisibilityProblem() throws InterruptedException {
        System.out.println("=== Example 1: Visibility Problem ===");

        // Without volatile, the reader thread might never see the update
        // because it could read from its CPU cache
        final boolean[] keepRunning = {true};

        Thread writer = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            keepRunning[0] = false; // Without volatile, this might not be visible
            System.out.println("Writer: set keepRunning = false");
        }, "Writer");

        Thread reader = new Thread(() -> {
            int iterations = 0;
            while (keepRunning[0]) { // Might loop forever without volatile
                iterations++;
            }
            System.out.println("Reader: stopped after " + iterations + " iterations");
        }, "Reader");

        writer.start();
        reader.start();
        writer.join(2000);
        reader.join(2000);

        if (reader.isAlive()) {
            reader.interrupt();
            System.out.println("Reader was still running (visibility problem!)");
        }

        System.out.println();
    }

    // Example 2: Volatile guarantees visibility
    static void example2_VolatileVisibility() throws InterruptedException {
        System.out.println("=== Example 2: Volatile Visibility ===");

        // volatile guarantees:
        // 1. Visibility: writes are immediately visible to other threads
        // 2. Ordering: prevents reordering of operations around volatile access

        boolean[] running = {true};
        final int[] counter = {0};

        Thread worker = new Thread(() -> {
            while (running) {
                counter[0]++;
            }
            System.out.println("Worker stopped. Counter: " + counter[0]);
        });

        worker.start();
        TimeUnit.MILLISECONDS.sleep(100);

        running = false; // volatile ensures this is visible to worker
        worker.join();

        System.out.println();
    }

    // Example 3: Happens-before relationships
    static void example3_HappensBefore() throws InterruptedException {
        System.out.println("=== Example 3: Happens-Before Relationships ===");

        // Happens-before guarantees:
        // 1. Program order: each action in a thread happens-before next action
        // 2. Monitor lock: unlock happens-before subsequent lock
        // 3. Volatile: write happens-before subsequent read
        // 4. Thread.start(): any action in starting thread happens-before
        //    any action in started thread
        // 5. Thread.join(): any action in joined thread happens-before
        //    return from join
        // 6. Transitivity: if A happens-before B and B happens-before C,
        //    then A happens-before C

        final int[] result = {0};
        final Object monitor = new Object();

        Thread writer = new Thread(() -> {
            synchronized (monitor) { // Monitor lock release
                result[0] = 42;
            }
        });

        Thread reader = new Thread(() -> {
            synchronized (monitor) { // Monitor lock acquire (happens-before)
                System.out.println("Read value: " + result[0]);
            }
        });

        writer.start();
        writer.join(); // Thread.join happens-before
        reader.start();
        reader.join();

        System.out.println();
    }

    // Example 4: Instruction reordering
    static void example4_Reordering() throws InterruptedException {
        System.out.println("=== Example 4: Instruction Reordering ===");

        // Without proper synchronization, the JVM and CPU can reorder instructions
        // as long as the single-threaded behavior is preserved

        // Classic example: double-checked locking
        // Broken without volatile:
        // if (instance == null) {          // read 1
        //     synchronized (lock) {
        //         if (instance == null) {  // read 2
        //             instance = new Singleton(); // 1. allocate 2. initialize 3. assign
        //         }                        // could be reordered to 1. allocate 3. assign 2. initialize
        //         }                        // another thread might see non-null but uninitialized object!
        //     }

        // Solution: use volatile
        // volatile Singleton instance;
        // The volatile write of the instance variable happens-before any subsequent read

        System.out.println("Reordering can cause problems in:");
        System.out.println("1. Double-checked locking (without volatile)");
        System.out.println("2. Lazy initialization patterns");
        System.out.println("3. Publishing objects before construction is complete");

        // Demonstrate with a simple example
        final int[] data = {0, 0};
        final boolean[] ready = {false};

        Thread writer = new Thread(() -> {
            data[0] = 42;       // Step 1
            data[1] = 99;       // Step 2
            ready[0] = true;    // Step 3 (volatile would ensure ordering)
        });

        Thread reader = new Thread(() -> {
            while (!ready[0]) { // Might see ready=true but data not yet updated
                Thread.yield();
            }
            // Without volatile, might see ready=true but data[0]=0 and data[1]=0
            System.out.println("Data: [" + data[0] + ", " + data[1] + "]");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println();
    }

    // Example 5: Memory barriers and fences
    static void example5_MemoryBarrier() throws InterruptedException {
        System.out.println("=== Example 5: Memory Barriers ===");

        // Memory barriers/fences enforce ordering:
        // 1. Load Barrier (acquire): ensures reads after barrier see all prior writes
        // 2. Store Barrier (release): ensures writes before barrier are visible to reads after
        // 3. Full Barrier: both load and store

        // In Java, these are achieved through:
        // - synchronized block (entering = acquire, exiting = release)
        // - volatile variable (every access is a full barrier)
        // - Thread.start() and Thread.join()
        // - Concurrent utility classes

        // Acquire-Release pattern
        int[] sharedData = {0};
        boolean[] published = {false};

        Thread publisher = new Thread(() -> {
            sharedData[0] = 42; // Write data
            published[0] = true; // volatile write = release barrier
            // All writes above are visible before volatile write
        });

        Thread consumer = new Thread(() -> {
            while (!published[0]) { // volatile read = acquire barrier
                Thread.yield();
            }
            // All reads below see writes that happened before volatile write
            System.out.println("Published data: " + sharedData[0]);
        });

        publisher.start();
        consumer.start();
        publisher.join();
        consumer.join();

        System.out.println("Key takeaways:");
        System.out.println("- Use volatile for flags and simple publications");
        System.out.println("- Use synchronized for compound operations");
        System.out.println("- Use java.util.concurrent for complex synchronization");
        System.out.println("- Always prefer high-level concurrency utilities over raw synchronization");

        System.out.println();
    }
}
