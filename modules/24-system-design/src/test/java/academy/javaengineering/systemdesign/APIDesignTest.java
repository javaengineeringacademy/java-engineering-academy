package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Design Tests")
class APIDesignTest {

    @Test
    @DisplayName("Should demonstrate API types without throwing")
    void shouldDemonstrateAPITypes() {
        assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
        assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
    }

    @Test
    @DisplayName("Should demonstrate REST principles without throwing")
    void shouldDemonstratePrinciples() {
        assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
        assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
    }

    @Test
    @DisplayName("Should call all API design demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All API design demonstrations",
            () -> assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes()),
            () -> assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples())
        );
    }

    @Test
    @DisplayName("Should handle repeated API type demonstrations")
    void shouldHandleRepeatedAPICalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
        }
    }

    @Test
    @DisplayName("Should handle repeated principle demonstrations")
    void shouldHandleRepeatedPrincipleCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
        }
    }

    @Test
    @DisplayName("Should demonstrate API types before principles in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
        assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
    }

    @Test
    @DisplayName("Should handle rapid alternating API and principles calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
            assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
        }
    }

    @Test
    @DisplayName("Should be callable multiple rounds without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> APIDesignExample.demonstrateAPITypes());
            assertDoesNotThrow(() -> APIDesignExample.demonstratePrinciples());
        }
    }
}
