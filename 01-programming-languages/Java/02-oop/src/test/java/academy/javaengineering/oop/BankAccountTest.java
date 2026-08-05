package academy.javaengineering.oop;

import academy.javaengineering.oop.encapsulation.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankAccount Encapsulation Tests")
class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("ACC-001", "Alice", new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Account initialized with correct values")
    void initialization() {
        assertEquals("ACC-001", account.getAccountId());
        assertEquals("Alice", account.getOwnerName());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    @DisplayName("Deposit increases balance")
    void deposit() {
        account.deposit(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), account.getBalance());
    }

    @Test
    @DisplayName("Withdraw decreases balance")
    void withdraw() {
        account.withdraw(new BigDecimal("300.00"));
        assertEquals(new BigDecimal("700.00"), account.getBalance());
    }

    @Test
    @DisplayName("Deposit rejects negative amount")
    void depositNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("-100")));
    }

    @Test
    @DisplayName("Withdraw rejects insufficient funds")
    void insufficientFunds() {
        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(new BigDecimal("2000.00")));
    }

    @Test
    @DisplayName("Owner name cannot be blank")
    void blankOwnerName() {
        assertThrows(IllegalArgumentException.class,
                () -> account.setOwnerName(""));
    }

    @Test
    @DisplayName("Close deactivates account")
    void closeAccount() {
        account.close();
        assertFalse(account.isActive());
        assertThrows(IllegalStateException.class,
                () -> account.deposit(new BigDecimal("100")));
    }

    @Test
    @DisplayName("Cannot create account with negative balance")
    void negativeInitialBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("X", "Bob", new BigDecimal("-100")));
    }
}
