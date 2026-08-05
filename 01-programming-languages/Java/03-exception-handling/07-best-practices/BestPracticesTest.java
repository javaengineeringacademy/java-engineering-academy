import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BestPracticesTest {

    @Test
    void testAvoidCatchingGenericException() {
        assertDoesNotThrow(BestPractices::avoidCatchingGenericException);
    }

    @Test
    void testUseSpecificExceptions() {
        assertDoesNotThrow(BestPractices::useSpecificExceptions);
    }

    @Test
    void testPreserveCause() {
        assertDoesNotThrow(BestPractices::preserveCause);
    }

    @Test
    void testProcessOrderValid() {
        assertDoesNotThrow(() -> BestPractices.processOrder("123"));
    }

    @Test
    void testProcessOrderNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            BestPractices.processOrder(null);
        });
    }

    @Test
    void testProcessOrderEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            BestPractices.processOrder("");
        });
    }

    @Test
    void testTryWithResources() {
        assertDoesNotThrow(BestPractices::useTryWithResources);
    }
}
