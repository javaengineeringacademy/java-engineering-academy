package transforming;

import java.util.*;
import java.util.stream.*;

/**
 * Transforming Operations Examples
 * Demonstrates map(), flatMap(), and primitive streams
 */
public class TransformingExample {

    record Employee(String name, List<String> skills) {}
    record Product(String name, double price) {}

    public static void main(String[] args) {
        System.out.println("=== Transforming Operations ===\n");

        mapExample();
        flatMapExample();
        primitiveStreamsExample();
        combiningExample();
        practicalExamples();
    }

    // --- map() Examples ---

    static void mapExample() {
        System.out.println("--- map() Examples ---");

        List<String> names = List.of("alice", "bob", "charlie", "david");

        // Transform to uppercase
        List<String> upper = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Uppercase: " + upper);

        // Transform to length
        List<Integer> lengths = names.stream()
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println("Lengths: " + lengths);

        // Transform with lambda
        List<String> greetings = names.stream()
            .map(name -> "Hello, " + name + "!")
            .collect(Collectors.toList());
        System.out.println("Greetings: " + greetings);

        // Method reference
        List<Integer> nameLengths = names.stream()
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println("Name lengths: " + nameLengths);

        System.out.println();
    }

    // --- flatMap() Examples ---

    static void flatMapExample() {
        System.out.println("--- flatMap() Examples ---");

        // Flatten nested lists
        List<List<Integer>> nested = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );
        List<Integer> flat = nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        System.out.println("Flattened: " + flat);

        // Split strings into characters
        List<String> words = List.of("Hello", "World");
        List<Character> chars = words.stream()
            .flatMap(word -> word.chars()
                .mapToObj(c -> (char) c))
            .collect(Collectors.toList());
        System.out.println("Characters: " + chars);

        // Split sentences into words
        List<String> sentences = List.of("Hello World", "Java Streams");
        List<String> words2 = sentences.stream()
            .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
            .collect(Collectors.toList());
        System.out.println("Words: " + words2);

        System.out.println();
    }

    // --- Primitive Streams Examples ---

    static void primitiveStreamsExample() {
        System.out.println("--- Primitive Streams ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        // Sum of lengths
        int totalLength = names.stream()
            .mapToInt(String::length)
            .sum();
        System.out.println("Total length: " + totalLength);

        // Average length
        OptionalDouble average = names.stream()
            .mapToInt(String::length)
            .average();
        System.out.println("Average length: " + average.orElse(0));

        // Min and max length
        OptionalInt minLength = names.stream()
            .mapToInt(String::length)
            .min();
        OptionalInt maxLength = names.stream()
            .mapToInt(String::length)
            .max();
        System.out.println("Min length: " + minLength.orElse(0));
        System.out.println("Max length: " + maxLength.orElse(0));

        // IntStream.range
        List<Integer> range = IntStream.range(1, 6)
            .boxed()
            .collect(Collectors.toList());
        System.out.println("Range 1-5: " + range);

        // IntStream.rangeClosed
        List<Integer> rangeClosed = IntStream.rangeClosed(1, 5)
            .boxed()
            .collect(Collectors.toList());
        System.out.println("RangeClosed 1-5: " + rangeClosed);

        // Statistics
        IntSummaryStatistics stats = names.stream()
            .mapToInt(String::length)
            .summaryStatistics();
        System.out.println("Stats: " + stats);

        System.out.println();
    }

    // --- Combining Transformations ---

    static void combiningExample() {
        System.out.println("--- Combining Transformations ---");

        List<String> names = List.of("alice", "bob", "charlie", "david", "eve");

        // Filter then transform
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .map(name -> name + "!")
            .collect(Collectors.toList());
        System.out.println("Filtered + transformed: " + result);

        // FlatMap then Map
        List<String> sentences = List.of("Hello World", "Java Streams");
        List<String> upperWords = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("FlatMap + Map: " + upperWords);

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Extract domains from emails
        List<String> emails = List.of(
            "alice@example.com",
            "bob@test.org",
            "charlie@example.com"
        );
        List<String> domains = emails.stream()
            .map(email -> email.split("@")[1])
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Domains: " + domains);

        // Example 2: Extract all skills from employees
        List<Employee> employees = List.of(
            new Employee("Alice", List.of("Java", "Python")),
            new Employee("Bob", List.of("JavaScript", "TypeScript"))
        );
        List<String> allSkills = employees.stream()
            .flatMap(emp -> emp.skills().stream())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("All skills: " + allSkills);

        // Example 3: Capitalize first letter
        List<String> words = List.of("hello", "world", "java");
        List<String> capitalized = words.stream()
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
            .collect(Collectors.toList());
        System.out.println("Capitalized: " + capitalized);

        // Example 4: Parse numbers and sum
        List<String> numbers = List.of("1", "2", "3", "4", "5");
        int sum = numbers.stream()
            .mapToInt(Integer::parseInt)
            .sum();
        System.out.println("Sum: " + sum);

        // Example 5: Create product list with formatted prices
        List<Product> products = List.of(
            new Product("Laptop", 999.99),
            new Product("Phone", 699.99),
            new Product("Tablet", 299.99)
        );
        List<String> productPrices = products.stream()
            .map(p -> String.format("%s: $%.2f", p.name(), p.price()))
            .collect(Collectors.toList());
        System.out.println("Products: " + productPrices);

        System.out.println();
    }
}
