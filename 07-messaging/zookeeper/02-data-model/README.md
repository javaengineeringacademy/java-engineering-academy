# Data Model & Znodes

> Package: `academy.messaging.zookeeper.datamodel`

## Overview

Zookeeper's data model is a hierarchical tree of znodes (zookeeper nodes). Each znode can store data and have children, forming a file-system-like structure.

## Znode Structure

### Data Structure

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         Znode Structure                                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                        Znode                                    │    │
│  │                                                                  │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                    Data (byte[])                         │    │    │
│  │  │                    Max 1MB                               │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  │                                                                  │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                    ACLs                                  │    │    │
│  │  │                    Access Control Lists                  │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  │                                                                  │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                    Stat                                   │    │    │
│  │  │  • czxid - creation transaction id                       │    │    │
│  │  │  • mzxid - modification transaction id                   │    │    │
│  │  │  • ctime - creation time                                 │    │    │
│  │  │  • mtime - modification time                             │    │    │
│  │  │  • version - data version                                │    │    │
│  │  │  • cversion - children version                           │    │    │
│  │  │  • aversion - ACL version                                │    │    │
│  │  │  • ephemeralOwner - session id (ephemeral)               │    │    │
│  │  │  • dataLength - data size                                 │    │    │
│  │  │  • numChildren - number of children                       │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  │                                                                  │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │                    Children                               │    │    │
│  │  │                    Child znodes                           │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  │                                                                  │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Example Hierarchy

```
/
├── /app1
│   ├── /app1/config
│   │   ├── /app1/config/db.url = "jdbc:mysql://..."
│   │   ├── /app1/config/db.user = "admin"
│   │   └── /app1/config/cache.ttl = "300"
│   ├── /app1/services
│   │   ├── /app1/services/api
│   │   │   ├── /app1/services/api/instance-0000000001 [ephemeral]
│   │   │   └── /app1/services/api/instance-0000000002 [ephemeral]
│   │   └── /app1/services/worker
│   │       └── /app1/services/worker/instance-0000000001 [ephemeral]
│   └── /app1/locks
│       └── /app1/locks/resource1
│           ├── /app1/locks/resource1/lock-0000000001 [ephemeral sequential]
│           └── /app1/locks/resource1/lock-0000000002 [ephemeral sequential]
│
├── /app2
│   └── /app2/config
│       └── /app2/config/api.key = "abc123"
│
└── /zookeeper
    ├── /zookeeper/quota
    └── /zookeeper/config
```

## Znode Types

### 1. Persistent Znodes

```
Characteristics:
  • Survive after session ends
  • Must be explicitly deleted
  • Good for configuration, metadata

Use Cases:
  • Configuration storage
  • Service metadata
  • Leader election candidates
  • Lock resources

Example:
  /config/app1/db.url = "jdbc:mysql://..."
  
  This persists even if the client disconnects.
```

### 2. Ephemeral Znodes

```
Characteristics:
  • Automatically deleted when session ends
  • Created with session ID
  • Cannot have children
  • Good for presence tracking

Use Cases:
  • Service registration
  • Leader election
  • Distributed locks
  • Presence detection

Example:
  /services/api/instance-0000000001
  • Created when service starts
  • Deleted when service stops
  • Indicates service is alive
```

### 3. Sequential Znodes

```
Characteristics:
  • Appends monotonically increasing counter
  • Counter is parent's cversion
  • Globally unique
  • Good for ordering

Use Cases:
  • Queues
  • Leader election
  • Lock acquisition order
  • Event logging

Example:
  /queue/task-0000000001
  /queue/task-0000000002
  /queue/task-0000000003
```

### 4. Persistent Sequential

```
Characteristics:
  • Survives session ends
  • Has sequential number
  • Good for long-lived ordered data

Use Cases:
  • Long-lived queues
  • Audit trails
  • Historical data

Example:
  /audit/log-0000000001
  /audit/log-0000000002
```

### 5. Ephemeral Sequential

```
Characteristics:
  • Deleted when session ends
  • Has sequential number
  • Good for short-lived ordering

Use Cases:
  • Lock acquisition
  • Leader election
  • Temporary queues

Example:
  /locks/resource1/lock-0000000001
  /locks/resource1/lock-0000000002
```

## Znode Operations

### Create

```java
// Create with data
client.create().forPath("/path", "data".getBytes());

// Create with ACL
client.create()
    .withACL(Arrays.asList(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE)))
    .forPath("/path", "data".getBytes());

// Create ephemeral
client.create().withMode(CreateMode.EPHEMERAL)
    .forPath("/path", "data".getBytes());

// Create sequential
client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
    .forPath("/path-", "data".getBytes());

// Create parent nodes
client.create().creatingParentsIfNeeded()
    .forPath("/a/b/c", "data".getBytes());
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
```

### Update

```java
// Set data
client.setData().forPath("/path", "new data".getBytes());

// Set data with version (optimistic locking)
client.setData().withVersion(stat.getVersion())
    .forPath("/path", "new data".getBytes());

// Set ACL
client.setACL()
    .withACL(Arrays.asList(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE)))
    .forPath("/path");
```

### Delete

