package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Message Queue Tests")
class MessageQueueTest {
    @Test @DisplayName("Should demonstrate technologies")
    void shouldDemonstrateTechnologies() {
        assertDoesNotThrow(() -> MessageQueueExample.demonstrateTechnologies());
    }
    @Test @DisplayName("Should demonstrate patterns")
    void shouldDemonstratePatterns() {
        assertDoesNotThrow(() -> MessageQueueExample.demonstratePatterns());
    }
}
