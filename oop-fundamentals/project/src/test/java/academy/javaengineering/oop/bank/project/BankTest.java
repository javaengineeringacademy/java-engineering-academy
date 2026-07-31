package academy.javaengineering.oop.bank.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Bank Management System.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class BankTest {

    private Bank bank;
    private Customer alice;
    private Customer bob;

    @BeforeEach
    void setUp() {
        bank = new Bank("Test Bank");
        alice = new Customer("C001", "Alice", "Johnson", "alice@email.com");
        bob = new Customer("C002", "Bob", "Smith", "bob@email.com");
    }

    @Test
    void shouldCreateSavingsAccount() {
        Account account = bank.createSavingsAccount(alice, 10000.00);
        
        assertNotNull(account);
        assertEquals("Alice Johnson", account.getCustomer().getFullName());
        assertEquals(10000.00, account.getBalance(), 0.001);
        assertTrue(account instanceof InterestBearing);
    }

    @Test
    void shouldCreateCheckingAccount() {
        Account account = bank.createCheckingAccount(bob, 5000.00);
        
        assertNotNull(account);
        assertEquals("Bob Smith", account.getCustomer().getFullName());
        assertEquals(5000.00, account.getBalance(), 0.001);
        assertTrue(account instanceof CheckingAccount);
    }

    @Test
    void shouldCreateBusinessAccount() {
        Account account = bank.createBusinessAccount(alice, 50000.00);
        
        assertNotNull(account);
        assertEquals(50000.00, account.getBalance(), 0.001);
        assertTrue(account instanceof InterestBearing);
    }

    @Test
    void shouldTransferBetweenAccounts() {
        Account from = bank.createSavingsAccount(alice, 10000.00);
        Account to = bank.createCheckingAccount(bob, 5000.00);
        
        boolean success = bank.transfer(from, to, 2000.00);
        
        assertTrue(success);
        assertEquals(8000.00, from.getBalance(), 0.001);
        assertEquals(7000.00, to.getBalance(), 0.001);
    }

    @Test
    void shouldRejectTransferWithInsufficientFunds() {
        Account from = bank.createSavingsAccount(alice, 1000.00);
        Account to = bank.createCheckingAccount(bob, 5000.00);
        
        boolean success = bank.transfer(from, to, 5000.00);
        
        assertFalse(success);
        assertEquals(1000.00, from.getBalance(), 0.001);
        assertEquals(5000.00, to.getBalance(), 0.001);
    }

    @Test
    void shouldApplyInterest() {
        Account account = bank.createSavingsAccount(alice, 10000.00);
        
        bank.applyInterest();
        
        // 10000 + (10000 * 0.05) = 10500
        assertEquals(10500.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldDisplayAllAccounts() {
        bank.createSavingsAccount(alice, 10000.00);
        bank.createCheckingAccount(bob, 5000.00);
        
        // Should not throw
        assertDoesNotThrow(bank::displayAllAccounts);
    }

    @Test
    void shouldGetAllAccounts() {
        bank.createSavingsAccount(alice, 10000.00);
        bank.createCheckingAccount(bob, 5000.00);
        
        assertEquals(2, bank.getAccounts().size());
    }
}