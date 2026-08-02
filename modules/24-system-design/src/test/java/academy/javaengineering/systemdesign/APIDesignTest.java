package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Design Tests")
class APIDesignTest {
    @Test @DisplayName("Should demonstrate API types")
    void shouldDemonstrateAPITypes() {
        assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
    }
    @Test @DisplayName("Should demonstrate REST principles")
    void shouldDemonstratePrinciples() {
        assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
    }
}
