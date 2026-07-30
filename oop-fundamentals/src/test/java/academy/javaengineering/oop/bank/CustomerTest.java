package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = new Customer("C001", "John Doe");
        
        assertEquals("C001", customer.customerId());
        assertEquals("John Doe", customer.name());
        assertTrue(customer.getAccounts().isEmpty());
    }

    @Test
    void testCustomerAddAccount() {
        Customer customer = new Customer("C001", "John Doe");
        SavingsAccount account = new SavingsAccount("SA001", customer, new BigDecimal("1000.00"), BigDecimal.valueOf(0.05));
        
        customer.addAccount(account);
        
        assertEquals(1, customer.getAccounts().size());
        assertEquals(account, customer.getAccounts().get(0));
    }

    @Test
    void testCustomerGetAccountsReturnsUnmodifiableList() {
        Customer customer = new Customer("C001", "John Doe");
        SavingsAccount account = new SavingsAccount("SA001", customer, new BigDecimal("1000.00"), BigDecimal.valueOf(0.05));
        customer.addAccount(account);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            customer.getAccounts().add(account);
        });
    }
}