# Apache Zookeeper - Distributed Coordination Service

> Package: `academy.messaging.zookeeper`

## What is Zookeeper?

Apache Zookeeper is a centralized service for distributed coordination. It provides:
- **Configuration management** - Store and retrieve configuration
- **Naming service** - Register and discover services
- **Distributed synchronization** - Locks, barriers, queues
- **Group management** - Track members of a group
- **Leader election** - Elect a leader among processes

### Core Concepts

```java
// Zookeeper as a distributed协调 service
// Use cases: service discovery, config, leader election, distributed locks
```

## Why Zookeeper?

### 1. **Consensus & Coordination**
- Provides a shared tree of data (znodes)
- Maintains consistency across distributed systems
- Handles partial failures gracefully

### 2. **Reliability**
- Replicated across multiple servers
- Automatic failover
- No single point of failure

### 3. **Simplicity**
- Simple API: create, delete, exists, getData, setData, getChildren
- Watches for notifications
- Session-based connection management

## Architecture

### Node Types

```
┌─────────────────────────────────────────────────────────────────┐
│                    Zookeeper Cluster                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐      ┌─────────────┐      ┌─────────────┐     │
│  │   Leader    │ ──── │  Follower   │ ──── │  Follower   │     │
│  │             │      │             │      │             │     │
│  │ • Write     │      │ • Read      │      │ • Read      │     │
│  │   requests  │      │ • Forward   │      │ • Forward   │     │
│  │ • Proposal  │      │   writes    │      │   writes    │     │
│  │   broadcast │      │ • Vote      │      │ • Vote      │     │
│  └─────────────┘      └─────────────┘      └─────────────┘     │
│           │                   │                   │             │
│           └───────────────────┼───────────────────┘             │
│                               │                                 │
│  ┌────────────────────────────┴────────────────────────────┐    │
│  │                    ZAB Protocol                         │    │
│  │              (Atomic Broadcast)                          │    │
│  │                                                         │    │
│  │  • Leader election                                       │    │
│  │  • Discovery (sync)                                     │    │
│  │  • Synchronization                                      │    │
│  │  • Broadcast                                            │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                 │
│  ┌─────────────┐                                               │
│  │  Observer   │  (optional, read-only)                        │
│  │             │                                               │
│  │ • Scale     │                                               │
│  │   reads     │                                               │
│  │ • No votes  │                                               │
│  └─────────────┘                                               │
└─────────────────────────────────────────────────────────────────┘
```

### Leader/Follower Roles

| Role | Responsibilities |
|------|------------------|
| **Leader** | Handles write requests, proposes changes, broadcasts |
| **Follower** | Handles reads, forwards writes, participates in elections |
| **Observer** | Handles reads only, does not vote, scales read capacity |

## ZAB Protocol (Zookeeper Atomic Broadcast)

### Protocol Phases

```
Phase 1: Leader Election
─────────────────────────────────────────────────
  Servers determine who will be leader
  Uses fast leader election algorithm

Phase 2: Discovery
─────────────────────────────────────────────────
  New leader discovers system state
  Learns about pending transactions
  Ensures consensus on state

Phase 3: Synchronization
─────────────────────────────────────────────────
  Leader syncs with all followers
  Ensures everyone has same state
  Recovers from failures

Phase 4: Broadcast
─────────────────────────────────────────────────
  Normal operation mode
  Leader proposes changes
  Followers acknowledge
  Commit after quorum
```

### Key Properties

- **Atomicity** - All servers agree on value
- **Ordering** - All transactions ordered consistently
- **Durability** - Once committed, never lost
- **Single-leader** - Only leader processes writes

## Data Model: Znodes

### Znode Hierarchy

```
/
├── /config
│   ├── /config/app1
│   │   ├── /config/app1/db.url
│   │   ├── /config/app1/db.user
│   │   └── /config/app1/db.password
│   └── /config/app2
│       ├── /config/app2/db.url
│       └── /config/app2/cache.ttl
│
├── /services
│   ├── /services/user-service
│   │   ├── /services/user-service/instance_0000000001
│   │   ├── /services/user-service/instance_0000000002
│   │   └── /services/user-service/instance_0000000003
│   └── /services/order-service
│       ├── /services/order-service/instance_0000000001
│       └── /services/order-service/instance_0000000002
│
├── /locks
│   ├── /locks/resource1
│   ├── /locks/resource2
│   └── /locks/resource3
│
└── /leader
    └── /leader/candidate
        ├── /leader/candidate/0000000001
        └── /leader/candidate/0000000002
```

