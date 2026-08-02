package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chat Application Tests")
class ChatApplicationTest {

    @Test
    @DisplayName("Should demonstrate components without throwing")
    void shouldDemonstrateComponents() {
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
    }

    @Test
    @DisplayName("Should demonstrate flow without throwing")
    void shouldDemonstrateFlow() {
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
    }

    @Test
    @DisplayName("Should call all chat application demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All chat application demonstrations",
            () -> assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents()),
            () -> assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow())
        );
    }

    @Test
    @DisplayName("Should handle repeated component demonstrations")
    void shouldHandleRepeatedComponentCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
        }
    }

    @Test
    @DisplayName("Should handle repeated flow demonstrations")
    void shouldHandleRepeatedFlowCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
        }
    }

    @Test
    @DisplayName("Should demonstrate components before flow in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
    }

    @Test
    @DisplayName("Should handle rapid alternating component and flow calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
        }
    }

    @Test
    @DisplayName("Should be callable multiple rounds without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
            assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
        }
    }
}
