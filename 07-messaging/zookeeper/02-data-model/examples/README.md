# Data Model Examples

## Example 1: CRUD Operations

```java
package academy.messaging.zookeeper.datamodel.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CRUDOperations {
    
    private final CuratorFramework client;
    
    public CRUDOperations(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .namespace("example")
            .build();
        client.start();
    }
    
    public void createExample() throws Exception {
        // Simple create
        client.create().forPath("/config/app1", "data".getBytes());
        
        // Create with parents
        client.create().creatingParentsIfNeeded()
            .forPath("/services/api/instance-001", "data".getBytes());
        
        // Create ephemeral
        client.create().withMode(CreateMode.EPHEMERAL)
            .forPath("/services/api/instance-002", "data".getBytes());
        
        // Create sequential
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
            .forPath("/queue/task-", "data".getBytes());
        
        System.out.println("Created znodes successfully");
    }
    
    public void readExample() throws Exception {
        // Get data
        byte[] data = client.getData().forPath("/config/app1");
        System.out.println("Data: " + new String(data));
        
        // Get data with stat
        Stat stat = new Stat();
        data = client.getData().storingStatIn(stat).forPath("/config/app1");
        System.out.println("Version: " + stat.getVersion());
        
        // Get children
        java.util.List<String> children = client.getChildren().forPath("/services");
        System.out.println("Children: " + children);
        
        // Check existence
        Stat exists = client.checkExists().forPath("/config/app1");
        System.out.println("Exists: " + (exists != null));
    }
    
    public void updateExample() throws Exception {
        // Simple update
        client.setData().forPath("/config/app1", "new data".getBytes());
        
        // Update with version (optimistic locking)
        Stat stat = client.checkExists().forPath("/config/app1");
        client.setData().withVersion(stat.getVersion())
            .forPath("/config/app1", "versioned data".getBytes());
        
        System.out.println("Updated successfully");
    }
    
    public void deleteExample() throws Exception {
        // Simple delete
        client.delete().forPath("/config/app1");
        
        // Delete with version
        Stat stat = client.checkExists().forPath("/services/api/instance-001");
        if (stat != null) {
            client.delete().withVersion(stat.getVersion())
                .forPath("/services/api/instance-001");
        }
        
        // Delete children recursively
        client.delete().deletingChildrenIfNeeded().forPath("/services");
        
        System.out.println("Deleted successfully");
    }
    
    public static void main(String[] args) throws Exception {
        CRUDOperations example = new CRUDOperations("localhost:2181");
        
        example.createExample();
        example.readExample();
        example.updateExample();
        example.deleteExample();
        
        Thread.sleep(1000);
    }
}
```

---

## Example 2: Znode Type Demonstration

```java
package academy.messaging.zookeeper.datamodel.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class ZnodeTypesExample {
    
    private final CuratorFramework client;
    
    public ZnodeTypesExample(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    public void demonstratePersistent() throws Exception {
        System.out.println("=== Persistent Znodes ===");
        
        // Create persistent znode
        client.create().creatingParentsIfNeeded()
            .withMode(CreateMode.PERSISTENT)
            .forPath("/persistent/config", "persistent data".getBytes());
        
        System.out.println("Created persistent znode");
        System.out.println("Will survive session end");
    }
    
    public void demonstrateEphemeral() throws Exception {
        System.out.println("\n=== Ephemeral Znodes ===");
        
        // Create ephemeral znode
        client.create().creatingParentsIfNeeded()
            .withMode(CreateMode.EPHEMERAL)
            .forPath("/ephemeral/instance", "ephemeral data".getBytes());
        
        System.out.println("Created ephemeral znode");
        System.out.println("Will be deleted when session ends");
    }
    
    public void demonstrateSequential() throws Exception {
        System.out.println("\n=== Sequential Znodes ===");
        
        // Create sequential znodes
        for (int i = 0; i < 3; i++) {
            String path = client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/sequential/item-", ("item " + i).getBytes());
            System.out.println("Created: " + path);
        }
    }
    
    public void demonstrateEphemeralSequential() throws Exception {
        System.out.println("\n=== Ephemeral Sequential Znodes ===");
        
        // Create ephemeral sequential znodes
        for (int i = 0; i < 3; i++) {
            String path = client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/locks/resource-", ("lock " + i).getBytes());
            System.out.println("Created: " + path);
        }
    }
    
    public static void main(String[] args) throws Exception {
        ZnodeTypesExample example = new ZnodeTypesExample("localhost:2181");
        
        example.demonstratePersistent();
        example.demonstrateEphemeral();
        example.demonstrateSequential();
        example.demonstrateEphemeralSequential();
        
        System.out.println("\n=== Observations ===");
        System.out.println("1. Sequential znodes have increasing numbers");
        System.out.println("2. Ephemeral znodes will disappear when client disconnects");
        System.out.println("3. Persistent znodes remain until explicitly deleted");
        
        Thread.sleep(1000);
    }
}
```

