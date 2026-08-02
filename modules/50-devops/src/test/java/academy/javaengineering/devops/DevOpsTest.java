package academy.javaengineering.devops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DevOps Tests")
class DevOpsTest {

    @Test
    @DisplayName("PipelineStage should be created correctly")
    void testPipelineStage() {
        var stage = new DevOpsPipeline.PipelineStage("build", "SUCCESS", 5000, "Build completed");
        
        assertEquals("build", stage.name());
        assertEquals("SUCCESS", stage.status());
        assertEquals(5000, stage.durationMs());
    }

    @Test
    @DisplayName("CICDAutomation should run tests")
    void testRunTests() {
        var automation = new CICDAutomation();
        var results = automation.runTests("/project");
        
        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(r -> r.passed()));
    }

    @Test
    @DisplayName("ContainerOperations should build image")
    void testBuildImage() {
        var ops = new ContainerOperations();
        var info = ops.buildImage("Dockerfile", "myapp");
        
        assertEquals("myapp", info.name());
        assertEquals("built", info.status());
    }
}
