# Service Discovery

> Package: `academy.messaging.zookeeper.servicediscovery`

## Overview

Service discovery enables services to find and communicate with each other. Zookeeper provides a reliable foundation for service registration and discovery.

## Service Discovery Pattern

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Service Discovery Flow                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐     1. Register      ┌─────────────┐                 │
│  │   Service   │ ─────────────────────►│  Zookeeper  │                 │
│  │   Provider  │                       │             │                 │
│  └─────────────┘                       └─────────────┘                 │
│        │                                     ▲                         │
│        │                                     │                         │
│        │  4. Call service                    │  3. Watch                │
│        │                                     │                         │
│        ▼                                     │                         │
│  ┌─────────────┐     2. Discover     ┌─────┴───────┐                 │
│  │   Service   │ ◄────────────────────│   Service   │                 │
│  │   Consumer  │                       │   Consumer  │                 │
│  └─────────────┘                       └─────────────┘                 │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## ZNode Structure

```
/services
├── /services/user-service
│   ├── /services/user-service/instance-0000000001 [ephemeral]
│   │   data: {"host":"10.0.0.1","port":8080,"status":"UP"}
│   ├── /services/user-service/instance-0000000002 [ephemeral]
│   │   data: {"host":"10.0.0.2","port":8080,"status":"UP"}
│   └── /services/user-service/instance-0000000003 [ephemeral]
│       data: {"host":"10.0.0.3","port":8080,"status":"UP"}
│
├── /services/order-service
│   ├── /services/order-service/instance-0000000001 [ephemeral]
│   │   data: {"host":"10.0.1.1","port":8081,"status":"UP"}
│   └── /services/order-service/instance-0000000002 [ephemeral]
│       data: {"host":"10.0.1.2","port":8081,"status":"UP"}
│
└── /services/payment-service
    └── /services/payment-service/instance-0000000001 [ephemeral]
        data: {"host":"10.0.2.1","port":8082,"status":"UP"}
```

## Implementation

### Service Registration

```java
package academy.messaging.zookeeper.servicediscovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;

public class ServiceRegistrar {
    
    private final ServiceDiscovery<ServiceMetadata> discovery;
    
    public ServiceRegistrar(CuratorFramework client, String basePath) {
        this.discovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
            .client(client)
            .basePath(basePath)
            .build();
    }
    
    public void register(String serviceName, String host, int port) throws Exception {
        ServiceInstance<ServiceMetadata> instance = ServiceInstance.<ServiceMetadata>builder()
            .name(serviceName)
            .payload(new ServiceMetadata(host, port, "UP"))
            .address(host)
            .port(port)
            .build();
        
        discovery.registerService(instance);
        System.out.println("Registered: " + serviceName + " at " + host + ":" + port);
    }
    
    public void unregister(String serviceName) throws Exception {
        discovery.unregisterService(serviceName);
        System.out.println("Unregistered: " + serviceName);
    }
    
    public static class ServiceMetadata {
        private String host;
        private int port;
        private String status;
        
        public ServiceMetadata() {}
        
        public ServiceMetadata(String host, int port, String status) {
            this.host = host;
            this.port = port;
            this.status = status;
        }
        
        // Getters and setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
```

### Service Discovery

```java
package academy.messaging.zookeeper.servicediscovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.util.Collection;
import java.util.List;

public class ServiceDiscoveryClient {
    
    private final ServiceDiscovery<ServiceMetadata> discovery;
    
    public ServiceDiscoveryClient(CuratorFramework client, String basePath) {
        this.discovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
            .client(client)
            .basePath(basePath)
            .build();
    }
    
    // Get all instances of a service
    public Collection<ServiceInstance<ServiceMetadata>> discoverAll(String serviceName) 
            throws Exception {
        return discovery.queryForInstances(serviceName);
    }
    
    // Get a single instance (load balanced)
    public ServiceInstance<ServiceMetadata> discoverOne(String serviceName) 
            throws Exception {
        ServiceProvider<ServiceMetadata> provider = discovery.serviceProviderBuilder()
            .serviceName(serviceName)
            .providerStrategy(new RandomStrategy<>())
            .build();
        
        provider.start();
        try {
            return provider.getInstance();
        } finally {
            provider.close();
        }
    }
    
    // Get instance by ID
    public ServiceInstance<ServiceMetadata> discoverById(String serviceName, String id) 
            throws Exception {
        return discovery.queryForInstance(serviceName, id);
    }
    
    // Get instances by role
    public List<ServiceInstance<ServiceMetadata>> discoverByRole(String serviceName, String role) 
            throws Exception {
        return discovery.queryForInstances(serviceName, role);
    }
}
```

### Service Consumer

