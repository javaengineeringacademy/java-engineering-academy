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
    @DisplayName("Should demonstrate service types without throwing")
    void shouldDemonstrateServiceTypes() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateServiceTypes());
        assertDoesNotThrow(() -> ServiceExample.demonstrateServiceTypes());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate ClusterIP service without throwing")
    void shouldDemonstrateClusterIP() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP());
        assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate NodePort service without throwing")
    void shouldDemonstrateNodePort() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort());
        assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate LoadBalancer service without throwing")
    void shouldDemonstrateLoadBalancer() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateLoadBalancer());
        assertDoesNotThrow(() -> ServiceExample.demonstrateLoadBalancer());
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should create ServiceExample instance successfully")
    void shouldCreateInstance() {
        ServiceExample instance = new ServiceExample();
        assertNotNull(instance);
        assertInstanceOf(ServiceExample.class, instance);
    }

    @Test
    @DisplayName("Should call all service type demonstrations together")
    void shouldCallAllServiceTypeDemonstrations() {
        assertAll("All service type demonstrations",
            () -> assertDoesNotThrow(() -> ServiceExample.demonstrateServiceTypes()),
            () -> assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP()),
            () -> assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort()),
            () -> assertDoesNotThrow(() -> ServiceExample.demonstrateLoadBalancer())
        );
    }

    @Test
    @DisplayName("Should handle rapid repeated ClusterIP demonstrations")
    void shouldHandleRapidClusterIPCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should handle rapid repeated NodePort demonstrations")
    void shouldHandleRapidNodePortCalls() {
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort());
        }
        assertNotNull(example);
    }

    @Test
    @DisplayName("Should demonstrate all service types in correct order")
    void shouldDemonstrateInCorrectOrder() {
        assertDoesNotThrow(() -> ServiceExample.demonstrateServiceTypes());
        assertDoesNotThrow(() -> ServiceExample.demonstrateClusterIP());
        assertDoesNotThrow(() -> ServiceExample.demonstrateNodePort());
        assertDoesNotThrow(() -> ServiceExample.demonstrateLoadBalancer());
        assertNotNull(example);
    }
}
