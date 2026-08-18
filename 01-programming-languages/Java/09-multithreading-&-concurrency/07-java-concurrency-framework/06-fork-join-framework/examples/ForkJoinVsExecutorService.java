package academy.javaengineering.concurrency.framework.forkjoin;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Compare ForkJoinPool and ExecutorService:
 * - Same problem solved both ways
 * - Performance comparison
 */
public class ForkJoinVsExecutorService {

    // ==================== ForkJoinPool Approach ====================

    static class ForkJoinSumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 100_000;
        private final long[] array;
        private final int start, end;

        ForkJoinSumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int size = end - start;
            if (size <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) sum += array[i];
                return sum;
            }
            int mid = start + size / 2;
            ForkJoinSumTask left = new ForkJoinSumTask(array, start, mid);
            ForkJoinSumTask right = new ForkJoinSumTask(array, mid, end);
            left.fork();
            return right.compute() + left.join();
        }
    }

    // ==================== ExecutorService Approach ====================

    static class ExecutorChunkSum implements Callable<Long> {
        private final long[] array;
        private final int start, end;

        ExecutorChunkSum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = start; i < end; i++) sum += array[i];
            return sum;
        }
    }

    // ==================== Parallel Sort Comparison ====================

    static class ForkJoinMergeSort extends RecursiveAction {
        private static final int THRESHOLD = 50_000;
        private final int[] array;
        private final int start, end;
        private final int[] temp;

        ForkJoinMergeSort(int[] array, int start, int end, int[] temp) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.temp = temp;
        }

        @Override
        protected void compute() {
            int size = end - start;
            if (size <= THRESHOLD) {
                java.util.Arrays.sort(array, start, end);
                return;
            }
            int mid = start + size / 2;
            invokeAll(
                    new ForkJoinMergeSort(array, start, mid, temp),
                    new ForkJoinMergeSort(array, mid, end, temp)
            );
            merge(array, start, mid, end, temp);
        }

        private static void merge(int[] arr, int start, int mid, int end, int[] temp) {
            System.arraycopy(arr, start, temp, start, end - start);
            int i = start, j = mid, k = start;
            while (i < mid && j < end) {
                if (temp[i] <= temp[j]) {
                    arr[k++] = temp[i++];
                } else {
                    arr[k++] = temp[j++];
                }
            }
            while (i < mid) arr[k++] = temp[i++];
            while (j < end) arr[k++] = temp[j++];
        }
    }

    static class ExecutorMergeSort implements Runnable {
        private final int[] array;
        private final int start, end;
        private final int[] temp;
        private final ExecutorService executor;
        private final CountDownLatch latch;

        ExecutorMergeSort(int[] array, int start, int end, int[] temp,
                          ExecutorService executor, CountDownLatch latch) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.temp = temp;
            this.executor = executor;
            this.latch = latch;
        }

        @Override
        public void run() {
            int size = end - start;
            if (size <= 50_000) {
                java.util.Arrays.sort(array, start, end);
                latch.countDown();
                return;
            }
            int mid = start + size / 2;
            CountDownLatch childLatch = new CountDownLatch(2);
            executor.execute(new ExecutorMergeSort(array, start, mid, temp, executor, childLatch));
            executor.execute(new ExecutorMergeSort(array, mid, end, temp, executor, childLatch));
            try {
                childLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            merge(array, start, mid, end, temp);
            latch.countDown();
        }

        private static void merge(int[] arr, int start, int mid, int end, int[] temp) {
            System.arraycopy(arr, start, temp, start, end - start);
            int i = start, j = mid, k = start;
            while (i < mid && j < end) {
                if (temp[i] <= temp[j]) {
                    arr[k++] = temp[i++];
                } else {
                    arr[k++] = temp[j++];
                }
            }
            while (i < mid) arr[k++] = temp[i++];
            while (j < end) arr[k++] = temp[j++];
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== ForkJoinPool vs ExecutorService Comparison ===\n");

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.printf("Available cores: %d%n%n", cores);

        // --- Test 1: Parallel Sum ---
        System.out.println("--- Test 1: Parallel Sum ---");
        int size = 100_000_000;
        long[] sumArray = new long[size];
        for (int i = 0; i < size; i++) sumArray[i] = i + 1;
        long expectedSum = ((long) size * (size + 1)) / 2;

        // ForkJoinPool
        ForkJoinPool fjPool = new ForkJoinPool(cores);
        long start = System.nanoTime();
        long fjSum = fjPool.invoke(new ForkJoinSumTask(sumArray, 0, sumArray.length));
        long fjTime = System.nanoTime() - start;
        fjPool.shutdown();

        // ExecutorService
        ExecutorService execPool = Executors.newFixedThreadPool(cores);
        int chunkSize = size / cores;
        start = System.nanoTime();
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            int s = i * chunkSize;
            int e = (i == cores - 1) ? size : s + chunkSize;
            futures.add(execPool.submit(new ExecutorChunkSum(sumArray, s, e)));
        }
        long execSum = 0;
        for (Future<Long> f : futures) execSum += f.get();
        long execTime = System.nanoTime() - start;
        execPool.shutdown();

        System.out.printf("Array size: %,d%n", size);
        System.out.printf("ForkJoinPool:    %,d — %.2f ms%n", fjSum, fjTime / 1_000_000.0);
        System.out.printf("ExecutorService: %,d — %.2f ms%n", execSum, execTime / 1_000_000.0);
        System.out.printf("Results match: %b%n", fjSum == expectedSum && execSum == expectedSum);
        System.out.printf("Speedup: %.2fx%n%n", (double) execTime / Math.max(fjTime, 1));

        // --- Test 2: Parallel Sort ---
        System.out.println("--- Test 2: Parallel Merge Sort ---");
        int sortSize = 20_000_000;
        int[] fjSortArray = new int[sortSize];
        int[] execSortArray = new int[sortSize];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < sortSize; i++) {
            fjSortArray[i] = rand.nextInt(sortSize * 10);
            execSortArray[i] = fjSortArray[i];
        }

        // ForkJoinPool sort
        ForkJoinPool fjSortPool = new ForkJoinPool(cores);
        int[] fjTemp = new int[sortSize];
        start = System.nanoTime();
        fjSortPool.invoke(new ForkJoinMergeSort(fjSortArray, 0, sortSize, fjTemp));
        long fjSortTime = System.nanoTime() - start;
        fjSortPool.shutdown();

        // ExecutorService sort
        ExecutorService execSortPool = Executors.newFixedThreadPool(cores);
        int[] execTemp = new int[sortSize];
        CountDownLatch sortLatch = new CountDownLatch(1);
        start = System.nanoTime();
        execSortPool.execute(new ExecutorMergeSort(execSortArray, 0, sortSize, execTemp,
                execSortPool, sortLatch));
        sortLatch.await();
        long execSortTime = System.nanoTime() - start;
        execSortPool.shutdown();

        boolean fjSorted = isSorted(fjSortArray);
        boolean execSorted = isSorted(execSortArray);

        System.out.printf("Array size: %,d%n", sortSize);
        System.out.printf("ForkJoinPool:    %.2f ms — sorted: %b%n", fjSortTime / 1_000_000.0, fjSorted);
        System.out.printf("ExecutorService: %.2f ms — sorted: %b%n", execSortTime / 1_000_000.0, execSorted);
        System.out.printf("Speedup: %.2fx%n%n", (double) execSortTime / Math.max(fjSortTime, 1));

        // --- Summary ---
        System.out.println("--- Summary ---");
        System.out.println("ForkJoinPool advantages:");
        System.out.println("  - Work stealing balances load automatically");
        System.out.println("  - Better for recursive divide-and-conquer problems");
        System.out.println("  - No need to pre-partition work");
        System.out.println("  - Uses available threads more efficiently");
        System.out.println();
        System.out.println("ExecutorService advantages:");
        System.out.println("  - Simpler for independent, unrelated tasks");
        System.out.println("  - More flexible scheduling policies");
        System.out.println("  - Better for I/O-bound or mixed workloads");
        System.out.println("  - Easier to manage task lifecycle");
        System.out.println("\n=== Comparison complete ===");
    }

    private static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) return false;
        }
        return true;
    }
}
