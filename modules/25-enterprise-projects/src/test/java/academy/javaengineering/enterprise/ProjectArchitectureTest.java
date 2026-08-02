package academy.javaengineering.enterprise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Project Architecture Tests")
class ProjectArchitectureTest {
    @Test @DisplayName("Should demonstrate clean architecture")
    void shouldDemonstrateCleanArchitecture() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateCleanArchitecture());
    }
    @Test @DisplayName("Should demonstrate hexagonal architecture")
    void shouldDemonstrateHexagonal() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateHexagonal());
    }
    @Test @DisplayName("Should demonstrate DDD")
    void shouldDemonstrateDDD() {
        assertDoesNotThrow(() -> ProjectArchitectureExample.demonstrateDDD());
    }
}
