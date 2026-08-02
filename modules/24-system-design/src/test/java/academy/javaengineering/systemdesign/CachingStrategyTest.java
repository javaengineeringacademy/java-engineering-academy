package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Caching Strategy Tests")
class CachingStrategyTest {
    @Test @DisplayName("Should demonstrate caching patterns")
    void shouldDemonstratePatterns() {
        assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
    }
    @Test @DisplayName("Should demonstrate eviction strategies")
    void shouldDemonstrateStrategies() {
        assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
    }
}
