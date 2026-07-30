package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    @Test
    void testTransactionTypeValues() {
        TransactionType[] types = TransactionType.values();
        
        assertEquals(5, types.length); // DEPOSIT, WITHDRAWAL, INTEREST, FEE, TRANSFER
        
        for (TransactionType type : types) {
            assertNotNull(type.name());
            assertNotNull(type.toString());
        }
    }

    @Test
    void testTransactionTypeValueOf() {
        assertEquals(TransactionType.DEPOSIT, TransactionType.valueOf("DEPOSIT"));
        assertEquals(TransactionType.WITHDRAWAL, TransactionType.valueOf("WITHDRAWAL"));
        assertEquals(TransactionType.INTEREST, TransactionType.valueOf("INTEREST"));
        assertEquals(TransactionType.FEE, TransactionType.valueOf("FEE"));
        assertEquals(TransactionType.TRANSFER, TransactionType.valueOf("TRANSFER"));
    }

    @Test
    void testTransactionTypeInvalidValueThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionType.valueOf("INVALID");
        });
    }
}