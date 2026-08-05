import java.util.*;
import java.util.stream.*;

/**
 * Comprehensive examples of the Stream API in Java 21.
 *
 * <p>This class demonstrates all aspects of the Stream API including
 * stream creation, intermediate operations, terminal operations, and
 * parallel processing. Each example is self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Stream creation from various sources</li>
 *   <li>Intermediate operations (filter, map, flatMap, sorted, distinct)</li>
 *   <li>Terminal operations (collect, reduce, forEach, count)</li>
 *   <li>Parallel streams</li>
 *   <li>Stream debugging</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class StreamExample {

    private StreamExample() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates stream creation methods.
     */
    public static void streamCreation() {
        System.out.println("=== Stream Creation ===\n");

        // From Collection
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        Stream<String> listStream = list.stream();
        System.out.println("From list: " + listStream.toList());

        // From Array
        int[] array = {1, 2, 3, 4, 5};
        IntStream arrayStream = Arrays.stream(array);
        System.out.println("From array: " + arrayStream.boxed().toList());

        // From Values
        Stream<String> valueStream = Stream.of("x", "y", "z");
        System.out.println("From values: " + valueStream.toList());

        // From Range
        IntStream rangeStream = IntStream.range(0, 5);
        System.out.println("Range: " + rangeStream.boxed().toList());

        IntStream rangeClosedStream = IntStream.rangeClosed(1, 5);
        System.out.println("Range closed: " + rangeClosedStream.boxed().toList());

        // From Generator
        Stream<Double> generatorStream = Stream.generate(Math::random).limit(3);
        System.out.println("Generator: " + generatorStream.toList());

        // From Iterator
        Stream<Integer> iteratorStream = Stream.iterate(0, n -> n + 2).limit(5);
        System.out.println("Iterator: " + iteratorStream.toList());
    }

    /**
     * Demonstrates intermediate operations.
     */
    public static void intermediateOperations() {
        System.out.println("\n=== Intermediate Operations ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter
        List<Integer> even = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        System.out.println("Even: " + even);

        // Map
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .toList();
        System.out.println("Squares: " + squares);

        // FlatMap
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2),
            Arrays.asList(3, 4),
            Arrays.asList(5, 6)
        );
        List<Integer> flat = nested.stream()
            .flatMap(Collection::stream)
            .toList();
        System.out.println("Flat: " + flat);

        // Distinct
        List<Integer> duplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
        List<Integer> unique = duplicates.stream()
            .distinct()
            .toList();
        System.out.println("Distinct: " + unique);

        // Sorted
        List<Integer> unsorted = Arrays.asList(5, 3, 1, 4, 2);
        List<Integer> sorted = unsorted.stream()
            .sorted()
            .toList();
        System.out.println("Sorted: " + sorted);

        // Limit and Skip
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
     * Demonstrates parallel streams.
     */
    public static void parallelStreams() {
        System.out.println("\n=== Parallel Streams ===\n");

        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().toList();

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

        // Thread information
        System.out.println("\nParallel stream threads:");
        IntStream.rangeClosed(1, 8)
            .parallel()
            .forEach(n -> 
                System.out.println("  " + n + " on " + Thread.currentThread().getName()));
    }

    /**
     * Demonstrates stream debugging.
     */
    public static void streamDebugging() {
        System.out.println("\n=== Stream Debugging ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Using peek for debugging
        List<Integer> result = numbers.stream()
            .filter(n -> {
                boolean keep = n % 2 == 0;
                System.out.println("Filter " + n + ": " + keep);
                return keep;
            })
            .map(n -> {
                int mapped = n * n;
                System.out.println("Map " + n + " -> " + mapped);
                return mapped;
            })
            .toList();

        System.out.println("Result: " + result);
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        streamCreation();
        intermediateOperations();
        terminalOperations();
        parallelStreams();
        streamDebugging();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Streams are created from collections, arrays, or values");
        System.out.println("2. Intermediate operations are lazy");
        System.out.println("3. Terminal operations trigger processing");
        System.out.println("4. Parallel streams use ForkJoinPool");
        System.out.println("5. Use peek() for debugging");
    }
}
