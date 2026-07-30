package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionCreateDeposit() {
        Transaction tx = Transaction.create(TransactionType.DEPOSIT, new Money("100.00", "USD"), "Initial deposit");
        
        assertEquals(TransactionType.DEPOSIT, tx.type());
        assertEquals(new Money("100.00", "USD"), tx.amount());
        assertEquals("Initial deposit", tx.description());
        assertNotNull(tx.timestamp());
    }

    @Test
    void testTransactionCreateWithdrawal() {
        Transaction tx = Transaction.create(TransactionType.WITHDRAWAL, new Money("50.00", "USD"), "ATM withdrawal");
        
        assertEquals(TransactionType.WITHDRAWAL, tx.type());
        assertEquals(new Money("50.00", "USD"), tx.amount());
        assertEquals("ATM withdrawal", tx.description());
    }

    @Test
    void testTransactionCreateInterest() {
        Transaction tx = Transaction.create(TransactionType.INTEREST, new Money("5.00", "USD"), "Monthly interest");
        
        assertEquals(TransactionType.INTEREST, tx.type());
        assertEquals(new Money("5.00", "USD"), tx.amount());
    }

    @Test
    void testTransactionImmutable() {
        Transaction tx = Transaction.create(TransactionType.DEPOSIT, new Money("100.00", "USD"), "Test");
        
        // Transaction should be immutable (record)
        assertEquals(TransactionType.DEPOSIT, tx.type());
        assertEquals(new Money("100.00", "USD"), tx.amount());
    }
}