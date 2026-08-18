package academy.javaengineering.concurrency.framework.forkjoin;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates work stealing in ForkJoinPool:
 * - Multiple threads with different workload sizes
 * - Idle thread steals from busy thread's deque
 * - Performance comparison with regular thread pool
 */
public class WorkStealingExample {

    static final AtomicInteger tasksStolen = new AtomicInteger(0);

    static class UnevenTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 5000;
        private final long[] array;
        private final int start, end;
        private final String label;

        UnevenTask(long[] array, int start, int end, String label) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.label = label;
        }

        @Override
        protected Long compute() {
            int size = end - start;
            if (size <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                System.out.printf("  [%s] Thread %s computed sum for range [%d, %d) = %d%n",
                        label, Thread.currentThread().getName(), start, end, sum);
                return sum;
            }

            int mid = start + size / 2;
            UnevenTask left = new UnevenTask(array, start, mid, label + "-L");
            UnevenTask right = new UnevenTask(array, mid, end, label + "-R");

            // Fork left, compute right in current thread
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    static class StealingTracker extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10000;
        private final long[] array;
        private final int start, end;

        StealingTracker(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int size = end - start;
            if (size <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = start + size / 2;
            StealingTracker left = new StealingTracker(array, start, mid);
            StealingTracker right = new StealingTracker(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();
            tasksStolen.incrementAndGet();

            return leftResult + rightResult;
        }
    }

    static class HeavySumTask implements Callable<Long> {
        private final long[] array;
        private final int start, end;

        HeavySumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== ForkJoinPool Work Stealing Demo ===\n");

        // --- Demo 1: Uneven workloads across threads ---
        System.out.println("--- Demo 1: Uneven Workload Distribution ---");
        System.out.println("Creating tasks with varying sizes to demonstrate work stealing.\n");

        long[] unevenArray = new long[500_000];
        for (int i = 0; i < unevenArray.length; i++) {
            unevenArray[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long expectedSum = (500_000L * 500_001L) / 2;

        System.out.println("Pool parallelism: " + pool.getParallelism());
        System.out.println("Pool running threads: " + pool.getRunningThreadCount());
        System.out.println();

        long startTime = System.nanoTime();
        long fjResult = pool.invoke(new UnevenTask(unevenArray, 0, unevenArray.length, "ROOT"));
        long fjTime = System.nanoTime() - startTime;

        System.out.printf("%nForkJoinPool result: %d (expected: %d) — match: %b%n",
                fjResult, expectedSum, fjResult == expectedSum);
        System.out.printf("Time: %.2f ms%n%n", fjTime / 1_000_000.0);

        // Show pool statistics
        System.out.println("Pool statistics after execution:");
        System.out.println("  Steal count: " + pool.getStealCount());
        System.out.println("  Queued task count: " + pool.getQueuedTaskCount());
        System.out.println("  Running threads: " + pool.getRunningThreadCount());
        pool.shutdown();

        // --- Demo 2: Work stealing tracking ---
        System.out.println("\n--- Demo 2: Tracking Work Stealing ---");
        tasksStolen.set(0);

        long[] trackArray = new long[1_000_000];
        for (int i = 0; i < trackArray.length; i++) {
            trackArray[i] = i + 1;
        }

        ForkJoinPool trackPool = new ForkJoinPool();
        long trackExpected = (1_000_000L * 1_000_001L) / 2;

        long trackStart = System.nanoTime();
        long trackResult = trackPool.invoke(new StealingTracker(trackArray, 0, trackArray.length));
        long trackTime = System.nanoTime() - trackStart;

        System.out.printf("Result: %d (expected: %d) — match: %b%n",
                trackResult, trackExpected, trackResult == trackExpected);
        System.out.printf("Tasks forked/joined (approximate steal count): %d%n", tasksStolen.get());
        System.out.printf("Pool steal count: %d%n", trackPool.getStealCount());
        System.out.printf("Time: %.2f ms%n", trackTime / 1_000_000.0);
        trackPool.shutdown();

        // --- Demo 3: Performance comparison ---
        System.out.println("\n--- Demo 3: ForkJoinPool vs ExecutorService ---");

        long[] perfArray = new long[10_000_000];
        for (int i = 0; i < perfArray.length; i++) {
            perfArray[i] = i + 1;
        }
        long perfExpected = (10_000_000L * 10_000_001L) / 2;
        int cores = Runtime.getRuntime().availableProcessors();

        // ForkJoinPool
        ForkJoinPool fjPool = new ForkJoinPool(cores);
        long fjStart = System.nanoTime();
        long fjPerfResult = fjPool.invoke(new StealingTracker(perfArray, 0, perfArray.length));
        long fjTotal = System.nanoTime() - fjStart;
        fjPool.shutdown();

        // ExecutorService with fixed thread pool
        ExecutorService execPool = Executors.newFixedThreadPool(cores);
        int chunkSize = perfArray.length / cores;
        long execStart = System.nanoTime();
        java.util.List<Future<Long>> futures = new java.util.ArrayList<>();
        for (int i = 0; i < cores; i++) {
            int s = i * chunkSize;
            int e = (i == cores - 1) ? perfArray.length : s + chunkSize;
            futures.add(execPool.submit(new HeavySumTask(perfArray, s, e)));
        }
        long execResult = 0;
        for (Future<Long> f : futures) {
            execResult += f.get();
        }
        long execTotal = System.nanoTime() - execStart;
        execPool.shutdown();

        System.out.printf("Array size: %,d elements%n", perfArray.length);
        System.out.printf("Cores: %d%n%n", cores);
        System.out.printf("ForkJoinPool:   %,d — %.2f ms%n", fjPerfResult, fjTotal / 1_000_000.0);
        System.out.printf("ExecutorService: %,d — %.2f ms%n", execResult, execTotal / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) execTotal / fjTotal);
        System.out.printf("Results match expected: %b%n", fjPerfResult == perfExpected && execResult == perfExpected);

        System.out.println("\n=== Work stealing allows idle threads to dynamically balance load ===");
    }
}
