package academy.javaengineering.jvm.profiling;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Exercise 1: JMH Benchmark Exercise
 *
 * Task: Create a proper JMH-style benchmark comparing ArrayList vs LinkedList
 * for different operations. Since we don't have the JMH framework available,
 * simulate the benchmark methodology manually.
 *
 * Requirements:
 * 1. Benchmark add operations (beginning, middle, end)
 * 2. Benchmark get operations (by index)
 * 3. Benchmark contains operations (search)
 * 4. Use warmup iterations before measurement
 * 5. Calculate and display average times
 * 6. Use multiple iterations for statistical accuracy
 *
 * Run this class and observe the output. Compare results with the solution.
 */
public class ProfilingExercise1 {

    private static final int DATA_SIZE = 10_000;
    private static final int WARMUP_ITERATIONS = 100;
    private static final int MEASUREMENT_ITERATIONS = 500;

    public static void main(String[] args) {
        System.out.println("=== JMH-Style Benchmark Exercise ===\n");
        System.out.printf("Data size: %d, Warmup: %d, Measurement: %d%n%n",
                DATA_SIZE, WARMUP_ITERATIONS, MEASUREMENT_ITERATIONS);

        // TODO: Implement benchmarkAddToBeginning
        // TODO: Implement benchmarkAddToEnd
        // TODO: Implement benchmarkGetByIndex
        // TODO: Implement benchmarkContains
        // TODO: Compare ArrayList vs LinkedList for each operation

        System.out.println("Implement each benchmark method below.");
        System.out.println("Follow JMH methodology: warmup, then measure.");
    }

    /**
     * TODO: Benchmark adding elements to the beginning of a list.
     * Steps:
     * 1. Warmup phase: run WARMUP_ITERATIONS times without recording
     * 2. Measurement phase: run MEASUREMENT_ITERATIONS times, record time
     * 3. Calculate average time per operation
     * 4. Compare ArrayList vs LinkedList
     */
    static void benchmarkAddToBeginning() {
        // TODO: Implement this method
        // For ArrayList: list.add(0, element) - shifts all elements, O(n)
        // For LinkedList: list.addFirst(element) or list.add(0, element) - O(1)
        System.out.println("TODO: benchmarkAddToBeginning");
    }

    /**
     * TODO: Benchmark adding elements to the end of a list.
     */
    static void benchmarkAddToEnd() {
        // TODO: Implement this method
        // Both should be O(1) amortized
        System.out.println("TODO: benchmarkAddToEnd");
    }

    /**
     * TODO: Benchmark random access by index.
     */
    static void benchmarkGetByIndex() {
        // TODO: Implement this method
        // ArrayList: O(1) direct array access
        // LinkedList: O(n) must traverse from head/tail
        System.out.println("TODO: benchmarkGetByIndex");
    }

    /**
     * TODO: Benchmark searching for an element (contains).
     */
    static void benchmarkContains() {
        // TODO: Implement this method
        // Both are O(n) linear search
        // But ArrayList has better cache locality
        System.out.println("TODO: benchmarkContains");
    }

    // Helper methods you may need:
    static List<Integer> createArrayList(int size) {
        return IntStream.range(0, size).boxed().collect(Collectors.toList());
    }

    static void warmup(Runnable benchmark) {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            benchmark.run();
        }
    }

    static long measureAverageNanos(Runnable operation) {
        long totalNanos = 0;
        for (int i = 0; i < MEASUREMENT_ITERATIONS; i++) {
            long start = System.nanoTime();
            operation.run();
            totalNanos += System.nanoTime() - start;
        }
        return totalNanos / MEASUREMENT_ITERATIONS;
    }
}
