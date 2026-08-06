package academy.javaengineering.senior.concurrency;

import java.util.concurrent.*;

public class ForkJoinDemo {

    // ============================================================
    // 1. RecursiveTask - Returns a value
    // ============================================================

    public static class ParallelSum extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start;
        private final int end;

        public ParallelSum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = (start + end) / 2;
            ParallelSum left = new ParallelSum(array, start, mid);
            ParallelSum right = new ParallelSum(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    // ============================================================
    // 2. RecursiveAction - No return value
    // ============================================================

    public static class ParallelFill extends RecursiveAction {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start;
        private final int end;
        private final long value;

        public ParallelFill(long[] array, int start, int end, long value) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.value = value;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    array[i] = value;
                }
                return;
            }

            int mid = (start + end) / 2;
            invokeAll(
                new ParallelFill(array, start, mid, value),
                new ParallelFill(array, mid, end, value)
            );
        }
    }

    // ============================================================
    // 3. Parallel Merge Sort
    // ============================================================

    public static class ParallelMergeSort extends RecursiveAction {
        private static final int THRESHOLD = 5_000;
        private final int[] array;
        private final int[] temp;
        private final int start;
        private final int end;

        public ParallelMergeSort(int[] array, int[] temp, int start, int end) {
            this.array = array;
            this.temp = temp;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                java.util.Arrays.sort(array, start, end);
                return;
            }

            int mid = (start + end) / 2;
            ParallelMergeSort left = new ParallelMergeSort(array, temp, start, mid);
            ParallelMergeSort right = new ParallelMergeSort(array, temp, mid, end);

            left.fork();
            right.compute();
            left.join();

            merge(array, temp, start, mid, end);
        }

        private void merge(int[] arr, int[] tmp, int left, int mid, int right) {
            System.arraycopy(arr, left, tmp, left, right - left);
            int i = left, j = mid, k = left;
            while (i < mid && j < right) {
                arr[k++] = tmp[i] <= tmp[j] ? tmp[i++] : tmp[j++];
            }
            while (i < mid) arr[k++] = tmp[i++];
        }
    }

    // ============================================================
    // 4. Work-Stealing Demo
    // ============================================================

    public static void workStealingDemo() throws Exception {
        System.out.println("=== Work-Stealing Algorithm ===");

        ForkJoinPool pool = new ForkJoinPool(
            Runtime.getRuntime().availableProcessors(),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null,
            true
        );

        try {
            System.out.println("Parallelism: " + pool.getParallelism());
            System.out.println("Running threads: " + pool.getActiveThreadCount());
            System.out.println("Queued tasks: " + pool.getQueuedTaskCount());
        } finally {
            pool.shutdown();
        }
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. RecursiveTask: Parallel Sum ===");
        long[] array = new long[100_000];
        for (int i = 0; i < array.length; i++) array[i] = i + 1;

        ForkJoinPool pool = new ForkJoinPool();
        try {
            long sum = pool.invoke(new ParallelSum(array, 0, array.length));
            System.out.println("Parallel sum: " + sum);
            System.out.println("Expected: " + (100_000L * 100_001L / 2));
        } finally {
            pool.shutdown();
        }

        System.out.println("\n=== 2. RecursiveAction: Parallel Fill ===");
        long[] fillArray = new long[50_000];
        ForkJoinPool pool2 = new ForkJoinPool();
        try {
            pool2.invoke(new ParallelFill(fillArray, 0, fillArray.length, 42));
            System.out.println("All elements equal 42: " + (fillArray[0] == 42 && fillArray[49_999] == 42));
        } finally {
            pool2.shutdown();
        }

        System.out.println("\n=== 3. Parallel Merge Sort ===");
        int[] sortArray = new int[50_000];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < sortArray.length; i++) sortArray[i] = rand.nextInt(100_000);

        int[] temp = new int[sortArray.length];
        ForkJoinPool pool3 = new ForkJoinPool();
        try {
            long start = System.currentTimeMillis();
            pool3.invoke(new ParallelMergeSort(sortArray, temp, 0, sortArray.length));
            long elapsed = System.currentTimeMillis() - start;

            boolean sorted = true;
            for (int i = 1; i < sortArray.length; i++) {
                if (sortArray[i] < sortArray[i - 1]) { sorted = false; break; }
            }
            System.out.println("Array sorted: " + sorted);
            System.out.println("Elapsed: " + elapsed + "ms");
        } finally {
            pool3.shutdown();
        }

        workStealingDemo();
    }
}
