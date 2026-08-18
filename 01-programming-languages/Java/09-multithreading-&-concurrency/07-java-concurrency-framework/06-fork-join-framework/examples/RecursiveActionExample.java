package academy.javaengineering.concurrency.framework.forkjoin;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Complete RecursiveAction example:
 * - Parallel array processing
 * - No return value pattern
 * - invokeAll() usage
 */
public class RecursiveActionExample {

    static class ParallelArrayMultiplier extends RecursiveAction {
        private static final int THRESHOLD = 10_000;
        private final double[] array;
        private final int start, end;
        private final double factor;

        ParallelArrayMultiplier(double[] array, int start, int end, double factor) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.factor = factor;
        }

        @Override
        protected void compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    array[i] *= factor;
                }
                return;
            }

            int mid = start + size / 2;
            invokeAll(
                    new ParallelArrayMultiplier(array, start, mid, factor),
                    new ParallelArrayMultiplier(array, mid, end, factor)
            );
        }
    }

    static class ParallelArrayNormalizer extends RecursiveAction {
        private static final int THRESHOLD = 10_000;
        private final double[] array;
        private final int start, end;
        private final double min, max;

        ParallelArrayNormalizer(double[] array, int start, int end, double min, double max) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.min = min;
            this.max = max;
        }

        @Override
        protected void compute() {
            int size = end - start;
            double range = max - min;

            if (size <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    array[i] = (array[i] - min) / range;
                }
                return;
            }

            int mid = start + size / 2;
            invokeAll(
                    new ParallelArrayNormalizer(array, start, mid, min, max),
                    new ParallelArrayNormalizer(array, mid, end, min, max)
            );
        }
    }

    static class ParallelArrayFill extends RecursiveAction {
        private static final int THRESHOLD = 10_000;
        private final int[] array;
        private final int start, end;
        private final java.util.function.IntUnaryOperator generator;

        ParallelArrayFill(int[] array, int start, int end, java.util.function.IntUnaryOperator generator) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.generator = generator;
        }

        @Override
        protected void compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    array[i] = generator.applyAsInt(i);
                }
                return;
            }

            int mid = start + size / 2;
            invokeAll(
                    new ParallelArrayFill(array, start, mid, generator),
                    new ParallelArrayFill(array, mid, end, generator)
            );
        }
    }

    static class ParallelArrayPrinter extends RecursiveAction {
        private static final int THRESHOLD = 100;
        private final int[] array;
        private final int start, end;
        private final StringBuilder sb;

        ParallelArrayPrinter(int[] array, int start, int end, StringBuilder sb) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.sb = sb;
        }

        @Override
        protected void compute() {
            int size = end - start;

            if (size <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    sb.append(array[i]).append(" ");
                }
                return;
            }

            int mid = start + size / 2;
            invokeAll(
                    new ParallelArrayPrinter(array, start, mid, sb),
                    new ParallelArrayPrinter(array, mid, end, sb)
            );
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== RecursiveAction Examples ===\n");

        // --- Example 1: Parallel Array Multiplication ---
        System.out.println("--- Example 1: Parallel Array Multiplication ---");
        double[] multiplyArray = new double[1_000_000];
        for (int i = 0; i < multiplyArray.length; i++) {
            multiplyArray[i] = i + 1.0;
        }

        ForkJoinPool pool = new ForkJoinPool();

        long start = System.nanoTime();
        pool.invoke(new ParallelArrayMultiplier(multiplyArray, 0, multiplyArray.length, 2.5));
        long elapsed = System.nanoTime() - start;

        System.out.printf("First 5 elements after multiply by 2.5: ");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%.1f ", multiplyArray[i]);
        }
        System.out.println();
        System.out.printf("Expected first element: %.1f%n", 1.0 * 2.5);
        System.out.printf("Expected last element: %.1f%n", 1_000_000.0 * 2.5);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Example 2: Parallel Array Normalization ---
        System.out.println("--- Example 2: Parallel Array Normalization (min-max scaling) ---");
        double[] normalizeArray = new double[2_000_000];
        for (int i = 0; i < normalizeArray.length; i++) {
            normalizeArray[i] = Math.sin(i * 0.001) * 100 + 50;  // range ~[-50, 150]
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double v : normalizeArray) {
            if (v < min) min = v;
            if (v > max) max = v;
        }
        System.out.printf("Original range: [%.2f, %.2f]%n", min, max);

        start = System.nanoTime();
        pool.invoke(new ParallelArrayNormalizer(normalizeArray, 0, normalizeArray.length, min, max));
        elapsed = System.nanoTime() - start;

        double normMin = Double.MAX_VALUE;
        double normMax = Double.MIN_VALUE;
        for (double v : normalizeArray) {
            if (v < normMin) normMin = v;
            if (v > normMax) normMax = v;
        }
        System.out.printf("Normalized range: [%.4f, %.4f]%n", normMin, normMax);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Example 3: Parallel Array Fill ---
        System.out.println("--- Example 3: Parallel Array Fill with Lambda ---");
        int[] fillArray = new int[5_000_000];

        start = System.nanoTime();
        pool.invoke(new ParallelArrayFill(fillArray, 0, fillArray.length, i -> i * i));
        elapsed = System.nanoTime() - start;

        System.out.printf("First 5 elements (i*i): ");
        for (int i = 0; i < 5; i++) {
            System.out.printf("%d ", fillArray[i]);
        }
        System.out.println();
        System.out.printf("Element at index 100: %d (expected: %d)%n", fillArray[100], 100 * 100);
        System.out.printf("Time: %.2f ms%n%n", elapsed / 1_000_000.0);

        // --- Example 4: Parallel Array Print ---
        System.out.println("--- Example 4: Parallel Array Print (StringBuilder) ---");
        int[] printArray = new int[200];
        for (int i = 0; i < printArray.length; i++) {
            printArray[i] = i + 1;
        }

        StringBuilder sb = new StringBuilder();
        start = System.nanoTime();
        pool.invoke(new ParallelArrayPrinter(printArray, 0, printArray.length, sb));
        elapsed = System.nanoTime() - start;

        String result = sb.toString().trim();
        String[] parts = result.split(" ");
        System.out.printf("Total elements printed: %d%n", parts.length);
        System.out.printf("First 10: %s%n", String.join(" ", Arrays.copyOfRange(parts, 0, 10)));
        System.out.printf("Last 10: %s%n", String.join(" ", Arrays.copyOfRange(parts, parts.length - 10, parts.length)));
        System.out.printf("Time: %.2f ms%n", elapsed / 1_000_000.0);

        pool.shutdown();
        System.out.println("\n=== All RecursiveAction examples completed ===");
    }
}
