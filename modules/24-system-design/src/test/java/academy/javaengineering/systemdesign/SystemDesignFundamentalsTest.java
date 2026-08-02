package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("System Design Fundamentals Tests")
class SystemDesignFundamentalsTest {
    @Test @DisplayName("Should demonstrate CAP theorem")
    void shouldDemonstrateCAP() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
    }
    @Test @DisplayName("Should demonstrate scalability")
    void shouldDemonstrateScalability() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
    }
    @Test @DisplayName("Should demonstrate availability")
    void shouldDemonstrateAvailability() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
    }
}
