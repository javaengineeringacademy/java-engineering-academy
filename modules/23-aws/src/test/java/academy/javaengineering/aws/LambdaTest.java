package academy.javaengineering.aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Lambda Tests")
class LambdaTest {

    private LambdaExample example;

    @BeforeEach
    void setUp() {
        example = new LambdaExample();
    }

    @Test
    @DisplayName("Should demonstrate Lambda limits")
    void shouldDemonstrateLambdaLimits() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits());
    }

    @Test
    @DisplayName("Should demonstrate trigger types")
    void shouldDemonstrateTriggerTypes() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateTriggerTypes());
    }

    @Test
    @DisplayName("Should demonstrate best practices")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateBestPractices());
    }
}
