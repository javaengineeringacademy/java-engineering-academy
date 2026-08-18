# Leader Election

> Package: `academy.messaging.zookeeper.leaderelection`

## Overview

Leader election is the process of selecting a single instance from a group to coordinate actions. Zookeeper provides reliable leader election through ephemeral sequential znodes.

## Leader Election Algorithm

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Leader Election Algorithm                             │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  1. All candidates create ephemeral sequential znodes                   │
│                                                                         │
│     /leader/candidate                                                   │
│     ├── /leader/candidate/0000000001                                    │
│     ├── /leader/candidate/0000000002                                    │
│     └── /leader/candidate/0000000003                                    │
│                                                                         │
│  2. Candidate with lowest number becomes leader                         │
│                                                                         │
│     Leader: 0000000001                                                  │
│     Followers: 0000000002, 0000000003                                   │
│                                                                         │
│  3. Non-leaders watch the next lower znode                              │
│                                                                         │
│     0000000003 watches 0000000002                                       │
│     0000000002 watches 0000000001                                       │
│                                                                         │
│  4. When leader dies (ephemeral deleted):                               │
│                                                                         │
│     0000000002's watch fires                                            │
│     0000000002 becomes new leader                                       │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Implementation

### Basic Leader Election

```java
package academy.messaging.zookeeper.leaderelection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LeaderElectionExample {
    
    private final CuratorFramework client;
    
    public LeaderElectionExample(CuratorFramework client) {
        this.client = client;
    }
    
    // Method 1: LeaderLatch
    public void leaderLatchExample() throws Exception {
        LeaderLatch latch = new LeaderLatch(client, "/leader/candidate");
        latch.start();
        
        // Wait to become leader
        latch.await();
        
        System.out.println("I am the leader!");
        
        // Do leader work
        doLeaderWork();
        
        // Release leadership
        latch.close();
    }
    
    // Method 2: LeaderSelector
    public void leaderSelectorExample() throws Exception {
        LeaderSelector selector = new LeaderSelector(client, "/leader/candidate",
            new LeaderSelectorListener() {
                @Override
                public void takeLeadership(CuratorFramework client) throws Exception {
                    System.out.println("I am the leader!");
                    
                    // Do leader work
                    doLeaderWork();
                    
                    // Leadership is automatically released when method returns
                }
                
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    // Handle connection state changes
                }
            });
        
        selector.autoRequeue();  // Re-queue after losing leadership
        selector.start();
        
        // Wait for shutdown
        Thread.sleep(Long.MAX_VALUE);
        
        selector.close();
    }
    
    private void doLeaderWork() throws Exception {
        // Simulate leader work
        Thread.sleep(10000);
    }
}
```

### Custom Leader Election

```java
package academy.messaging.zookeeper.leaderelection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CustomLeaderElection {
    
    private final CuratorFramework client;
    private final String electionPath;
    private final String instanceId;
    private volatile boolean isLeader = false;
    
    public CustomLeaderElection(CuratorFramework client, String electionPath, 
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
        String myNode = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
            .forPath(electionPath + "/candidate-", instanceId.getBytes());
        
        System.out.println("Created node: " + myNode);
        
        // Check if we are leader
        checkLeadership(myNode);
    }
    
    private void checkLeadership(String myNode) throws Exception {
        while (true) {
            List<String> children = client.getChildren().forPath(electionPath);
            
            // Sort children (sequential znodes are naturally sorted)
            children.sort(String::compareTo);
            
            // Get my position
            String myShortNode = myNode.substring(myNode.lastIndexOf('/') + 1);
            int myIndex = children.indexOf(myShortNode);
            
            if (myIndex == 0) {
                // I am the leader
                if (!isLeader) {
                    isLeader = true;
                    onBecomeLeader();
                }
            } else {
                // I am not the leader, watch the next lower node
                if (isLeader) {
                    isLeader = false;
                    onLoseLeadership();
                }
                
                String watchNode = children.get(myIndex - 1);
                String watchPath = electionPath + "/" + watchNode;
                
                System.out.println("Watching: " + watchPath);
                
                // Watch for deletion
                CountDownLatch latch = new CountDownLatch(1);
                byte[] data = client.getData().usingWatcher(event -> {
                    System.out.println("Watch triggered: " + event.getType());
                    latch.countDown();
                }).forPath(watchPath);
                
                // Wait for watch to trigger
                latch.await(30, TimeUnit.SECONDS);
                
                // Re-check leadership
                continue;
            }
            
            // Wait before re-checking
            Thread.sleep(1000);
        }
    }
    
    protected void onBecomeLeader() {
        System.out.println(instanceId + " became leader");
        // Initialize leader state
    }
    
    protected void onLoseLeadership() {
        System.out.println(instanceId + " lost leadership");
        // Cleanup leader state
    }
    
    public boolean isLeader() {
        return isLeader;
    }
}
```

