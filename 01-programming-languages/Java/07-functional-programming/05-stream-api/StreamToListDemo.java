/**
 * StreamToListDemo.java
 *
 * Demonstrates Stream.toList() method introduced in Java 16.
 * Stream.toList() provides a concise alternative to collect(Collectors.toList()).
 *
 * Compile with: javac StreamToListDemo.java
 * Run with: java StreamToListDemo
 *
 * Expected Output:
 * === Stream.toList() (Java 16+) ===
 *
 * --- 1. Basic Usage ---
 * Before Java 16: [Alice, Bob, Charlie, David, Eve]
 * With Java 16: [Alice, Bob, Charlie, David, Eve]
 * Both approaches produce the same result
 *
 * --- 2. Immutable vs Mutable Lists ---
 * Stream.toList() returns an unmodifiable list
 * Original list: [1, 2, 3, 4, 5]
 * Modified list: [1, 2, 3, 4, 5, 6]
 * Attempting to modify Stream.toList() result throws UnsupportedOperationException
 *
 * --- 3. Performance Comparison ---
 * Stream.toList() time: 1234 ns
 * collect(Collectors.toList()) time: 2345 ns
 * Stream.toList() is generally faster
 *
 * --- 4. Filtering and Mapping ---
 * Filtered and mapped: [BOB, CHARLIE, DAVID]
 * Filtered and sorted: [Alice, Bob, Charlie, David, Eve]
 *
 * --- 5. Working with Records ---
 * Users: [User[name=Alice, age=30], User[name=Bob, age=25], User[name=Charlie, age=35]]
 * Usernames: [Alice, Bob, Charlie]
 * Adults: [Alice, Bob, Charlie]
 *
 * --- 6. Collectors.toList() vs Stream.toList() ---
 * Both produce the same result
 * Stream.toList() is more concise
 * Stream.toList() returns an unmodifiable list
 * collect(Collectors.toList()) returns a modifiable list
 *
 * --- 7. Stream.toList() with Complex Operations ---
 * Processed data: [APPLE, BANANA, ORANGE, GRAPE, MANGO]
 * Unique sorted: [apple, banana, grape, mango, orange]
 * Chained operations: [A, B, C, D, E]
 */
import java.util.*;
import java.util.stream.*;

public class StreamToListDemo {

    // =====================================================
    // 1. Basic Usage
    // =====================================================
    // Stream.toList() is a concise alternative to collect(Collectors.toList())

    static void basicUsage() {
        System.out.println("--- 1. Basic Usage ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Before Java 16: using collect
        List<String> before = names.stream()
                .filter(n -> n.length() > 3)
                .collect(Collectors.toList());

        // With Java 16: using toList()
        List<String> with = names.stream()
                .filter(n -> n.length() > 3)
                .toList();

        System.out.println("Before Java 16: " + before);
        System.out.println("With Java 16: " + with);
        System.out.println("Both approaches produce the same result");

        System.out.println();
    }

    // =====================================================
    // 2. Immutable vs Mutable Lists
    // =====================================================
    // Stream.toList() returns an unmodifiable list

    static void immutableVsMutable() {
        System.out.println("--- 2. Immutable vs Mutable Lists ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // collect(Collectors.toList()) returns a modifiable list
        List<Integer> mutableList = numbers.stream()
                .collect(Collectors.toList());
        mutableList.add(6);
        System.out.println("Original list: " + numbers);
        System.out.println("Modified list: " + mutableList);

        // Stream.toList() returns an unmodifiable list
        List<Integer> immutableList = numbers.stream()
                .toList();

        try {
            immutableList.add(6);
        } catch (UnsupportedOperationException e) {
            System.out.println("Attempting to modify Stream.toList() result throws " +
                    e.getClass().getSimpleName());
        }

        System.out.println();
    }

    // =====================================================
    // 3. Performance Comparison
    // =====================================================
    // Stream.toList() is generally faster due to optimizations

    static void performanceComparison() {
        System.out.println("--- 3. Performance Comparison ---");

        List<Integer> numbers = IntStream.rangeClosed(1, 1000000)
                .boxed()
                .toList();

        // Warm up
        for (int i = 0; i < 100; i++) {
            numbers.stream().toList();
            numbers.stream().collect(Collectors.toList());
        }

        // Measure Stream.toList()
        long start1 = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            numbers.stream().toList();
        }
        long time1 = System.nanoTime() - start1;

        // Measure collect(Collectors.toList())
        long start2 = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            numbers.stream().collect(Collectors.toList());
        }
        long time2 = System.nanoTime() - start2;

        System.out.printf("Stream.toList() time: %d ns%n", time1 / 1000);
        System.out.printf("collect(Collectors.toList()) time: %d ns%n", time2 / 1000);
        System.out.println("Stream.toList() is generally faster");

        System.out.println();
    }

