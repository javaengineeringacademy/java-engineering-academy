package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        
        assertEquals("C001", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    void testCustomerAddAccount() {
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        SavingsAccount account = new SavingsAccount("ACC001", customer, new Money("100.00", "USD"), BigDecimal.valueOf(0.05));
        
        customer.addAccount(account);
        
        assertEquals(1, customer.getAccounts().size());
        assertTrue(customer.getAccounts().contains(account));
    }

    @Test
    void testCustomerRemoveAccount() {
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        SavingsAccount account = new SavingsAccount("ACC001", customer, new Money("100.00", "USD"), BigDecimal.valueOf(0.05));
        
        customer.addAccount(account);
        customer.removeAccount("ACC001");
        
        assertEquals(0, customer.getAccounts().size());
    }

    @Test
    void testCustomerRemoveNonExistentAccount() {
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        
        assertThrows(IllegalArgumentException.class, () -> customer.removeAccount("NONEXISTENT"));
    }
}