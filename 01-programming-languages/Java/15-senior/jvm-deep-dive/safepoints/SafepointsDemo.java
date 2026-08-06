import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * SafepointsDemo - Understanding JVM Safepoints
 * 
 * Safepoints are points in program execution where the JVM can safely
 * perform operations that require all threads to be in a known state.
 * This includes garbage collection, JIT deoptimization, and other
 * runtime operations.
 * 
 * Key concepts:
 * - Safepoints ensure thread safety during GC and JIT operations
 * - Threads must reach a safepoint before the JVM can proceed
 * - Polling mechanisms check if a thread needs to stop
 * - Long-running loops without method calls can delay safepoints
 */
public class SafepointsDemo {

    private static final int ITERATIONS = 1_000_000_000;
    private static final AtomicInteger sharedCounter = new AtomicInteger(0);
    private static volatile boolean keepRunning = true;

    /**
     * Demonstrates safepoint delay caused by long-running loops.
     * The JVM cannot safely stop a thread executing a tight loop
     * until it reaches a safepoint (method call, back edge, etc.)
     */
    public static void longRunningLoop() {
        System.out.println("=== Long Running Loop Demo ===");
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            // This loop has safepoints at:
            // - Back edge (loop condition check)
            // - Method calls within the loop
            sharedCounter.incrementAndGet();
        }

        long endTime = System.nanoTime();
        System.out.println("Completed " + ITERATIONS + " iterations");
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Note: JVM inserted safepoint polls at loop back edges");
    }

    /**
     * Demonstrates safepoint behavior with tight loops.
     * Without method calls, safepoint checks may be minimal,
     * potentially causing longer pause times when GC is triggered.
     */
    public static void tightLoop() {
        System.out.println("\n=== Tight Loop Without Safepoint Polls ===");
        long startTime = System.nanoTime();

        int localCounter = 0;
        // Tight loop - fewer safepoint opportunities
        for (int i = 0; i < ITERATIONS; i++) {
            localCounter++;
            // No method calls = fewer safepoints
            // JVM will still insert polling at back edges
        }

        long endTime = System.nanoTime();
        System.out.println("Completed with localCounter: " + localCounter);
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    /**
     * Shows safepoint synchronization with multiple threads.
     * All threads must reach a safepoint before GC can proceed.
     */
    public static void multiThreadedSafepoint() throws InterruptedException {
        System.out.println("\n=== Multi-Threaded Safepoint Demo ===");
        final int threadCount = 4;
        Thread[] threads = new Thread[threadCount];

        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    // Each thread performs work with safepoint checks
                    for (int j = 0; j < 100_000_000; j++) {
                        sharedCounter.incrementAndGet();
                        // Method calls act as safepoint poll locations
                    }
                    System.out.println("Thread " + threadId + " completed");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final counter: " + sharedCounter.get());
        System.out.println("All threads reached safepoints");
    }

    /**
     * Demonstrates thread states at safepoints.
     * During safepoint operations, threads are in one of these states:
     * - _thread_in_vm: Thread executing VM code
     * - _thread_in_native: Thread executing native code
     * - _thread_blocked: Thread blocked on monitor
     * - _thread_uninitialized: Thread not yet started
     */
    public static void threadStatesAtSafepoint() throws InterruptedException {
        System.out.println("\n=== Thread States at Safepoints ===");

        Thread workerThread = new Thread(() -> {
            System.out.println("Worker thread started, state: " + Thread.currentThread().getState());

            // Simulate work with method calls (safepoint locations)
            for (int i = 0; i < 10; i++) {
                performComputation();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("Worker thread completing, state: " + Thread.currentThread().getState());
        });

        workerThread.start();
        Thread.sleep(50); // Let thread run

        System.out.println("Main thread checking worker state: " + workerThread.getState());
        workerThread.join();
    }

    private static void performComputation() {
        // Method calls are safepoint poll locations
        int x = 42 * 42;
        double y = Math.sqrt(x);
        sharedCounter.addAndGet((int) y);
    }

    /**
     * Shows how safepoints impact latency.
     * Long safepoint pauses can cause latency spikes.
     * This is critical for low-latency applications.
     */
    public static void safepointLatencyImpact() {
        System.out.println("\n=== Safepoint Latency Impact ===");

        long[] latencies = new long[100];
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();

            // Perform operation that might trigger safepoint
            for (int j = 0; j < 1_000_000; j++) {
                sharedCounter.incrementAndGet();
            }

            long end = System.nanoTime();
            latencies[i] = end - start;
        }

        // Calculate statistics
        long total = 0;
        long max = 0;
        for (long latency : latencies) {
            total += latency;
            if (latency > max) max = latency;
        }
        long avg = total / latencies.length;

        System.out.println("Average operation time: " + avg / 1_000 + " microseconds");
        System.out.println("Max operation time: " + max / 1_000 + " microseconds");
        System.out.println("Note: Spikes may indicate safepoint pauses");
        System.out.println("Use -XX:+PrintGCApplicationStoppedTime to monitor");
    }

    /**
     * Demonstrates monitoring safepoints with JVM flags.
     * Useful flags for debugging safepoint issues:
     * -XX:+PrintGCApplicationStoppedTime
     * -XX:+PrintSafepointStatistics
     * -XX:SafepointTimeout=5000
     * -XX:+UnlockDiagnosticVMOptions
     * -XX:+LogCompilation
     */
    public static void safepointMonitoringInfo() {
        System.out.println("\n=== Safepoint Monitoring Commands ===");
        System.out.println("JVM flags to monitor safepoints:");
        System.out.println("  -XX:+PrintGCApplicationStoppedTime");
        System.out.println("  -XX:+PrintSafepointStatistics");
        System.out.println("  -XX:PrintSafepointStatisticsCount=1");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions");
        System.out.println("  -XX:GCSafepointDelay=10");
        System.out.println();
        System.out.println("Log file analysis:");
        System.out.println("  Look for 'safepoint' entries in GC logs");
        System.out.println("  Total time for safepoint operations");
        System.out.println("  Spin times and block times");
    }

    /**
     * Main entry point demonstrating all safepoint concepts.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("JVM Safepoints Deep Dive");
        System.out.println("========================");

        // Basic safepoint demonstration
        longRunningLoop();

        // Tight loop comparison
        tightLoop();

        // Multi-threaded safepoint synchronization
        multiThreadedSafepoint();

        // Thread states at safepoints
        threadStatesAtSafepoint();

        // Latency impact demonstration
        safepointLatencyImpact();

        // Monitoring information
        safepointMonitoringInfo();

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("1. Safepoints ensure safe JVM operations (GC, deopt)");
        System.out.println("2. Method calls and back edges are safepoint locations");
        System.out.println("3. All threads must reach safepoint before GC proceeds");
        System.out.println("4. Long loops without calls can delay safepoints");
        System.out.println("5. Monitor with -XX:+PrintGCApplicationStoppedTime");
        System.out.println("6. Safepoint delays cause latency spikes");
    }
}
