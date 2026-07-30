package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("SavingsAccount: deposit and withdraw")
    void testSavingsAccountDepositAndWithdraw() {
        SavingsAccount account = new SavingsAccount("SA001", new Customer("C001", "John"), 
            new BigDecimal("1000.00"), new BigDecimal("0.05"));
        
        account.deposit(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), account.balance());
        
        account.withdraw(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("1300.00"), account.balance());
    }

    @Test
    @DisplayName("SavingsAccount: withdraw more than balance throws exception")
    void testSavingsAccountWithdrawInsufficientFunds() {
        SavingsAccount account = new SavingsAccount("SA001", new Customer("C001", "John"), 
            new BigDecimal("1000.00"), new BigDecimal("0.05"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(new BigDecimal("1500.00"));
        });
    }

    @Test
    @DisplayName("SavingsAccount: calculate interest")
    void testSavingsAccountCalculateInterest() {
        SavingsAccount account = new SavingsAccount("SA001", new Customer("C001", "John"), 
            new BigDecimal("1000.00"), new BigDecimal("0.12")); // 12% annual
        
        BigDecimal interest = account.applyMonthlyInterest();
        // Monthly interest = 1000 * 0.12 / 12 = 10.00
        assertEquals(new BigDecimal("10.00"), interest);
        assertEquals(new BigDecimal("1010.00"), account.balance());
    }

    @Test
    @DisplayName("CurrentAccount: deposit and withdraw within balance")
    void testCurrentAccountDepositAndWithdraw() {
        CurrentAccount account = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00")); // 500 overdraft
        
        account.deposit(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), account.balance());
        
        account.withdraw(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("1300.00"), account.balance());
    }

    @Test
    @DisplayName("CurrentAccount: withdraw using overdraft")
    void testCurrentAccountWithdrawOverdraft() {
        CurrentAccount account = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00")); // 500 overdraft
        
        // Withdraw 1200 (1000 balance + 200 overdraft)
        account.withdraw(new BigDecimal("1200.00"));
        
        assertEquals(new BigDecimal("-200.00"), account.balance());
        assertEquals(new BigDecimal("200.00"), account.overdraftUsed());
        assertEquals(new BigDecimal("300.00"), account.overdraftLimit().subtract(account.overdraftUsed()));
    }

    @Test
    @DisplayName("CurrentAccount: withdraw exceeding overdraft throws exception")
    void testCurrentAccountWithdrawExceedsOverdraft() {
        CurrentAccount account = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(new BigDecimal("2000.00"));
        });
    }

    @Test
    @DisplayName("CurrentAccount: deposit repays overdraft first")
    void testCurrentAccountDepositRepaysOverdraft() {
        CurrentAccount account = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00"));
        
        // Go into overdraft
        account.withdraw(new BigDecimal("1200.00"));
        assertEquals(new BigDecimal("200.00"), account.overdraftUsed());
        
        // Deposit 300 - should repay overdraft first
        account.deposit(new BigDecimal("300.00"));
        
        // 100 should go to overdraft (now 0), 200 to balance
        assertEquals(new BigDecimal("0.00"), account.overdraftUsed());
        assertEquals(new BigDecimal("200.00"), account.balance());
    }

    @Test
    @DisplayName("Available balance for CurrentAccount includes overdraft")
    void testCurrentAccountAvailableBalance() {
        CurrentAccount account = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00"));
        
        assertEquals(new BigDecimal("1500.00"), account.availableBalance());
        
        account.withdraw(new BigDecimal("1200.00"));
        assertEquals(new BigDecimal("300.00"), account.availableBalance());
    }

    @Test
    @DisplayName("Account accountType returns correct type")
    void testAccountType() {
        SavingsAccount savings = new SavingsAccount("SA001", new Customer("C001", "John"), 
            new BigDecimal("1000.00"), new BigDecimal("0.05"));
        CurrentAccount current = new CurrentAccount("CA001", new Customer("C002", "Jane"), 
            new BigDecimal("1000.00"), new BigDecimal("500.00"));
        
        assertEquals("SAVINGS", savings.accountType());
        assertEquals("CURRENT", current.accountType());
    }
}