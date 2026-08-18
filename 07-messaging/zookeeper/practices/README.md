# Practices

## Practice 1: Build a Service Registry

### Objective
Create a service registry that allows services to register and discover each other.

### Requirements
- Services register with name, host, port
- Services can discover other services
- Changes are watched in real-time
- Handle service deregistration

### Implementation

```java
package academy.messaging.zookeeper.practices;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {
    
    private final CuratorFramework client;
    private final ConcurrentHashMap<String, ServiceInstance> services 
        = new ConcurrentHashMap<>();
    
    public ServiceRegistry(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    // Register a service
    public void register(String name, String host, int port) throws Exception {
        String path = "/services/" + name + "/" + host + ":" + port;
        
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(path, (host + ":" + port).getBytes());
        }
        
        services.put(name + ":" + host + ":" + port, 
            new ServiceInstance(name, host, port));
        
        System.out.println("Registered: " + name + " at " + host + ":" + port);
    }
    
    // Discover services
    public List<ServiceInstance> discover(String name) throws Exception {
        String path = "/services/" + name;
        List<String> children = client.getChildren().forPath(path);
        
        List<ServiceInstance> instances = new ArrayList<>();
        for (String child : children) {
            byte[] data = client.getData().forPath(path + "/" + child);
            String[] parts = new String(data).split(":");
            instances.add(new ServiceInstance(name, parts[0], Integer.parseInt(parts[1])));
        }
        
        return instances;
    }
    
    // Deregister
    public void deregister(String name, String host, int port) throws Exception {
        String path = "/services/" + name + "/" + host + ":" + port;
        client.delete().forPath(path);
        services.remove(name + ":" + host + ":" + port);
    }
    
    static class ServiceInstance {
        String name;
        String host;
        int port;
        
        ServiceInstance(String name, String host, int port) {
            this.name = name;
            this.host = host;
            this.port = port;
        }
    }
}
```

### Tasks
1. Implement the ServiceRegistry class
2. Add health check functionality
3. Implement load balancing
4. Add watch for real-time updates

---

## Practice 2: Build a Distributed Lock

### Objective
Implement a distributed lock that supports:
- Lock acquisition with timeout
- Lock release
- Deadlock prevention

### Requirements
- Use ephemeral sequential znodes
- Implement proper watch mechanism
- Handle session expiry

### Implementation

```java
package academy.messaging.zookeeper.practices;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedLock {
    
    private final CuratorFramework client;
    private final String lockPath;
    private String myLockNode;
    
    public DistributedLock(CuratorFramework client, String lockPath) {
        this.client = client;
        this.lockPath = lockPath;
    }
    
    public boolean acquire(long timeout, TimeUnit unit) throws Exception {
        // Ensure lock path exists
        if (client.checkExists().forPath(lockPath) == null) {
            client.create().creatingParentsIfNeeded()
                .forPath(lockPath, new byte[0]);
        }
        
        // Create ephemeral sequential znode
        myLockNode = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
            .forPath(lockPath + "/lock-", new byte[0]);
        
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        
        while (System.currentTimeMillis() < endTime) {
            List<String> children = client.getChildren().forPath(lockPath);
            children.sort(String::compareTo);
            
            String myShortNode = myLockNode.substring(myLockNode.lastIndexOf('/') + 1);
            int myIndex = children.indexOf(myShortNode);
            
            if (myIndex == 0) {
                return true; // Lock acquired
            }
            
            // Watch the next lower node
            String watchNode = children.get(myIndex - 1);
            String watchPath = lockPath + "/" + watchNode;
            
            CountDownLatch latch = new CountDownLatch(1);
            client.getData().usingWatcher(event -> latch.countDown())
                .forPath(watchPath);
            
            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) {
                break;
            }
            
            latch.await(remaining, TimeUnit.MILLISECONDS);
        }
        
        cleanup();
        return false;
    }
    
    public void release() throws Exception {
        if (myLockNode != null) {
            client.delete().forPath(myLockNode);
            myLockNode = null;
        }
    }
    
    private void cleanup() throws Exception {
        if (myLockNode != null) {
            client.delete().quietly().forPath(myLockNode);
            myLockNode = null;
        }
    }
}
```

### Tasks
1. Implement the DistributedLock class
2. Add deadlock detection
3. Implement lock reentry
4. Add lock queuing visualization

---

## Practice 3: Build a Leader Election

