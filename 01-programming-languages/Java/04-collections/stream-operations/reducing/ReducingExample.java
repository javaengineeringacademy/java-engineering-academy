package reducing;

import java.util.*;
import java.util.stream.*;

/**
 * Reducing Operations Examples
 * Demonstrates reduce(), count(), min(), max()
 */
public class ReducingExample {

    public static void main(String[] args) {
        System.out.println("=== Reducing Operations ===\n");

        reduceExample();
        reduceWithIdentityExample();
        countExample();
        minMaxExample();
        practicalExamples();
    }

    // --- reduce(BinaryOperator) Examples ---

    static void reduceExample() {
        System.out.println("--- reduce(BinaryOperator) Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sum using lambda
        Optional<Integer> sum = numbers.stream()
            .reduce((a, b) -> a + b);
        System.out.println("Sum (lambda): " + sum.orElse(0));

        // Sum using method reference
        Optional<Integer> sum2 = numbers.stream()
            .reduce(Integer::sum);
        System.out.println("Sum (method ref): " + sum2.orElse(0));

        // Max
        Optional<Integer> max = numbers.stream()
            .reduce(Integer::max);
        System.out.println("Max: " + max.orElse(-1));

        // Product
        Optional<Integer> product = numbers.stream()
            .reduce((a, b) -> a * b);
        System.out.println("Product: " + product.orElse(0));

        // Concatenate strings
        List<String> words = List.of("Hello", " ", "World");
        Optional<String> sentence = words.stream()
            .reduce(String::concat);
        System.out.println("Concatenated: " + sentence.orElse(""));

        System.out.println();
    }

    // --- reduce(identity, accumulator) Examples ---

    static void reduceWithIdentityExample() {
        System.out.println("--- reduce(identity, accumulator) Examples ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sum with identity
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum with identity: " + sum);

        // Product with identity
        int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println("Product with identity: " + product);

        // Concatenate with identity
        String joined = numbers.stream()
            .map(String::valueOf)
            .reduce("", String::concat);
        System.out.println("Joined: " + joined);

        // Join with separator
        String csv = numbers.stream()
            .map(String::valueOf)
            .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.println("CSV: " + csv);

        // Factorial using reduce
        int n = 5;
        int factorial = IntStream.rangeClosed(1, n)
            .reduce(1, (a, b) -> a * b);
        System.out.println("Factorial of " + n + ": " + factorial);

        System.out.println();
    }

    // --- count() Examples ---

    static void countExample() {
        System.out.println("--- count() Examples ---");

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Total count
        long count = names.stream()
            .count();
        System.out.println("Total count: " + count);

        // Count filtered
        long longNames = names.stream()
            .filter(name -> name.length() > 4)
            .count();
        System.out.println("Names > 4 chars: " + longNames);

        // Count in range
        long evenCount = IntStream.rangeClosed(1, 10)
            .filter(n -> n % 2 == 0)
            .count();
        System.out.println("Even numbers 1-10: " + evenCount);

        // Count with condition
        long startsWithA = names.stream()
            .filter(name -> name.startsWith("A"))
            .count();
        System.out.println("Names starting with A: " + startsWithA);

        System.out.println();
    }

    // --- min/max Examples ---

    static void minMaxExample() {
        System.out.println("--- min/max Examples ---");

        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

        // Min
        Optional<Integer> min = numbers.stream()
            .min(Integer::compareTo);
        System.out.println("Min: " + min.orElse(-1));

        // Max
        Optional<Integer> max = numbers.stream()
            .max(Integer::compareTo);
        System.out.println("Max: " + max.orElse(-1));

        // With Comparator.comparing
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");
        Optional<String> shortest = names.stream()
            .min(Comparator.comparingInt(String::length));
        System.out.println("Shortest name: " + shortest.orElse(""));

        Optional<String> longest = names.stream()
            .max(Comparator.comparingInt(String::length));
        System.out.println("Longest name: " + longest.orElse(""));

        // Custom object min/max
        record Product(String name, double price) {}
        List<Product> products = List.of(
            new Product("Laptop", 999.99),
            new Product("Phone", 699.99),
            new Product("Tablet", 299.99),
            new Product("Watch", 199.99)
        );

        Optional<Product> cheapest = products.stream()
            .min(Comparator.comparingDouble(Product::price));
        System.out.println("Cheapest: " + cheapest.orElse(null));

        Optional<Product> mostExpensive = products.stream()
            .max(Comparator.comparingDouble(Product::price));
        System.out.println("Most expensive: " + mostExpensive.orElse(null));

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Calculate average
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        OptionalDouble average = numbers.stream()
            .mapToInt(Integer::intValue)
            .average();
        System.out.println("Average: " + average.orElse(0));

        // Example 2: Sum of lengths
        List<String> words = List.of("Hello", "World", "Java");
        int totalLength = words.stream()
            .mapToInt(String::length)
            .sum();
        System.out.println("Total length: " + totalLength);

        // Example 3: Find longest word
        String longestWord = words.stream()
            .reduce("", (a, b) -> a.length() >= b.length() ? a : b);
        System.out.println("Longest word: " + longestWord);

        // Example 4: Matrix sum
        List<List<Integer>> matrix = List.of(
            List.of(1, 2, 3),
            List.of(4, 5, 6),
            List.of(7, 8, 9)
        );
        int matrixSum = matrix.stream()
            .flatMap(Collection::stream)
            .reduce(0, Integer::sum);
        System.out.println("Matrix sum: " + matrixSum);

        // Example 5: String statistics
        List<String> sentences = List.of(
            "Java is great",
            "Streams are powerful",
            "Lambda expressions simplify code"
        );
        long totalWords = sentences.stream()
            .map(s -> s.split(" ").length)
            .count();
        System.out.println("Total sentences: " + totalWords);

        Optional<String> longestSentence = sentences.stream()
            .max(Comparator.comparingInt(s -> s.split(" ").length));
        System.out.println("Longest sentence: " + longestSentence.orElse(""));

        System.out.println();
    }
}
