package academy.javaengineering.oop.bank.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Customer class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class CustomerTest {

    @Test
    void shouldCreateCustomer() {
        Customer customer = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        
        assertEquals("C001", customer.getCustomerId());
        assertEquals("Alice", customer.getFirstName());
        assertEquals("Johnson", customer.getLastName());
        assertEquals("Alice Johnson", customer.getFullName());
        assertEquals("alice@email.com", customer.getEmail());
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        Customer c1 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        Customer c2 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        Customer c3 = new Customer("C002", "Bob", "Smith", "bob@email.com");
        
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(null));
    }

    @Test
    void shouldImplementHashCodeCorrectly() {
        Customer c1 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        Customer c2 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void shouldWorkInCollections() {
        Customer c1 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        Customer c2 = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        
        java.util.Set<Customer> set = new java.util.HashSet<>();
        set.add(c1);
        set.add(c2);
        
        assertEquals(1, set.size());
    }

    @Test
    void shouldReturnStringRepresentation() {
        Customer customer = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        String str = customer.toString();
        
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Johnson"));
    }
}