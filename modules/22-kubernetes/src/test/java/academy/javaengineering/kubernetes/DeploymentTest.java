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
    @DisplayName("Should demonstrate deployment specification")
    void shouldDemonstrateDeploymentSpec() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateDeploymentSpec());
    }

    @Test
    @DisplayName("Should demonstrate rolling update")
    void shouldDemonstrateRollingUpdate() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollingUpdate());
    }

    @Test
    @DisplayName("Should demonstrate scaling")
    void shouldDemonstrateScaling() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateScaling());
    }

    @Test
    @DisplayName("Should demonstrate rollback")
    void shouldDemonstrateRollback() {
        assertDoesNotThrow(() -> DeploymentExample.demonstrateRollback());
    }
}
