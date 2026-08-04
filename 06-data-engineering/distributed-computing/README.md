# Distributed Computing

## Table of Contents

1. [What is Distributed Computing](#what-is-distributed-computing)
2. [CAP Theorem](#cap-theorem)
3. [Consistency Models](#consistency-models)
4. [Partitioning](#partitioning)
5. [Replication](#replication)
6. [Fault Tolerance](#fault-tolerance)
7. [Consensus Algorithms](#consensus-algorithms)
8. [Distributed Transactions](#distributed-transactions)
9. [MapReduce Paradigm](#mapreduce-paradigm)
10. [Key Takeaways](#key-takeaways)

---

## What is Distributed Computing

Distributed computing is a model where components of a software system are located on networked computers that communicate and coordinate their actions by passing messages. The goal is to achieve a common objective while tolerating failures and leveraging parallelism.

### Why Distribute

- **Scalability**: Handle workloads beyond single-machine capacity
- **Fault Tolerance**: Continue operating despite node failures
- **Latency**: Place computation closer to data or users
- **Cost**: Commodity hardware is cheaper than high-end servers
- **Geographic Distribution**: Serve users across regions

### Key Challenges

- **Partial Failure**: Some nodes may fail while others continue
- **Network Unreliability**: Messages may be lost, delayed, or duplicated
- **Clock Synchronization**: Physical clocks across nodes are never perfectly synchronized
- **Concurrency**: Multiple nodes may access shared resources simultaneously
- **Consistency**: Keeping data consistent across replicas is complex

### Distributed System Properties

| Property | Description |
|----------|-------------|
| Availability | System remains operational despite failures |
| Consistency | All nodes see the same data at the same time |
| Partition Tolerance | System continues despite network partitions |
| Tolerance | System can recover from failures |
| Scalability | System can grow to handle increased load |

---

## CAP Theorem

The CAP theorem, proposed by Eric Brewer in 2000, states that a distributed data store can provide at most two of three guarantees simultaneously:

### The Three Properties

**Consistency (C)**:
- Every read receives the most recent write or an error
- All nodes see the same data at the same time
- Achieved through synchronization protocols

**Availability (A)**:
- Every request receives a non-error response
- No guarantee it contains the most recent write
- System is always operational

**Partition Tolerance (P)**:
- System continues to operate despite network partitions
- Messages between nodes may be lost
- Network splits are inevitable in distributed systems

### CAP Combinations

```
        C
       / \
      /   \
     A─────P
     
CA: Consistency + Availability (no partition tolerance)
   → Single-node systems, not truly distributed
   
CP: Consistency + Partition Tolerance
   →牺牲 availability during partitions
   → Examples: HBase, MongoDB, Redis Cluster
   
AP: Availability + Partition Tolerance
   →犧牲 consistency during partitions
   → Examples: Cassandra, DynamoDB, CouchDB
```

### Practical Implications

| System | CAP Choice | Behavior During Partition |
|--------|------------|--------------------------|
| HBase | CP | Rejects writes, returns errors |
| Cassandra | AP | Accepts writes, eventual consistency |
| MongoDB | CP | Primary election, read from primary |
| DynamoDB | AP | Last-writer-wins conflict resolution |
| PostgreSQL | CP | Leader election required |

### PACELC Theorem

Extension of CAP: If Partition, choose Availability or Consistency; Else, choose Latency or Consistency.

```
if (partition) {
    choose between A and C
} else {
    choose between L and C
}
```

---

## Consistency Models

### Strong Consistency (Linearizability)

- All operations appear to execute atomically in a single global order
- Any read returns the result of the most recent write
- Highest consistency, lowest performance

```
Client 1: Write(x=1) ──────── Write Complete
Client 2:           Read(x) → 1 (always)
```

**Use cases**: Financial transactions, leader election, distributed locks

### Sequential Consistency

- Operations appear in some sequential order
- Each client's operations appear in their program order
- Weaker than linearizability, better performance

```
Client 1: Write(x=1) Write(y=2)
Client 2: Read(x) → 1  Read(y) → 2
(Order preserved per client, but not globally)
```

### Causal Consistency

- Operations that are causally related appear in order
- Concurrent operations may appear in different orders on different nodes

```
Write(x=1) → Write(x=2)  (causal)
Read(x) may return 1 or 2 depending on node
```

### Eventual Consistency

- If no new updates, all replicas eventually converge
- Weakest useful consistency guarantee
- Highest availability and performance

```
Client 1: Write(x=1) ────────
Client 2:           Read(x) → 0 (stale)
Client 2:                       Read(x) → 1 (eventually)
```

**Use cases**: DNS, social media feeds, shopping carts

### Read-Your-Writes Consistency

- A client always reads its own writes
- Other clients may see stale data

```
Client 1: Write(x=1) → Read(x) → 1 (always)
Client 2:                    Read(x) → 0 or 1
```

### Monotonic Read Consistency

- Once a client reads a value, subsequent reads never return older values

```
Client: Read(x) → 1 → Read(x) → 1 or 2 (never 0)
```

---

## Partitioning

Partitioning splits data across multiple nodes to distribute load and enable parallel processing.

### Hash Partitioning

```python
partition_id = hash(key) % num_partitions
```

**Pros**:
- Even data distribution
- Simple to implement
- Good for point queries

**Cons**:
- Poor range query performance
- Resizing requires rehashing all data

**Example**: Consistent hashing for dynamic membership

### Range Partitioning

```
Partition 0: keys [A - F]
Partition 1: keys [G - M]
Partition 2: keys [N - S]
Partition 3: keys [T - Z]
```

**Pros**:
- Excellent range query performance
- Natural ordering preserved

**Cons**:
- Risk of hotspots with sequential keys
- May result in uneven partition sizes

### Consistent Hashing

```
         0
    ┌────┴────┐
    │  Node A │
    │  Node B │
    │  Node C │
    │  Node D │
    └────┬────┘
         360°
```

- Nodes and keys mapped to same hash ring
- Each key assigned to next node clockwise
- Adding/removing node only affects adjacent keys
- Used in: DynamoDB, Cassandra, Apache Kafka

### Directory-Based Partitioning

- Central directory maps each key to its partition
- Most flexible but introduces single point of failure
- Used in: Google's Bigtable, HBase

### Partitioning Strategies

| Strategy | Best For | Example |
|----------|----------|---------|
| Hash | Uniform distribution, point lookups | Cassandra |
| Range | Range queries, ordered data | HBase, Bigtable |
| Consistent Hash | Dynamic membership | DynamoDB |
| Geo | Geographic proximity | Cassandra, CockroachDB |

### Multi-Dimensional Partitioning

Combining multiple partitioning strategies:
- Primary partitioning: Hash by customer_id
- Secondary partitioning: Range by timestamp
- Enables efficient queries on multiple dimensions

---

## Replication

Replication copies data across multiple nodes for fault tolerance and performance.

### Synchronous Replication

```
Client → Leader → Follower 1 (ack)
                → Follower 2 (ack)
Client ← Leader ← All followers ack
```

**Pros**: Strong consistency, no data loss
**Cons**: Higher latency, reduced availability

### Asynchronous Replication

```
Client → Leader → Ack (immediately)
                → Follower 1 (async)
                → Follower 2 (async)
```

**Pros**: Lower latency, higher availability
**Cons**: Risk of data loss, stale reads

### Semi-Synchronous Replication

```
Client → Leader → Follower 1 (sync)
                → Follower 2 (async)
Client ← Leader ← At least one follower ack
```

**Pros**: Balanced consistency and performance
**Cons**: Complexity in implementation

### Replication Strategies

**Leader-Based (Master-Slave)**:
- Single leader handles all writes
- Followers replicate from leader
- Read scaling through followers
- Single point of failure for writes

**Multi-Leader (Master-Master)**:
- Multiple nodes accept writes
- Conflict resolution required
- Higher write availability
- Complex consistency management

**Leaderless (Dynamo-Style)**:
- Any node can accept reads/writes
- Quorum-based consistency (W + R > N)
- Tunable consistency levels
- Used in: Cassandra, Riak, DynamoDB

### Conflict Resolution

| Strategy | Description | Trade-off |
|----------|-------------|-----------|
| Last Writer Wins (LWW) | Timestamp-based, latest wins | Data loss possible |
| Vector Clocks | Track causal dependencies | Complex, metadata overhead |
| CRDTs | Conflict-free data structures | Limited to specific data types |
| Application Logic | Custom resolution rules | Application complexity |

---

## Fault Tolerance

Fault tolerance is the ability of a system to continue operating despite component failures.

### Types of Failures

1. **Crash Failures**: Node stops completely (power loss, OOM)
2. **Byzantine Failures**: Node behaves arbitrarily (bugs, malicious)
3. **Network Partitions**: Communication between nodes is lost
4. **Timing Failures**: Nodes respond too slowly
5. **Software Failures**: Bugs, configuration errors

### Failure Detection

**Heartbeat Mechanism**:
```
Node A → heartbeat → Node B (every T seconds)
Node B: no heartbeat for 3T → declare Node A dead
```

**Gossip Protocol**:
- Each node periodically shares state with random peers
- Information propagates through the cluster
- Eventually consistent failure detection

### Recovery Strategies

**Task Retry**:
- Failed tasks automatically restarted
- Requires idempotent operations
- Exponential backoff prevents thundering herd

**Checkpointing**:
- Periodic snapshot of computation state
- Recovery from last checkpoint on failure
- Trade-off: checkpoint frequency vs. recovery time

**Replication**:
- Data replicated across multiple nodes
- Failed node's work taken over by replica
- Leader election for leader-based systems

### Availability Calculations

For a system with N replicas, each with availability p:

**Series (all required)**:
```
A_system = p^N
```

**Parallel (any one required)**:
```
A_system = 1 - (1-p)^N
```

Example: 3 nodes with 99.9% availability each:
- Series: 99.9%^3 = 99.7% (less available)
- Parallel: 1 - 0.001^3 = 99.999999% (more available)

---

## Consensus Algorithms

Consensus algorithms ensure that multiple nodes agree on a single value or order of operations.

### Raft

Designed for understandability, used in etcd, Consul, TiKV.

**Components**:
- **Leader**: Handles all client requests, replicates log entries
- **Followers**: Receive log entries from leader, vote in elections
- **Candidates**: Temporary state during leader election

**Log Replication**:
```
Client → Leader: Append Entry
Leader → Followers: Replicate Entry
Followers → Leader: Ack
Leader: Entry committed after majority ack
Leader → Followers: Notify commit
```

**Leader Election**:
1. Follower timeout triggers election
2. Follower becomes candidate, increments term
3. Candidate requests votes from peers
4. First candidate with majority votes becomes leader

**Safety Properties**:
- At most one leader per term
- Leader's log is at least as up-to-date as followers
- Committed entries are never lost

### Paxos

Classic consensus algorithm, used in Google's Chubby, Spanner.

**Roles**:
- **Proposer**: Proposes values
- **Acceptor**: Accepts or rejects proposals
- **Learner**: Learns accepted values

**Two-Phase Protocol**:
1. **Prepare Phase**: Proposer sends prepare(n), accepts promise for proposal n
2. **Accept Phase**: Proposer sends accept(n, v), accepts accept if promise was given

**Multi-Paxos**:
- Optimizes repeated consensus for log replication
- Stable leader reduces round trips
- Used in production systems

### Raft vs Paxos

| Aspect | Raft | Paxos |
|--------|------|-------|
| Understandability | High (designed for it) | Low (complex) |
| Leader | Required | Optional (Multi-Paxos) |
| Log Replication | Built-in | Separate concern |
| Membership Changes | Joint consensus | Configuration changes |
| Used in | etcd, Consul, TiKV | Chubby, Spanner |

### Byzantine Fault Tolerance (BFT)

Tolerates arbitrary/malicious failures:

- **PBFT**: Practical Byzantine Fault Tolerance
  - Tolerates f faulty nodes with 3f+1 total nodes
  - Three-phase protocol
  - Used in: blockchain, financial systems

- **Tendermint**: BFT consensus for blockchain
  - Two-round voting protocol
  - Used in: Cosmos network

### ZAB (ZooKeeper Atomic Broadcast)

- Used in Apache ZooKeeper
- Primary-backup replication
- Linearizable writes
- Used for: coordination, configuration, leader election

---

## Distributed Transactions

Distributed transactions span multiple nodes and must maintain ACID properties.

### Two-Phase Commit (2PC)

**Phase 1 - Prepare**:
```
Coordinator → Participants: Prepare
Participants: Execute transaction locally, hold locks
Participants → Coordinator: Vote (Yes/No)
```

**Phase 2 - Commit/Abort**:
```
If all voted Yes:
  Coordinator → Participants: Commit
Else:
  Coordinator → Participants: Abort
```

**Issues**:
- Blocking: Participants wait for coordinator during failure
- Single point of failure: Coordinator failure blocks all participants
- Latency: Two round trips required

### Three-Phase Commit (3PC)

Adds a pre-commit phase to reduce blocking:
1. Can Commit
2. Pre-Commit
3. Do Commit

**Advantage**: Non-blocking under certain failure models
**Disadvantage**: More complex, still not fully fault-tolerant

### Saga Pattern

Long-lived transactions broken into a sequence of local transactions:

```
T1 → T2 → T3 → T4
Each Ti has a compensating transaction Ci

If T3 fails:
Compensate: C2 → C1
```

**Implementation**:
- **Choreography**: Events trigger next step
- **Orchestration**: Central coordinator manages flow

### Event Sourcing

Store all changes as a sequence of events:

```
Event 1: AccountCreated(id=1, balance=0)
Event 2: Deposited(id=1, amount=100)
Event 3: Withdrawn(id=1, amount=50)
Current State: balance = 50
```

**Benefits**:
- Complete audit trail
- Temporal queries
- Easy replay and recovery

### Consistency Patterns

| Pattern | Consistency | Use Case |
|---------|-------------|----------|
| 2PC | Strong | Banking, inventory |
| Saga | Eventual | Microservices, e-commerce |
| Event Sourcing | Eventual | Audit, compliance |
| TCC (Try-Confirm-Cancel) | Strong | Resource reservation |

---

## MapReduce Paradigm

MapReduce is a programming model for processing large datasets in parallel across a distributed cluster.

### Map Phase

```python
def map(key, value):
    # Process input key-value pair
    # Emit intermediate key-value pairs
    for word in value.split():
        emit(word, 1)
```

- Input split into chunks
- Each chunk processed independently by a map task
- Produces intermediate key-value pairs

### Shuffle and Sort

```
Map Output: (word, 1) pairs
            ↓
Partition by key hash
            ↓
Sort within each partition
            ↓
Group by key: (word, [1, 1, 1, ...])
```

### Reduce Phase

```python
def reduce(key, values):
    # values is an iterator for this key
    total = sum(values)
    emit(key, total)
```

- Receives all values for each key
- Produces final output

### Word Count Example

```
Input: "hello world hello world hello"

Map:
  ("hello", 1), ("world", 1), ("hello", 1), ("world", 1), ("hello", 1)

Shuffle/Sort:
  ("hello", [1, 1, 1])
  ("world", [1, 1])

Reduce:
  ("hello", 3)
  ("world", 2)
```

### MapReduce Execution

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Input Split │    │  Input Split │    │  Input Split │
│     Map      │    │     Map      │    │     Map      │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       ▼                   ▼                   ▼
┌──────────────────────────────────────────────────────┐
│              Shuffle and Sort                        │
└──────────────────────────┬───────────────────────────┘
                           │
       ┌───────────────────┼───────────────────┐
       ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│    Reduce    │    │    Reduce    │    │    Reduce    │
└──────────────┘    └──────────────┘    └──────────────┘
```

### Extensions to MapReduce

- **Combiner**: Mini-reducer on map side (local aggregation)
- **Partitioner**: Controls which reducer gets which keys
- **InputFormat**: Defines how input is read and split
- **OutputFormat**: Defines how output is written

### Limitations of MapReduce

- Disk-based intermediate data (slow for iterative algorithms)
- Two-phase model is rigid
- No native support for iterative processing (ML)
- Shuffle phase is network-intensive
- Replaced by more flexible frameworks (Spark, Flink)

---

## Key Takeaways

1. **Distributed computing** is essential for scaling beyond single-machine limits but introduces complexity
2. **CAP theorem** forces trade-offs between consistency, availability, and partition tolerance — you can't have all three
3. **Consistency models** range from strong (linearizable) to weak (eventual) — choose based on use case
4. **Partitioning** distributes data — hash for uniform distribution, range for ordered queries, consistent hashing for dynamic membership
5. **Replication** provides fault tolerance — synchronous for consistency, asynchronous for performance
6. **Fault tolerance** requires detection (heartbeats, gossip), recovery (retry, checkpointing), and redundancy (replication)
7. **Consensus algorithms** (Raft, Paxos) ensure nodes agree on a single value despite failures
8. **Distributed transactions** are expensive — prefer Sagas and eventual consistency where possible
9. **MapReduce** pioneered distributed processing but has been superseded by more flexible frameworks
10. **The fallacies of distributed computing** remind us that network, latency, and failure are always present

---

## Further Reading

- *Designing Data-Intensive Applications* by Martin Kleppmann
- *Distributed Systems* by Maarten van Steen and Andrew Tanenbaum
- *Distributed Algorithms* by Nancy Lynch
- *Understanding Distributed Systems* by Roberto Vitillo
- *The Art of Multiprocessor Programming* by Maurice Herlihy
