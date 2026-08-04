package academy.javaengineering.cicd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CI/CD Tests")
class CICDTest {

    @Test
    @DisplayName("Should have Java pipeline stages")
    void testPipelineStages() {
        var stages = PipelineConfig.getJavaPipelineStages();
        assertFalse(stages.isEmpty());
        assertTrue(stages.stream().anyMatch(s -> s.name().equals("Build")));
        assertTrue(stages.stream().anyMatch(s -> s.name().equals("Test")));
    }

    @Test
    @DisplayName("Should have quality gates")
    void testQualityGates() {
        var gates = PipelineConfig.getQualityGates();
        assertFalse(gates.isEmpty());
        assertTrue(gates.stream().anyMatch(g -> g.name().equals("Code Coverage")));
    }

    @Test
    @DisplayName("Should have deployment strategies")
    void testDeploymentStrategies() {
        var strategies = DeploymentStrategies.getStrategies();
        assertFalse(strategies.isEmpty());
        assertTrue(strategies.stream().anyMatch(s -> s.name().equals("Blue-Green")));
    }
}
