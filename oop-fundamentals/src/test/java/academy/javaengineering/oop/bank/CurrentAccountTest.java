package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    @Test
    void testCurrentAccountCreation() {
        Customer customer = new Customer("C001", "Jane Smith", "jane@example.com");
        CurrentAccount account = new CurrentAccount("ACC002", customer, new Money("500.00", "USD"), new Money("1000.00", "USD"));
        
        assertEquals("ACC002", account.getAccountNumber());
        assertEquals(customer, account.getOwner());
        assertEquals(new Money("500.00", "USD"), account.getBalance());
        assertEquals(new Money("1000.00", "USD"), account.getOverdraftLimit());
    }

    @Test
    void testCurrentAccountWithdrawWithinOverdraft() {
        CurrentAccount account = new CurrentAccount("ACC002", 
            new Customer("C001", "Jane"), 
            new Money("500.00", "USD"), 
            new Money("1000.00", "USD"));
        
        account.withdraw(new Money("1200.00", "USD")); // Balance: -700
        
        assertEquals(new Money("-700.00", "USD"), account.getBalance());
        assertEquals(new Money("700.00", "USD"), account.getOverdraftUsed());
    }

    @Test
    void testCurrentAccountWithdrawExceedsOverdraft() {
        CurrentAccount account = new CurrentAccount("ACC002", 
            new Customer("C001", "Jane"), 
            new Money("500.00", "USD"), 
            new Money("1000.00", "USD"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(new Money("2000.00", "USD")));
    }

    @Test
    void testCurrentAccountDepositRepaysOverdraft() {
        CurrentAccount account = new CurrentAccount("ACC002", 
            new Customer("C001", "Jane"), 
            new Money("500.00", "USD"), 
            new Money("1000.00", "USD"));
        
        account.withdraw(new Money("1200.00", "USD")); // -700, overdraft used 700
        account.deposit(new Money("800.00", "USD"));   // 800 - 700 = 100 balance, 0 overdraft
        
        assertEquals(new Money("100.00", "USD"), account.getBalance());
        assertEquals(new Money("0.00", "USD"), account.getOverdraftUsed());
    }

    @Test
    void testCurrentAccountAvailableBalance() {
        CurrentAccount account = new CurrentAccount("ACC002", 
            new Customer("C001", "Jane"), 
            new Money("500.00", "USD"), 
            new Money("1000.00", "USD"));
        
        // Available = balance + (overdraft limit - overdraft used)
        // = 500 + (1000 - 0) = 1500
        assertEquals(new Money("1500.00", "USD"), account.availableBalance());
        
        account.withdraw(new Money("1200.00", "USD"));
        // Balance: -700, overdraft used: 700
        // Available = -700 + (1000 - 700) = -400
        assertEquals(new Money("-400.00", "USD"), account.availableBalance());
    }
}