package academy.javaengineering.microservices;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MicroservicesFundamentalsTest {

    @Test
    void testRegisterService() {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();
        example.registerService("test-service", "localhost", 8080, "1.0.0");
        assertEquals(1, example.getAllServices().size());
    }

    @Test
    void testDiscoverService() {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();
        example.registerService("test-service", "localhost", 8080, "1.0.0");
        MicroservicesFundamentalsExample.ServiceInfo info = example.discoverService("test-service");
        assertNotNull(info);
        assertEquals("test-service", info.getName());
    }

    @Test
    void testDiscoverServiceNotFound() {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();
        assertThrows(IllegalArgumentException.class, () -> {
            example.discoverService("nonexistent");
        });
    }

    @Test
    void testHealthCheck() {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();
        example.registerService("test-service", "localhost", 8080, "1.0.0");
        example.healthCheck("test-service");
        assertNotNull(example.discoverService("test-service"));
    }

    @Test
    void testGetHealthyServices() {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();
        example.registerService("service1", "localhost", 8081, "1.0.0");
        example.registerService("service2", "localhost", 8082, "1.0.0");
        assertNotNull(example.getHealthyServices());
    }
}
