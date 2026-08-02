package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Message Queue Tests")
class MessageQueueTest {

    @Test
    @DisplayName("Should demonstrate technologies without throwing")
    void shouldDemonstrateTechnologies() {
        assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
        assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
    }

    @Test
    @DisplayName("Should demonstrate patterns without throwing")
    void shouldDemonstratePatterns() {
        assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
        assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
    }

    @Test
    @DisplayName("Should call all message queue demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All message queue demonstrations",
            () -> assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies()),
            () -> assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns())
        );
    }

    @Test
    @DisplayName("Should handle repeated technology demonstrations")
    void shouldHandleRepeatedTechnologyCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
        }
    }

    @Test
    @DisplayName("Should handle repeated pattern demonstrations")
    void shouldHandleRepeatedPatternCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
        }
    }

    @Test
    @DisplayName("Should demonstrate technologies before patterns in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
        assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
    }

    @Test
    @DisplayName("Should handle rapid alternating technology and pattern calls")
    void shouldHandleAlternatingCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
            assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
        }
    }

    @Test
    @DisplayName("Should be callable multiple rounds without degradation")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
            assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
        }
    }
}
