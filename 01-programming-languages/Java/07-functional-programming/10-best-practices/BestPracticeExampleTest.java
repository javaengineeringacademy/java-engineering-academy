import java.util.*;
import java.util.stream.*;

/**
 * Tests for BestPracticeExample.
 */
class BestPracticeExampleTest {

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but got false");
        }
    }

    static void testLambdaBestPractices() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");

        List<String> longNames = names.stream()
            .filter(s -> s.length() > 3)
            .toList();
        assertEquals(4, longNames.size());

        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        assertEquals("ALICE", upperNames.get(0));

        Predicate<String> isLong = s -> s.length() > 3;
        List<String> longNamesNamed = names.stream()
            .filter(isLong)
            .toList();
        assertEquals(4, longNamesNamed.size());
    }

    static void testStreamBestPractices() {
        record Product(String name, double price, int stock) {}

        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, 10),
            new Product("Phone", 699.99, 25),
            new Product("Tablet", 299.99, 5),
            new Product("Headphones", 199.99, 30)
        );

        List<String> affordableProducts = products.stream()
            .filter(p -> p.price() < 500)
            .map(Product::name)
            .toList();
        assertEquals(2, affordableProducts.size());
        assertTrue(affordableProducts.contains("Tablet"));
        assertTrue(affordableProducts.contains("Headphones"));
    }

    public static void main(String[] args) {
        testLambdaBestPractices();
        testStreamBestPractices();
        System.out.println("All BestPracticeExample tests passed!");
    }
}
