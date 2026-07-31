package academy.javaengineering.oop.encapsulation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for encapsulated BankAccount class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class BankAccountTest {

    @Test
    void shouldCreateAccountWithValidBalance() {
        BankAccount account = new BankAccount("John", 1000);
        assertEquals("John", account.getOwner());
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldDepositValidAmount() {
        BankAccount account = new BankAccount("John", 1000);
        assertTrue(account.deposit(500));
        assertEquals(1500.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectNegativeDeposit() {
        BankAccount account = new BankAccount("John", 1000);
        assertFalse(account.deposit(-100));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldWithdrawValidAmount() {
        BankAccount account = new BankAccount("John", 1000);
        assertTrue(account.withdraw(300));
        assertEquals(700.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectOverdraft() {
        BankAccount account = new BankAccount("John", 1000);
        assertFalse(account.withdraw(5000));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectNegativeWithdrawal() {
        BankAccount account = new BankAccount("John", 1000);
        assertFalse(account.withdraw(-100));
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

    @Test
    void shouldLogTransactions() {
        BankAccount account = new BankAccount("John", 1000);
        account.deposit(500);
        account.withdraw(200);
        
        var log = account.getTransactionLog();
        assertEquals(3, log.size()); // Initial + deposit + withdrawal
    }

    @Test
    void shouldReturnUnmodifiableLog() {
        BankAccount account = new BankAccount("John", 1000);
        var log = account.getTransactionLog();
        
        assertThrows(UnsupportedOperationException.class, 
            () -> log.add("tampering"));
    }

    @Test
    void shouldThrowOnNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("John", -100));
    }
}