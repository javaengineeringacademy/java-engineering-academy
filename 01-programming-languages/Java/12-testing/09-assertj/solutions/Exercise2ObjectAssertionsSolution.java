package academy.javaengineering.testing.assertj.solutions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Exercise2ObjectAssertionsSolution {

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
        assertThat(product.getName()).isEqualTo("Laptop");
        assertThat(product.getPrice()).isGreaterThan(500.0);
        assertThat(product.getCategory()).isEqualTo("Electronics");
    }

    @Test
    void shouldAssertExceptions() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Invalid input");
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid input");
    }
}
