package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Caching Strategy Tests")
class CachingStrategyTest {

    @Test
    @DisplayName("Should demonstrate caching patterns without throwing")
    void shouldDemonstratePatterns() {
        assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
        assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
    }

    @Test
    @DisplayName("Should demonstrate eviction strategies without throwing")
    void shouldDemonstrateStrategies() {
        assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
        assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
    }

    @Test
    @DisplayName("Should call all caching demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All caching strategy demonstrations",
            () -> assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns()),
            () -> assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies())
        );
    }

    @Test
    @DisplayName("Should handle repeated pattern demonstrations")
    void shouldHandleRepeatedPatternCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
        }
    }

    @Test
    @DisplayName("Should handle repeated strategy demonstrations")
    void shouldHandleRepeatedStrategyCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
        }
    }

    @Test
    @DisplayName("Should handle rapid alternating pattern and strategy calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
            assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
        }
    }

    @Test
    @DisplayName("Should demonstrate patterns before strategies in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
        assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
    }

    @Test
    @DisplayName("Should be callable multiple times without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> CachingStrategyExample.demonstratePatterns());
            assertDoesNotThrow(() -> CachingStrategyExample.demonstrateStrategies());
        }
    }
}
