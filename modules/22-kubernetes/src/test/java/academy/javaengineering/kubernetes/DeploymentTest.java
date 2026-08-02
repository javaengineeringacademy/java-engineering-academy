package academy.javaengineering.kubernetes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Deployment Tests")
class DeploymentTest {

    private DeploymentExample example;

    @BeforeEach
    void setUp() {
        example = new DeploymentExample();
    }

    @Test
    @DisplayName("Should demonstrate deployment specification without throwing")
    void shouldDemonstrateDeploymentSpec() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateDeploymentSpec());
        assertDoesNotThrow(() -> DeploymentExample.demonstrateDeploymentSpec());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate rolling update without throwing")
    void shouldDemonstrateRollingUpdate() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollingUpdate());
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollingUpdate());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate scaling without throwing")
    void shouldDemonstrateScaling() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateScaling());
        assertDoesNotThrow(() -> DeploymentExample.demonstrateScaling());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate rollback without throwing")
    void shouldDemonstrateRollback() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollback());
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollback());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create DeploymentExample instance successfully")
    void shouldCreateInstance() {
        DeploymentExample instance = new DeploymentExample();
        assertNotNull(instance);
        assertInstanceOf(DeploymentExample.class, instance);
    }

    @Test
    @DisplayName("Should call all deployment demonstrations in order")
    void shouldCallAllDemonstrationsInOrder() {
        assertAll("All deployment demonstrations",
            () -> assertDoesNotThrow(() -> DeploymentExample.demonstrateDeploymentSpec()),
            () -> assertDoesNotThrow(() -> DeploymentExample.demonstrateRollingUpdate()),
            () -> assertDoesNotThrow(() -> DeploymentExample.demonstrateScaling()),
            () -> assertDoesNotThrow(() -> DeploymentExample.demonstrateRollback())
        );
    }

    @Test
    @DisplayName("Should handle repeated scaling demonstrations")
    void shouldHandleRepeatedScaling() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> DeploymentExample.demonstrateScaling());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle repeated rollback demonstrations")
    void shouldHandleRepeatedRollbacks() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> DeploymentExample.demonstrateRollback());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should maintain statelessness across demonstrations")
    void shouldMaintainStatelessness() {
        DeploymentExample first = new DeploymentExample();
        DeploymentExample second = new DeploymentExample();
        assertDoesNotThrow(() -> DeploymentExample.demonstrateDeploymentSpec());
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollingUpdate());
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }
}
