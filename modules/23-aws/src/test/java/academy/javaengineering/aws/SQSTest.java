package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SQS Tests")
class SQSTest {

    private SQSExample example;

    @BeforeEach
    void setUp() {
        example = new SQSExample();
    }

    @Test
    @DisplayName("Should demonstrate queue types without throwing")
    void shouldDemonstrateQueueTypes() {
        assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes());
        assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate features without throwing")
    void shouldDemonstrateFeatures() {
        assertDoesNotThrow(() -> SQSExample.demonstrateFeatures());
        assertDoesNotThrow(() -> SQSExample.demonstrateFeatures());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate best practices without throwing")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> SQSExample.demonstrateBestPractices());
        assertDoesNotThrow(() -> SQSExample.demonstrateBestPractices());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create SQSExample instance successfully")
    void shouldCreateInstance() {
        SQSExample instance = new SQSExample();
        assertNotNull(instance);
        assertInstanceOf(SQSExample.class, instance);
    }

    @Test
    @DisplayName("Should call all SQS demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All SQS demonstrations",
            () -> assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes()),
            () -> assertDoesNotThrow(() -> SQSExample.demonstrateFeatures()),
            () -> assertDoesNotThrow(() -> SQSExample.demonstrateBestPractices())
        );
    }

    @Test
    @DisplayName("Should handle repeated queue type demonstrations")
    void shouldHandleRepeatedQueueTypeCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated SQS features demonstrations")
    void shouldHandleRepeatedFeatureCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> SQSExample.demonstrateFeatures());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across demonstrations")
    void shouldMaintainStatelessness() {
        SQSExample first = new SQSExample();
        SQSExample second = new SQSExample();
        assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes());
        assertDoesNotThrow(() -> SQSExample.demonstrateBestPractices());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
