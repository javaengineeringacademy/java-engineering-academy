package academy.javaengineering.kubernetes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pod Tests")
class PodTest {

    private PodExample example;

    @BeforeEach
    void setUp() {
        example = new PodExample();
    }

    @Test
    @DisplayName("Should demonstrate pod specification")
    void shouldDemonstratePodSpec() {
        assertDoesNotThrow(() -> PodExample.demonstratePodSpec());
    }

    @Test
    @DisplayName("Should demonstrate init containers")
    void shouldDemonstrateInitContainers() {
        assertDoesNotThrow(() -> PodExample.demonstrateInitContainers());
    }

    @Test
    @DisplayName("Should demonstrate sidecar pattern")
    void shouldDemonstrateSidecarPattern() {
        assertDoesNotThrow(() -> PodExample.demonstrateSidecarPattern());
    }

    @Test
    @DisplayName("Should demonstrate resource limits")
    void shouldDemonstrateResourceLimits() {
        assertDoesNotThrow(() -> PodExample.demonstrateResourceLimits());
    }
}
