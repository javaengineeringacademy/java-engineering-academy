package academy.javaengineering.docker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Docker Compose Tests")
class DockerComposeTest {

    @TempDir
    Path tempDir;

    private DockerComposeExample example;

    @BeforeEach
    void setUp() {
        example = new DockerComposeExample();
    }

    @Test
    @DisplayName("Should demonstrate basic compose structure")
    void shouldDemonstrateBasicComposeStructure() {
        assertDoesNotThrow(() -> DockerComposeExample.demonstrateBasicCompose());
    }

    @Test
    @DisplayName("Should demonstrate service dependencies")
    void shouldDemonstrateServiceDependencies() {
        assertDoesNotThrow(() -> DockerComposeExample.demonstrateServiceDependencies());
    }

    @Test
    @DisplayName("Should demonstrate network configuration")
    void shouldDemonstrateNetworkConfiguration() {
        assertDoesNotThrow(() -> DockerComposeExample.demonstrateNetworkConfiguration());
    }

    @Test
    @DisplayName("Should demonstrate volume management")
    void shouldDemonstrateVolumeManagement() {
        assertDoesNotThrow(() -> DockerComposeExample.demonstrateVolumeManagement());
    }

    @Test
    @DisplayName("Should demonstrate environment variables")
    void shouldDemonstrateEnvironmentVariables() {
        assertDoesNotThrow(() -> DockerComposeExample.demonstrateEnvironmentVariables());
    }

    @Test
    @DisplayName("Should generate full stack compose file")
    void shouldGenerateFullStackComposeFile() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertNotNull(compose);
        assertTrue(compose.contains("version: '3.8'"));
        assertTrue(compose.contains("services:"));
        assertTrue(compose.contains("networks:"));
        assertTrue(compose.contains("volumes:"));
    }

    @Test
    @DisplayName("Compose file should define multiple services")
    void composeFileShouldDefineMultipleServices() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("gateway:"), "Should have gateway service");
        assertTrue(compose.contains("app:"), "Should have app service");
        assertTrue(compose.contains("discovery:"), "Should have discovery service");
        assertTrue(compose.contains("db:"), "Should have database service");
        assertTrue(compose.contains("redis:"), "Should have redis service");
    }

    @Test
    @DisplayName("Compose file should include health checks")
    void composeFileShouldIncludeHealthChecks() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("healthcheck:"), "Should have health checks");
        assertTrue(compose.contains("condition: service_healthy"), 
            "Should use service_healthy condition");
    }

    @Test
    @DisplayName("Compose file should configure networks")
    void composeFileShouldConfigureNetworks() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("networks:"), "Should define networks");
        assertTrue(compose.contains("- web"), "Should use web network");
        assertTrue(compose.contains("- backend"), "Should use backend network");
    }

    @Test
    @DisplayName("Compose file should configure volumes")
    void composeFileShouldConfigureVolumes() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("volumes:"), "Should define volumes");
        assertTrue(compose.contains("pgdata:"), "Should have postgres volume");
    }

    @Test
    @DisplayName("Should write compose file to file system")
    void shouldWriteComposeFileToFileSystem() throws IOException {
        Path outputPath = tempDir.resolve("docker-compose.yml");
        
        Files.writeString(outputPath, DockerComposeExample.generateFullStackCompose());
        
        assertTrue(Files.exists(outputPath));
        String content = Files.readString(outputPath);
        assertTrue(content.contains("version: '3.8'"));
    }

    @Test
    @DisplayName("Compose file should include service dependencies")
    void composeFileShouldIncludeServiceDependencies() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("depends_on:"), "Should define dependencies");
    }

    @Test
    @DisplayName("Compose file should include environment variables")
    void composeFileShouldIncludeEnvironmentVariables() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("environment:"), "Should define environment");
    }

    @Test
    @DisplayName("Compose file should include port mappings")
    void composeFileShouldIncludePortMappings() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("ports:"), "Should define ports");
        assertTrue(compose.contains("- \"80:80\""), "Should map port 80");
        assertTrue(compose.contains("- \"8080:8080\""), "Should map port 8080");
    }

    @Test
    @DisplayName("Compose file should include volume mounts")
    void composeFileShouldIncludeVolumeMounts() {
        String compose = DockerComposeExample.generateFullStackCompose();
        
        assertTrue(compose.contains("volumes:"), "Should define volume mounts");
        assertTrue(compose.contains("/var/lib/postgresql/data"), 
            "Should mount postgres data");
    }
}
