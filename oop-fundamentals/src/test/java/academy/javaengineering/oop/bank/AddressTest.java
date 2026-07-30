package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testAddressCreation() {
        Address addr = new Address("123 Main St", "New York", "NY", "10001", "USA");
        
        assertEquals("123 Main St", addr.street());
        assertEquals("New York", addr.city());
        assertEquals("NY", addr.state());
        assertEquals("10001", addr.zipCode());
        assertEquals("USA", addr.country());
    }

    @Test
    void testAddressEquals() {
        Address a1 = new Address("123 Main St", "New York", "NY", "10001", "USA");
        Address a2 = new Address("123 Main St", "New York", "NY", "10001", "USA");
        Address a3 = new Address("456 Oak Ave", "Boston", "MA", "02101", "USA");
        
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1, a3);
    }
}