package academy.javaengineering.oop.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoanStatusTest {

    @Test
    void testLoanStatusValues() {
        assertEquals("PENDING", LoanStatus.PENDING.name());
        assertEquals("APPROVED", LoanStatus.APPROVED.name());
        assertEquals("REJECTED", LoanStatus.REJECTED.name());
        assertEquals("ACTIVE", LoanStatus.ACTIVE.name());
        assertEquals("CLOSED", LoanStatus.CLOSED.name());
        assertEquals("DEFAULTED", LoanStatus.DEFAULTED.name());
    }

    @Test
    void testLoanStatusValuesCount() {
        assertEquals(6, LoanStatus.values().length);
    }
}