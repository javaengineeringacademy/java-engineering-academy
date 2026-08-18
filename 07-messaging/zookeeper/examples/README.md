# Comprehensive Examples

## Example 1: Service Discovery Complete

```java
package academy.messaging.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.util.Collection;

public class ServiceDiscoveryExample {
    
    public static void main(String[] args) throws Exception {
        // Create client
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .namespace("service-discovery")
            .build();
        client.start();
        
        // Service Discovery
        ServiceDiscovery<String> discovery = ServiceDiscoveryBuilder.builder(String.class)
            .client(client)
            .basePath("/services")
            .build();
        discovery.start();
        
        // Register service
        ServiceInstance<String> instance = ServiceInstance.<String>builder()
            .name("my-service")
            .payload("1.0.0")
            .address("localhost")
            .port(8080)
            .build();
        discovery.registerService(instance);
        
        // Discover services
        Collection<ServiceInstance<String>> instances = discovery.queryForInstances("my-service");
        for (ServiceInstance<String> svc : instances) {
            System.out.println("Found: " + svc.getAddress() + ":" + svc.getPort());
        }
        
        // Service Provider with load balancing
        ServiceProvider<String> provider = discovery.serviceProviderBuilder()
            .serviceName("my-service")
            .providerStrategy(new RandomStrategy<>())
            .build();
        provider.start();
        
        ServiceInstance<String> selected = provider.getInstance();
        System.out.println("Selected: " + selected.getAddress() + ":" + selected.getPort());
        
        provider.close();
        discovery.close();
        client.close();
    }
}
```

---

## Example 2: Leader Election Complete

```java
package academy.messaging.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;

public class LeaderElectionExample {
    
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        // Create leader selector
        LeaderSelector selector = new LeaderSelector(client, "/leader/election",
            new LeaderSelectorListener() {
                @Override
                public void takeLeadership(CuratorFramework client) throws Exception {
                    System.out.println("I am the leader!");
                    // Do leader work
                    Thread.sleep(10000);
                    System.out.println("Releasing leadership");
                }
                
                @Override
                public void stateChanged(CuratorFramework client, 
                    org.apache.curator.framework.state.ConnectionState state) {
                    System.out.println("State changed: " + state);
                }
            });
        
        selector.autoRequeue();
        selector.start();
        
        Thread.sleep(60000);
        
        selector.close();
        client.close();
    }
}
```

---

## Example 3: Distributed Lock Complete

```java
package academy.messaging.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DistributedLockExample {
    
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        // Run multiple threads competing for lock
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    InterProcessMutex lock = new InterProcessMutex(client, "/locks/resource");
                    
                    for (int j = 0; j < 3; j++) {
                        if (lock.acquire(10, TimeUnit.SECONDS)) {
                            try {
                                int value = counter.incrementAndGet();
                                System.out.println("Thread " + threadId + 
                                    " acquired lock, value: " + value);
                                Thread.sleep(100);
                            } finally {
                                lock.release();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        
        Thread.sleep(30000);
        client.close();
    }
}
```

---

## Example 4: Configuration Management Complete

```java
package academy.messaging.zookeeper.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.cache.NodeCache;

public class ConfigurationManagementExample {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        // Create config
        String config = "{\"db.url\":\"jdbc:mysql://localhost:3306/mydb\"," +
            "\"db.user\":\"admin\",\"cache.ttl\":\"300\"}";
        
        if (client.checkExists().forPath("/config/app1") == null) {
            client.create().creatingParentsIfNeeded()
                .forPath("/config/app1", config.getBytes());
        }
        
        // Watch for changes
        NodeCache cache = new NodeCache(client, "/config/app1");
        cache.getListenable().addListener(() -> {
            if (cache.getCurrentData() != null) {
                String newConfig = new String(cache.getCurrentData().getData());
                System.out.println("Config updated: " + newConfig);
                
                // Reload configuration
                reloadConfiguration(newConfig);
            }
        });
        cache.start();
        
        // Simulate config update
        Thread.sleep(5000);
        String updatedConfig = "{\"db.url\":\"jdbc:mysql://newhost:3306/mydb\"," +
            "\"db.user\":\"admin\",\"cache.ttl\":\"600\"}";
        client.setData().forPath("/config/app1", updatedConfig.getBytes());
        
        Thread.sleep(10000);
        cache.close();
        client.close();
    }
    
    private static void reloadConfiguration(String config) {
        try {
            // Parse and apply new configuration
            System.out.println("Reloading configuration...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Example 5: Complete Microservice

```java
package academy.messaging.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.leader.LeaderSelector;

import java.util.concurrent.TimeUnit;

public class MicroserviceExample {
    
    private final CuratorFramework client;
    private final ServiceDiscovery<String> discovery;
    private final String serviceName;
    private final int port;
    
    public MicroserviceExample(String serviceName, int port) throws Exception {
        this.serviceName = serviceName;
        this.port = port;
        
        this.client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        this.discovery = ServiceDiscoveryBuilder.builder(String.class)
            .client(client)
            .basePath("/services")
            .build();
        discovery.start();
    }
    
    public void start() throws Exception {
        // Register service
        ServiceInstance<String> instance = ServiceInstance.<String>builder()
            .name(serviceName)
            .address("localhost")
            .port(port)
            .build();
        discovery.registerService(instance);
        
        // Setup leader election for coordination
        LeaderSelector selector = new LeaderSelector(client, "/leader/" + serviceName,
            (leaderClient) -> {
                System.out.println(serviceName + " is the leader");
                // Do leader-specific work
            });
        selector.autoRequeue();
        selector.start();
        
        System.out.println(serviceName + " started on port " + port);
    }
    
    public void stop() throws Exception {
        discovery.close();
        client.close();
    }
    
    public static void main(String[] args) throws Exception {
        // Start multiple instances
        for (int i = 0; i < 3; i++) {
            MicroserviceExample service = new MicroserviceExample("user-service", 8080 + i);
            service.start();
        }
        
        Thread.sleep(60000);
    }
}
```

---

## Example 6: Distributed Queue

```java
package academy.messaging.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;

public class DistributedQueueExample {
    
    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        // Create queue
        QueueSerializer<String> serializer = new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }
            
            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
        
        QueueConsumer<String> consumer = item -> {
            System.out.println("Consumed: " + item);
        };
        
        DistributedQueue<String> queue = QueueBuilder.builder(
            client, consumer, serializer, "/queue/tasks").build();
        queue.start();
        
        // Produce items
        for (int i = 0; i < 10; i++) {
            queue.publish("task-" + i);
            System.out.println("Published: task-" + i);
        }
        
        Thread.sleep(5000);
        queue.close();
        client.close();
    }
}
```
