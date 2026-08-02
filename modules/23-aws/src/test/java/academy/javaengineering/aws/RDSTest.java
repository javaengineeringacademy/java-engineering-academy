package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RDS Tests")
class RDSTest {

    private RDSExample example;

    @BeforeEach
    void setUp() {
        example = new RDSExample();
    }

    @Test
    @DisplayName("Should demonstrate engines")
    void shouldDemonstrateEngines() {
        assertDoesNotThrow(() -> RDSExample.demonstrateEngines());
    }

    @Test
    @DisplayName("Should demonstrate features")
    void shouldDemonstrateFeatures() {
        assertDoesNotThrow(() -> RDSExample.demonstrateFeatures());
    }

    @Test
    @DisplayName("Should demonstrate best practices")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> RDSExample.demonstrateBestPractices());
    }
}
