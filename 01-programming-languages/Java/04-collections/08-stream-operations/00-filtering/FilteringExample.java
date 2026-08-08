package filtering;

import java.util.*;
import java.util.stream.*;

/**
 * Filtering Operations Examples
 * Demonstrates filter(), distinct(), takeWhile(), dropWhile()
 */
public class FilteringExample {

    // Sample data class
    record Person(String name, int age, String city, String role) {}

    public static void main(String[] args) {
        System.out.println("=== Filtering Operations ===\n");

        filterExample();
        distinctExample();
        takeWhileExample();
        dropWhileExample();
        combiningFiltersExample();
        practicalExamples();
    }

    // --- filter() Examples ---

    static void filterExample() {
        System.out.println("--- filter() Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter even numbers
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Evens: " + evens);

        // Filter numbers greater than 5
        List<Integer> greaterThan5 = numbers.stream()
            .filter(n -> n > 5)
            .collect(Collectors.toList());
        System.out.println("Greater than 5: " + greaterThan5);

        // Filter strings by length
        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");
        List<String> longNames = names.stream()
            .filter(name -> name.length() > 4)
            .collect(Collectors.toList());
        System.out.println("Names > 4 chars: " + longNames);

        // Method reference
        List<String> aNames = names.stream()
            .filter(name -> name.startsWith("A"))
            .collect(Collectors.toList());
        System.out.println("Names starting with A: " + aNames);

        System.out.println();
    }

    // --- distinct() Examples ---

    static void distinctExample() {
        System.out.println("--- distinct() Examples ---");

        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        List<Integer> unique = numbers.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Unique: " + unique);

        // Remove nulls
        List<String> names = List.of("Alice", null, "Bob", null, "Charlie");
        List<String> nonNull = names.stream()
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Non-null unique: " + nonNull);

        System.out.println();
    }

    // --- takeWhile() Examples ---

    static void takeWhileExample() {
        System.out.println("--- takeWhile() Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);
        List<Integer> taken = numbers.stream()
            .takeWhile(n -> n < 4)
            .collect(Collectors.toList());
        System.out.println("Take while < 4: " + taken);

        // With sorted data
        List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> takenSorted = sorted.stream()
            .takeWhile(n -> n <= 5)
            .collect(Collectors.toList());
        System.out.println("Take while <= 5 (sorted): " + takenSorted);

        System.out.println();
    }

    // --- dropWhile() Examples ---

    static void dropWhileExample() {
        System.out.println("--- dropWhile() Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);
        List<Integer> dropped = numbers.stream()
            .dropWhile(n -> n < 4)
            .collect(Collectors.toList());
        System.out.println("Drop while < 4: " + dropped);

        // With sorted data
        List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> droppedSorted = sorted.stream()
            .dropWhile(n -> n < 4)
            .collect(Collectors.toList());
        System.out.println("Drop while < 4 (sorted): " + droppedSorted);

        System.out.println();
    }

    // --- Combining Filters ---

    static void combiningFiltersExample() {
        System.out.println("--- Combining Filters ---");

        List<Person> people = List.of(
            new Person("Alice", 30, "New York", "Engineer"),
            new Person("Bob", 25, "San Francisco", "Designer"),
            new Person("Charlie", 35, "New York", "Engineer"),
            new Person("David", 28, "Chicago", "Manager"),
            new Person("Eve", 32, "San Francisco", "Designer")
        );

        // Engineers in New York
        List<Person> nyEngineers = people.stream()
            .filter(p -> p.role().equals("Engineer"))
            .filter(p -> p.city().equals("New York"))
            .collect(Collectors.toList());
        System.out.println("NY Engineers: " + nyEngineers);

        // People under 30 in San Francisco
        List<Person> youngSF = people.stream()
            .filter(p -> p.city().equals("San Francisco"))
            .filter(p -> p.age() < 30)
            .collect(Collectors.toList());
        System.out.println("Young SF residents: " + youngSF);

        // Combined predicate with and()
        List<Person> result = people.stream()
            .filter(p -> p.age() >= 25 && p.age() <= 30)
            .collect(Collectors.toList());
        System.out.println("Age 25-30: " + result);

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Filter and transform
        List<String> words = List.of("hello", "world", "java", "stream", "api", "programming");
        List<String> result = words.stream()
            .filter(w -> w.length() > 4)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Long words uppercase: " + result);

        // Example 2: Find first matching element
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Optional<Integer> firstEven = numbers.stream()
            .filter(n -> n % 2 == 0)
            .findFirst();
        System.out.println("First even: " + firstEven.orElse(-1));

        // Example 3: Check if any element matches
        boolean hasNegative = numbers.stream()
            .anyMatch(n -> n < 0);
        System.out.println("Has negative: " + hasNegative);

        // Example 4: Count filtered elements
        long count = numbers.stream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Numbers > 5: " + count);

        // Example 5: Remove duplicates and sort
        List<String> names = List.of("Charlie", "Alice", "Bob", "Alice", "Charlie");
        List<String> uniqueSorted = names.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Unique sorted: " + uniqueSorted);

        System.out.println();
    }
}
