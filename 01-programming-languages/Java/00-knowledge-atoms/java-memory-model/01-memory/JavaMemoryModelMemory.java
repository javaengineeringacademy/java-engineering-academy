package academy.javaengineering.knowledgeatoms.memorymodel;

public class JavaMemoryModelMemory {

    private static final int ITERATIONS = 1_000_000;
    private static volatile boolean volatileFlag = false;
    private static boolean nonVolatileFlag = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java Memory Model Memory Analysis ===\n");

        // 1. Memory visibility demonstration
        System.out.println("--- Memory Visibility ---");
        demonstrateVisibility();

        // 2. Volatile memory cost
        System.out.println("\n--- Volatile Memory Cost ---");
        System.out.println("Volatile write: ~10-20 CPU cycles (memory barrier)");
        System.out.println("Volatile read: ~1-2 CPU cycles (store-load barrier)");
        System.out.println("Non-volatile read: ~1 CPU cycle (register/cache)");

        // 3. Synchronized memory cost
        System.out.println("\n--- Synchronized Memory Cost ---");
        System.out.println("Uncontended synchronized: ~10-20 CPU cycles");
        System.out.println("Contended synchronized: ~1000+ CPU cycles (thread parking)");
        System.out.println("ReentrantLock: ~20-50 CPU cycles");

        // 4. False sharing
        System.out.println("\n--- False Sharing ---");
        System.out.println("Two threads writing to different variables on same cache line");
        System.out.println("Cache line size: 64 bytes (most modern CPUs)");
        System.out.println("Solution: @Contended annotation or padding");
        demonstrateFalseSharing();

        // 5. Thread-local memory
        System.out.println("\n--- Thread-Local Memory ---");
        System.out.println("Each thread has:");
        System.out.println("  - Program counter (which instruction)");
        System.out.println("  - JVM stack (local variables, method calls)");
        System.out.println("  - Native method stack");
        System.out.println("  - CPU registers and caches");
        System.out.println("Thread-local memory is not shared, no synchronization needed");
    }

    private static void demonstrateVisibility() throws InterruptedException {
        // Without volatile — reader may never see the write
        Thread writer = new Thread(() -> {
            nonVolatileFlag = true;  // may never be visible
        });

        Thread reader = new Thread(() -> {
            while (!nonVolatileFlag) {
                // may loop forever due to CPU caching
            }
            System.out.println("Non-volatile: reader saw the write");
        });

        writer.start();
        reader.start();
        writer.join(100);
        reader.join(100);

        if (reader.isAlive()) {
            reader.interrupt();
            System.out.println("Non-volatile: reader did NOT see the write (thread cached)");
        }

        // With volatile — guaranteed visibility
        volatileFlag = false;
        Thread writer2 = new Thread(() -> {
            volatileFlag = true;  // flushed to main memory
        });

        Thread reader2 = new Thread(() -> {
            while (!volatileFlag) {
                // re-reads from main memory each iteration
            }
            System.out.println("Volatile: reader saw the write");
        });

        writer2.start();
        reader2.start();
        writer2.join();
        reader2.join();
    }

    private static void demonstrateFalseSharing() throws InterruptedException {
        // Simulate false sharing with padded vs unpadded counters
        final int[] counter1 = {0};
        final int[] counter2 = {0};

        // Without padding — may share cache line
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) counter1[0]++;
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) counter2[0]++;
        });

        long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long unpaddedTime = System.nanoTime() - start;

        System.out.println("Unpadded counters: " + unpaddedTime / 1_000_000 + " ms");
        System.out.println("Padded counters (with @Contended): would be ~2-3x faster");
    }
}