```java
// Delete
client.delete().forPath("/path");

// Delete with version
client.delete().withVersion(stat.getVersion()).forPath("/path");

// Delete children recursively
client.delete().deletingChildrenIfNeeded().forPath("/path");

// Guaranteed delete (retry until successful)
client.delete().guaranteed().forPath("/path");
```

## Watches

### Watch Types

```java
// Data watch (one-time)
byte[] data = client.getData().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
}).forPath("/path");

// Children watch (one-time)
List<String> children = client.getChildren().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
}).forPath("/path");

// Existence watch (one-time)
Stat stat = client.checkExists().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
}).forPath("/path");
```

### Watch Events

```java
public class WatchEvent {
    // KeeperState: SyncConnected, Disconnected, Expired, AuthFailed
    // EventType: NodeCreated, NodeDeleted, NodeDataChanged, NodeChildrenChanged
    // Path: the path that triggered the event
    
    KeeperState state = event.getState();
    EventType type = event.getType();
    String path = event.getPath();
}
```

## ACLs (Access Control Lists)

### ACL Schema

```
ACL = [scheme:permissions:digest]

Schemes:
  ┌─────────┬──────────────────────────────────────────────────────────┐
  │ world   │ Anyone (no authentication required)                      │
  ├─────────┼──────────────────────────────────────────────────────────┤
  │ auth    │ Any authenticated user                                   │
  ├─────────┼──────────────────────────────────────────────────────────┤
  │ digest  │ User:password (SHA1 hash)                                │
  ├─────────┼──────────────────────────────────────────────────────────┤
  │ ip      │ IP address (e.g., 192.168.1.0/24)                       │
  ├─────────┼──────────────────────────────────────────────────────────┤
  │ sasl    │ Kerberos principal                                       │
  └─────────┴──────────────────────────────────────────────────────────┘

Permissions (CRWD):
  • CREATE (c): Can create child znodes
  • READ (r): Can get data and list children
  • WRITE (w): Can set data
  • DELETE (d): Can delete child znodes
  • ADMIN (a): Can set permissions
```

### ACL Examples

```java
// World-readable, no permissions
acl = new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE);

// Auth-only
acl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, 
              new Id("auth", ""));

// Digest-based
acl = new ACL(ZooDefs.Perms.ALL, 
              new Id("digest", "user:password"));

// IP-based
acl = new ACL(ZooDefs.Perms.READ, 
              new Id("ip", "192.168.1.0/24"));

// Multiple ACLs
List<ACL> acls = Arrays.asList(
    new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE),
    new ACL(ZooDefs.Perms.ALL, new Id("digest", "admin:password"))
);
```

## Data Serialization

### Best Practices

```java
// 1. Use compact formats (JSON, Protobuf, Avro)

// JSON Example
String json = "{\"host\":\"10.0.0.1\",\"port\":8080}";
client.create().forPath("/services/api/instance-0000000001", json.getBytes());

// 2. Keep data small (< 1MB)
// Bad: Storing large files
// Good: Store metadata, reference external storage

// 3. Version your data
String data = "{\"version\":1,\"host\":\"10.0.0.1\"}";

// 4. Use compression for large payloads
byte[] compressed = compress(data.getBytes());
client.create().forPath("/path", compressed);
```

### Serialization Libraries

```java
// Jackson (JSON)
ObjectMapper mapper = new ObjectMapper();
byte[] data = mapper.writeValueAsBytes(myObject);

// Protobuf
MyMessage message = MyMessage.newBuilder().build();
byte[] data = message.toByteArray();

// Avro
GenericRecord record = new GenericData.Record(schema);
byte[] data = serialize(record);
```

## Performance Considerations

### Znode Limits

| Limit | Value | Notes |
|-------|-------|-------|
| Data size | 1 MB | Recommended < 100KB |
| Children | Unlimited | But impacts performance |
| Path length | 1 MB | Practical limit |
| Watchers | Unlimited | But impacts memory |

### Optimization Tips

```java
// 1. Keep data small
// Bad: 1MB of data
// Good: 1KB of metadata

// 2. Minimize znode count
// Bad: 1 znode per item
// Good: Batch items in single znode

// 3. Use appropriate znode type
// Persistent for config
// Ephemeral for presence
// Sequential for ordering

// 4. Batch operations
List<String> paths = Arrays.asList("/a", "/b", "/c");
for (String path : paths) {
    client.create().forPath(path);
}

// 5. Use watches sparingly
// Each watch consumes resources
```

## Common Patterns

### Service Registration

```
/create /services/my-service/instance-0000000001
  data: {"host":"10.0.0.1","port":8080,"status":"UP"}
  ephemeral: true
  sequential: true
```

### Configuration Storage

```
/create /config/app1/db.url
  data: "jdbc:mysql://localhost:3306/mydb"
  persistent: true
```

### Leader Election

```
/create /leader/candidate
  persistent: true

/create /leader/candidate/0000000001
  ephemeral: true
  sequential: true
```

### Distributed Lock

```
/create /locks/resource1
  persistent: true

/create /locks/resource1/lock-0000000001
  ephemeral: true
  sequential: true
```
