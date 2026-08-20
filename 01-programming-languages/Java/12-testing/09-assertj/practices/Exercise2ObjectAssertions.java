package academy.javaengineering.testing.assertj.practices;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Exercise 2: Object and Exception Assertions
 *
 * Tasks:
 * 1. Assert object properties using extracting()
 * 2. Assert exceptions with messages
 * 3. Assert nested objects
 */
class Exercise2ObjectAssertions {

    static class Product {
        private final String name;
        private final double price;
        private final String category;
        Product(String name, double price, String category) {
            this.name = name; this.price = price; this.category = category;
        }
        String getName() { return name; }
        double getPrice() { return price; }
        String getCategory() { return category; }
    }

    @Test
    void shouldAssertProductProperties() {
        Product product = new Product("Laptop", 999.99, "Electronics");
        // TODO: Assert name is "Laptop"
        // TODO: Assert price is greater than 500
        // TODO: Assert category is "Electronics"
    }

    @Test
    void shouldAssertExceptions() {
        // TODO: Assert IllegalArgumentException is thrown with "Invalid input"
    }
}
