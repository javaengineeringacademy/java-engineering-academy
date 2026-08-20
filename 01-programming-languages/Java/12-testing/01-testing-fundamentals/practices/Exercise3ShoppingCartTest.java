package academy.javaengineering.testing.fundamentals.practices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 3: Write comprehensive tests for ShoppingCart.
 *
 * Tasks:
 * 1. Test empty cart state
 * 2. Test adding items
 * 3. Test removing items
 * 4. Test total calculation
 * 5. Test invalid operations (null item, negative quantity)
 * 6. Use @Nested to group tests by scenario
 */
class Product {
    private final String name;
    private final double price;

    Product(String name, double price) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.name = name;
        this.price = price;
    }

    String getName() { return name; }
    double getPrice() { return price; }
}

class CartItem {
    private final Product product;
    private int quantity;

    CartItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.product = product;
        this.quantity = quantity;
    }

    Product getProduct() { return product; }
    int getQuantity() { return quantity; }
    void setQuantity(int quantity) { this.quantity = quantity; }
    double getSubtotal() { return product.getPrice() * quantity; }
}

class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();

    void addItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    void removeItem(String productName) {
        items.removeIf(item -> item.getProduct().getName().equals(productName));
    }

    int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    boolean isEmpty() { return items.isEmpty(); }
    int getUniqueItemCount() { return items.size(); }
}

class Exercise3ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Nested
    @DisplayName("Empty cart behavior")
    class EmptyCartTests {
        // TODO: Test that empty cart has zero items
        // TODO: Test that empty cart has zero total
        // TODO: Test that isEmpty returns true
    }

    @Nested
    @DisplayName("Adding items")
    class AddItemTests {
        // TODO: Test adding a single item
        // TODO: Test adding multiple different items
        // TODO: Test adding same item twice increases quantity
        // TODO: Test adding with null product throws exception
        // TODO: Test adding with zero quantity throws exception
    }

    @Nested
    @DisplayName("Removing items")
    class RemoveItemTests {
        // TODO: Test removing existing item
        // TODO: Test removing non-existent item (no error)
        // TODO: Test cart is empty after removing all items
    }

    @Nested
    @DisplayName("Total calculation")
    class TotalTests {
        // TODO: Test total with one item
        // TODO: Test total with multiple items
        // TODO: Test total after removal updates correctly
    }
}
