package academy.javaengineering.microservices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceDiscoveryExample {

    private final Map<String, List<ServiceInstance>> registry = new ConcurrentHashMap<>();

    public static class ServiceInstance {
        private final String id;
        private final String serviceName;
        private final String host;
        private final int port;
        private final Map<String, String> metadata;
        private final long registrationTime;

        public ServiceInstance(String id, String serviceName, String host, int port, Map<String, String> metadata) {
            this.id = id;
            this.serviceName = serviceName;
            this.host = host;
            this.port = port;
            this.metadata = metadata != null ? metadata : new HashMap<>();
            this.registrationTime = System.currentTimeMillis();
        }

        public String getId() { return id; }
        public String getServiceName() { return serviceName; }
        public String getHost() { return host; }
        public int getPort() { return port; }
        public Map<String, String> getMetadata() { return metadata; }
        public long getRegistrationTime() { return registrationTime; }

        public String getUri() {
            return "http://" + host + ":" + port;
        }
    }

    public void register(String serviceName, String host, int port, Map<String, String> metadata) {
        String id = UUID.randomUUID().toString();
        ServiceInstance instance = new ServiceInstance(id, serviceName, host, port, metadata);
        registry.computeIfAbsent(serviceName, k -> new ArrayList<>()).add(instance);
        System.out.println("Registered: " + serviceName + " [" + id + "] at " + host + ":" + port);
    }

    public List<ServiceInstance> discover(String serviceName) {
        return registry.getOrDefault(serviceName, Collections.emptyList());
    }

    public ServiceInstance discoverOne(String serviceName) {
        List<ServiceInstance> instances = discover(serviceName);
        if (instances.isEmpty()) {
            throw new RuntimeException("No instances found for: " + serviceName);
        }
        return instances.get(new Random().nextInt(instances.size()));
    }

    public void deregister(String serviceName, String instanceId) {
        List<ServiceInstance> instances = registry.get(serviceName);
        if (instances != null) {
            instances.removeIf(i -> i.getId().equals(instanceId));
            System.out.println("Deregistered: " + instanceId);
        }
    }

    public Map<String, Integer> getServiceCount() {
        Map<String, Integer> counts = new HashMap<>();
        registry.forEach((name, instances) -> counts.put(name, instances.size()));
        return counts;
    }

    public static void main(String[] args) {
        ServiceDiscoveryExample discovery = new ServiceDiscoveryExample();

        System.out.println("=== Service Discovery Demo ===\n");

        discovery.register("user-service", "localhost", 8081, Map.of("version", "1.0"));
        discovery.register("user-service", "localhost", 8082, Map.of("version", "1.0"));
        discovery.register("order-service", "localhost", 8083, Map.of("version", "2.0"));

        System.out.println("\n--- Discover user-service ---");
        List<ServiceInstance> instances = discovery.discover("user-service");
        instances.forEach(i -> System.out.println("  " + i.getUri()));

        System.out.println("\n--- Random instance ---");
        ServiceInstance instance = discovery.discoverOne("order-service");
        System.out.println("Selected: " + instance.getUri());

        System.out.println("\n--- Service counts ---");
        System.out.println(discovery.getServiceCount());
    }
}
