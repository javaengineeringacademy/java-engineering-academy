package academy.javaengineering.kubernetes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Service Tests")
class ServiceTest {

    private ServiceExample example;

    @BeforeEach
    void setUp() {
        example = new ServiceExample();
    }

    @Test
    @DisplayName("Should demonstrate service types")
    void shouldDemonstrateServiceTypes() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateServiceTypes());
    }

    @Test
    @DisplayName("Should demonstrate ClusterIP service")
    void shouldDemonstrateClusterIP() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP());
    }

    @Test
    @DisplayName("Should demonstrate NodePort service")
    void shouldDemonstrateNodePort() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort());
    }

    @Test
    @DisplayName("Should demonstrate LoadBalancer service")
    void shouldDemonstrateLoadBalancer() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateLoadBalancer());
    }
}
