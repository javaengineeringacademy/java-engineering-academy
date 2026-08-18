# Curator Framework

> Package: `academy.messaging.zookeeper.curator`

## Overview

Apache Curator is the recommended Java client for Zookeeper. It provides connection management, retry policies, and recipes for common patterns.

## Why Curator?

### Raw Zookeeper vs Curator

```
Raw Zookeeper API:
───────────────────────────────────────────────────────────────
• Manual connection management
• No automatic retries
• One-time watches only
• Complex error handling
• No built-in recipes

Curator Framework:
───────────────────────────────────────────────────────────────
• Automatic connection management
• Built-in retry policies
• Persistent watches
• Fluent API
• Built-in recipes (locks, barriers, caches)
• Session expiry handling
• Namespace support
```

## Setup

### Maven Dependency

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>5.5.0</version>
</dependency>
```

### Client Configuration

```java
CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("zk1:2181,zk2:2181,zk3:2181")
    .sessionTimeoutMs(30000)
    .connectionTimeoutMs(15000)
    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
    .namespace("my-app")
    .build();

client.start();
```

### Retry Policies

```java
// Exponential backoff (recommended)
new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries)
// Example: 1s base, 3 retries

// Fixed interval
new RetryNTimes(count, sleepTimeMs)
// Example: 5 retries, 1s each

// Forever retry
new RetryForever(sleepTimeMs)
// Example: Retry every 1s

// Bounded exponential
new BoundedExponentialBackoffRetry(baseSleep, maxSleep, maxRetries)
```

## CRUD Operations

### Create

```java
// Basic create
client.create().forPath("/path", "data".getBytes());

// Create with parents
client.create().creatingParentsIfNeeded()
    .forPath("/a/b/c", "data".getBytes());

// Create ephemeral
client.create().withMode(CreateMode.EPHEMERAL)
    .forPath("/path", "data".getBytes());

// Create sequential
client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
    .forPath("/prefix-", "data".getBytes());

// Create with ACL
client.create()
    .withACL(aclList)
    .forPath("/path", "data".getBytes());

// Background create
client.create().inBackground((client, event) -> {
    System.out.println("Created: " + event.getPath());
}).forPath("/path", "data".getBytes());
```

### Read

```java
// Get data
byte[] data = client.getData().forPath("/path");

// Get data with stat
Stat stat = new Stat();
byte[] data = client.getData().storingStatIn(stat).forPath("/path");

// Get children
List<String> children = client.getChildren().forPath("/path");

// Check existence
Stat stat = client.checkExists().forPath("/path");
boolean exists = stat != null;

// Get ACL
List<ACL> acls = client.getACL().forPath("/path");

// Background read
client.getData().inBackground((client, event) -> {
    byte[] data = event.getData();
}).forPath("/path");
```

### Update

```java
// Set data
client.setData().forPath("/path", "new data".getBytes());

// Set with version
client.setData().withVersion(stat.getVersion())
    .forPath("/path", "versioned data".getBytes());

// Set ACL
client.setACL().withACL(aclList).forPath("/path");

// Background update
client.setData().inBackground((client, event) -> {
    System.out.println("Updated");
}).forPath("/path", "new data".getBytes());
```

### Delete

```java
// Delete
client.delete().forPath("/path");

// Delete with version
client.delete().withVersion(stat.getVersion()).forPath("/path");

// Delete children recursively
client.delete().deletingChildrenIfNeeded().forPath("/path");

// Guaranteed delete
client.delete().guaranteed().forPath("/path");

// Background delete
client.delete().inBackground((client, event) -> {
    System.out.println("Deleted");
}).forPath("/path");
```

## Watches

### One-Time Watch

```java
// Watch with getData
byte[] data = client.getData().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
    System.out.println("Path: " + event.getPath());
}).forPath("/path");

// Watch with getChildren
List<String> children = client.getChildren().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
}).forPath("/path");

// Watch with exists
Stat stat = client.checkExists().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
}).forPath("/path");
```

### Persistent Watch (Curator)

```java
// NodeCache - single node
NodeCache nodeCache = new NodeCache(client, "/path");
nodeCache.getListenable().addListener(() -> {
    if (nodeCache.getCurrentData() != null) {
        System.out.println("Data: " + 
            new String(nodeCache.getCurrentData().getData()));
    }
});
nodeCache.start();

