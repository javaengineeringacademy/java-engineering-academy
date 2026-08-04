package academy.javaengineering.docker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Docker Fundamentals Tests")
class DockerFundamentalsTest {

    @TempDir
    Path tempDir;

    private DockerFundamentalsExample example;

    @BeforeEach
    void setUp() {
        example = new DockerFundamentalsExample();
    }

    @Test
    @DisplayName("Should demonstrate Docker CLI commands")
    void shouldDemonstrateDockerCommands() {
        assertDoesNotThrow(() -> DockerFundamentalsExample.demonstrateDockerCommands());
    }

    @Test
    @DisplayName("Should demonstrate Dockerfile syntax")
    void shouldDemonstrateDockerfileSyntax() {
        assertDoesNotThrow(() -> DockerFundamentalsExample.demonstrateDockerfileSyntax());
    }

    @Test
    @DisplayName("Should generate valid Dockerfile")
    void shouldGenerateValidDockerfile() throws IOException {
        Path dockerfilePath = tempDir.resolve("Dockerfile");
        
        DockerFundamentalsExample.generateDockerfile(dockerfilePath);
        
        assertTrue(Files.exists(dockerfilePath));
        String content = Files.readString(dockerfilePath);
        assertTrue(content.contains("FROM eclipse-temurin:21-jre-jammy"));
        assertTrue(content.contains("EXPOSE 8080"));
        assertTrue(content.contains("HEALTHCHECK"));
        assertTrue(content.contains("USER app"));
    }

    @Test
    @DisplayName("Dockerfile should contain security best practices")
    void dockerfileShouldContainSecurityPractices() throws IOException {
        Path dockerfilePath = tempDir.resolve("Dockerfile");
        
        DockerFundamentalsExample.generateDockerfile(dockerfilePath);
        
        String content = Files.readString(dockerfilePath);
        assertTrue(content.contains("USER app"), "Should run as non-root user");
        assertTrue(content.contains("HEALTHCHECK"), "Should have health check");
        assertTrue(content.contains("-XX:+UseContainerSupport"), "Should use container support");
    }

    @Test
    @DisplayName("Should list container lifecycle states")
    void shouldListContainerLifecycleStates() {
        assertDoesNotThrow(() -> DockerFundamentalsExample.demonstrateContainerLifecycle());
    }

    @Test
    @DisplayName("Should list Docker image layers")
    void shouldListDockerImageLayers() {
        assertDoesNotThrow(() -> DockerFundamentalsExample.demonstrateImageManagement());
    }

    @Test
    @DisplayName("Should generate Dockerfile with JVM tuning")
    void shouldGenerateDockerfileWithJvmTuning() throws IOException {
        Path dockerfilePath = tempDir.resolve("Dockerfile");
        
        DockerFundamentalsExample.generateDockerfile(dockerfilePath);
        
        String content = Files.readString(dockerfilePath);
        assertTrue(content.contains("-XX:MaxRAMPercentage=75.0"), 
            "Should set MaxRAMPercentage for container support");
    }

    @Test
    @DisplayName("Should handle file creation errors gracefully")
    void shouldHandleFileCreationErrors() {
        Path invalidPath = Path.of("/nonexistent/path/Dockerfile");
        
        assertThrows(IOException.class, () -> 
            DockerFundamentalsExample.generateDockerfile(invalidPath));
    }
}