### Objective
Implement a leader election system where:
- Only one instance is leader at a time
- Leader fails over automatically
- New instances can join election

### Requirements
- Use ephemeral sequential znodes
- Implement watch mechanism
- Handle network partitions

### Implementation

```java
package academy.messaging.zookeeper.practices;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LeaderElection {
    
    private final CuratorFramework client;
    private final String electionPath;
    private final String instanceId;
    private volatile boolean isLeader = false;
    private String myNode;
    
    public LeaderElection(CuratorFramework client, String electionPath, 
                          String instanceId) {
        this.client = client;
        this.electionPath = electionPath;
        this.instanceId = instanceId;
    }
    
    public void participate() throws Exception {
        // Ensure election path exists
        if (client.checkExists().forPath(electionPath) == null) {
            client.create().creatingParentsIfNeeded()
                .forPath(electionPath, new byte[0]);
        }
        
        // Create ephemeral sequential znode
        myNode = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
            .forPath(electionPath + "/candidate-", instanceId.getBytes());
        
        checkLeadership();
    }
    
    private void checkLeadership() throws Exception {
        while (true) {
            List<String> children = client.getChildren().forPath(electionPath);
            children.sort(String::compareTo);
            
            String myShortNode = myNode.substring(myNode.lastIndexOf('/') + 1);
            int myIndex = children.indexOf(myShortNode);
            
            if (myIndex == 0) {
                if (!isLeader) {
                    isLeader = true;
                    onBecomeLeader();
                }
            } else {
                if (isLeader) {
                    isLeader = false;
                    onLoseLeadership();
                }
                
                // Watch the next lower node
                String watchNode = children.get(myIndex - 1);
                String watchPath = electionPath + "/" + watchNode;
                
                CountDownLatch latch = new CountDownLatch(1);
                client.getData().usingWatcher(event -> latch.countDown())
                    .forPath(watchPath);
                
                latch.await();
            }
            
            Thread.sleep(1000);
        }
    }
    
    protected void onBecomeLeader() {
        System.out.println(instanceId + " became leader");
    }
    
    protected void onLoseLeadership() {
        System.out.println(instanceId + " lost leadership");
    }
    
    public boolean isLeader() {
        return isLeader;
    }
}
```

### Tasks
1. Implement the LeaderElection class
2. Add health check for leader
3. Implement leader resignation
4. Add election history tracking

---

## Practice 4: Build a Configuration Manager

### Objective
Create a configuration management system that:
- Stores configuration in Zookeeper
- Watches for changes
- Propagates updates to clients

### Requirements
- Support hierarchical configuration
- Real-time updates via watches
- Configuration versioning

### Implementation

```java
package academy.messaging.zookeeper.practices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationManager {
    
    private final CuratorFramework client;
    private final String configPath;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, String> configCache 
        = new ConcurrentHashMap<>();
    
    public ConfigurationManager(CuratorFramework client, String configPath) {
        this.client = client;
        this.configPath = configPath;
    }
    
    public void loadConfig(String appId) throws Exception {
        String path = configPath + "/" + appId;
        
        if (client.checkExists().forPath(path) != null) {
            byte[] data = client.getData().forPath(path);
            configCache.put(appId, new String(data));
        }
        
        // Watch for changes
        NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            if (cache.getCurrentData() != null) {
                String newConfig = new String(cache.getCurrentData().getData());
                configCache.put(appId, newConfig);
                onConfigChanged(appId, newConfig);
            }
        });
        cache.start();
    }
    
    public String getConfig(String appId) {
        return configCache.get(appId);
    }
    
    public void updateConfig(String appId, String config) throws Exception {
        String path = configPath + "/" + appId;
        
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded()
                .forPath(path, config.getBytes());
        } else {
            client.setData().forPath(path, config.getBytes());
        }
    }
    
    protected void onConfigChanged(String appId, String newConfig) {
        System.out.println("Config changed for " + appId);
    }
}
```

### Tasks
1. Implement the ConfigurationManager class
2. Add configuration validation
3. Implement configuration history
4. Add rollback capability

---

## Discussion Questions

1. How would you handle network partitions in service discovery?
2. What are the tradeoffs of different load balancing strategies?
3. How would you implement configuration encryption?
4. How would you handle configuration conflicts in multi-datacenter setup?
5. What metrics would you monitor for a production Zookeeper cluster?
