package academy.javaengineering.concurrency.examples;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinExamples {

    public static void main(String[] args) throws Exception {
        example1_BasicForkJoin();
        example2_RecursiveTaskSum();
        example3_RecursiveActionMerge();
        example4_ForkJoinPoolParallelism();
        example5_WorkStealing();
    }

    // Example 1: Basic ForkJoinPool usage
    static void example1_BasicForkJoin() throws Exception {
        System.out.println("=== Example 1: Basic ForkJoinPool ===");

        ForkJoinPool pool = new ForkJoinPool();

        pool.submit(() -> {
            System.out.println("Running in ForkJoinPool on " +
                    Thread.currentThread().getName());
        });

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println("Common pool parallelism: " + commonPool.getParallelism());
        System.out.println("Common pool thread count: " + commonPool.getPoolSize());

        System.out.println();
    }

    // Example 2: RecursiveTask - parallel sum calculation
    static void example2_RecursiveTaskSum() throws Exception {
        System.out.println("=== Example 2: RecursiveTask (Parallel Sum) ===");

        long[] array = new long[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool(4);
        long result = pool.invoke(new SumTask(array, 0, array.length - 1));
        System.out.println("Parallel sum: " + result);
        System.out.println("Expected: " + (100 * 101 / 2));

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 3: RecursiveAction - parallel processing
    static void example3_RecursiveActionMerge() throws Exception {
        System.out.println("=== Example 3: RecursiveAction (Parallel Processing) ===");

        int[] array = {38, 27, 43, 3, 9, 82, 10, 55, 12, 44};
        System.out.println("Original array: " + java.util.Arrays.toString(array));

        ForkJoinPool pool = new ForkJoinPool(2);
        pool.invoke(new ParallelPrinter(array, 0, array.length - 1, 0));

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 4: ForkJoinPool parallelism settings
    static void example4_ForkJoinPoolParallelism() throws Exception {
        System.out.println("=== Example 4: ForkJoinPool Parallelism ===");

        ForkJoinPool customPool = new ForkJoinPool(8);
        System.out.println("Custom pool parallelism: " + customPool.getParallelism());
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

        System.out.println("Default common pool parallelism: " +
                ForkJoinPool.commonPool().getParallelism());

        Instant start = Instant.now();
        customPool.submit(() -> {
            for (int i = 0; i < 100; i++) {
                Math.sqrt(i);
            }
        });

        customPool.shutdown();
        customPool.awaitTermination(3, TimeUnit.SECONDS);
        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("Work completed in " + elapsed.toMillis() + "ms");

        System.out.println();
    }

    // Example 5: Work-stealing demonstration
    static void example5_WorkStealing() throws Exception {
        System.out.println("=== Example 5: Work-Stealing ===");

        ForkJoinPool pool = new ForkJoinPool(4);

        long result = pool.invoke(new FibonacciTask(10));
        System.out.println("Fibonacci(10) = " + result);

        System.out.println("Pool statistics:");
        System.out.println("Steal count: " + pool.getStealCount());
        System.out.println("Pool size: " + pool.getPoolSize());
        System.out.println("Queued tasks: " + pool.getQueuedTaskCount());

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // RecursiveTask implementation for sum
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10;
        private final long[] array;
        private final int start;
        private final int end;

        public SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start + 1;

            if (length <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = start + length / 2;
            SumTask left = new SumTask(array, start, mid - 1);
            SumTask right = new SumTask(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    // RecursiveAction for parallel printing
    static class ParallelPrinter extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;
        private final int depth;

        public ParallelPrinter(int[] array, int start, int end, int depth) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.depth = depth;
        }

        @Override
        protected void compute() {
            String indent = "  ".repeat(depth);

            if (end - start <= 2) {
                System.out.println(indent + "Leaf: [" + start + "-" + end + "] = " +
                        java.util.Arrays.toString(java.util.Arrays.copyOfRange(array, start, end + 1)));
                return;
            }

            int mid = (start + end) / 2;
            System.out.println(indent + "Fork: [" + start + "-" + end + "] split at " + mid);

            ParallelPrinter left = new ParallelPrinter(array, start, mid, depth + 1);
            ParallelPrinter right = new ParallelPrinter(array, mid + 1, end, depth + 1);

            invokeAll(left, right);
        }
    }

    // Fibonacci task for work-stealing demo
    static class FibonacciTask extends RecursiveTask<Long> {
        private final int n;

        public FibonacciTask(int n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n <= 1) return (long) n;

            FibonacciTask f1 = new FibonacciTask(n - 1);
            FibonacciTask f2 = new FibonacciTask(n - 2);

            f1.fork();
            long result = f2.compute() + f1.join();
            return result;
        }
    }
}
