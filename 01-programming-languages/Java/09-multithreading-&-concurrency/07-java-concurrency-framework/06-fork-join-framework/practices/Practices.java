package academy.javaengineering.concurrency.framework.forkjoin.practices;

import java.util.concurrent.*;
import java.util.Vector;

/**
 * Fork/Join Framework Exercises
 *
 * Exercise 1: Parallel Average
 *   - Calculate the average of a large double array using RecursiveTask<Double>
 *   - Use divide-and-conquer: compute sum in subtasks, divide by total count
 *
 * Exercise 2: Parallel Word Count
 *   - Count occurrences of a target word across multiple String arrays
 *   - Use RecursiveTask<Integer> with threshold-based splitting
 *
 * Exercise 3: Parallel Matrix Multiply
 *   - Multiply two large matrices in parallel using RecursiveAction
 *   - Split by rows, compute dot products for each element
 *
 * Instructions:
 *   - Complete each exercise by implementing the TODO sections
 *   - Each exercise has a main() method that tests your implementation
 *   - Run each main() method to verify your solution
 *   - Check solutions/Solutions.java for reference implementations
 */
public class Practices {

    // ==================== Exercise 1: Parallel Average ====================
    // TODO: Implement a RecursiveTask<Double> that computes the average of a double array
    // Requirements:
    //   - Use a threshold of 10,000 elements
    //   - For sub-threshold ranges, compute sum directly and divide by count
    //   - For larger ranges, split into two subtasks
    //   - Combine results: (leftSum + rightSum) / totalCount
    // Hint: Return the sum from each subtask, compute average in the main task

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
            // TODO: Implement this method
            // 1. If (end - start <= THRESHOLD): compute and return sum
            // 2. Otherwise: split, fork left, compute right, join left, return combined sum
            throw new UnsupportedOperationException("TODO: Implement ParallelAverageTask.compute()");
        }
    }

    // ==================== Exercise 2: Parallel Word Count ====================
    // TODO: Implement a RecursiveTask<Integer> that counts occurrences of a target word
    // Requirements:
    //   - Use a threshold of 1,000 elements per subtask
    //   - Count exact word matches (split by whitespace)
    //   - Combine results by adding subtask counts

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
            // TODO: Implement this method
            // 1. If small enough: iterate and count exact matches
            // 2. Otherwise: split, fork, compute, join, combine
            throw new UnsupportedOperationException("TODO: Implement WordCountTask.compute()");
        }
    }

    // ==================== Exercise 3: Parallel Matrix Multiply ====================
    // TODO: Implement a RecursiveAction that multiplies two matrices in parallel
    // Requirements:
    //   - Split work by rows (each subtask handles a range of rows)
    //   - Use a threshold of 100 rows
    //   - For each element C[i][j], compute dot product of row i and column j
    //   - No return value (RecursiveAction)

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
            // TODO: Implement this method
            // 1. If (rowEnd - rowStart <= THRESHOLD): compute rows directly
            // 2. Otherwise: split rows, invokeAll two subtasks
            throw new UnsupportedOperationException("TODO: Implement MatrixMultiplyTask.compute()");
        }
    }

    // ==================== Test Harness ====================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Fork/Join Framework Exercises ===\n");
        System.out.println("Complete the TODO sections in this file.");
        System.out.println("Run each exercise's main method to test.\n");

        // Exercise 1 test (will fail until implemented)
        System.out.println("--- Exercise 1: Parallel Average ---");
        try {
            double[] avgArray = new double[100_000];
            for (int i = 0; i < avgArray.length; i++) avgArray[i] = i + 1.0;
            ForkJoinPool pool = new ForkJoinPool();
            double avg = pool.invoke(new ParallelAverageTask(avgArray, 0, avgArray.length));
            double expected = (100_000.0 + 1.0) / 2.0;
            System.out.printf("Average: %.2f (expected: %.2f) — %s%n",
                    avg, expected, Math.abs(avg - expected) < 0.01 ? "PASS" : "FAIL");
            pool.shutdown();
        } catch (UnsupportedOperationException e) {
            System.out.println("Not yet implemented: " + e.getMessage());
        }

        // Exercise 2 test (will fail until implemented)
        System.out.println("\n--- Exercise 2: Parallel Word Count ---");
        try {
            String[] texts = new String[10_000];
            java.util.Arrays.fill(texts, "hello world java hello");
            String target = "hello";
            int expectedCount = 10_000 * 2;  // "hello" appears twice per string

            ForkJoinPool pool = new ForkJoinPool();
            int count = pool.invoke(new WordCountTask(texts, 0, texts.length, target));
            System.out.printf("Count: %d (expected: %d) — %s%n",
                    count, expectedCount, count == expectedCount ? "PASS" : "FAIL");
            pool.shutdown();
        } catch (UnsupportedOperationException e) {
            System.out.println("Not yet implemented: " + e.getMessage());
        }

        // Exercise 3 test (will fail until implemented)
        System.out.println("\n--- Exercise 3: Parallel Matrix Multiply ---");
        try {
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

            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new MatrixMultiplyTask(A, B, C, 0, n));

            // Verify a few elements
            double expected00 = 0;
            for (int k = 0; k < n; k++) expected00 += A[0][k] * B[k][0];
            System.out.printf("C[0][0]: %.2f (expected: %.2f) — %s%n",
                    C[0][0], expected00, Math.abs(C[0][0] - expected00) < 0.01 ? "PASS" : "FAIL");
            pool.shutdown();
        } catch (UnsupportedOperationException e) {
            System.out.println("Not yet implemented: " + e.getMessage());
        }

        System.out.println("\n=== Exercises complete ===");
    }
}
