package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    @Test
    void testSavingsAccountCreation() {
        Customer customer = new Customer("C001", "John Doe", "john@example.com");
        SavingsAccount account = new SavingsAccount("ACC001", customer, new Money("1000.00", "USD"), BigDecimal.valueOf(0.05));
        
        assertEquals("ACC001", account.getAccountNumber());
        assertEquals(customer, account.getOwner());
        assertEquals(new Money("1000.00", "USD"), account.getBalance());
        assertEquals(BigDecimal.valueOf(0.05), account.getInterestRate());
    }

    @Test
    void testSavingsAccountApplyInterest() {
        SavingsAccount account = new SavingsAccount("ACC001", 
            new Customer("C001", "John"), 
            new Money("1000.00", "USD"), 
            BigDecimal.valueOf(0.12)); // 12% annual
        
        Money interest = account.applyInterest();
        
        // Monthly interest = 1000 * 0.12 / 12 = 10
        assertEquals(new Money("10.00", "USD"), interest);
        assertEquals(new Money("1010.00", "USD"), account.getBalance());
    }

    @Test
    void testSavingsAccountWithdrawInsufficientFunds() {
        SavingsAccount account = new SavingsAccount("ACC001", 
            new Customer("C001", "John"), 
            new Money("100.00", "USD"), 
            BigDecimal.valueOf(0.05));
        
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(new Money("200.00", "USD")));
    }
}