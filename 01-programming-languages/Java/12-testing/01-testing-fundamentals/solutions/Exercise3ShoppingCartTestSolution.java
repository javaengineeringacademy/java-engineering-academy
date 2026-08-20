package academy.javaengineering.testing.fundamentals.solutions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductSolution {
    private final String name;
    private final double price;

    ProductSolution(String name, double price) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.name = name;
        this.price = price;
    }

    String getName() { return name; }
    double getPrice() { return price; }
}

class CartItemSolution {
    private final ProductSolution product;
    private int quantity;

    CartItemSolution(ProductSolution product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    ProductSolution getProduct() { return product; }
    int getQuantity() { return quantity; }
    void setQuantity(int quantity) { this.quantity = quantity; }
    double getSubtotal() { return product.getPrice() * quantity; }
}

class ShoppingCartSolution {
    private final List<CartItemSolution> items = new ArrayList<>();

    void addItem(ProductSolution product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        for (CartItemSolution item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItemSolution(product, quantity));
    }

    void removeItem(String productName) {
        items.removeIf(item -> item.getProduct().getName().equals(productName));
    }

    int getItemCount() {
        return items.stream().mapToInt(CartItemSolution::getQuantity).sum();
    }

    double getTotal() {
        return items.stream().mapToDouble(CartItemSolution::getSubtotal).sum();
    }

    boolean isEmpty() { return items.isEmpty(); }
    int getUniqueItemCount() { return items.size(); }
}

class Exercise3ShoppingCartTestSolution {

    private ShoppingCartSolution cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCartSolution();
    }

    @Nested
    @DisplayName("Empty cart behavior")
    class EmptyCartTests {
        @Test
        @DisplayName("Empty cart has zero items")
        void shouldHaveZeroItems() {
            assertEquals(0, cart.getItemCount());
        }

        @Test
        @DisplayName("Empty cart has zero total")
        void shouldHaveZeroTotal() {
            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Empty cart reports as empty")
        void shouldBeEmpty() {
            assertTrue(cart.isEmpty());
        }
    }

    @Nested
    @DisplayName("Adding items")
    class AddItemTests {
        @Test
        @DisplayName("Adding a single item works")
        void shouldAddSingleItem() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            assertEquals(1, cart.getItemCount());
            assertEquals(999.99, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Adding multiple different items")
        void shouldAddMultipleDifferentItems() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            cart.addItem(new ProductSolution("Mouse", 29.99), 2);
            assertEquals(3, cart.getItemCount());
            assertEquals(1059.97, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Adding same item twice increases quantity")
        void shouldIncreaseQuantityForDuplicateItem() {
            cart.addItem(new ProductSolution("Mouse", 29.99), 1);
            cart.addItem(new ProductSolution("Mouse", 29.99), 2);
            assertEquals(3, cart.getItemCount());
            assertEquals(1, cart.getUniqueItemCount());
        }

        @Test
        @DisplayName("Adding null product throws exception")
        void shouldThrowOnNullProduct() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addItem(null, 1));
        }

        @Test
        @DisplayName("Adding zero quantity throws exception")
        void shouldThrowOnZeroQuantity() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addItem(new ProductSolution("Mouse", 29.99), 0));
        }
    }

    @Nested
    @DisplayName("Removing items")
    class RemoveItemTests {
        @Test
        @DisplayName("Removing existing item works")
        void shouldRemoveExistingItem() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            cart.removeItem("Laptop");
            assertTrue(cart.isEmpty());
        }

        @Test
        @DisplayName("Removing non-existent item does not throw")
        void shouldNotThrowOnRemovingNonExistent() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            assertDoesNotThrow(() -> cart.removeItem("Phone"));
            assertEquals(1, cart.getItemCount());
        }

        @Test
        @DisplayName("Cart is empty after removing all items")
        void shouldBeEmptyAfterRemovingAll() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            cart.addItem(new ProductSolution("Mouse", 29.99), 2);
            cart.removeItem("Laptop");
            cart.removeItem("Mouse");
            assertTrue(cart.isEmpty());
        }
    }

    @Nested
    @DisplayName("Total calculation")
    class TotalTests {
        @Test
        @DisplayName("Total with one item")
        void shouldCalculateTotalForOneItem() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            assertEquals(999.99, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Total with multiple items")
        void shouldCalculateTotalForMultipleItems() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            cart.addItem(new ProductSolution("Mouse", 29.99), 2);
            assertEquals(1059.97, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Total updates after removal")
        void shouldUpdateTotalAfterRemoval() {
            cart.addItem(new ProductSolution("Laptop", 999.99), 1);
            cart.addItem(new ProductSolution("Mouse", 29.99), 2);
            cart.removeItem("Laptop");
            assertEquals(59.98, cart.getTotal(), 0.001);
        }
    }
}