### Znode Types

| Type | Description | Use Case |
|------|-------------|----------|
| **Persistent** | Survives session end | Configuration, service metadata |
| **Ephemeral** | Removed when session ends | Service registration, presence |
| **Sequential** | Appends monotonically increasing number | Queues, locks, leader election |
| **Persistent Sequential** | Combines persistent + sequential | Long-lived ordered entries |
| **Ephemeral Sequential** | Combines ephemeral + sequential | Short-lived ordered entries |

### Znode Structure

```java
// Znode contains:
// - Data (byte array, max 1MB)
// - ACLs (Access Control Lists)
// - Stat (metadata)
//   - version
//   - cversion
//   - aversion
//   - ctime (creation time)
//   - mtime (modification time)
//   - czxid (creation transaction id)
//   - mzxid (modification transaction id)
//   - ephemeralOwner (session id if ephemeral)
//   - dataLength
//   - numChildren
```

## Watches and Notifications

### Watch Mechanism

```
┌──────────────┐                    ┌──────────────┐
│   Client     │                    │  Zookeeper   │
└──────┬───────┘                    └──────┬───────┘
       │                                   │
       │  getData("/path", watch=true)     │
       │ ─────────────────────────────────>│
       │                                   │
       │  Data returned                    │
       │ <─────────────────────────────────│
       │                                   │
       │  (some time later)                │
       │                                   │
       │  Data changed at /path            │
       │ <─────────────────────────────────│
       │  WatchEvent: NodeDataChanged      │
       │                                   │
```

### Watch Types

| Type | Triggered By | One-time? |
|------|--------------|-----------|
| `getData()` | Node created/deleted/data changed | Yes |
| `getChildren()` | Child added/removed | Yes |
| `exists()` | Node created/deleted | Yes |

### Watch Event Details

```java
// WatchedEvent contains:
// - KeeperState: SyncConnected, Disconnected, Expired, AuthFailed
// - EventType: NodeCreated, NodeDeleted, NodeDataChanged, NodeChildrenChanged
// - Path: the path that triggered the event
```

## Session Management

### Session States

```
┌─────────────────────────────────────────────────────────────────┐
│                    Session Lifecycle                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────┐    heartbeat    ┌──────────┐                      │
│  │CONNECTED │ ─────────────── │CONNECTED │  (normal operation) │
│  └──────────┘                 └──────────┘                      │
│       │                         │    │                          │
│       │ no heartbeat            │    │ timeout                   │
│       │ (short)                 │    │ (30s default)             │
│       ▼                         │    ▼                          │
│  ┌──────────┐                   │  ┌──────────┐                 │
│  │DISCONNECT│                   │  │ EXPIRED  │                 │
│  └──────────┘                   │  └──────────┘                 │
│       │                         │       │                       │
│       │ reconnect               │       │ reconnect             │
│       │                         │       │                       │
│       ▼                         ▼       ▼                       │
│  ┌──────────┐              ┌──────────┐                        │
│  │CONNECTED │              │CONNECTED │  (new session)          │
│  └──────────┘              └──────────┘                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Session Handling

- **Session ID**: 64-bit identifier
- **Timeout**: Client-suggested, server-assigned (default: 30s)
- **Heartbeats**: Sent periodically to maintain session
- **Reconnection**: Client reconnects to new server with same session

## ACLs (Access Control Lists)

### ACL Schema

```
ACL = [scheme:permissions:digest]

Schemes:
  - world: Anyone
  - auth: Authenticated users
  - digest: User:password
  - ip: IP address
  - sasl: Kerberos

Permissions (CRWD):
  - CREATE (c): Can create child znodes
  - READ (r): Can get data and list children
  - WRITE (w): Can set data
  - DELETE (d): Can delete child znodes
  - ADMIN (a): Can set permissions