---

## Example 3: ACL Management

```java
package academy.messaging.zookeeper.datamodel.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.List;

public class ACLExample {
    
    private final CuratorFramework client;
    
    public ACLExample(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    public void worldACL() throws Exception {
        System.out.println("=== World ACL ===");
        
        // Anyone can read, no one can write
        List<ACL> acls = Arrays.asList(
            new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE)
        );
        
        client.create().creatingParentsIfNeeded()
            .withACL(acls)
            .forPath("/acl/world", "world readable".getBytes());
        
        System.out.println("Created with world-readable ACL");
        
        // Verify
        List<ACL> actualAcls = client.getACL().forPath("/acl/world");
        System.out.println("ACLs: " + actualAcls);
    }
    
    public void digestACL() throws Exception {
        System.out.println("\n=== Digest ACL ===");
        
        // User:password authentication
        List<ACL> acls = Arrays.asList(
            new ACL(ZooDefs.Perms.ALL, 
                    new org.apache.zookeeper.data.Id("digest", "user:password"))
        );
        
        client.create().creatingParentsIfNeeded()
            .withACL(acls)
            .forPath("/acl/digest", "digest protected".getBytes());
        
        System.out.println("Created with digest ACL");
    }
    
    public void ipACL() throws Exception {
        System.out.println("\n=== IP ACL ===");
        
        // Only allow specific IP range
        List<ACL> acls = Arrays.asList(
            new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                    new org.apache.zookeeper.data.Id("ip", "192.168.1.0/24"))
        );
        
        client.create().creatingParentsIfNeeded()
            .withACL(acls)
            .forPath("/acl/ip", "ip restricted".getBytes());
        
        System.out.println("Created with IP-based ACL");
    }
    
    public void authACL() throws Exception {
        System.out.println("\n=== Auth ACL ===");
        
        // Add authentication
        client.addAuthInfo("digest", "admin:secret".getBytes());
        
        // Create with auth
        List<ACL> acls = Arrays.asList(
            new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.CREATOR_ALL_ACL)
        );
        
        client.create().creatingParentsIfNeeded()
            .withACL(acls)
            .forPath("/acl/auth", "auth protected".getBytes());
        
        System.out.println("Created with auth ACL");
    }
    
    public void updateACL() throws Exception {
        System.out.println("\n=== Update ACL ===");
        
        // Create with default ACL
        client.create().creatingParentsIfNeeded()
            .forPath("/acl/updateable", "data".getBytes());
        
        // Update ACL
        List<ACL> newAcls = Arrays.asList(
            new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE)
        );
        
        client.setACL().withACL(newAcls).forPath("/acl/updateable");
        
        System.out.println("Updated ACL successfully");
    }
    
    public static void main(String[] args) throws Exception {
        ACLExample example = new ACLExample("localhost:2181");
        
        example.worldACL();
        example.digestACL();
        example.ipACL();
        example.authACL();
        example.updateACL();
        
        System.out.println("\n=== ACL Summary ===");
        System.out.println("• World: Anyone");
        System.out.println("• Digest: User:password");
        System.out.println("• IP: IP range");
        System.out.println("• Auth: Authenticated users");
        
        Thread.sleep(1000);
    }
}
```

---

## Example 4: Watch Mechanism

