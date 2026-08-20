package academy.javaengineering.testing.bdd.solutions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2StepDefinitionsSolution {

    static class ShoppingCart {
        private final List<String> items = new ArrayList<>();
        void addItem(String item) { items.add(item); }
        int getItemCount() { return items.size(); }
        void removeItem(String item) { items.remove(item); }
    }

    private ShoppingCart cart;

    void givenEmptyCart() {
        cart = new ShoppingCart();
    }

    void whenAddItem(String item) {
        cart.addItem(item);
    }

    void thenCartShouldHave(int expected) {
        assertEquals(expected, cart.getItemCount());
    }

    @Test
    void shouldAddItemToCart() {
        // Given
        givenEmptyCart();
        // When
        whenAddItem("Laptop");
        // Then
        thenCartShouldHave(1);
    }

    @Test
    void shouldAddMultipleItems() {
        givenEmptyCart();
        whenAddItem("Laptop");
        whenAddItem("Mouse");
        thenCartShouldHave(2);
    }
}
