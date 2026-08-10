package sorting;

import java.util.*;
import java.util.stream.*;

/**
 * Sorting Operations Examples
 * Demonstrates sorted() and sorted(Comparator)
 */
public class SortingExample {

    record Person(String name, int age, String city) {}
    record Product(String name, String category, double price) {}

    public static void main(String[] args) {
        System.out.println("=== Sorting Operations ===\n");

        sortedNaturalExample();
        sortedWithComparatorExample();
        comparatorFactoryExample();
        practicalExamples();
    }

    // --- sorted() Examples ---

    static void sortedNaturalExample() {
        System.out.println("--- sorted() Natural Order ---");

        List<Integer> numbers = List.of(5, 3, 1, 4, 2);
        List<Integer> sorted = numbers.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Sorted numbers: " + sorted);

        List<String> names = List.of("Charlie", "Alice", "Bob");
        List<String> sortedNames = names.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Sorted names: " + sortedNames);

        System.out.println();
    }

    // --- sorted(Comparator) Examples ---

    static void sortedWithComparatorExample() {
        System.out.println("--- sorted(Comparator) Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        // Sort by length
        List<String> byLength = names.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        System.out.println("By length: " + byLength);

        // Sort by length descending
        List<String> byLengthDesc = names.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .collect(Collectors.toList());
        System.out.println("By length desc: " + byLengthDesc);

        // Sort by last character
        List<String> byLastChar = names.stream()
            .sorted(Comparator.comparingInt(name -> name.charAt(name.length() - 1)))
            .collect(Collectors.toList());
        System.out.println("By last char: " + byLastChar);

        // Case-insensitive sort
        List<String> mixed = List.of("alice", "Bob", "CHARLIE");
        List<String> caseInsensitive = mixed.stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());
        System.out.println("Case insensitive: " + caseInsensitive);

        System.out.println();
    }

    // --- Comparator Factory Methods ---

    static void comparatorFactoryExample() {
        System.out.println("--- Comparator Factory Methods ---");

        List<Person> people = List.of(
            new Person("Alice", 30, "New York"),
            new Person("Bob", 25, "Chicago"),
            new Person("Charlie", 35, "New York"),
            new Person("David", 28, "Chicago")
        );

        // Sort by age
        List<Person> byAge = people.stream()
            .sorted(Comparator.comparingInt(Person::age))
            .collect(Collectors.toList());
        System.out.println("By age: " + byAge);

        // Sort by age then name
        List<Person> byAgeThenName = people.stream()
            .sorted(Comparator.comparingInt(Person::age)
                .thenComparing(Person::name))
            .collect(Collectors.toList());
        System.out.println("By age then name: " + byAgeThenName);

        // Sort by city then age descending
        List<Person> byCityThenAgeDesc = people.stream()
            .sorted(Comparator.comparing(Person::city)
                .thenComparing(Comparator.comparingInt(Person::age).reversed()))
            .collect(Collectors.toList());
        System.out.println("By city then age desc: " + byCityThenAgeDesc);

        // Sort products
        List<Product> products = List.of(
            new Product("Laptop", "Electronics", 999.99),
            new Product("Phone", "Electronics", 699.99),
            new Product("Desk", "Furniture", 299.99),
            new Product("Chair", "Furniture", 199.99)
        );

        List<Product> byCategoryThenPrice = products.stream()
            .sorted(Comparator.comparing(Product::category)
                .thenComparingDouble(Product::price))
            .collect(Collectors.toList());
        System.out.println("Products by category then price: " + byCategoryThenPrice);

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Sort and limit
        List<Integer> numbers = List.of(5, 3, 1, 4, 2, 8, 6, 7);
        List<Integer> top3 = numbers.stream()
            .sorted()
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("Top 3: " + top3);

        // Example 2: Sort and skip
        List<Integer> skipFirst3 = numbers.stream()
            .sorted()
            .skip(3)
            .collect(Collectors.toList());
        System.out.println("Skip first 3: " + skipFirst3);

        // Example 3: Sort map by value
        Map<String, Integer> scores = Map.of(
            "Alice", 85,
            "Bob", 92,
            "Charlie", 78
        );
        List<Map.Entry<String, Integer>> sortedScores = scores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toList());
        System.out.println("Sorted scores: " + sortedScores);

        // Example 4: Sort strings by multiple criteria
        List<String> words = List.of("Java", "is", "awesome", "for", "streams");
        List<String> sortedWords = words.stream()
            .sorted(Comparator.comparingInt(String::length)
                .thenComparing(String::compareTo))
            .collect(Collectors.toList());
        System.out.println("Sorted words: " + sortedWords);

        System.out.println();
    }
}
