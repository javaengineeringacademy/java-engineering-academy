package academy.javaengineering.functional.operations;

import java.util.*;
import java.util.stream.*;

/**
 * Comprehensive examples of Stream Operations in Java 21.
 *
 * <p>This class demonstrates all aspects of stream operations including
 * intermediate and terminal operations, stateful and stateless operations,
 * and short-circuit operations. Each example is self-contained and can be
 * run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Intermediate operations (filter, map, flatMap, sorted, distinct, peek, limit, skip)</li>
 *   <li>Terminal operations (collect, reduce, forEach, count, min, max, findFirst, findAny)</li>
 *   <li>Short-circuit operations (anyMatch, allMatch, noneMatch)</li>
 *   <li>Stateful vs stateless operations</li>
 *   <li>Performance considerations</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class StreamOperationExamples {

    private StreamOperationExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates intermediate operations.
     */
    public static void intermediateOperations() {
        System.out.println("=== Intermediate Operations ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter
        List<Integer> even = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        System.out.println("Even: " + even);

        // map
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .toList();
        System.out.println("Squares: " + squares);

        // flatMap
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2),
            Arrays.asList(3, 4),
            Arrays.asList(5, 6)
        );
        List<Integer> flat = nested.stream()
            .flatMap(Collection::stream)
            .toList();
        System.out.println("Flat: " + flat);

        // distinct
        List<Integer> duplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
        List<Integer> unique = duplicates.stream()
            .distinct()
            .toList();
        System.out.println("Distinct: " + unique);

        // sorted
        List<Integer> unsorted = Arrays.asList(5, 3, 1, 4, 2);
        List<Integer> sorted = unsorted.stream()
            .sorted()
            .toList();
        System.out.println("Sorted: " + sorted);

        // limit and skip
        List<Integer> limited = numbers.stream()
            .limit(5)
            .toList();
        System.out.println("Limit 5: " + limited);

        List<Integer> skipped = numbers.stream()
            .skip(5)
            .toList();
        System.out.println("Skip 5: " + skipped);
    }

    /**
     * Demonstrates terminal operations.
     */
    public static void terminalOperations() {
        System.out.println("\n=== Terminal Operations ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // forEach
        System.out.print("forEach: ");
        numbers.stream()
            .limit(5)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // reduce
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // count
        long count = numbers.stream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Count > 5: " + count);

        // anyMatch, allMatch, noneMatch
        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);

        System.out.println("Any even: " + anyEven);
        System.out.println("All positive: " + allPositive);
        System.out.println("None negative: " + noneNegative);

        // findFirst, findAny
        Optional<Integer> first = numbers.stream()
            .filter(n -> n > 5)
            .findFirst();
        System.out.println("First > 5: " + first.orElse(-1));

        Optional<Integer> any = numbers.stream()
            .filter(n -> n > 5)
            .findAny();
        System.out.println("Any > 5: " + any.orElse(-1));

        // min, max
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        System.out.println("Min: " + min.orElse(-1));
        System.out.println("Max: " + max.orElse(-1));
    }

    /**
     * Demonstrates short-circuit operations.
     */
    public static void shortCircuitOperations() {
        System.out.println("\n=== Short-Circuit Operations ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // limit - stops after n elements
        System.out.println("Limit 3:");
        numbers.stream()
            .filter(n -> {
                System.out.println("  Processing: " + n);
                return n % 2 == 0;
            })
            .limit(3)
            .forEach(n -> System.out.println("  Found: " + n));

        // findFirst - stops on first match
        System.out.println("\nFindFirst > 5:");
        Optional<Integer> first = numbers.stream()
            .filter(n -> {
                System.out.println("  Checking: " + n);
                return n > 5;
            })
            .findFirst();
        System.out.println("  Result: " + first.orElse(-1));

        // anyMatch - stops on first match
        System.out.println("\nAnyMatch > 5:");
        boolean any = numbers.stream()
            .filter(n -> {
                System.out.println("  Checking: " + n);
                return n > 5;
            })
            .anyMatch(n -> true);
        System.out.println("  Result: " + any);
    }

    /**
     * Demonstrates performance considerations.
     */
    public static void performanceConsiderations() {
        System.out.println("\n=== Performance Considerations ===\n");

        List<Integer> numbers = IntStream.rangeClosed(1, 10_000_000).boxed().toList();

        // Sequential
        long start = System.nanoTime();
        long sumSeq = numbers.stream()
            .mapToLong(Integer::longValue)
            .sum();
        long seqTime = System.nanoTime() - start;

        // Parallel
        start = System.nanoTime();
        long sumPar = numbers.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();
        long parTime = System.nanoTime() - start;

        System.out.println("Sequential sum: " + sumSeq);
        System.out.println("Parallel sum: " + sumPar);
        System.out.printf("Sequential: %.2f ms%n", seqTime / 1_000_000.0);
        System.out.printf("Parallel: %.2f ms%n", parTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) seqTime / parTime);
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        intermediateOperations();
        terminalOperations();
        shortCircuitOperations();
        performanceConsiderations();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Intermediate operations are lazy");
        System.out.println("2. Terminal operations trigger processing");
        System.out.println("3. Short-circuit operations stop early");
        System.out.println("4. Use parallel for large datasets");
        System.out.println("5. Filter before map for better performance");
    }
}
