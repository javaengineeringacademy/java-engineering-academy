package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Bank Management System.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class BankManagementSystemTest {

    @Test
    void shouldCreateSavingsAccount() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        Account2 account = bank.createSavingsAccount("Alice", 10000);
        
        assertNotNull(account);
        assertEquals("Alice", account.getOwner());
        assertEquals(10000, account.getBalance(), 0.001);
        assertTrue(account instanceof InterestBearing);
    }

    @Test
    void shouldCreateCheckingAccount() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        Account2 account = bank.createCheckingAccount("Bob", 5000);
        
        assertNotNull(account);
        assertEquals("Bob", account.getOwner());
        assertEquals(5000, account.getBalance(), 0.001);
        assertTrue(account instanceof CheckingAccount3);
    }

    @Test
    void shouldCreateBusinessAccount() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        Account2 account = bank.createBusinessAccount("Charlie's Corp", 50000);
        
        assertNotNull(account);
        assertEquals("Charlie's Corp", account.getOwner());
        assertEquals(50000, account.getBalance(), 0.001);
        assertTrue(account instanceof InterestBearing);
    }

    @Test
    void shouldTransferBetweenAccounts() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        Account2 from = bank.createSavingsAccount("Alice", 10000);
        Account2 to = bank.createCheckingAccount("Bob", 5000);
        
        bank.transfer(from, to, 2000);
        
        assertEquals(8000, from.getBalance(), 0.001);
        assertEquals(7000, to.getBalance(), 0.001);
    }

    @Test
    void shouldApplyInterest() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        Account2 account = bank.createSavingsAccount("Alice", 10000);
        
        bank.applyInterestToAll();
        
        // 10000 + (10000 * 0.05) = 10500
        assertEquals(10500, account.getBalance(), 0.001);
    }

    @Test
    void shouldDisplayAllAccounts() {
        BankManagementSystem bank = new BankManagementSystem("Test Bank");
        bank.createSavingsAccount("Alice", 10000);
        bank.createCheckingAccount("Bob", 5000);
        
        // Should not throw
        assertDoesNotThrow(bank::displayAllAccounts);
    }
}