// PathChildrenCache - children
PathChildrenCache cache = new PathChildrenCache(client, "/path", true);
cache.getListenable().addListener((client, event) -> {
    System.out.println("Event: " + event.getType());
});
cache.start();

// TreeCache - entire subtree
TreeCache treeCache = new TreeCache(client, "/path");
treeCache.getListenable().addListener((client, event) -> {
    System.out.println("Event: " + event.getType());
});
treeCache.start();
```

## Recipes

### Leader Election

```java
// LeaderLatch
LeaderLatch latch = new LeaderLatch(client, "/leader/candidate");
latch.start();

if (latch.hasLeadership()) {
    System.out.println("I am the leader!");
}

latch.close();

// LeaderSelector
LeaderSelector selector = new LeaderSelector(client, "/leader/candidate",
    (curator, leader) -> {
        System.out.println("Leadership acquired");
        // Do leader work
    });
selector.autoRequeue();
selector.start();
```

### Distributed Lock

```java
// Mutex
InterProcessMutex lock = new InterProcessMutex(client, "/locks/resource");
if (lock.acquire(10, TimeUnit.SECONDS)) {
    try {
        // Critical section
    } finally {
        lock.release();
    }
}

// Read-Write Lock
InterProcessReadWriteLock rwLock = 
    new InterProcessReadWriteLock(client, "/locks/resource");

// Read lock
rwLock.readLock().acquire();
try {
    // Read critical section
} finally {
    rwLock.readLock().release();
}

// Write lock
rwLock.writeLock().acquire();
try {
    // Write critical section
} finally {
    rwLock.writeLock().release();
}
```

### Barrier

```java
// Simple barrier
DistributedBarrier barrier = 
    new DistributedBarrier(client, "/barriers/phase1");
barrier.setBarrier();
barrier.waitOnBarrier();

// Double barrier
DistributedDoubleBarrier doubleBarrier = 
    new DistributedDoubleBarrier(client, "/barriers/phase1", 5);
doubleBarrier.enter();
try {
    // Work
} finally {
    doubleBarrier.leave();
}
```

### Queue

```java
// Priority queue
InterProcessPriorityQueue<String> queue = 
    new InterProcessPriorityQueue<>(client, "/queues/priority",
        (o1, o2) -> o1.compareTo(o2));
queue.put("high priority");
queue.put("low priority");
String item = queue.take();
```

## Namespace Support

```java
// Namespace isolates paths
CuratorFramework client = CuratorFrameworkFactory.builder()
    .namespace("my-app")
    .build();

// Operations use relative paths
client.create().forPath("/config", "data".getBytes());
// Actually creates: /my-app/config

// List all namespaced paths
List<String> children = client.getChildren().forPath("/");
// Returns children of /my-app
```

## Error Handling

### Connection Errors

```java
client.getConnectionStateListener().addListener((curator, state) -> {
    switch (state) {
        case LOST:
            // Session expired
            // Re-establish watches
            reestablishWatches();
            break;
        case RECONNECTED:
            // Session recovered
            // Restore state
            restoreState();
            break;
        case SUSPENDED:
            // Connection lost
            // Wait for reconnect
            break;
    }
});
```

### Retry Handling

```java
// Curator retries automatically
// But you can handle failures

try {
    client.create().forPath("/path", "data".getBytes());
} catch (KeeperException.NodeExistsException e) {
    // Node already exists
} catch (KeeperException.NoNodeException e) {
    // Parent doesn't exist
} catch (Exception e) {
    // Other errors
}
```

## Performance Tips

```java
// 1. Use background operations
client.create().inBackground().forPath("/path", "data".getBytes());

// 2. Batch operations
List<String> paths = Arrays.asList("/a", "/b", "/c");
for (String path : paths) {
    client.create().forPath(path, "data".getBytes());
}

// 3. Use appropriate cache
NodeCache nodeCache = new NodeCache(client, "/path");
// Not TreeCache for single node

// 4. Close resources
nodeCache.close();

// 5. Use namespace for isolation
CuratorFramework client = CuratorFrameworkFactory.builder()
    .namespace("my-app")
    .build();
```

## Summary

| Feature | Curator | Raw Zookeeper |
|---------|---------|---------------|
| Connection | Automatic | Manual |
| Retries | Built-in | Manual |
| Watches | Persistent | One-time |
| Recipes | Built-in | Manual |
| Error Handling | Simplified | Complex |
| Namespace | Supported | Not supported |
