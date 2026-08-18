package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in ForkJoinPool.
 */
public class ForkJoinPoolMemory {

    public static void main(String[] args) throws Exception {
        demonstrateWorkQueueMemory();
        demonstrateTaskObjectMemory();
        demonstrateCommonPoolMemory();
    }

    static void demonstrateWorkQueueMemory() throws Exception {
        System.out.println("=== Work Queue Memory ===");

        ForkJoinPool pool = new ForkJoinPool(2);

        // Each worker has its own deque (array-based, power-of-2 size)
        ForkJoinTask<Long> task = new MemorySumTask(1, 100000);
        Long result = pool.invoke(task);
        System.out.println("Result: " + result);
        System.out.println("Each worker's deque is lazily allocated");

        pool.shutdown();
    }

    static void demonstrateTaskObjectMemory() throws Exception {
        System.out.println("\n=== Task Object Memory ===");

        ForkJoinPool pool = new ForkJoinPool();

        // Recursive splitting creates a tree of task objects on the heap
        ForkJoinTask<Long> task = new MemorySumTask(1, 1000000);
        Long result = pool.invoke(task);
        System.out.println("Result: " + result);
        System.out.println("After join(), task objects become eligible for GC");

        pool.shutdown();
    }

    static void demonstrateCommonPoolMemory() throws Exception {
        System.out.println("\n=== Common Pool Memory ===");

        ForkJoinPool common = ForkJoinPool.commonPool();
        System.out.println("Common pool threads: " + common.getPoolSize());

        // Tasks submitted to common pool share its work queue
        CompletableFuture<Long> f1 = CompletableFuture.supplyAsync(() -> {
            return java.util.stream.LongStream.rangeClosed(1, 1000).sum();
        });
        CompletableFuture<Long> f2 = CompletableFuture.supplyAsync(() -> {
            return java.util.stream.LongStream.rangeClosed(1001, 2000).sum();
        });

        System.out.println("Sum 1..1000: " + f1.get());
        System.out.println("Sum 1001..2000: " + f2.get());
    }

    static class MemorySumTask extends RecursiveTask<Long> {
        private final long start, end;
        private static final long THRESHOLD = 10000;

        MemorySumTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (long i = start; i <= end; i++) sum += i;
                return sum;
            }

            long mid = (start + end) / 2;
            MemorySumTask left = new MemorySumTask(start, mid);
            MemorySumTask right = new MemorySumTask(mid + 1, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }
}
