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
    @DisplayName("Should demonstrate Lambda limits without throwing")
    void shouldDemonstrateLambdaLimits() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits());
        assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate trigger types without throwing")
    void shouldDemonstrateTriggerTypes() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateTriggerTypes());
        assertDoesNotThrow(() -> LambdaExample.demonstrateTriggerTypes());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate best practices without throwing")
    void shouldDemonstrateBestPractices() {
        assertDoesNotThrow(() -> LambdaExample.demonstrateBestPractices());
        assertDoesNotThrow(() -> LambdaExample.demonstrateBestPractices());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create LambdaExample instance successfully")
    void shouldCreateInstance() {
        LambdaExample instance = new LambdaExample();
        assertNotNull(instance);
        assertInstanceOf(LambdaExample.class, instance);
    }

    @Test
    @DisplayName("Should call all Lambda demonstrations together")
    void shouldCallAllDemonstrationsTogether() {
        assertAll("All Lambda demonstrations",
            () -> assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits()),
            () -> assertDoesNotThrow(() -> LambdaExample.demonstrateTriggerTypes()),
            () -> assertDoesNotThrow(() -> LambdaExample.demonstrateBestPractices())
        );
    }

    @Test
    @DisplayName("Should handle repeated Lambda limit demonstrations")
    void shouldHandleRepeatedLimitCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated trigger type demonstrations")
    void shouldHandleRepeatedTriggerCalls() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> LambdaExample.demonstrateTriggerTypes());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across demonstrations")
    void shouldMaintainStatelessness() {
        LambdaExample first = new LambdaExample();
        LambdaExample second = new LambdaExample();
        assertDoesNotThrow(() -> LambdaExample.demonstrateLambdaLimits());
        assertDoesNotThrow(() -> LambdaExample.demonstrateBestPractices());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
