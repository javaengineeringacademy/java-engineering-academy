package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionCreateDeposit() {
        Transaction t = Transaction.create(TransactionType.DEPOSIT, new BigDecimal("100.00"), "Salary deposit");
        
        assertEquals(TransactionType.DEPOSIT, t.type());
        assertEquals(new BigDecimal("100.00"), t.amount());
        assertEquals("Salary deposit", t.description());
        assertNotNull(t.timestamp());
    }

    @Test
    void testTransactionCreateWithdrawal() {
        Transaction t = Transaction.create(TransactionType.WITHDRAWAL, new BigDecimal("50.00"), "ATM withdrawal");
        
        assertEquals(TransactionType.WITHDRAWAL, t.type());
        assertEquals(new BigDecimal("50.00"), t.amount());
        assertEquals("ATM withdrawal", t.description());
    }

    @Test
    void testTransactionNullAmountThrows() {
        assertThrows(NullPointerException.class, () -> {
            Transaction.create(TransactionType.DEPOSIT, null, "Test");
        });
    }

    @Test
    void testTransactionNullDescriptionThrows() {
        assertThrows(NullPointerException.class, () -> {
            Transaction.create(TransactionType.DEPOSIT, BigDecimal.TEN, null);
        });
    }
}