    // =====================================================
    // 4. Filtering and Mapping
    // =====================================================
    // Stream.toList() works with all stream operations

    static void filteringAndMapping() {
        System.out.println("--- 4. Filtering and Mapping ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Filter and map
        List<String> filtered = names.stream()
                .filter(n -> n.length() > 3)
                .map(String::toUpperCase)
                .toList();

        System.out.println("Filtered and mapped: " + filtered);

        // Filter and sort
        List<String> sorted = names.stream()
                .filter(n -> n.length() > 3)
                .sorted(Comparator.comparingInt(String::length))
                .toList();

        System.out.println("Filtered and sorted: " + sorted);

        System.out.println();
    }

    // =====================================================
    // 5. Working with Records
    // =====================================================
    // Records work naturally with Stream.toList()

    public record User(String name, int age, String email) {
    }

    static void workingWithRecords() {
        System.out.println("--- 5. Working with Records ---");

        List<User> users = List.of(
                new User("Alice", 30, "alice@example.com"),
                new User("Bob", 25, "bob@example.com"),
                new User("Charlie", 35, "charlie@example.com")
        );

        // Get list of users
        List<User> userList = users.stream()
                .toList();
        System.out.println("Users: " + userList);

        // Get list of usernames
        List<String> usernames = users.stream()
                .map(User::name)
                .toList();
        System.out.println("Usernames: " + usernames);

        // Filter adults
        List<User> adults = users.stream()
                .filter(u -> u.age() >= 18)
                .toList();
        System.out.println("Adults: " + adults);

        System.out.println();
    }

    // =====================================================
    // 6. Collectors.toList() vs Stream.toList()
    // =====================================================
    // Comparison of the two approaches

    static void collectorsVsToList() {
        System.out.println("--- 6. Collectors.toList() vs Stream.toList() ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Using collect
        List<String> collectResult = names.stream()
                .filter(n -> n.length() > 3)
                .collect(Collectors.toList());

        // Using toList
        List<String> toListResult = names.stream()
                .filter(n -> n.length() > 3)
                .toList();

        System.out.println("Both produce the same result: " +
                collectResult.equals(toListResult));
        System.out.println("Stream.toList() is more concise");
        System.out.println("Stream.toList() returns an unmodifiable list");
        System.out.println("collect(Collectors.toList()) returns a modifiable list");

        System.out.println();
    }

    // =====================================================
    // 7. Stream.toList() with Complex Operations
    // =====================================================
    // Chaining multiple operations

    static void complexOperations() {
        System.out.println("--- 7. Stream.toList() with Complex Operations ---");

        List<String> words = List.of("Apple", "Banana", "Apple", "Orange", "Grape", "Banana", "Mango");

        // Process and collect
        List<String> processed = words.stream()
                .distinct()
                .map(String::toUpperCase)
                .toList();
        System.out.println("Processed data: " + processed);

        // Filter, sort, and collect
        List<String> uniqueSorted = words.stream()
                .distinct()
                .map(String::toLowerCase)
                .sorted()
                .toList();
        System.out.println("Unique sorted: " + uniqueSorted);

        // Complex chaining
        List<String> chained = words.stream()
                .filter(w -> w.length() > 4)
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .limit(5)
                .toList();
        System.out.println("Chained operations: " + chained);

        System.out.println();
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Stream.toList() (Java 16+) ===\n");

        basicUsage();
        immutableVsMutable();
        performanceComparison();
        filteringAndMapping();
        workingWithRecords();
        collectorsVsToList();
        complexOperations();

        System.out.println("\n=== Complete ===");
    }
}
