package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of ForkJoinPool and ForkJoinTask.
 */
public class ForkJoinPoolInternals {

    public static void main(String[] args) throws Exception {
        demonstrateWorkStealing();
        demonstrateRecursiveTask();
        demonstrateCommonPool();
    }

    static void demonstrateWorkStealing() throws Exception {
        System.out.println("=== Work-Stealing ===");

        ForkJoinPool pool = new ForkJoinPool(4);
        System.out.println("Parallelism: " + pool.getParallelism());
        System.out.println("Pool size: " + pool.getPoolSize());

        long sum = pool.invoke(new SumTask(1, 1_000_000));
        System.out.println("Sum: " + sum);

        System.out.println("Steal count: " + pool.getStealCount());
        System.out.println("Queued tasks: " + pool.getQueuedTaskCount());

        pool.shutdown();
    }

    static void demonstrateRecursiveTask() throws Exception {
        System.out.println("\n=== Recursive Task Splitting ===");

        ForkJoinPool pool = new ForkJoinPool();

        ForkJoinTask<Long> task = new SumTask(1, 100);
        Long result = pool.invoke(task);
        System.out.println("Sum 1..100 = " + result);

        pool.shutdown();
    }

    static void demonstrateCommonPool() {
        System.out.println("\n=== Common Pool ===");

        ForkJoinPool common = ForkJoinPool.commonPool();
        System.out.println("Common pool parallelism: " + common.getParallelism());

        // Parallel streams use the common pool
        long sum = java.util.stream.LongStream.rangeClosed(1, 1000)
            .parallel()
            .sum();
        System.out.println("Parallel stream sum: " + sum);
    }

    static class SumTask extends RecursiveTask<Long> {
        private final long start, end;
        private static final long THRESHOLD = 10_000;

        SumTask(long start, long end) {
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
            SumTask left = new SumTask(start, mid);
            SumTask right = new SumTask(mid + 1, end);

            left.fork(); // push onto current worker's deque
            long rightResult = right.compute(); // compute inline
            long leftResult = left.join(); // may steal from another worker

            return leftResult + rightResult;
        }
    }
}
