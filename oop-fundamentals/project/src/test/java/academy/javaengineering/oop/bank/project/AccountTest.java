package academy.javaengineering.oop.bank.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Account hierarchy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class AccountTest {

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer("C001", "Test", "User", "test@email.com");
    }

    @Test
    void shouldDepositValidAmount() {
        Account account = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        assertTrue(account.deposit(500.00));
        assertEquals(1500.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectNegativeDeposit() {
        Account account = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        assertFalse(account.deposit(-100.00));
        assertEquals(1000.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldWithdrawValidAmount() {
        Account account = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        assertTrue(account.withdraw(300.00));
        assertEquals(700.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectOverdraft() {
        Account account = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        assertFalse(account.withdraw(5000.00));
        assertEquals(1000.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldApplyInterestOnSavings() {
        SavingsAccount account = new SavingsAccount(testCustomer, 10000.00, 0.10);
        
        account.applyInterest();
        
        // 10000 + (10000 * 0.10) = 11000
        assertEquals(11000.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldSupportOverdraftOnChecking() {
        CheckingAccount account = new CheckingAccount(testCustomer, 1000.00, 500.00);
        
        // Can withdraw up to balance + overdraft
        assertTrue(account.withdraw(1200.00));
        assertEquals(-200.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldRejectOverdraftExceedingLimit() {
        CheckingAccount account = new CheckingAccount(testCustomer, 1000.00, 500.00);
        
        assertFalse(account.withdraw(2000.00));
        assertEquals(1000.00, account.getBalance(), 0.001);
    }

    @Test
    void shouldChargeFeeOnBusinessWithdrawal() {
        BusinessAccount account = new BusinessAccount(testCustomer, 10000.00, 0.02);
        
        account.withdraw(100.00);
        
        // 10000 - 100 - 2.50 fee = 9897.50
        assertEquals(9897.50, account.getBalance(), 0.001);
    }

    @Test
    void shouldGenerateUniqueAccountNumbers() {
        Account acc1 = new SavingsAccount(testCustomer, 1000.00, 0.05);
        Account acc2 = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        assertNotEquals(acc1.getAccountNumber(), acc2.getAccountNumber());
    }

    @Test
    void shouldTrackTransactions() {
        Account account = new SavingsAccount(testCustomer, 1000.00, 0.05);
        
        account.deposit(500.00);
        account.withdraw(200.00);
        
        assertEquals(3, account.getTransactions().size()); // Initial + deposit + withdrawal
    }
}