### Leader Election with Health Check

```java
package academy.messaging.zookeeper.leaderelection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;

import java.util.List;
import java.util.stream.Collectors;

public class LeaderElectionWithHealthCheck {
    
    private final CuratorFramework client;
    private final LeaderSelector selector;
    private volatile boolean healthy = true;
    
    public LeaderElectionWithHealthCheck(CuratorFramework client, String electionPath) {
        this.client = client;
        this.selector = new LeaderSelector(client, electionPath, this::takeLeadership);
    }
    
    private void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println("Taking leadership");
        
        // Start health check
        startHealthCheck();
        
        try {
            while (healthy && !Thread.currentThread().isInterrupted()) {
                // Do leader work
                doLeaderWork();
                
                // Check health
                if (!healthy) {
                    System.out.println("Health check failed, stepping down");
                    break;
                }
                
                Thread.sleep(1000);
            }
        } finally {
            System.out.println("Releasing leadership");
            stopHealthCheck();
        }
    }
    
    private void startHealthCheck() {
        // Monitor health endpoint
        // If health check fails, set healthy = false
    }
    
    private void stopHealthCheck() {
        // Stop health monitoring
    }
    
    private void doLeaderWork() throws Exception {
        // Simulate work
        Thread.sleep(1000);
    }
    
    public void start() {
        selector.autoRequeue();
        selector.start();
    }
    
    public void stop() {
        selector.interruptLeadership();
        selector.close();
    }
    
    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}
```

## Election Patterns

### Pattern 1: Single Leader

```
Use Case: Coordinating distributed tasks
───────────────────────────────────────────────────────────────
Only one instance processes tasks
Others wait as standby

ZNode Structure:
  /coordinator
  └── /coordinator/leader [ephemeral sequential]
```

### Pattern 2: Leader with Followers

```
Use Case: Master-slave replication
───────────────────────────────────────────────────────────────
Leader handles writes
Followers handle reads

ZNode Structure:
  /cluster
  ├── /cluster/leader [ephemeral sequential]
  └── /cluster/followers
      ├── /cluster/followers/f1 [ephemeral]
      └── /cluster/followers/f2 [ephemeral]
```

### Pattern 3: Group Leader

```
Use Case: Coordinating a group of workers
───────────────────────────────────────────────────────────────
Each group has one leader
Leaders coordinate across groups

ZNode Structure:
  /groups
  ├── /groups/group1
  │   └── /groups/group1/leader [ephemeral sequential]
  └── /groups/group2
      └── /groups/group2/leader [ephemeral sequential]
```

## Failover Handling

### Leader Failure

```
Timeline:
───────────────────────────────────────────────────────────────
t=0: Leader fails
t=1: Follower's watch triggers
t=2: Follower checks position
t=3: Follower becomes new leader
t=4: New leader initializes

Recovery Time: 100ms - 1s
```

### Split Brain Prevention

```
Mechanism: Ephemeral znodes
───────────────────────────────────────────────────────────────
• Ephemeral znodes deleted on session expiry
• Ensures only one leader at a time
• Prevents split-brain scenario

Network Partition:
• Isolated leader's session expires
• Ephemeral znodes deleted
• New leader elected in remaining cluster
```

## Best Practices

```
✓ Use ephemeral sequential znodes
✓ Implement health checks
✓ Handle session expiry
✓ Use watches for fast failover
✓ Keep leader logic simple
✓ Test failover scenarios

✗ Don't use persistent znodes for election
✗ Don't ignore session expiry
✗ Don't store state in leader
✗ Don't skip health checks
✗ Don't block leader thread
```

## Summary

| Concept | Key Point |
|---------|-----------|
| Ephemeral Sequential | Unique, ordered znodes |
| Watch | Notification on deletion |
| LeaderLatch | Simple leader election |
| LeaderSelector | Re-queue support |
| Failover | 100ms - 1s typically |
