package reducing;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Reducing Operations
 */
public class ReducingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Reducing Operations Tests ===\n");

        testReduceSum();
        testReduceProduct();
        testReduceMax();
        testReduceEmpty();
        testReduceWithIdentity();
        testReduceWithIdentityEmpty();
        testCount();
        testCountEmpty();
        testCountFiltered();
        testMin();
        testMax();
        testMinMaxWithComparator();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testReduceSum() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Optional<Integer> result = numbers.stream()
            .reduce(Integer::sum);
        assertTrue("Reduce sum", result.isPresent() && result.get() == 15);
    }

    static void testReduceProduct() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Optional<Integer> result = numbers.stream()
            .reduce((a, b) -> a * b);
        assertTrue("Reduce product", result.isPresent() && result.get() == 120);
    }

    static void testReduceMax() {
        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);
        Optional<Integer> result = numbers.stream()
            .reduce(Integer::max);
        assertTrue("Reduce max", result.isPresent() && result.get() == 9);
    }

    static void testReduceEmpty() {
        List<Integer> numbers = List.of();
        Optional<Integer> result = numbers.stream()
            .reduce(Integer::sum);
        assertTrue("Reduce empty stream", result.isEmpty());
    }

    static void testReduceWithIdentity() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int result = numbers.stream()
            .reduce(0, Integer::sum);
        assertEqual("Reduce with identity", 15, result);
    }

    static void testReduceWithIdentityEmpty() {
        List<Integer> numbers = List.of();
        int result = numbers.stream()
            .reduce(0, Integer::sum);
        assertEqual("Reduce with identity on empty", 0, result);
    }

    static void testCount() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        long result = names.stream()
            .count();
        assertEqual("Count", 3L, result);
    }

    static void testCountEmpty() {
        List<String> names = List.of();
        long result = names.stream()
            .count();
        assertEqual("Count empty", 0L, result);
    }

    static void testCountFiltered() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        long result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .count();
        assertEqual("Count filtered", 3L, result);
    }

    static void testMin() {
        List<Integer> numbers = List.of(3, 1, 4, 1, 5);
        Optional<Integer> result = numbers.stream()
            .min(Integer::compareTo);
        assertTrue("Min", result.isPresent() && result.get() == 1);
    }

    static void testMax() {
        List<Integer> numbers = List.of(3, 1, 4, 1, 5);
        Optional<Integer> result = numbers.stream()
            .max(Integer::compareTo);
        assertTrue("Max", result.isPresent() && result.get() == 5);
    }

    static void testMinMaxWithComparator() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Optional<String> shortest = names.stream()
            .min(Comparator.comparingInt(String::length));
        Optional<String> longest = names.stream()
            .max(Comparator.comparingInt(String::length));
        assertTrue("Min with comparator", shortest.isPresent() && shortest.get().equals("Bob"));
        assertTrue("Max with comparator", longest.isPresent() && longest.get().equals("Charlie"));
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
