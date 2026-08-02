package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chat Application Tests")
class ChatApplicationTest {
    @Test @DisplayName("Should demonstrate components")
    void shouldDemonstrateComponents() {
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateComponents());
    }
    @Test @DisplayName("Should demonstrate flow")
    void shouldDemonstrateFlow() {
        assertDoesNotThrow(() -> ChatApplicationExample.demonstrateFlow());
    }
}
