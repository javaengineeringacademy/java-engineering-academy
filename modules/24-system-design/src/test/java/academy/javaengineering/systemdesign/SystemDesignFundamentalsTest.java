package academy.javaengineering.systemdesign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("System Design Fundamentals Tests")
class SystemDesignFundamentalsTest {

    @Test
    @DisplayName("Should demonstrate CAP theorem without throwing")
    void shouldDemonstrateCAP() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
    }

    @Test
    @DisplayName("Should demonstrate scalability without throwing")
    void shouldDemonstrateScalability() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
    }

    @Test
    @DisplayName("Should demonstrate availability without throwing")
    void shouldDemonstrateAvailability() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
    }

    @Test
    @DisplayName("Should call all system design demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All system design demonstrations",
            () -> assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP()),
            () -> assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability()),
            () -> assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability())
        );
    }

    @Test
    @DisplayName("Should handle repeated CAP theorem demonstrations")
    void shouldHandleRepeatedCAPCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
        }
    }

    @Test
    @DisplayName("Should handle repeated scalability demonstrations")
    void shouldHandleRepeatedScalabilityCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
        }
    }

    @Test
    @DisplayName("Should handle repeated availability demonstrations")
    void shouldHandleRepeatedAvailabilityCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
        }
    }

    @Test
    @DisplayName("Should demonstrate all concepts in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
        assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
    }

    @Test
    @DisplayName("Should handle multiple rounds of all demonstrations")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateCAP());
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateScalability());
            assertDoesNotThrow(() -> SystemDesignFundamentalsExample.demonstrateAvailability());
        }
    }
}
