package academy.javaengineering.testing.bdd.practices;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Step Definition Pattern
 *
 * Tasks:
 * 1. Create step definition methods
 * 2. Implement Given, When, Then methods
 * 3. Test shopping cart scenario
 */
class Exercise2StepDefinitions {

    static class ShoppingCart {
        private final List<String> items = new ArrayList<>();
        void addItem(String item) { items.add(item); }
        int getItemCount() { return items.size(); }
        void removeItem(String item) { items.remove(item); }
    }

    private ShoppingCart cart;

    // TODO: Implement step methods
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
        // Use step methods to test scenario
    }
}