```java
package academy.messaging.zookeeper.servicediscovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceConsumer {
    
    private final CuratorFramework client;
    private final ConcurrentMap<String, List<ServiceInstance<ServiceMetadata>>> serviceCache 
        = new ConcurrentHashMap<>();
    
    public ServiceConsumer(CuratorFramework client) {
        this.client = client;
    }
    
    public void watchService(String serviceName) throws Exception {
        String servicePath = "/services/" + serviceName;
        
        PathChildrenCache cache = new PathChildrenCache(client, servicePath, true);
        cache.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("Service added: " + event.getData().getPath());
                    refreshCache(serviceName);
                    break;
                case CHILD_REMOVED:
                    System.out.println("Service removed: " + event.getData().getPath());
                    refreshCache(serviceName);
                    break;
                case CHILD_UPDATED:
                    System.out.println("Service updated: " + event.getData().getPath());
                    refreshCache(serviceName);
                    break;
            }
        });
        cache.start();
    }
    
    private void refreshCache(String serviceName) throws Exception {
        String servicePath = "/services/" + serviceName;
        List<String> children = client.getChildren().forPath(servicePath);
        
        List<ServiceInstance<ServiceMetadata>> instances = new ArrayList<>();
        for (String child : children) {
            byte[] data = client.getData().forPath(servicePath + "/" + child);
            // Deserialize data
            instances.add(deserializeInstance(child, data));
        }
        
        serviceCache.put(serviceName, instances);
    }
    
    public ServiceInstance<ServiceMetadata> getService(String serviceName) {
        List<ServiceInstance<ServiceMetadata>> instances = serviceCache.get(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("No instances available for " + serviceName);
        }
        
        // Simple random load balancing
        int index = ThreadLocalRandom.current().nextInt(instances.size());
        return instances.get(index);
    }
}
```

## Load Balancing Strategies

### Random

```java
public class RandomStrategy implements LoadBalanceStrategy {
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        int index = ThreadLocalRandom.current().nextInt(instances.size());
        return instances.get(index);
    }
}
```

### Round Robin

```java
public class RoundRobinStrategy implements LoadBalanceStrategy {
    private final AtomicInteger counter = new AtomicInteger(0);
    
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        int index = Math.abs(counter.getAndIncrement() % instances.size());
        return instances.get(index);
    }
}
```

### Weighted Random

```java
public class WeightedRandomStrategy implements LoadBalanceStrategy {
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        int totalWeight = instances.stream()
            .mapToInt(i -> i.getWeight())
            .sum();
        
        int random = ThreadLocalRandom.current().nextInt(totalWeight);
        int currentWeight = 0;
        
        for (ServiceInstance instance : instances) {
            currentWeight += instance.getWeight();
            if (random < currentWeight) {
                return instance;
            }
        }
        
        return instances.get(instances.size() - 1);
    }
}
```

### Consistent Hashing

```java
public class ConsistentHashStrategy implements LoadBalanceStrategy {
    private final TreeMap<Long, ServiceInstance> ring = new TreeMap<>();
    
    public ConsistentHashStrategy(List<ServiceInstance> instances, int virtualNodes) {
        for (ServiceInstance instance : instances) {
            for (int i = 0; i < virtualNodes; i++) {
                long hash = hash(instance.getAddress() + "#" + i);
                ring.put(hash, instance);
            }
        }
    }
    
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        // Use request key for consistent hashing
        long hash = hash(getRequestKey());
        Map.Entry<Long, ServiceInstance> entry = ring.ceilingEntry(hash);
        return entry != null ? entry.getValue() : ring.firstEntry().getValue();
    }
    
    private long hash(String key) {
        // Use MurmurHash or similar
        return Math.abs((long) key.hashCode());
    }
}
```

## Health Checks

### Service Health

```java
public class ServiceHealthChecker {
    
    private final CuratorFramework client;
    
    public ServiceHealthChecker(CuratorFramework client) {
        this.client = client;
    }
    
    public void updateHealth(String serviceName, String instanceId, String status) 
            throws Exception {
        String path = "/services/" + serviceName + "/" + instanceId;
        
        byte[] data = client.getData().forPath(path);
        ServiceMetadata metadata = deserialize(data);
        metadata.setStatus(status);
        
        client.setData().forPath(path, serialize(metadata));
    }
    
    public List<ServiceInstance<ServiceMetadata>> getHealthyInstances(String serviceName) 
            throws Exception {
        return discoverAll(serviceName).stream()
            .filter(i -> "UP".equals(i.getPayload().getStatus()))
            .collect(Collectors.toList());
    }
}
```

### Health Check Patterns

```
Pattern 1: TTL-based
───────────────────────────────────────────────────────────────
Service writes to /services/name/instance with TTL
Watcher removes if not refreshed

Pattern 2: Heartbeat
───────────────────────────────────────────────────────────────
Service sends heartbeat every X seconds
Consumer checks last heartbeat time

Pattern 3: Health Endpoint
───────────────────────────────────────────────────────────────
Consumer calls /health endpoint
Service responds with status
```

## Best Practices

```
✓ Use ephemeral znodes for registration
✓ Include health status in instance data
✓ Use watches for real-time updates
✓ Implement circuit breaker for failed services
✓ Cache service instances locally
✓ Handle session expiry gracefully

✗ Don't store large data in znodes
✗ Don't poll for changes (use watches)
✗ Don't ignore health checks
✗ Don't use static instance lists
✗ Don't skip load balancing
```

## Summary

| Component | Purpose |
|-----------|---------|
| ServiceInstance | Represents a service instance |
| ServiceDiscovery | Manages registration |
| ServiceProvider | Discovers instances |
| PathChildrenCache | Watches for changes |
| LoadBalanceStrategy | Selects instance |
