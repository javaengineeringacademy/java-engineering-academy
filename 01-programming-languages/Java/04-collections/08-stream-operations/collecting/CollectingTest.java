package collecting;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Collecting Operations
 */
public class CollectingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Collecting Operations Tests ===\n");

        testToList();
        testToSet();
        testToMap();
        testToMapWithMerge();
        testJoining();
        testJoiningWithDelimiter();
        testJoiningWithFixes();
        testGroupingBy();
        testGroupingByCounting();
        testPartitioningBy();
        testPartitioningByWithSumming();
        testSummarizingInt();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testToList() {
        List<Integer> numbers = List.of(1, 2, 3);
        List<Integer> result = numbers.stream()
            .collect(Collectors.toList());
        assertEqual("ToList", List.of(1, 2, 3), result);
    }

    static void testToSet() {
        List<Integer> numbers = List.of(1, 2, 2, 3, 3);
        Set<Integer> result = numbers.stream()
            .collect(Collectors.toSet());
        assertEqual("ToSet", Set.of(1, 2, 3), result);
    }

    static void testToMap() {
        List<String> names = List.of("Alice", "Bob");
        Map<String, Integer> result = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length
            ));
        Map<String, Integer> expected = Map.of("Alice", 5, "Bob", 3);
        assertEqual("ToMap", expected, result);
    }

    static void testToMapWithMerge() {
        List<String> names = List.of("Alice", "Bob", "Alice");
        Map<String, Integer> result = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length,
                (existing, replacement) -> existing
            ));
        Map<String, Integer> expected = Map.of("Alice", 5, "Bob", 3);
        assertEqual("ToMap with merge", expected, result);
    }

    static void testJoining() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String result = names.stream()
            .collect(Collectors.joining());
        assertEqual("Joining", "AliceBobCharlie", result);
    }

    static void testJoiningWithDelimiter() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String result = names.stream()
            .collect(Collectors.joining(", "));
        assertEqual("Joining with delimiter", "Alice, Bob, Charlie", result);
    }

    static void testJoiningWithFixes() {
        List<String> names = List.of("Alice", "Bob");
        String result = names.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        assertEqual("Joining with fixes", "[Alice, Bob]", result);
    }

    static void testGroupingBy() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");
        Map<Character, List<String>> result = names.stream()
            .collect(Collectors.groupingBy(name -> name.charAt(0)));
        Map<Character, List<String>> expected = Map.of(
            'A', List.of("Alice"),
            'B', List.of("Bob"),
            'C', List.of("Charlie"),
            'D', List.of("David")
        );
        assertEqual("GroupingBy", expected, result);
    }

    static void testGroupingByCounting() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");
        Map<Integer, Long> result = names.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.counting()
            ));
        Map<Integer, Long> expected = Map.of(3L, 2L, 5L, 2L, 7L, 1L);
        assertEqual("GroupingBy counting", expected, result);
    }

    static void testPartitioningBy() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        Map<Boolean, List<Integer>> result = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        Map<Boolean, List<Integer>> expected = Map.of(
            false, List.of(1, 3, 5),
            true, List.of(2, 4, 6)
        );
        assertEqual("PartitioningBy", expected, result);
    }

    static void testPartitioningByWithSumming() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        Map<Boolean, Integer> result = numbers.stream()
            .collect(Collectors.partitioningBy(
                n -> n % 2 == 0,
                Collectors.summingInt(Integer::intValue)
            ));
        Map<Boolean, Integer> expected = Map.of(false, 9, true, 12);
        assertEqual("PartitioningBy summing", expected, result);
    }

    static void testSummarizingInt() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        IntSummaryStatistics result = numbers.stream()
            .collect(Collectors.summarizingInt(Integer::intValue));
        assertTrue("SummarizingInt count", result.getCount() == 5);
        assertTrue("SummarizingInt sum", result.getSum() == 15);
        assertTrue("SummarizingInt min", result.getMin() == 1);
        assertTrue("SummarizingInt max", result.getMax() == 5);
        assertTrue("SummarizingInt avg", result.getAverage() == 3.0);
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
