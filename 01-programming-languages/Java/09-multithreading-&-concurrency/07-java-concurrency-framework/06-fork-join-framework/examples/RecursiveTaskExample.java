package academy.javaengineering.concurrency.framework.forkjoin;

import java.util.concurrent.*;

/**
 * Complete RecursiveTask example:
 * - Parallel sum using divide-and-conquer
 * - Threshold-based splitting
 * - Demonstrates fork/join pattern
 */
public class RecursiveTaskExample {

    static class ParallelSumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start, end;

        ParallelSumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int size = end - start;

            // Base case: small enough to compute directly
            if (size <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            // Recursive case: split into two subtasks
            int mid = start + size / 2;
            ParallelSumTask left = new ParallelSumTask(array, start, mid);
            ParallelSumTask right = new ParallelSumTask(array, mid, end);

            // Fork left, compute right in current thread
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    static class ParallelMaxTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10_000;
        private final int[] array;
        private final int start, end;

        ParallelMaxTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                int max = Integer.MIN_VALUE;
                for (int i = start; i < end; i++) {
                    if (array[i] > max) max = array[i];
                }
                return max;
            }

            int mid = start + size / 2;
            ParallelMaxTask left = new ParallelMaxTask(array, start, mid);
            ParallelMaxTask right = new ParallelMaxTask(array, mid, end);

            left.fork();
            int rightMax = right.compute();
            int leftMax = left.join();

            return Math.max(leftMax, rightMax);
        }
    }

    static class ParallelCountTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start, end;
        private final long threshold;

        ParallelCountTask(long[] array, int start, int end, long threshold) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.threshold = threshold;
        }

        @Override
        protected Long compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                long count = 0;
                for (int i = start; i < end; i++) {
                    if (array[i] > threshold) count++;
                }
                return count;
            }

            int mid = start + size / 2;
            ParallelCountTask left = new ParallelCountTask(array, start, mid, threshold);
            ParallelCountTask right = new ParallelCountTask(array, mid, end, threshold);

            left.fork();
            long rightCount = right.compute();
            long leftCount = left.join();

            return leftCount + rightCount;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== RecursiveTask Examples ===\n");

        // --- Example 1: Parallel Sum ---
        System.out.println("--- Example 1: Parallel Sum ---");
        long[] sumArray = new long[1_000_000];
        for (int i = 0; i < sumArray.length; i++) {
            sumArray[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long expectedSum = (1_000_000L * 1_000_001L) / 2;

        long start = System.nanoTime();
        long parallelSum = pool.invoke(new ParallelSumTask(sumArray, 0, sumArray.length));
        long elapsed = System.nanoTime() - start;

        System.out.printf("Sum: %,d%n", parallelSum);
        System.out.printf("Expected: %,d%n", expectedSum);
        System.out.printf("Match: %b%n", parallelSum == expectedSum);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Example 2: Parallel Max ---
        System.out.println("--- Example 2: Parallel Max ---");
        int[] maxArray = new int[2_000_000];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < maxArray.length; i++) {
            maxArray[i] = rand.nextInt(10_000_000);
        }
        maxArray[1_500_000] = Integer.MAX_VALUE;  // known max

        start = System.nanoTime();
        int parallelMax = pool.invoke(new ParallelMaxTask(maxArray, 0, maxArray.length));
        elapsed = System.nanoTime() - start;

        System.out.printf("Max: %,d%n", parallelMax);
        System.out.printf("Expected: %d (Integer.MAX_VALUE)%n", Integer.MAX_VALUE);
        System.out.printf("Match: %b%n", parallelMax == Integer.MAX_VALUE);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Example 3: Parallel Count (elements above threshold) ---
        System.out.println("--- Example 3: Parallel Count Above Threshold ---");
        long[] countArray = new long[5_000_000];
        for (int i = 0; i < countArray.length; i++) {
            countArray[i] = i + 1;
        }
        long threshold = 4_000_000L;
        long expectedCount = countArray.length - threshold;  // 1,000,000

        start = System.nanoTime();
        long parallelCount = pool.invoke(new ParallelCountTask(countArray, 0, countArray.length, threshold));
        elapsed = System.nanoTime() - start;

        System.out.printf("Count above %,d: %,d%n", threshold, parallelCount);
        System.out.printf("Expected: %,d%n", expectedCount);
        System.out.printf("Match: %b%n", parallelCount == expectedCount);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Show ForkJoinPool stats ---
        System.out.println("--- ForkJoinPool Statistics ---");
        System.out.printf("Parallelism: %d%n", pool.getParallelism());
        System.out.printf("Steal count: %d%n", pool.getStealCount());
        System.out.printf("Queued tasks: %d%n", pool.getQueuedTaskCount());

        pool.shutdown();
        System.out.println("\n=== All RecursiveTask examples completed ===");
    }
}
