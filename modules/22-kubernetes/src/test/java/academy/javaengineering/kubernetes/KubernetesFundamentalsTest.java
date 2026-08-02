package academy.javaengineering.kubernetes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Kubernetes Fundamentals Tests")
class KubernetesFundamentalsTest {

    private KubernetesFundamentalsExample example;

    @BeforeEach
    void setUp() {
        example = new KubernetesFundamentalsExample();
    }

    @Test
    @DisplayName("Should demonstrate cluster architecture without throwing")
    void shouldDemonstrateClusterArchitecture() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateClusterArchitecture());
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateClusterArchitecture());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate kubectl commands without throwing")
    void shouldDemonstrateKubectlCommands() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateKubectlCommands());
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateKubectlCommands());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate resource types without throwing")
    void shouldDemonstrateResourceTypes() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateResourceTypes());
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateResourceTypes());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate labels and selectors without throwing")
    void shouldDemonstrateLabelsAndSelectors() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateLabelsAndSelectors());
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateLabelsAndSelectors());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create KubernetesFundamentalsExample instance successfully")
    void shouldCreateInstance() {
        KubernetesFundamentalsExample instance = new KubernetesFundamentalsExample();
        assertNotNull(instance);
        assertInstanceOf(KubernetesFundamentalsExample.class, instance);
    }

    @Test
    @DisplayName("Should call all fundamental demonstrations in sequence")
    void shouldCallAllDemonstrationsInSequence() {
        assertAll("All Kubernetes fundamental demonstrations",
            () -> assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateClusterArchitecture()),
            () -> assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateKubectlCommands()),
            () -> assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateResourceTypes()),
            () -> assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateLabelsAndSelectors())
        );
    }

    @Test
    @DisplayName("Should handle repeated kubectl command demonstrations")
    void shouldHandleRepeatedKubectlCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateKubectlCommands());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated resource type demonstrations")
    void shouldHandleRepeatedResourceTypeCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateResourceTypes());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain consistent behavior across multiple instances")
    void shouldMaintainConsistentBehavior() {
        KubernetesFundamentalsExample instance1 = new KubernetesFundamentalsExample();
        KubernetesFundamentalsExample instance2 = new KubernetesFundamentalsExample();
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateClusterArchitecture());
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateLabelsAndSelectors());
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
}
