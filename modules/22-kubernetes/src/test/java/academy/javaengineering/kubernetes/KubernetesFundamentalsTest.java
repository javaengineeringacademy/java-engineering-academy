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
    @DisplayName("Should demonstrate cluster architecture")
    void shouldDemonstrateClusterArchitecture() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateClusterArchitecture());
    }

    @Test
    @DisplayName("Should demonstrate kubectl commands")
    void shouldDemonstrateKubectlCommands() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateKubectlCommands());
    }

    @Test
    @DisplayName("Should demonstrate resource types")
    void shouldDemonstrateResourceTypes() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateResourceTypes());
    }

    @Test
    @DisplayName("Should demonstrate labels and selectors")
    void shouldDemonstrateLabelsAndSelectors() {
        assertDoesNotThrow(() -> KubernetesFundamentalsExample.demonstrateLabelsAndSelectors());
    }
}
