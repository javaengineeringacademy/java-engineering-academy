package sorting;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Sorting Operations
 */
public class SortingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Sorting Operations Tests ===\n");

        testSortedNatural();
        testSortedEmpty();
        testSortedByLength();
        testSortedByLengthDesc();
        testSortedByComparator();
        testSortedMultipleCriteria();
        testSortedThenLimit();
        testSortedThenSkip();
        testSortedReverseOrder();
        testSortedCaseInsensitive();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testSortedNatural() {
        List<Integer> numbers = List.of(5, 3, 1, 4, 2);
        List<Integer> result = numbers.stream()
            .sorted()
            .collect(Collectors.toList());
        assertEqual("Sorted natural", List.of(1, 2, 3, 4, 5), result);
    }

    static void testSortedEmpty() {
        List<Integer> numbers = List.of();
        List<Integer> result = numbers.stream()
            .sorted()
            .collect(Collectors.toList());
        assertTrue("Sorted empty", result.isEmpty());
    }

    static void testSortedByLength() {
        List<String> names = List.of("Charlie", "Bob", "Alice");
        List<String> result = names.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        assertEqual("Sorted by length", List.of("Bob", "Alice", "Charlie"), result);
    }

    static void testSortedByLengthDesc() {
        List<String> names = List.of("Bob", "Alice", "Charlie");
        List<String> result = names.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .collect(Collectors.toList());
        assertEqual("Sorted by length desc", List.of("Charlie", "Alice", "Bob"), result);
    }

    static void testSortedByComparator() {
        List<Integer> numbers = List.of(3, 1, 4, 1, 5);
        List<Integer> result = numbers.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        assertEqual("Sorted reverse order", List.of(5, 4, 3, 1, 1), result);
    }

    static void testSortedMultipleCriteria() {
        record Person(String name, int age) {}
        List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 30)
        );
        List<Person> result = people.stream()
            .sorted(Comparator.comparingInt(Person::age)
                .thenComparing(Person::name))
            .collect(Collectors.toList());
        assertTrue("Sorted multiple criteria",
            result.get(0).name().equals("Bob") &&
            result.get(1).name().equals("Alice") &&
            result.get(2).name().equals("Charlie"));
    }

    static void testSortedThenLimit() {
        List<Integer> numbers = List.of(5, 3, 1, 4, 2);
        List<Integer> result = numbers.stream()
            .sorted()
            .limit(3)
            .collect(Collectors.toList());
        assertEqual("Sorted then limit", List.of(1, 2, 3), result);
    }

    static void testSortedThenSkip() {
        List<Integer> numbers = List.of(5, 3, 1, 4, 2);
        List<Integer> result = numbers.stream()
            .sorted()
            .skip(3)
            .collect(Collectors.toList());
        assertEqual("Sorted then skip", List.of(4, 5), result);
    }

    static void testSortedReverseOrder() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = names.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        assertEqual("Sorted reverse order", List.of("Charlie", "Bob", "Alice"), result);
    }

    static void testSortedCaseInsensitive() {
        List<String> names = List.of("alice", "Bob", "CHARLIE");
        List<String> result = names.stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());
        assertEqual("Sorted case insensitive", List.of("alice", "Bob", "CHARLIE"), result);
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
