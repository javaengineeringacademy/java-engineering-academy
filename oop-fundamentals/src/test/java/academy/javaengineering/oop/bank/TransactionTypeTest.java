package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    @Test
    void testTransactionTypeValues() {
        assertEquals("DEPOSIT", TransactionType.DEPOSIT.name());
        assertEquals("WITHDRAWAL", TransactionType.WITHDRAWAL.name());
        assertEquals("INTEREST", TransactionType.INTEREST.name());
        assertEquals("TRANSFER_IN", TransactionType.TRANSFER_IN.name());
        assertEquals("TRANSFER_OUT", TransactionType.TRANSFER_OUT.name());
    }

    @Test
    void testTransactionTypeValuesCount() {
        assertEquals(5, TransactionType.values().length);
    }

    @Test
    void testTransactionTypeValueOf() {
        assertEquals(TransactionType.DEPOSIT, TransactionType.valueOf("DEPOSIT"));
    }
}