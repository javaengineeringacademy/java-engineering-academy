package academy.javaengineering.oop;

import academy.javaengineering.oop.classesandobjects.ClassesAndObjects;
import academy.javaengineering.oop.classesandobjects.ClassesAndObjects.Customer;
import academy.javaengineering.oop.classesandobjects.ClassesAndObjects.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassesAndObjects Tests")
class ClassesAndObjectsTest {

    @Test
    @DisplayName("Default constructor initializes with default values")
    void defaultConstructor() {
        Customer customer = new Customer();
        assertEquals(0L, customer.getId());
        assertEquals("Unknown", customer.getName());
        assertEquals("", customer.getEmail());
        assertEquals("STANDARD", customer.getTier());
    }

    @Test
    @DisplayName("Parameterized constructor sets all fields")
    void parameterizedConstructor() {
        Customer customer = new Customer(1001L, "Alice", "alice@test.com", "GOLD");
        assertEquals(1001L, customer.getId());
        assertEquals("Alice", customer.getName());
        assertEquals("alice@test.com", customer.getEmail());
        assertEquals("GOLD", customer.getTier());
    }

    @Test
    @DisplayName("Copy constructor creates independent copy")
    void copyConstructor() {
        Customer original = new Customer(1L, "Bob", "bob@test.com", "PLATINUM");
        Customer copy = new Customer(original);

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getName(), copy.getName());
        assertNotSame(original, copy);
    }

    @Test
    @DisplayName("Free shipping qualification based on tier")
    void freeShipping() {
        Customer gold = new Customer(1L, "A", "a@t.com", "GOLD");
        Customer platinum = new Customer(2L, "B", "b@t.com", "PLATINUM");
        Customer standard = new Customer(3L, "C", "c@t.com", "STANDARD");

        assertTrue(gold.qualifiesForFreeShipping());
        assertTrue(platinum.qualifiesForFreeShipping());
        assertFalse(standard.qualifiesForFreeShipping());
    }

    @Test
    @DisplayName("Equality based on ID")
    void equality() {
        Customer a = new Customer(1L, "Alice", "a@t.com", "GOLD");
        Customer b = new Customer(1L, "Bob", "b@t.com", "SILVER");
        Customer c = new Customer(2L, "Alice", "a@t.com", "GOLD");

        assertEquals(a, b, "Same ID should be equal");
        assertNotEquals(a, c, "Different ID should not be equal");
    }

    @Test
    @DisplayName("OrderItem line total calculation")
    void orderItemLineTotal() {
        OrderItem item = new OrderItem("SKU-1", "Widget", 3, 25.0);
        assertEquals(75.0, item.getLineTotal(), 0.001);
    }

    @Test
    @DisplayName("OrderItem immutability")
    void orderItemImmutability() {
        OrderItem item = new OrderItem("SKU-1", "Widget", 2, 10.0);
        assertEquals("SKU-1", item.getProductId());
        assertEquals("Widget", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(10.0, item.getUnitPrice(), 0.001);
    }
}
