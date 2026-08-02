package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("E-Commerce Platform Tests")
class ECommercePlatformTest {
    @Test @DisplayName("Should demonstrate services")
    void shouldDemonstrateServices() {
        assertDoesNotThrow(() -> ECommercePlatformExample.demonstrateServices());
    }
    @Test @DisplayName("Should demonstrate flows")
    void shouldDemonstrateFlows() {
        assertDoesNotThrow(() -> ECommercePlatformExample.demonstrateFlows());
    }
}
