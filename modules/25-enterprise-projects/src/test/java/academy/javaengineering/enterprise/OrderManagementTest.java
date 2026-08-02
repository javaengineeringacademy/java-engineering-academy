package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Management Tests")
class OrderManagementTest {
    @Test @DisplayName("Should demonstrate saga pattern")
    void shouldDemonstrateSagaPattern() {
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateSagaPattern());
    }
    @Test @DisplayName("Should demonstrate order states")
    void shouldDemonstrateOrderStates() {
        assertDoesNotThrow(() -> OrderManagementExample.demonstrateOrderStates());
    }
}
