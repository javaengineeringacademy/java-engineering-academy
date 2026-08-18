# Zookeeper Architecture

> Package: `academy.messaging.zookeeper.architecture`

## Overview

Zookeeper uses a replicated server architecture with a leader-follower model. Understanding this architecture is critical for proper deployment and operation.

## Cluster Components

### Server Roles

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Zookeeper Ensemble                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                         LEADER                                   │    │
│  │                                                                  │    │
│  │  • Handles all write requests                                   │    │
│  │  • Proposes changes via ZAB protocol                            │    │
│  │  • Broadcasts committed transactions                            │    │
│  │  • Maintains transaction log                                    │    │
│  │  • Runs leader election                                         │    │
│  │                                                                  │    │
│  │  Port 2888: Follower connections                                │    │
│  │  Port 3888: Leader election                                     │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                               │                                         │
│           ┌───────────────────┼───────────────────┐                    │
│           │                   │                   │                    │
│           ▼                   ▼                   ▼                    │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐          │
│  │   FOLLOWER 1    │ │   FOLLOWER 2    │ │   FOLLOWER 3    │          │
│  │                  │ │                  │ │                  │          │
│  │ • Handles reads │ │ • Handles reads │ │ • Handles reads │          │
│  │ • Forwards      │ │ • Forwards      │ │ • Forwards      │          │
│  │   writes        │ │   writes        │ │   writes        │          │
│  │ • Votes on      │ │ • Votes on      │ │ • Votes on      │          │
│  │   proposals     │ │   proposals     │ │   proposals     │          │
│  │ • Participates  │ │ • Participates  │ │ • Participates  │          │
│  │   in elections  │ │   in elections  │ │   in elections  │          │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘          │
│                               │                                         │
│                               ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                       OBSERVER                                   │    │
│  │                                                                  │    │
│  │  • Read-only node                                               │    │
│  │  • Does NOT participate in elections                            │    │
│  │  • Does NOT vote on proposals                                   │    │
│  │  • Scales read capacity                                         │    │
│  │  • Useful for geographic distribution                           │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Port Mapping

| Port | Purpose | Protocol |
|------|---------|----------|
| 2181 | Client connections | TCP |
| 2888 | Follower → Leader sync | TCP |
| 3888 | Leader election | TCP |

## ZAB Protocol (Zookeeper Atomic Broadcast)

### Protocol Overview

ZAB ensures consistency across all servers through a consensus protocol.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    ZAB Protocol Phases                                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Phase 1: LEADER ELECTION                                               │
│  ─────────────────────────────────────────────────────────────────────  │
│  • Triggered on startup or leader failure                               │
│  • Uses Fast Leader Election algorithm                                  │
│  • Each server votes for itself initially                               │
│  • Votes propagated, highest zxid wins                                  │
│  • Majority needed to elect leader                                      │
│                                                                         │
│  Phase 2: DISCOVERY                                                     │
│  ─────────────────────────────────────────────────────────────────────  │
│  • New leader contacts followers                                        │
│  • Learns about pending (uncommitted) transactions                     │
│  • Ensures consensus on current state                                   │
│  • Leader establishes epoch number                                      │
│                                                                         │
│  Phase 3: SYNCHRONIZATION                                               │
│  ─────────────────────────────────────────────────────────────────────  │
│  • Leader sends snapshot and transaction log to followers               │
│  • Ensures all servers have same state                                  │
│  • Resolves any discrepancies                                           │
│  • Critical for recovery                                                │
│                                                                         │
│  Phase 4: BROADCAST (Normal Operation)                                  │
│  ─────────────────────────────────────────────────────────────────────  │
│  • Leader proposes changes (PROPOSE message)                            │
│  • Followers acknowledge (ACK message)                                  │
│  • Leader commits after quorum (COMMIT message)                         │
│  • All servers apply transaction                                        │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### ZAB Message Flow

```
Client          Leader          Follower 1      Follower 2
  │               │                │               │
  │  write request│                │               │
  │──────────────>│                │               │
  │               │                │               │
  │               │   PROPOSE      │               │
  │               │───────────────>│               │
  │               │                │               │
  │               │   PROPOSE      │               │
  │               │───────────────────────────────>│
  │               │                │               │
  │               │      ACK       │               │
  │               │<───────────────│               │
  │               │                │               │
  │               │      ACK       │               │
  │               │<───────────────────────────────│
  │               │                │               │
  │               │  Quorum reached                │
  │               │                │               │
  │               │   COMMIT       │               │
  │               │───────────────>│               │
  │               │                │               │
  │               │   COMMIT       │               │
  │               │───────────────────────────────>│
  │               │                │               │
  │  response     │                │               │
  │<──────────────│                │               │
  │               │                │               │
```

### Key Concepts

#### Transaction ID (zxid)
```
zxid = 64-bit integer
  ├── Upper 32 bits: Epoch (leader term)
  └── Lower 32 bits: Counter (transaction number)

Example:
  zxid = 0x100000001
  epoch = 1
  counter = 1
```

#### Quorum
```
For N servers, need (N/2 + 1) votes to commit

Examples:
  3 servers → need 2 votes
  5 servers → need 3 votes
  7 servers → need 4 votes
```

## Failure Modes

### Leader Failure

```
Timeline:
─────────────────────────────────────────────────────────────────
t=0:  Leader fails
t=1:  Followers detect timeout
t=2:  Leader election triggered
t=3:  New leader elected
t=4:  Synchronization phase
t=5:  Normal operation resumes

Recovery time: 200ms - 2s (depends on config)
```

### Network Partition

