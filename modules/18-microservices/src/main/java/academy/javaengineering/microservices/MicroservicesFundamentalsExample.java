package academy.javaengineering.microservices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MicroservicesFundamentalsExample {

    private final Map<String, ServiceInfo> services = new ConcurrentHashMap<>();

    public static class ServiceInfo {
        private final String name;
        private final String host;
        private final int port;
        private final String version;
        private ServiceStatus status;

        public ServiceInfo(String name, String host, int port, String version) {
            this.name = name;
            this.host = host;
            this.port = port;
            this.version = version;
            this.status = ServiceStatus.UP;
        }

        public String getName() { return name; }
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getVersion() { return version; }
        public ServiceStatus getStatus() { return status; }
        public void setStatus(ServiceStatus status) { this.status = status; }

        public String getUrl() {
            return "http://" + host + ":" + port;
        }
    }

    public enum ServiceStatus {
        UP, DOWN, STARTING, STOPPING
    }

    public void registerService(String name, String host, int port, String version) {
        ServiceInfo info = new ServiceInfo(name, host, port, version);
        services.put(name, info);
        System.out.println("Service registered: " + name + " at " + host + ":" + port);
    }

    public ServiceInfo discoverService(String name) {
        ServiceInfo info = services.get(name);
        if (info == null) {
            throw new IllegalArgumentException("Service not found: " + name);
        }
        if (info.getStatus() != ServiceStatus.UP) {
            throw new RuntimeException("Service is not available: " + name);
        }
        return info;
    }

    public List<ServiceInfo> getAllServices() {
        return new ArrayList<>(services.values());
    }

    public List<ServiceInfo> getHealthyServices() {
        return services.values().stream()
                .filter(s -> s.getStatus() == ServiceStatus.UP)
                .toList();
    }

    public void healthCheck(String serviceName) {
        ServiceInfo info = services.get(serviceName);
        if (info != null) {
            boolean healthy = Math.random() > 0.1;
            info.setStatus(healthy ? ServiceStatus.UP : ServiceStatus.DOWN);
            System.out.println("Health check for " + serviceName + ": " + info.getStatus());
        }
    }

    public static void main(String[] args) {
        MicroservicesFundamentalsExample example = new MicroservicesFundamentalsExample();

        System.out.println("=== Microservices Fundamentals Demo ===\n");

        example.registerService("user-service", "localhost", 8081, "1.0.0");
        example.registerService("order-service", "localhost", 8082, "1.0.0");
        example.registerService("product-service", "localhost", 8083, "1.0.0");

        System.out.println("\n--- Discovering Services ---");
        try {
            ServiceInfo userService = example.discoverService("user-service");
            System.out.println("Found: " + userService.getName() + " at " + userService.getUrl());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Health Checks ---");
        example.services.keySet().forEach(example::healthCheck);

        System.out.println("\n--- Healthy Services ---");
        List<ServiceInfo> healthy = example.getHealthyServices();
        System.out.println("Healthy services: " + healthy.size());
    }
}