```

### ACL Examples

```java
// World-readable, no permissions
acl.add(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE));

// Auth-only
acl.add(new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, 
                new Id("auth", "")));

// Digest-based
acl.add(new ACL(ZooDefs.Perms.ALL, 
                new Id("digest", "user:password")));

// IP-based
acl.add(new ACL(ZooDefs.Perms.READ, 
                new Id("ip", "192.168.1.0/24")));
```

## Use Cases

### 1. Service Discovery

```
Register:
  create /services/my-service/instance_0000000001
  data: { host: "10.0.0.1", port: 8080 }
  ephemeral: true

Discover:
  getChildren /services/my-service
  
Watch:
  getChildren /services/my-service with watch
  
Notify on change:
  WatchEvent: NodeChildrenChanged
```

### 2. Configuration Management

```
Structure:
  /config
  ├── /config/app1
  │   ├── /config/app1/db.url = "jdbc:mysql://..."
  │   ├── /config/app1/db.user = "admin"
  │   └── /config/app1/cache.ttl = "300"
  └── /config/app2
      └── /config/app2/api.key = "abc123"

Flow:
  1. App starts, reads /config/app1
  2. Sets watch on /config/app1
  3. Config changes → watch fires
  4. App re-reads and applies new config
```

### 3. Leader Election

```
Sequence:
  1. Create /leader/candidate (persistent)
  2. Create ephemeral sequential child: /leader/candidate/0000000001
  3. Get children, sort
  4. If lowest number → you are leader
  5. If not → watch the next lower node
  6. If leader dies (ephemeral deleted) → watch fires → check again
```

### 4. Distributed Locks

```
Standard Lock:
  1. Create /locks/resource1 (persistent)
  2. Create ephemeral sequential: /locks/resource1/lock-0000000001
  3. Get children, sort
  4. If lowest → acquired
  5. If not → watch previous
  6. When previous deleted → check again

Write Lock:
  - Similar but uses -write as prefix
  - Multiple readers allowed, one writer
```

## Zookeeper vs etcd vs Consul

| Feature | Zookeeper | etcd | Consul |
|---------|-----------|------|--------|
| **Origin** | Apache (2010) | CoreOS (2013) | HashiCorp (2014) |
| **Language** | Java | Go | Go |
| **Consensus** | ZAB | Raft | Raft |
| **Data Model** | Tree (znodes) | Key-value | Key-value + Services |
| **Watch** | One-time | Long-polling | Long-polling |
| **Service Discovery** | Manual | Manual | Built-in |
| **Health Checks** | None | None | Built-in |
| **ACL System** | Complex | Simple | Simple + Namespaces |
| **Client Libraries** | Curator (Java), many | Official (Go), etcd3 | Official (Go), Consul API |
| **Use Case** | Coordination, locks, leader election | KV store, leader election | Service mesh, discovery |

## Curator Framework (Java Client)

### Overview

```java
// Curator is the recommended Java client for Zookeeper
// Features:
// - Connection management with retry
// - Recipes (locks, barriers, caches)
// - Fluent API
// - Background operations
// - Namespace support
```

### Basic Setup

```java
// Maven dependency
// <dependency>
//     <groupId>org.apache.curator</groupId>
//     <artifactId>curator-framework</artifactId>
//     <version>5.5.0</version>
// </dependency>

CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("localhost:2181")
    .sessionTimeoutMs(30000)
    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
    .namespace("my-app")
    .build();

client.start();
```

### CRUD Operations

```java
// Create
client.create().creatingParentsIfNeeded()
    .forPath("/config/app1/db.url", "jdbc:mysql://...".getBytes());

// Read
byte[] data = client.getData().forPath("/config/app1/db.url");

// Update
client.setData().forPath("/config/app1/db.url", "new-url".getBytes());

// Delete
client.delete().deletingChildrenIfNeeded().forPath("/config/app1");

