# Sessions & Watches

> Package: `academy.messaging.zookeeper.sessions`

## Overview

Zookeeper sessions maintain client connections and track ephemeral znodes. Watches provide notifications for data changes.

## Session Management

### Session Lifecycle

```
┌─────────────────────────────────────────────────────────────────────────┐
│                     Session Lifecycle                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌──────────┐                                                          │
│  │  NEW     │  Client connects, server assigns session ID              │
│  └────┬─────┘                                                          │
│       │                                                                  │
│       ▼                                                                  │
│  ┌──────────┐  Normal operation                                        │
│  │CONNECTED │  • Heartbeats sent                                       │
│  └────┬─────┘  • Data operations                                       │
│       │                                                                  │
│       │  Network issue or server failover                               │
│       ▼                                                                  │
│  ┌──────────┐  • Session remains valid                                 │
│  │DISCONNECT│  • No operations possible                                │
│  └────┬─────┘  • Client attempts reconnect                             │
│       │                                                                  │
│       │  Reconnect within timeout                                       │
│       │  OR                                                             │
│       │  Timeout expires                                                │
│       ▼                                                                  │
│  ┌──────────┐  • Session expired                                       │
│  │ EXPIRED  │  • Ephemeral znodes deleted                              │
│  └────┬─────┘  • Client must create new session                        │
│       │                                                                  │
│       ▼                                                                  │
│  ┌──────────┐  • New session created                                   │
│  │CONNECTED │  • Old ephemeral znodes gone                             │
│  └──────────┘                                                          │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Session Properties

```java
// Session attributes
public class Session {
    private long sessionId;        // 64-bit unique ID
    private int timeout;           // Session timeout (ms)
    private long lastActivity;     // Last activity timestamp
    private State state;           // Current state
}

// Session states
enum State {
    NEW,
    CONNECTED,
    DISCONNECTED,
    CLOSED,
    EXPIRED
}
```

### Session Timeout

```java
// Client specifies timeout
CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("localhost:2181")
    .sessionTimeoutMs(30000)  // 30 seconds
    .build();

// Server adjusts based on:
// 1. Client requested timeout
// 2. Server's min/max session timeout
// 3. Network conditions

// Timeout behavior:
// • Short timeout (5s): Quick failure detection, but sensitive to network
// • Long timeout (60s): Tolerant to network issues, but slow failure detection
```

### Heartbeats

```
Heartbeat Mechanism:
───────────────────────────────────────────────────────────────
Client sends PING periodically
  │
  ├──► Server responds with PONG
  │
  └──► Session stays alive

Heartbeat interval:
  • Default: 1/3 of session timeout
  • For 30s timeout: ~10s heartbeat

What happens if heartbeat fails:
  • Client marks session as disconnected
  • Client attempts reconnect
  • Server waits for timeout before expiry
```

### Session Recovery

```java
// Curator handles session recovery automatically
CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("localhost:2181")
    .sessionTimeoutMs(30000)
    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
    .connectionStateListener((curator, newState) -> {
        switch (newState) {
            case LOST:
                // Session expired, ephemeral znodes deleted
                System.out.println("Session lost!");
                break;
            case RECONNECTED:
                // Session recovered
                System.out.println("Reconnected!");
                break;
            case SUSPENDED:
                // Connection suspended
                System.out.println("Suspended!");
                break;
        }
    })
    .build();
```

## Watches

### Watch Types

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         Watch Types                                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │  getData() + Watch                                              │    │
│  │  ─────────────────────────────────────────────────────────────  │    │
│  │  Triggers when:                                                 │    │
│  │  • Node created (if doesn't exist)                              │    │
│  │  • Node deleted                                                 │    │
│  │  • Node data changed                                            │    │
│  │                                                                 │    │
│  │  Returns: byte[] data                                           │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │  getChildren() + Watch                                         │    │
│  │  ─────────────────────────────────────────────────────────────  │    │
│  │  Triggers when:                                                 │    │
│  │  • Child added                                                  │    │
│  │  • Child deleted                                                │    │
│  │                                                                 │    │
│  │  Returns: List<String> children                                 │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │  exists() + Watch                                              │    │
│  │  ─────────────────────────────────────────────────────────────  │    │
│  │  Triggers when:                                                 │    │
│  │  • Node created (if doesn't exist)                              │    │
│  │  • Node deleted                                                 │    │
│  │                                                                 │    │
│  │  Returns: Stat metadata                                         │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Watch Events

```java
// WatchedEvent structure
public class WatchedEvent {
    private KeeperState state;   // Connection state
    private EventType type;      // Event type
    private String path;         // Affected path
}

// KeeperState values
enum KeeperState {
    SyncConnected,      // Connected to server
    Disconnected,       // Lost connection
    Expired,            // Session expired
    AuthFailed          // Authentication failed
}

// EventType values
enum EventType {
    None,               // No event
    NodeCreated,        // Node created
    NodeDeleted,        // Node deleted
    NodeDataChanged,    // Node data changed
    NodeChildrenChanged // Children changed
}
```

### Watch Registration

```java
// Method 1: One-time watch (raw Zookeeper)
byte[] data = client.getData().usingWatcher(event -> {
    System.out.println("Event: " + event.getType());
    // Must re-register watch
}).forPath("/path");