```java
package academy.messaging.zookeeper.datamodel.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WatchExample {
    
    private final CuratorFramework client;
    
    public WatchExample(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    public void nodeCacheExample() throws Exception {
        System.out.println("=== NodeCache Example ===");
        
        // Create test znode
        client.create().creatingParentsIfNeeded()
            .forPath("/watch/node", "initial".getBytes());
        
        // Setup NodeCache
        NodeCache nodeCache = new NodeCache(client, "/watch/node");
        nodeCache.getListenable().addListener(() -> {
            System.out.println("Node changed: " + 
                new String(nodeCache.getCurrentData().getData()));
        });
        nodeCache.start();
        
        // Update znode
        Thread.sleep(100);
        client.setData().forPath("/watch/node", "updated".getBytes());
        
        Thread.sleep(1000);
        nodeCache.close();
    }
    
    public void pathChildrenCacheExample() throws Exception {
        System.out.println("\n=== PathChildrenCache Example ===");
        
        // Create test directory
        client.create().creatingParentsIfNeeded()
            .forPath("/watch/children".getBytes());
        
        // Setup PathChildrenCache
        PathChildrenCache cache = new PathChildrenCache(client, "/watch/children", true);
        cache.getListenable().addListener((client, event) -> {
            System.out.println("Event: " + event.getType() + 
                " Path: " + event.getData().getPath());
        });
        cache.start();
        
        // Add children
        Thread.sleep(100);
        client.create().forPath("/watch/children/child1", "data1".getBytes());
        client.create().forPath("/watch/children/child2", "data2".getBytes());
        
        Thread.sleep(1000);
        cache.close();
    }
    
    public void treeCacheExample() throws Exception {
        System.out.println("\n=== TreeCache Example ===");
        
        // Create test hierarchy
        client.create().creatingParentsIfNeeded()
            .forPath("/watch/tree/config", "config".getBytes());
        client.create().creatingParentsIfNeeded()
            .forPath("/watch/tree/services/api", "api".getBytes());
        
        // Setup TreeCache
        TreeCache treeCache = new TreeCache(client, "/watch/tree");
        treeCache.getListenable().addListener((client, event) -> {
            System.out.println("Event: " + event.getType() + 
                " Path: " + event.getData().getPath());
        });
        treeCache.start();
        
        // Modify hierarchy
        Thread.sleep(100);
        client.setData().forPath("/watch/tree/config", "new config".getBytes());
        client.create().forPath("/watch/tree/services/worker", "worker".getBytes());
        
        Thread.sleep(1000);
        treeCache.close();
    }
    
    public static void main(String[] args) throws Exception {
        WatchExample example = new WatchExample("localhost:2181");
        
        example.nodeCacheExample();
        example.pathChildrenCacheExample();
        example.treeCacheExample();
        
        System.out.println("\n=== Watch Summary ===");
        System.out.println("• NodeCache: Single node changes");
        System.out.println("• PathChildrenCache: Children changes");
        System.out.println("• TreeCache: Entire subtree changes");
    }
}
```

---

## Example 5: Data Serialization

```java
package academy.messaging.zookeeper.datamodel.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.Serializable;

public class SerializationExample {
    
    private final CuratorFramework client;
    private final ObjectMapper mapper = new ObjectMapper();
    
    public SerializationExample(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    // Simple POJO
    public static class ServiceInstance implements Serializable {
        private String host;
        private int port;
        private String status;
        private long timestamp;
        
        public ServiceInstance() {}
        
        public ServiceInstance(String host, int port, String status) {
            this.host = host;
            this.port = port;
            this.status = status;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters and setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    public void jsonExample() throws Exception {
        System.out.println("=== JSON Serialization ===");
        
        // Create instance
        ServiceInstance instance = new ServiceInstance("10.0.0.1", 8080, "UP");
        
        // Serialize to JSON
        String json = mapper.writeValueAsString(instance);
        System.out.println("JSON: " + json);
        
        // Store in Zookeeper
        client.create().creatingParentsIfNeeded()
            .forPath("/services/api/instance-001", json.getBytes());
        
        // Read and deserialize
        byte[] data = client.getData().forPath("/services/api/instance-001");
        ServiceInstance restored = mapper.readValue(data, ServiceInstance.class);
        
        System.out.println("Restored: " + restored.getHost() + ":" + restored.getPort());
    }
    
    public void versionedExample() throws Exception {
        System.out.println("\n=== Versioned Data ===");
        
        // Version 1
        String v1 = "{\"version\":1,\"host\":\"10.0.0.1\"}";
        client.create().creatingParentsIfNeeded()
            .forPath("/config/versioned", v1.getBytes());
        
        // Version 2
        String v2 = "{\"version\":2,\"host\":\"10.0.0.2\",\"newField\":\"value\"}";
        client.setData().forPath("/config/versioned", v2.getBytes());
        
        // Read with version handling
        byte[] data = client.getData().forPath("/config/versioned");
        String json = new String(data);
        
        // Parse and handle version
        if (json.contains("\"version\":2")) {
            System.out.println("Handling version 2");
        }
    }
    
    public static void main(String[] args) throws Exception {
        SerializationExample example = new SerializationExample("localhost:2181");
        
        example.jsonExample();
        example.versionedExample();
        
        System.out.println("\n=== Serialization Tips ===");
        System.out.println("• Use JSON for readability");
        System.out.println("• Use Protobuf for performance");
        System.out.println("• Include version for compatibility");
        System.out.println("• Keep payload small");
    }
}
```
