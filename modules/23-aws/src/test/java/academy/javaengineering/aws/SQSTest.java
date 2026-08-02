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
    @DisplayName("Should demonstrate queue types")
    void shouldDemonstrateQueueTypes() {
        assertDoesNotThrow(() -> SQSExample.demonstrateQueueTypes());
    }

    @Test
    @DisplayName("Should demonstrate features")
    void shouldDemonstrateFeatures() {
        assertDoesNotThrow(() -> SQSExample.demonstrateFeatures());
    }

    @Test
    @DisplayName("Should demonstrate best practices")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> SQSExample.demonstrateBestPractices());
    }
}
