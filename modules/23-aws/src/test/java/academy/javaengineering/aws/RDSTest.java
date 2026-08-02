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
    @DisplayName("Should demonstrate engines without throwing")
    void shouldDemonstrateEngines() {
        assertDoesNotThrow(() -> RDSExample.demonstrateEngines());
        assertDoesNotThrow(() -> RDSExample.demonstrateEngines());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate features without throwing")
    void shouldDemonstrateFeatures() {
        assertDoesNotThrow(() -> RDSExample.demonstrateFeatures());
        assertDoesNotThrow(() -> RDSExample.demonstrateFeatures());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate best practices without throwing")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> RDSExample.demonstrateBestPractices());
        assertDoesNotThrow(() -> RDSExample.demonstrateBestPractices());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create RDSExample instance successfully")
    void shouldCreateInstance() {
        RDSExample instance = new RDSExample();
        assertNotNull(instance);
        assertInstanceOf(RDSExample.class, instance);
    }

    @Test
    @DisplayName("Should call all RDS demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All RDS demonstrations",
            () -> assertDoesNotThrow(() -> RDSExample.demonstrateEngines()),
            () -> assertDoesNotThrow(() -> RDSExample.demonstrateFeatures()),
            () -> assertDoesNotThrow(() -> RDSExample.demonstrateBestPractices())
        );
    }

    @Test
    @DisplayName("Should handle repeated engine demonstrations")
    void shouldHandleRepeatedEngineCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> RDSExample.demonstrateEngines());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated RDS features demonstrations")
    void shouldHandleRepeatedFeatureCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> RDSExample.demonstrateFeatures());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across demonstrations")
    void shouldMaintainStatelessness() {
        RDSExample first = new RDSExample();
        RDSExample second = new RDSExample();
        assertDoesNotThrow(() -> RDSExample.demonstrateEngines());
        assertDoesNotThrow(() -> RDSExample.demonstrateBestPractices());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
