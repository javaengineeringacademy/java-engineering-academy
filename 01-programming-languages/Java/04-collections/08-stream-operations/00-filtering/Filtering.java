package filtering;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Filtering Operations
 */
public class FilteringTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Filtering Operations Tests ===\n");

        testFilterEvenNumbers();
        testFilterStrings();
        testFilterEmptyResult();
        testFilterAllMatch();
        testDistinct();
        testDistinctWithObjects();
        testTakeWhile();
        testTakeWhileFirstFalse();
        testDropWhile();
        testDropWhileFirstFalse();
        testCombiningFilters();
        testFilterWithMethodReference();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testFilterEvenNumbers() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        assertEqual("Filter even numbers", List.of(2, 4, 6), result);
    }

    static void testFilterStrings() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");
        List<String> result = names.stream()
            .filter(name -> name.length() > 4)
            .collect(Collectors.toList());
        assertEqual("Filter strings by length", List.of("Alice", "Charlie", "David"), result);
    }

    static void testFilterEmptyResult() {
        List<Integer> numbers = List.of(1, 3, 5, 7);
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        assertTrue("Filter returns empty when no match", result.isEmpty());
    }

    static void testFilterAllMatch() {
        List<Integer> numbers = List.of(2, 4, 6, 8);
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        assertEqual("Filter all match", List.of(2, 4, 6, 8), result);
    }

    static void testDistinct() {
        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3);
        List<Integer> result = numbers.stream()
            .distinct()
            .collect(Collectors.toList());
        assertEqual("Distinct integers", List.of(1, 2, 3), result);
    }

    static void testDistinctWithObjects() {
        List<String> names = List.of("Alice", "Bob", "Alice", "Charlie");
        List<String> result = names.stream()
            .distinct()
            .collect(Collectors.toList());
        assertEqual("Distinct strings", List.of("Alice", "Bob", "Charlie"), result);
    }

    static void testTakeWhile() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2);
        List<Integer> result = numbers.stream()
            .takeWhile(n -> n < 4)
            .collect(Collectors.toList());
        assertEqual("Take while < 4", List.of(1, 2, 3), result);
    }

    static void testTakeWhileFirstFalse() {
        List<Integer> numbers = List.of(5, 4, 3, 2, 1);
        List<Integer> result = numbers.stream()
            .takeWhile(n -> n < 4)
            .collect(Collectors.toList());
        assertTrue("Take while first false", result.isEmpty());
    }

    static void testDropWhile() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2);
        List<Integer> result = numbers.stream()
            .dropWhile(n -> n < 4)
            .collect(Collectors.toList());
        assertEqual("Drop while < 4", List.of(4, 5, 1, 2), result);
    }

    static void testDropWhileFirstFalse() {
        List<Integer> numbers = List.of(5, 4, 3, 2, 1);
        List<Integer> result = numbers.stream()
            .dropWhile(n -> n < 4)
            .collect(Collectors.toList());
        assertEqual("Drop while first false", List.of(5, 4, 3, 2, 1), result);
    }

    static void testCombiningFilters() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .filter(n -> n > 5)
            .collect(Collectors.toList());
        assertEqual("Combining filters", List.of(6, 8, 10), result);
    }

    static void testFilterWithMethodReference() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = names.stream()
            .filter(String::isEmpty)
            .collect(Collectors.toList());
        assertTrue("Filter with method reference", result.isEmpty());
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
