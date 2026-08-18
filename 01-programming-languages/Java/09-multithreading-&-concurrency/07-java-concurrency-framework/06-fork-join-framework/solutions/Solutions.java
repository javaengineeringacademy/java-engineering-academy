package academy.javaengineering.concurrency.framework.forkjoin.solutions;

import java.util.concurrent.*;

/**
 * Complete solutions for all Fork/Join Framework exercises.
 */
public class Solutions {

    // ==================== Exercise 1: Parallel Average ====================

    static class ParallelAverageTask extends RecursiveTask<Double> {
        private static final int THRESHOLD = 10_000;
        private final double[] array;
        private final int start, end;

        ParallelAverageTask(double[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                double sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = start + size / 2;
            ParallelAverageTask left = new ParallelAverageTask(array, start, mid);
            ParallelAverageTask right = new ParallelAverageTask(array, mid, end);

            left.fork();
            double rightSum = right.compute();
            double leftSum = left.join();

            return leftSum + rightSum;
        }
    }

    // ==================== Exercise 2: Parallel Word Count ====================

    static class WordCountTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 1_000;
        private final String[] texts;
        private final int start, end;
        private final String targetWord;

        WordCountTask(String[] texts, int start, int end, String targetWord) {
            this.texts = texts;
            this.start = start;
            this.end = end;
            this.targetWord = targetWord;
        }

        @Override
        protected Integer compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                int count = 0;
                for (int i = start; i < end; i++) {
                    String[] words = texts[i].split("\\s+");
                    for (String word : words) {
                        if (word.equals(targetWord)) count++;
                    }
                }
                return count;
            }

            int mid = start + size / 2;
            WordCountTask left = new WordCountTask(texts, start, mid, targetWord);
            WordCountTask right = new WordCountTask(texts, mid, end, targetWord);

            left.fork();
            int rightCount = right.compute();
            int leftCount = left.join();

            return leftCount + rightCount;
        }
    }

    // ==================== Exercise 3: Parallel Matrix Multiply ====================

    static class MatrixMultiplyTask extends RecursiveAction {
        private static final int THRESHOLD = 100;
        private final double[][] A, B, C;
        private final int rowStart, rowEnd;

        MatrixMultiplyTask(double[][] A, double[][] B, double[][] C,
                           int rowStart, int rowEnd) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
        }

        @Override
        protected void compute() {
            int size = rowEnd - rowStart;

            if (size <= THRESHOLD) {
                int cols = B[0].length;
                int kSize = B.length;
                for (int i = rowStart; i < rowEnd; i++) {
                    for (int j = 0; j < cols; j++) {
                        double sum = 0;
                        for (int k = 0; k < kSize; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] = sum;
                    }
                }
                return;
            }

            int mid = rowStart + size / 2;
            invokeAll(
                    new MatrixMultiplyTask(A, B, C, rowStart, mid),
                    new MatrixMultiplyTask(A, B, C, mid, rowEnd)
            );
        }
    }

    // ==================== Test Harness ====================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Fork/Join Framework Solutions ===\n");

        // --- Solution 1: Parallel Average ---
        System.out.println("--- Solution 1: Parallel Average ---");
        double[] avgArray = new double[100_000];
        for (int i = 0; i < avgArray.length; i++) avgArray[i] = i + 1.0;

        ForkJoinPool pool = new ForkJoinPool();
        double sum = pool.invoke(new ParallelAverageTask(avgArray, 0, avgArray.length));
        double avg = sum / avgArray.length;
        double expectedAvg = (100_000.0 + 1.0) / 2.0;

        System.out.printf("Sum: %.0f%n", sum);
        System.out.printf("Average: %.2f%n", avg);
        System.out.printf("Expected: %.2f%n", expectedAvg);
        System.out.printf("Match: %b%n%n", Math.abs(avg - expectedAvg) < 0.01);

        // --- Solution 2: Parallel Word Count ---
        System.out.println("--- Solution 2: Parallel Word Count ---");
        String[] texts = new String[10_000];
        java.util.Arrays.fill(texts, "hello world java hello");
        String target = "hello";
        int expectedCount = 10_000 * 2;

        int count = pool.invoke(new WordCountTask(texts, 0, texts.length, target));
        System.out.printf("Target word: \"%s\"%n", target);
        System.out.printf("Count: %d%n", count);
        System.out.printf("Expected: %d%n", expectedCount);
        System.out.printf("Match: %b%n%n", count == expectedCount);

        // --- Solution 3: Parallel Matrix Multiply ---
        System.out.println("--- Solution 3: Parallel Matrix Multiply ---");
        int n = 500;
        double[][] A = new double[n][n];
        double[][] B = new double[n][n];
        double[][] C = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = i + j;
                B[i][j] = i - j;
            }
        }

        long start = System.nanoTime();
        pool.invoke(new MatrixMultiplyTask(A, B, C, 0, n));
        long elapsed = System.nanoTime() - start;

        // Verify
        double expected00 = 0;
        for (int k = 0; k < n; k++) expected00 += A[0][k] * B[k][0];

        double expected10 = 0;
        for (int k = 0; k < n; k++) expected10 += A[1][k] * B[k][0];

        System.out.printf("Matrix size: %dx%d%n", n, n);
        System.out.printf("C[0][0]: %.2f (expected: %.2f) — %s%n",
                C[0][0], expected00, Math.abs(C[0][0] - expected00) < 0.01 ? "PASS" : "FAIL");
        System.out.printf("C[1][0]: %.2f (expected: %.2f) — %s%n",
                C[1][0], expected10, Math.abs(C[1][0] - expected10) < 0.01 ? "PASS" : "FAIL");
        System.out.printf("Time: %.2f ms%n", elapsed / 1_000_000.0);

        pool.shutdown();
        System.out.println("\n=== All solutions verified ===");
    }
}
