package academy.javaengineering.functional.bestpractices;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Comprehensive examples of Functional Programming Best Practices in Java 21.
 *
 * <p>This class demonstrates best practices for writing clean, maintainable,
 * and performant functional code. Each example shows both good and bad patterns.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Lambda best practices</li>
 *   <li>Stream best practices</li>
 *   <li>Composition patterns</li>
 *   <li>Performance optimization</li>
 *   <li>Error handling</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class BestPracticeExamples {

    private BestPracticeExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates lambda best practices.
     */
    public static void lambdaBestPractices() {
        System.out.println("=== Lambda Best Practices ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");

        // GOOD: Short lambda
        List<String> longNames = names.stream()
            .filter(s -> s.length() > 3)
            .toList();
        System.out.println("Long names: " + longNames);

        // GOOD: Method reference
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("Uppercase: " + upperNames);

        // GOOD: Named predicate
        Predicate<String> isLong = s -> s.length() > 3;
        List<String> longNamesNamed = names.stream()
            .filter(isLong)
            .toList();
        System.out.println("Long names (named): " + longNamesNamed);

        // BAD: Overly complex lambda
        List<String> complexResult = names.stream()
            .filter(s -> {
                if (s == null) return false;
                if (s.isEmpty()) return false;
                if (s.length() < 3) return false;
                return true;
            })
            .toList();
        System.out.println("Complex: " + complexResult);
    }

    /**
     * Demonstrates stream best practices.
     */
    public static void streamBestPractices() {
        System.out.println("\n=== Stream Best Practices ===\n");

        record Product(String name, double price, int stock) {}

        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, 10),
            new Product("Phone", 699.99, 25),
            new Product("Tablet", 299.99, 5),
            new Product("Headphones", 199.99, 30)
        );

        // GOOD: Filter before map
        List<String> affordableProducts = products.stream()
            .filter(p -> p.price() < 500)
            .map(Product::name)
            .toList();
        System.out.println("Affordable: " + affordableProducts);

        // GOOD: Use toList()
        List<String> allNames = products.stream()
            .map(Product::name)
            .toList();
        System.out.println("All names: " + allNames);

        // GOOD: Method references
        List<Product> inStock = products.stream()
            .filter(p -> p.stock() > 0)
            .toList();
        System.out.println("In stock: " + inStock.size());
    }

    /**
     * Demonstrates composition best practices.
     */
    public static void compositionBestPractices() {
        System.out.println("\n=== Composition Best Practices ===\n");

        // GOOD: Named functions
        UnaryOperator<String> trim = String::trim;
        UnaryOperator<String> toLower = String::toLowerCase;
        UnaryOperator<String> removeSpecial = s -> s.replaceAll("[^a-z0-9\\s]", "");
        UnaryOperator<String> normalizeSpaces = s -> s.replaceAll("\\s+", "_");

        Function<String, String> pipeline = trim
            .andThen(toLower)
            .andThen(removeSpecial)
            .andThen(normalizeSpaces);

        List<String> inputs = Arrays.asList("  Hello, World!  ", "  Java 8  ", "  Lambda  ");
        List<String> processed = inputs.stream()
            .map(pipeline)
            .toList();
        System.out.println("Processed: " + processed);

        // GOOD: Cache composed functions
        Function<String, String> cachedPipeline = Function.<String>identity()
            .andThen(String::trim)
            .andThen(String::toLowerCase)
            .andThen(s -> s.replaceAll("\\s+", "_"));
    }

    /**
     * Demonstrates performance best practices.
     */
    public static void performanceBestPractices() {
        System.out.println("\n=== Performance Best Practices ===\n");

        List<Integer> numbers = IntStream.rangeClosed(1, 1000).boxed().toList();

        // GOOD: Filter before map
        long start = System.nanoTime();
        List<Integer> result1 = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .toList();
        long filterFirst = System.nanoTime() - start;

        // BAD: Map before filter
        start = System.nanoTime();
        List<Integer> result2 = numbers.stream()
            .map(n -> n * n)
            .filter(n -> n % 2 == 0)
            .toList();
        long mapFirst = System.nanoTime() - start;

        System.out.println("Filter first: " + filterFirst / 1_000_000.0 + " ms");
        System.out.println("Map first: " + mapFirst / 1_000_000.0 + " ms");
        System.out.println("Speedup: " + (double) mapFirst / filterFirst + "x");
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        lambdaBestPractices();
        streamBestPractices();
        compositionBestPractices();
        performanceBestPractices();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Keep lambdas short and focused");
        System.out.println("2. Use method references when possible");
        System.out.println("3. Filter before map");
        System.out.println("4. Use toList() over collect(Collectors.toList())");
        System.out.println("5. Cache composed functions");
    }
}
