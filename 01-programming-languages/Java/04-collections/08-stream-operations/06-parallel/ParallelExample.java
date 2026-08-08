package parallel;

import java.util.*;
import java.util.stream.*;

/**
 * Parallel Operations Examples
 * Demonstrates parallelStream(), .parallel(), .sequential()
 */
public class ParallelExample {

    public static void main(String[] args) {
        System.out.println("=== Parallel Operations ===\n");

        parallelStreamExample();
        parallelConvertExample();
        sequentialExample();
        performanceExample();
        parallelReduceExample();
        practicalExamples();
    }

    // --- parallelStream() Examples ---

    static void parallelStreamExample() {
        System.out.println("--- parallelStream() Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Parallel filter
        List<Integer> evens = numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Parallel evens: " + evens);

        // Check if parallel
        boolean isParallel = numbers.parallelStream().isParallel();
        System.out.println("Is parallel: " + isParallel);

        // Parallel count
        long count = numbers.parallelStream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Parallel count > 5: " + count);

        System.out.println();
    }

    // --- Convert to Parallel ---

    static void parallelConvertExample() {
        System.out.println("--- Convert to Parallel ---");

        // Convert existing stream
        Stream<Integer> stream = IntStream.range(1, 11).boxed();
        Stream<Integer> parallelStream = stream.parallel();

        List<Integer> result = parallelStream
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Converted parallel: " + result);

        System.out.println();
    }

    // --- Sequential ---

    static void sequentialExample() {
        System.out.println("--- Sequential Conversion ---");

        // Start parallel, convert to sequential
        List<Integer> result = IntStream.range(1, 11)
            .boxed()
            .parallel()
            .filter(n -> n % 2 == 0)
            .sequential()
            .collect(Collectors.toList());
        System.out.println("Sequential result: " + result);

        // Check state at different points
        Stream<Integer> stream = IntStream.range(1, 6).boxed();
        System.out.println("Initial parallel: " + stream.isParallel());

        Stream<Integer> parallel = stream.parallel();
        System.out.println("After parallel(): " + parallel.isParallel());

        Stream<Integer> seq = parallel.sequential();
        System.out.println("After sequential(): " + seq.isParallel());

        System.out.println();
    }

    // --- Performance Example ---

    static void performanceExample() {
        System.out.println("--- Performance Comparison ---");

        int size = 1_000_000;
        List<Integer> largeList = IntStream.rangeClosed(1, size)
            .boxed()
            .collect(Collectors.toList());

        // Sequential
        long start = System.nanoTime();
        long seqSum = largeList.stream()
            .mapToLong(Integer::longValue)
            .sum();
        long seqTime = System.nanoTime() - start;
        System.out.printf("Sequential sum: %d, Time: %d ms%n", seqSum, seqTime / 1_000_000);

        // Parallel
        start = System.nanoTime();
        long parSum = largeList.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();
        long parTime = System.nanoTime() - start;
        System.out.printf("Parallel sum: %d, Time: %d ms%n", parSum, parTime / 1_000_000);

        System.out.println();
    }

    // --- Parallel Reduce ---

    static void parallelReduceExample() {
        System.out.println("--- Parallel Reduce ---");

        // Parallel reduce with combiner
        int sum = IntStream.rangeClosed(1, 1000)
            .parallel()
            .reduce(0, Integer::sum, Integer::sum);
        System.out.println("Parallel sum: " + sum);

        // Parallel collect
        List<Integer> result = IntStream.rangeClosed(1, 100)
            .parallel()
            .boxed()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Parallel evens: " + result.size());

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Parallel processing large dataset
        List<Integer> largeList = IntStream.rangeClosed(1, 100_000)
            .boxed()
            .collect(Collectors.toList());

        long count = largeList.parallelStream()
            .filter(n -> n % 2 == 0)
            .count();
        System.out.println("Parallel even count: " + count);

        // Example 2: Parallel sum
        long sum = IntStream.rangeClosed(1, 10_000_000)
            .parallel()
            .sum();
        System.out.println("Parallel sum 1-10M: " + sum);

        // Example 3: Parallel map-reduce
        double result = DoubleStream.generate(Math::random)
            .limit(1_000_000)
            .parallel()
            .map(d -> d * d)
            .reduce(0.0, Double::sum);
        System.out.println("Parallel map-reduce: " + String.format("%.2f", result));

        // Example 4: Parallel collect with ordering
        List<String> items = IntStream.rangeClosed(1, 20)
            .parallel()
            .mapToObj(n -> "Item " + n)
            .collect(Collectors.toList());
        System.out.println("Parallel collect (size): " + items.size());

        System.out.println();
    }
}