// Method 2: Persistent watch (Curator)
NodeCache nodeCache = new NodeCache(client, "/path");
nodeCache.getListenable().addListener(() -> {
    System.out.println("Data: " + 
        new String(nodeCache.getCurrentData().getData()));
});
nodeCache.start();

// Method 3: Path watch (Curator)
PathChildrenCache cache = new PathChildrenCache(client, "/path", true);
cache.getListenable().addListener((client, event) -> {
    System.out.println("Event: " + event.getType());
});
cache.start();
```

### Watch Limitations

```
Limitations:
───────────────────────────────────────────────────────────────
1. One-time trigger
   • Watch fires once then removed
   • Must re-register for next notification

2. Order guarantee
   • Client sees watch event before new data
   • But not guaranteed across clients

3. Lost watches
   • Session disconnect may lose watches
   • Curator recipes handle this

4. Memory overhead
   • Each watch consumes server memory
   • Too many watches impact performance

5. No guarantee of delivery
   • Watch may not trigger if:
     • Session expires before watch fires
     • Server crashes before notification
```

### Watch Best Practices

```java
// 1. Use Curator recipes for persistent watches
NodeCache cache = new NodeCache(client, "/path");
cache.start();  // Automatically re-registers watches

// 2. Handle session expiry
client.getConnectionStateListener().addListener((curator, state) -> {
    if (state == ConnectionState.LOST) {
        // Re-establish watches
        reestablishWatches();
    }
});

// 3. Batch watch operations
PathChildrenCache cache = new PathChildrenCache(client, "/parent", true);
// Watches all children automatically

// 4. Use appropriate watch type
// NodeCache: Single node
// PathChildrenCache: Children only
// TreeCache: Entire subtree

// 5. Clean up watches
cache.close();  // Stop watching
```

## Connection Management

### Connection States

```java
// Connection states
enum ConnectionState {
    CONNECTED,          // Connected to server
    SUSPENDED,          // Connection lost but session valid
    RECONNECTED,        // Reconnected successfully
    LOST,               // Session expired
    READ_ONLY           // Connected to observer
}

// State transitions
CONNECTED → SUSPENDED → RECONNECTED → CONNECTED
CONNECTED → SUSPENDED → LOST → CONNECTED (new session)
```

### Connection Pooling

```java
// Curator handles connection pooling automatically
CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("zk1:2181,zk2:2181,zk3:2181")  // Multiple servers
    .sessionTimeoutMs(30000)
    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
    .build();

// Client automatically:
// • Connects to available server
// • Reconnects on failure
// • Balances across servers
```

### Retry Policies

```java
// Exponential backoff (recommended)
new ExponentialBackoffRetry(1000, 3)  // 1s base, 3 retries

// Fixed retry
new RetryNTimes(5, 1000)  // 5 retries, 1s delay

// Forever retry
new RetryForever(1000)  // Retry every 1s

// Custom retry
new RetryPolicy() {
    @Override
    public boolean allowRetry(int retryCount, long elapsedTimeMs, RetrySleeper sleeper) {
        return retryCount < 3;
    }
}
```

## Performance Considerations

### Session Timeout Tuning

```
Timeout Selection:
───────────────────────────────────────────────────────────────
Short (5-10s):
  ✓ Quick failure detection
  ✓ Fast leader election
  ✗ Sensitive to network jitter
  ✗ More frequent reconnections

Medium (15-30s):
  ✓ Balanced approach
  ✓ Good for most use cases
  ✓ Reasonable failure detection

Long (30-60s):
  ✓ Tolerant to network issues
  ✓ Fewer reconnections
  ✗ Slow failure detection
  ✗ Delayed leader election

Recommendation: 30s for most applications
```

### Watch Performance

```
Watch Impact:
───────────────────────────────────────────────────────────────
Memory:
  • Each watch: ~100 bytes
  • 10K watches: ~1MB
  • 100K watches: ~10MB

CPU:
  • Watch registration: Minimal
  • Watch notification: O(1) per watch
  • Watch re-registration: Minimal

Network:
  • Watch event: Small message
  • Frequent changes: High network usage

Best Practices:
  • Use Curator recipes (automatic re-registration)
  • Limit watch count
  • Use PathChildrenCache for child watches
  • Clean up unused watches
```

## Common Issues

### Session Expiry

```
Problem: Session expires, ephemeral znodes lost

Causes:
  • Network partition too long
  • Server too busy to process heartbeats
  • Timeout too short

Solutions:
  • Increase session timeout
  • Monitor network health
  • Use Curator connection listener
  • Handle reconnection gracefully
```

### Watch Loss

```
Problem: Watch doesn't fire

Causes:
  • Session expired before event
  • Watch was one-time (already fired)
  • Server crashed before notification

Solutions:
  • Use Curator persistent watches
  • Re-register watches after reconnect
  • Handle missing data gracefully
```

### Connection Flapping

```
Problem: Client rapidly connects/disconnects

Causes:
  • Network instability
  • Server overload
  • Timeout too short

Solutions:
  • Increase session timeout
  • Add connection retry backoff
  • Monitor network stability
  • Use multiple servers
```

## Summary

| Concept | Key Point |
|---------|-----------|
| Session | Maintains client state, 64-bit ID |
| Timeout | Client-suggested, server-adjusted |
| Heartbeat | Periodic ping to keep session alive |
| Expiry | Ephemeral znodes deleted |
| Watch | One-time notification |
| Re-register | Must re-register after watch fires |
| Curator | Handles watch persistence |
