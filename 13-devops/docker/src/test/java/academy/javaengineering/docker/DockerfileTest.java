package academy.javaengineering.docker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dockerfile Tests")
class DockerfileTest {

    @TempDir
    Path tempDir;

    private DockerfileExample example;

    @BeforeEach
    void setUp() {
        example = new DockerfileExample();
    }

    @Test
    @DisplayName("Should demonstrate layer optimization")
    void shouldDemonstrateLayerOptimization() {
        assertDoesNotThrow(() -> DockerfileExample.demonstrateLayerOptimization());
    }

    @Test
    @DisplayName("Should demonstrate multi-stage build")
    void shouldDemonstrateMultiStageBuild() {
        assertDoesNotThrow(() -> DockerfileExample.demonstrateMultiStageBuild());
    }

    @Test
    @DisplayName("Should demonstrate caching strategy")
    void shouldDemonstrateCachingStrategy() {
        assertDoesNotThrow(() -> DockerfileExample.demonstrateCachingStrategy());
    }

    @Test
    @DisplayName("Should demonstrate security best practices")
    void shouldDemonstrateSecurityBestPractices() {
        assertDoesNotThrow(() -> DockerfileExample.demonstrateSecurityBestPractices());
    }

    @Test
    @DisplayName("Should generate optimized Dockerfile")
    void shouldGenerateOptimizedDockerfile() {
        String dockerfile = DockerfileExample.generateOptimizedDockerfile();
        
        assertNotNull(dockerfile);
        assertTrue(dockerfile.contains("FROM eclipse-temurin:21-jdk-jammy AS builder"));
        assertTrue(dockerfile.contains("FROM eclipse-temurin:21-jre-jammy"));
        assertTrue(dockerfile.contains("USER spring"));
        assertTrue(dockerfile.contains("HEALTHCHECK"));
    }

    @Test
    @DisplayName("Dockerfile should use multi-stage build")
    void dockerfileShouldUseMultiStageBuild() {
        String dockerfile = DockerfileExample.generateOptimizedDockerfile();
        
        int fromCount = dockerfile.split("FROM ").length - 1;
        assertTrue(fromCount >= 2, "Should have at least 2 FROM instructions");
    }

    @Test
    @DisplayName("Dockerfile should include dependency caching")
    void dockerfileShouldIncludeDependencyCaching() {
        String dockerfile = DockerfileExample.generateOptimizedDockerfile();
        
        assertTrue(dockerfile.contains("pom.xml"), "Should copy pom.xml for dependency caching");
        assertTrue(dockerfile.contains("dependency:go-offline"), "Should use dependency:go-offline");
    }

    @Test
    @DisplayName("Dockerfile should set proper file ownership")
    void dockerfileShouldSetProperOwnership() {
        String dockerfile = DockerfileExample.generateOptimizedDockerfile();
        
        assertTrue(dockerfile.contains("COPY --from=builder"), "Should copy from builder stage");
        assertTrue(dockerfile.contains("chown"), "Should set file ownership");
        assertTrue(dockerfile.contains("USER spring"), "Should run as non-root user");
    }

    @Test
    @DisplayName("Dockerfile should include JVM container flags")
    void dockerfileShouldIncludeJvmContainerFlags() {
        String dockerfile = DockerfileExample.generateOptimizedDockerfile();
        
        assertTrue(dockerfile.contains("-XX:+UseContainerSupport"), 
            "Should enable container support");
        assertTrue(dockerfile.contains("-XX:MaxRAMPercentage=75.0"), 
            "Should set MaxRAMPercentage");
    }

    @Test
    @DisplayName("Should write Dockerfile to file system")
    void shouldWriteDockerfileToFileSystem() throws IOException {
        Path outputPath = tempDir.resolve("Dockerfile");
        
        Files.writeString(outputPath, DockerfileExample.generateOptimizedDockerfile());
        
        assertTrue(Files.exists(outputPath));
        String content = Files.readString(outputPath);
        assertTrue(content.contains("FROM eclipse-temurin:21-jdk-jammy AS builder"));
    }
}
