package transforming;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Transforming Operations
 */
public class TransformingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Transforming Operations Tests ===\n");

        testMapToUppercase();
        testMapToLength();
        testMapEmptyStream();
        testFlatMapNestedLists();
        testFlatMapSplitStrings();
        testFlatMapEmptyNested();
        testMapToIntSum();
        testMapToIntAverage();
        testMapToIntMinMax();
        testIntStreamRange();
        testChainingTransformations();
        testFlatMapThenMap();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testMapToUppercase() {
        List<String> names = List.of("alice", "bob", "charlie");
        List<String> result = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        assertEqual("Map to uppercase", List.of("ALICE", "BOB", "CHARLIE"), result);
    }

    static void testMapToLength() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<Integer> result = names.stream()
            .map(String::length)
            .collect(Collectors.toList());
        assertEqual("Map to length", List.of(5, 3, 7), result);
    }

    static void testMapEmptyStream() {
        List<String> names = List.of();
        List<String> result = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        assertTrue("Map empty stream", result.isEmpty());
    }

    static void testFlatMapNestedLists() {
        List<List<Integer>> nested = List.of(
            List.of(1, 2),
            List.of(3, 4),
            List.of(5, 6)
        );
        List<Integer> result = nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assertEqual("FlatMap nested lists", List.of(1, 2, 3, 4, 5, 6), result);
    }

    static void testFlatMapSplitStrings() {
        List<String> sentences = List.of("Hello World", "Java Streams");
        List<String> result = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .collect(Collectors.toList());
        assertEqual("FlatMap split strings", List.of("Hello", "World", "Java", "Streams"), result);
    }

    static void testFlatMapEmptyNested() {
        List<List<Integer>> nested = List.of(
            List.of(),
            List.of(1, 2),
            List.of()
        );
        List<Integer> result = nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assertEqual("FlatMap with empty nested", List.of(1, 2), result);
    }

    static void testMapToIntSum() {
        List<String> numbers = List.of("1", "2", "3", "4", "5");
        int result = numbers.stream()
            .mapToInt(Integer::parseInt)
            .sum();
        assertEqual("MapToInt sum", 15, result);
    }

    static void testMapToIntAverage() {
        List<String> numbers = List.of("2", "4", "6");
        OptionalDouble result = numbers.stream()
            .mapToInt(Integer::parseInt)
            .average();
        assertTrue("MapToInt average", result.isPresent() && result.getAsDouble() == 4.0);
    }

    static void testMapToIntMinMax() {
        List<String> numbers = List.of("3", "1", "4", "1", "5");
        OptionalInt min = numbers.stream()
            .mapToInt(Integer::parseInt)
            .min();
        OptionalInt max = numbers.stream()
            .mapToInt(Integer::parseInt)
            .max();
        assertEqual("MapToInt min", 1, min.orElse(-1));
        assertEqual("MapToInt max", 5, max.orElse(-1));
    }

    static void testIntStreamRange() {
        List<Integer> result = IntStream.range(1, 5)
            .boxed()
            .collect(Collectors.toList());
        assertEqual("IntStream range", List.of(1, 2, 3, 4), result);
    }

    static void testChainingTransformations() {
        List<String> names = List.of("alice", "bob", "charlie");
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        assertEqual("Chaining transformations", List.of("ALICE", "CHARLIE"), result);
    }

    static void testFlatMapThenMap() {
        List<List<Integer>> nested = List.of(
            List.of(1, 2),
            List.of(3, 4)
        );
        List<Integer> result = nested.stream()
            .flatMap(Collection::stream)
            .map(n -> n * 2)
            .collect(Collectors.toList());
        assertEqual("FlatMap then Map", List.of(2, 4, 6, 8), result);
    }

    // --- Assertion Helpers ---

    static void assertEqual(String testName, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName);
            System.out.println("  Expected: " + expected);
            System.out.println("  Actual:   " + actual);
            failed++;
        }
    }

    static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("✓ " + testName);
            passed++;
        } else {
            System.out.println("✗ " + testName);
            System.out.println("  Expected: true");
            System.out.println("  Actual:   false");
            failed++;
        }
    }
}
