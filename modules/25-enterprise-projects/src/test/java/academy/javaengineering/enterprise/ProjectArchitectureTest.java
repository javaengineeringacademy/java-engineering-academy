package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Project Architecture Tests")
class ProjectArchitectureTest {

    @Test
    @DisplayName("Should demonstrate clean architecture without throwing")
    void shouldDemonstrateCleanArchitecture() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
    }

    @Test
    @DisplayName("Should demonstrate hexagonal architecture without throwing")
    void shouldDemonstrateHexagonal() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
    }

    @Test
    @DisplayName("Should demonstrate DDD without throwing")
    void shouldDemonstrateDDD() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
    }

    @Test
    @DisplayName("Should call all architecture demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All project architecture demonstrations",
            () -> assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture()),
            () -> assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal()),
            () -> assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD())
        );
    }

    @Test
    @DisplayName("Should handle repeated clean architecture demonstrations")
    void shouldHandleRepeatedCleanArchCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
        }
    }

    @Test
    @DisplayName("Should handle repeated hexagonal architecture demonstrations")
    void shouldHandleRepeatedHexagonalCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
        }
    }

    @Test
    @DisplayName("Should handle repeated DDD demonstrations")
    void shouldHandleRepeatedDDDCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
        }
    }

    @Test
    @DisplayName("Should demonstrate all architectures in sequence")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
    }

    @Test
    @DisplayName("Should handle multiple rounds of all demonstrations")
    void shouldHandleMultipleRounds() {
        for (int round = 0; round < 3; round++) {
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
            assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
        }
    }
}
