package combining;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * Tests for Combining Operations
 */
public class CombiningTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Combining Operations Tests ===\n");

        testConcat();
        testConcatThree();
        testStreamOf();
        testStreamOfArray();
        testStreamEmpty();
        testIterate();
        testFibonacci();
        testGenerate();
        testGenerateWithState();
        testBuilder();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testConcat() {
        Stream<Integer> s1 = Stream.of(1, 2, 3);
        Stream<Integer> s2 = Stream.of(4, 5, 6);
        List<Integer> result = Stream.concat(s1, s2)
            .collect(Collectors.toList());
        assertEqual("Concat two streams", List.of(1, 2, 3, 4, 5, 6), result);
    }

    static void testConcatThree() {
        Stream<Integer> s1 = Stream.of(1, 2);
        Stream<Integer> s2 = Stream.of(3, 4);
        Stream<Integer> s3 = Stream.of(5, 6);
        List<Integer> result = Stream.concat(
            Stream.concat(s1, s2), s3
        ).collect(Collectors.toList());
        assertEqual("Concat three streams", List.of(1, 2, 3, 4, 5, 6), result);
    }

    static void testStreamOf() {
        List<String> result = Stream.of("Alice", "Bob", "Charlie")
            .collect(Collectors.toList());
        assertEqual("Stream.of values", List.of("Alice", "Bob", "Charlie"), result);
    }

    static void testStreamOfArray() {
        Integer[] arr = {1, 2, 3, 4, 5};
        List<Integer> result = Stream.of(arr)
            .collect(Collectors.toList());
        assertEqual("Stream.of array", List.of(1, 2, 3, 4, 5), result);
    }

    static void testStreamEmpty() {
        long result = Stream.empty().count();
        assertEqual("Stream.empty count", 0L, result);
    }

    static void testIterate() {
        List<Integer> result = Stream.iterate(1, n -> n * 2)
            .limit(5)
            .collect(Collectors.toList());
        assertEqual("Stream.iterate powers of 2", List.of(1, 2, 4, 8, 16), result);
    }

    static void testFibonacci() {
        List<Integer> result = Stream.iterate(
                new int[]{0, 1},
                fib -> new int[]{fib[1], fib[0] + fib[1]}
            )
            .limit(7)
            .map(fib -> fib[0])
            .collect(Collectors.toList());
        assertEqual("Fibonacci sequence", List.of(0, 1, 1, 2, 3, 5, 8), result);
    }

    static void testGenerate() {
        List<String> result = Stream.generate(() -> "X")
            .limit(5)
            .collect(Collectors.toList());
        assertEqual("Stream.generate constant", List.of("X", "X", "X", "X", "X"), result);
    }

    static void testGenerateWithState() {
        AtomicInteger counter = new AtomicInteger(0);
        List<Integer> result = Stream.generate(counter::incrementAndGet)
            .limit(5)
            .collect(Collectors.toList());
        assertEqual("Stream.generate with state", List.of(1, 2, 3, 4, 5), result);
    }

    static void testBuilder() {
        Stream<String> stream = Stream.<String>builder()
            .add("Alice")
            .add("Bob")
            .build();
        List<String> result = stream.collect(Collectors.toList());
        assertEqual("Builder pattern", List.of("Alice", "Bob"), result);
    }

    // --- Assertion Helpers ---

    static void assertEqual(String testName, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("  Expected: " + expected);
            System.out.println("  Actual:   " + actual);
            failed++;
        }
    }
}
