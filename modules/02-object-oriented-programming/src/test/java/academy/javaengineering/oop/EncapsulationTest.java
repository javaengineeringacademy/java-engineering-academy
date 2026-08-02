package academy.javaengineering.oop;

import academy.javaengineering.oop.`03-encapsulation`.EncapsulationExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Encapsulation Tests")
class EncapsulationTest {

    @Test
    @DisplayName("Bank account creation with valid data")
    void bankAccountCreation() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 1000.00);
        assertEquals("ACC-001", account.getAccountId());
        assertEquals("Alice", account.getHolderName());
        assertEquals(1000.00, account.getBalance(), 0.001);
        assertEquals("ACTIVE", account.getStatus());
    }

    @Test
    @DisplayName("Deposit increases balance")
    void deposit() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 1000.00);
        account.deposit(500.00);
        assertEquals(1500.00, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("Withdrawal test with sufficient funds")
    void withdrawalSufficient() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 1000.00);
        boolean result = account.withdraw(300.00);
        assertTrue(result);
        assertEquals(700.00, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("Withdrawal rejected when insufficient funds")
    void withdrawalInsufficient() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 100.00);
        boolean result = account.withdraw(1000.00);
        assertFalse(result);
        assertEquals(100.00, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("Deposit validation rejects negative amounts")
    void depositValidation() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 1000.00);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
    }

    @Test
    @DisplayName("Holder name validation rejects blank")
    void holderNameValidation() {
        BankAccount account = new BankAccount("ACC-001", "Alice", 1000.00);
        assertThrows(IllegalArgumentException.class, () -> account.setHolderName(""));
        assertThrows(IllegalArgumentException.class, () -> account.setHolderName(null));
    }

    @Test
    @DisplayName("In good standing check")
    void inGoodStanding() {
        BankAccount positive = new BankAccount("A", "A", 100);
        BankAccount negative = new BankAccount("B", "B", 0);

        assertTrue(positive.isInGoodStanding());
        assertTrue(negative.isInGoodStanding()); // At zero

        negative.withdraw(100);
        assertFalse(negative.isInGoodStanding());
    }

    @Test
    @DisplayName("Immutable address with withStreet")
    void immutableAddress() {
        Address addr1 = Address.usAddress("123 Main", "Springfield", "IL", "62704");
        Address addr2 = addr1.withStreet("456 Oak");

        assertEquals("123 Main", addr1.getStreet());
        assertEquals("456 Oak", addr2.getStreet());
        assertNotEquals(addr1, addr2);
    }

    @Test
    @DisplayName("Address equality and hashCode")
    void addressEquality() {
        Address a = Address.usAddress("123 Main", "Springfield", "IL", "62704");
        Address b = Address.usAddress("123 Main", "Springfield", "IL", "62704");
        Address c = Address.usAddress("456 Oak", "Springfield", "IL", "62704");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