// Check existence
Stat stat = client.checkExists().forPath("/config/app1");
```

### Watches

```java
// Using PathChildrenCache for automatic watches
PathChildrenCache cache = new PathChildrenCache(client, "/services", true);
cache.getListenable().addListener((client, event) -> {
    switch (event.getType()) {
        case CHILD_ADDED:
            System.out.println("Service added: " + event.getData().getPath());
            break;
        case CHILD_REMOVED:
            System.out.println("Service removed: " + event.getData().getPath());
            break;
    }
});
cache.start();
```

### Curator Recipes

```java
// Leader Election
LeaderLatch latch = new LeaderLatch(client, "/leader/candidate");
latch.start();
if (latch.hasLeadership()) {
    // This instance is leader
}

// Distributed Lock
InterProcessMutex lock = new InterProcessMutex(client, "/locks/resource1");
if (lock.acquire(10, TimeUnit.SECONDS)) {
    try {
        // Critical section
    } finally {
        lock.release();
    }
}

// Barrier
DistributedBarrier barrier = new DistributedBarrier(client, "/barriers/phase1");
barrier.setBarrier();
barrier.waitOnBarrier();
```

## Cluster Setup

### Configuration

```properties
# zoo.cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/var/lib/zookeeper
clientPort=2181

# Server IDs
server.1=zk1:2888:3888
server.2=zk2:2888:3888
server.3=zk3:2888:3888

# Observer mode
server.4=zk4:2888:3888:observer
```

### Port Mapping

```
Port 2181: Client connections
Port 2888: Follower connections to leader
Port 3888: Leader election
```

### Deployment

```bash
# MyID file
echo "1" > /var/lib/zookeeper/myid

# Start
zkServer.sh start

# Status
zkServer.sh status

# Client
zkCli.sh -server localhost:2181
```

## Performance Considerations

### Benchmarks

| Operation | Throughput | Latency |
|-----------|------------|---------|
| Read | 100K+ ops/sec | < 1ms |
| Write | 10K+ ops/sec | < 10ms |
| Mixed | 50K+ ops/sec | < 5ms |

### Optimization Tips

```java
// 1. Use connection pooling
// 2. Batch operations when possible
// 3. Use local caching for reads
// 4. Minimize watch registrations
// 5. Use appropriate session timeout

// Good: Short timeout for ephemeral data
CuratorFramework client = CuratorFrameworkFactory.builder()
    .sessionTimeoutMs(5000)  // 5 seconds
    .build();

// Bad: Long timeout causes stale data
CuratorFramework client = CuratorFrameworkFactory.builder()
    .sessionTimeoutMs(300000)  // 5 minutes
    .build();
```

### Common Pitfalls

| Pitfall | Solution |
|---------|----------|
| Watch loss | Use Curator recipes, not raw watches |
| Session expiry | Handle reconnection, restore state |
| Large znodes | Keep data < 1MB, use external storage |
| Network partitions | Ensure quorum, monitor health |
| Clock skew | Use NTP, don't rely on timestamps |
| Client libraries | Use Curator, not raw Zookeeper API |

## Quick Reference

### Common Commands

```bash
# Create znode
create /path "data"

# Read
get /path

# Update
set /path "new data"

# Delete
delete /path

# List children
ls /path

# Watch
get /path watch
ls /path watch

# ACLs
getAcl /path
setAcl /path world:anyone:r
```

### Curator Quick Reference

```java
// Create
client.create().forPath("/path", data);

// Read
client.getData().forPath("/path");

// Update
client.setData().forPath("/path", data);

// Delete
client.delete().forPath("/path");

// List
client.getChildren().forPath("/path");

// Exists
client.checkExists().forPath("/path");

// Watch
client.getData().usingWatcher(watcher).forPath("/path");
```

## Next Steps

1. [Architecture Deep Dive](01-architecture/README.md)
2. [Data Model & Znodes](02-data-model/README.md)
3. [Sessions & Watches](03-sessions-watches/README.md)
4. [Curator Framework](04-curator-framework/README.md)
5. [Service Discovery Patterns](05-service-discovery/README.md)
6. [Leader Election](06-leader-election/README.md)
7. [Distributed Locks](07-distributed-locks/README.md)
8. [Configuration Management](08-configuration-management/README.md)

## References

- [Apache Zookeeper Documentation](https://zookeeper.apache.org/doc/current/)
- [Curator Documentation](https://curator.apache.org/)
- [Zookeeper Administrator's Guide](https://zookeeper.apache.org/doc/current/zookeeperAdmin.html)
