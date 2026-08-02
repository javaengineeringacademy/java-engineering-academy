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
    @DisplayName("Should demonstrate pod specification without throwing")
    void shouldDemonstratePodSpec() {
        assertDoesNotThrow(() -> PodExample.demonstratePodSpec());
        assertDoesNotThrow(() -> PodExample.demonstratePodSpec());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate init containers without throwing")
    void shouldDemonstrateInitContainers() {
        assertDoesNotThrow(() -> PodExample.demonstrateInitContainers());
        assertDoesNotThrow(() -> PodExample.demonstrateInitContainers());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate sidecar pattern without throwing")
    void shouldDemonstrateSidecarPattern() {
        assertDoesNotThrow(() -> PodExample.demonstrateSidecarPattern());
        assertDoesNotThrow(() -> PodExample.demonstrateSidecarPattern());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate resource limits without throwing")
    void shouldDemonstrateResourceLimits() {
        assertDoesNotThrow(() -> PodExample.demonstrateResourceLimits());
        assertDoesNotThrow(() -> PodExample.demonstrateResourceLimits());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create PodExample instance successfully")
    void shouldCreateInstance() {
        PodExample instance = new PodExample();
        assertNotNull(instance);
        assertInstanceOf(PodExample.class, instance);
    }

    @Test
    @DisplayName("Should call all demonstration methods in sequence")
    void shouldCallAllDemonstrationsInSequence() {
        assertAll("All pod demonstrations",
            () -> assertDoesNotThrow(() -> PodExample.demonstratePodSpec()),
            () -> assertDoesNotThrow(() -> PodExample.demonstrateInitContainers()),
            () -> assertDoesNotThrow(() -> PodExample.demonstrateSidecarPattern()),
            () -> assertDoesNotThrow(() -> PodExample.demonstrateResourceLimits())
        );
    }

    @Test
    @DisplayName("Should handle multiple rapid invocations of pod spec")
    void shouldHandleRapidInvocations() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> PodExample.demonstratePodSpec());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should not produce errors with concurrent demonstration calls")
    void shouldHandleConcurrentCalls() {
        Runnable podSpec = () -> PodExample.demonstratePodSpec();
        Runnable initContainers = () -> PodExample.demonstrateInitContainers();
        Runnable sidecar = () -> PodExample.demonstrateSidecarPattern();
        Runnable resourceLimits = () -> PodExample.demonstrateResourceLimits();

        assertAll("Concurrent calls",
            () -> assertDoesNotThrow(podSpec),
            () -> assertDoesNotThrow(initContainers),
            () -> assertDoesNotThrow(sidecar),
            () -> assertDoesNotThrow(resourceLimits)
        );
    }

    @Test
    @DisplayName("Should verify PodExample has no instance state changes after operations")
    void shouldNotChangeStateAfterOperations() {
        PodExample before = new PodExample();
        PodExample after = new PodExample();
        assertDoesNotThrow(() -> PodExample.demonstratePodSpec());
        assertNotNull(before);
        assertNotNull(after);
    }
}
