package academy.javaengineering.microservices;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDiscoveryTest {

    @Test
    void testRegister() {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();
        discovery.register("test-service", "localhost", 8080, Map.of());
        assertEquals(1, discovery.discover("test-service").size());
    }

    @Test
    void testDiscover() {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();
        discovery.register("test-service", "localhost", 8080, Map.of());
        java.util.List<ServiceDiscoveryExample.ServiceInstance> instances = discovery.discover("test-service");
        assertFalse(instances.isEmpty());
    }

    @Test
    void testDiscoverOne() {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();
        discovery.register("test-service", "localhost", 8080, Map.of());
        ServiceDiscoveryExample.ServiceInstance instance = discovery.discoverOne("test-service");
        assertNotNull(instance);
    }

    @Test
    void testDeregister() {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();
        discovery.register("test-service", "localhost", 8080, Map.of());
        ServiceDiscoveryExample.ServiceInstance instance = discovery.discoverOne("test-service");
        discovery.deregister("test-service", instance.getId());
        assertTrue(discovery.discover("test-service").isEmpty());
    }

    @Test
    void testServiceCount() {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();
        discovery.register("service1", "localhost", 8081, Map.of());
        discovery.register("service2", "localhost", 8082, Map.of());
        Map<String, Integer> counts = discovery.getServiceCount();
        assertEquals(2, counts.size());
    }
}