```
Case 1: Partition isolates leader
─────────────────────────────────
  Leader │ Follower Follower
     ╳       │           │
             │           │
  Result: Leader elected from remaining

Case 2: Partition isolates minority
─────────────────────────────────
  Leader ── Follower │ Follower
                      │
  Result: No disruption, minority reforms

Case 3: Partition isolates majority
─────────────────────────────────
  Leader ── Follower │ Follower
                      │
  Result: Entire cluster unavailable
```

### Session Expiration

```
Session Lifecycle:
───────────────────────────────────────────────────────────────
Connected ──► Heartbeat ──► Disconnected ──► Expired ──► New Session
                │                                    │
                │ timeout (30s default)              │
                └────────────────────────────────────┘

States:
  • Connected: Normal operation
  • Disconnected: Temporary network issue
  • Expired: Session ended, ephemeral znodes removed
```

## Configuration Parameters

### Core Settings

```properties
# tickTime: Basic time unit in ms (default: 2000)
# Controls heartbeats and timeouts
tickTime=2000

# initLimit: Ticks for initial sync (default: 10)
# Leader-follower initial sync timeout
initLimit=10

# syncLimit: Ticks for sync (default: 5)
# Leader-follower sync timeout
syncLimit=5

# dataDir: Directory for persistent data
dataDir=/var/lib/zookeeper

# clientPort: Client connection port
clientPort=2181
```

### Performance Tuning

```properties
# maxClientCnxns: Max connections per IP (default: 60)
maxClientCnxns=100

# minSessionTimeout: Minimum session timeout (ms)
minSessionTimeout=4000

# maxSessionTimeout: Maximum session timeout (ms)
maxSessionTimeout=40000

# autopurge.snapRetainCount: Snapshots to retain
autopurge.snapRetainCount=5

# autopurge.purgeInterval: Hours between purges
autopurge.purgeInterval=1
```

### Cluster Configuration

```properties
# Server configuration
server.1=zk1:2888:3888:participant
server.2=zk2:2888:3888:participant
server.3=zk3:2888:3888:participant

# Observer configuration
server.4=zk4:2888:3888:observer

# Weight configuration (for weighted voting)
server.1.weight=1
server.2.weight=1
server.3.weight=1
```

## Observer Nodes

### When to Use Observers

```
Use Cases:
  • Scale read capacity without impacting writes
  • Geographic distribution (high latency links)
  • Testing (don't want to affect production)
  • Read-heavy workloads

Example:
  ┌─────────────────────────────────────────────────────────┐
  │                    US Data Center                        │
  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐     │
  │  │ Leader  │ │Follower │ │Follower │ │Observer │     │
  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘     │
  │       │           │           │           │           │
  │       └───────────┴───────────┴───────────┘           │
  │                       │                               │
  └───────────────────────┼───────────────────────────────┘
                          │
                          │ Replication
                          │
  ┌───────────────────────┼───────────────────────────────┐
  │                       │                               │
  │  ┌─────────────────────────────────────────────────┐  │
  │  │              EU Data Center                      │  │
  │  │                                                  │  │
  │  │  ┌─────────┐ ┌─────────┐ ┌─────────┐          │  │
  │  │  │Follower │ │Follower │ │Observer │          │  │
  │  │  └─────────┘ └─────────┘ └─────────┘          │  │
  │  │                                                  │  │
  │  └─────────────────────────────────────────────────┘  │
  └───────────────────────────────────────────────────────┘
```

### Observer Configuration

```properties
# In zoo.cfg
server.4=zk4:2888:3888:observer

# In myid file (on zk4)
echo "4" > /var/lib/zookeeper/myid

# Dynamic reconfiguration
zkServer.sh addObserver zk4:2888:3888
```

## Health Monitoring

### Health Check Commands

```bash
# Four Letter Words
echo ruok | nc localhost 2181    # Returns "imok" if healthy
echo stat | nc localhost 2181    # Server statistics
echo mntr | nc localhost 2181    # Metrics for monitoring
echo conf | nc localhost 2181    # Configuration
echo cons | nc localhost 2181    # Connections
echo dump | nc localhost 2181    # Session dump

# zkServer.sh
zkServer.sh status    # Shows leader/follower status
zkServer.sh start     # Start server
zkServer.sh stop      # Stop server
zkServer.sh restart   # Restart server
```

### Key Metrics

| Metric | Description | Alert Threshold |
|--------|-------------|-----------------|
| `zk_outstanding_requests` | Pending requests | > 100 |
| `zk_num_alive_connections` | Active connections | > 1000 |
| `zk_avg_latency` | Average latency (ms) | > 100 |
| `zk_followers` | Number of followers | < (N/2) |
| `zk_sync_time` | Last sync time (ms) | > 1000 |

## Best Practices

### Deployment

```
✓ Deploy odd number of servers (3, 5, 7)
✓ Distribute across availability zones
✓ Use dedicated servers for ZK
✓ Monitor disk I/O
✓ Use fast disks (SSD)
✓ Keep data directory small

✗ Don't deploy on same machine as application
✗ Don't use shared storage
✗ Don't run heavy workloads on ZK servers
✗ Don't ignore monitoring
```

### Configuration

```
✓ Set appropriate session timeout
✓ Use observer nodes for read scaling
✓ Monitor and tune performance
✓ Regular backups of data directory
✓ Test failover scenarios

✗ Don't store large data (> 1MB) in znodes
✗ Don't create too many watches
✗ Don't ignore network latency
✗ Don't use default settings in production
```

## Summary

| Component | Purpose | Key Point |
|-----------|---------|-----------|
| Leader | Write handling | Only 1 per cluster |
| Follower | Read handling + voting | Participates in consensus |
| Observer | Read-only scaling | Does not vote |
| ZAB | Consensus protocol | Ensures consistency |
| Quorum | Majority voting | (N/2 + 1) needed |
| Session | Client connection | Ephemeral znodes tied to this |
