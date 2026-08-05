# Consistency vs Availability

## Problem Statement

In a distributed system, when network partitions occur (and they always do), you must choose: do you serve stale data (sacrifice consistency) or reject requests (sacrifice availability)? You cannot have both.

## The CAP Theorem

Brewer's CAP theorem states that a distributed system can provide at most two of three guarantees:

- **Consistency (C)**: Every read receives the most recent write or an error. All nodes see the same data at the same time.
- **Availability (A)**: Every request receives a non-error response, without guaranteeing it contains the most recent write.
- **Partition Tolerance (P)**: The system continues to operate despite network partitions between nodes.

Since network partitions are inevitable, the real choice is between consistency and availability during a partition.

## PACELC: Beyond CAP

CAP only addresses behavior during partitions. PACELC extends this:

```
If Partition:
  Choose between Availability and Consistency
Else (normal operation):
  Choose between Latency and Consistency
```

This captures the everyday trade-off: even when the network is healthy, stronger consistency requires more coordination, which increases latency.

## When to Choose Consistency (CP Systems)

Choose consistency when incorrect data causes real harm:

- **Financial systems**: Double-spending or incorrect balances are unacceptable
- **Inventory management**: Overselling products destroys trust
- **Leader election**: Split-brain leaders corrupt data
- **Distributed locks**: Two processes holding the same lock defeats the purpose
- **Idempotency keys**: Preventing duplicate charges requires consistent deduplication

### Characteristics of CP Systems

- Use consensus protocols (Raft, Paxos, Zab)
- Higher write latency due to coordination
- May return errors during partitions
- Examples: HBase, MongoDB (with majority writes), ZooKeeper, etcd

## When to Choose Availability (AP Systems)

Choose availability when downtime costs more than stale data:

- **Social media feeds**: Showing a post 30 seconds late is acceptable; showing an error is not
- **DNS resolution**: Returning a cached record is better than failing resolution
- **Content delivery**: Serving slightly stale content beats serving nothing
- **Recommendation engines**: Approximate recommendations still provide value
- **Event logging**: Better to log an event with potential duplicates than lose it

### Characteristics of AP Systems

- Use eventual consistency models
- Lower latency since no cross-node coordination required
- May return stale data during and after partitions
- Examples: Cassandra, DynamoDB, CouchDB

## Real-World Examples

### Cassandra (AP by Default)

Cassandra tunable consistency. You choose per query:

```
// High consistency: wait for majority
CONSISTENCY QUORUM
SELECT * FROM users WHERE id = 123;

// High availability: accept any replica
CONSISTENCY ONE
SELECT * FROM users WHERE id = 123;
```

Write path uses quorum by default. Reads can be tuned. This flexibility lets you make different choices for different operations.

### HBase (CP)

HBase uses a single RegionServer as master for each region. If that server is unavailable, the region is unavailable. This guarantees consistency but reduces availability.

### PostgreSQL with Streaming Replication (CP-leaning)

Primary-replica setup. Writes go to primary. Replicas may lag. If primary dies, you must promote a replica, causing potential data loss. Consistency is prioritized over availability.

## The Latency-Consistency Spectrum

Even without partitions, you face this trade-off:

```
Strong Consistency <-----> Eventual Consistency
     High Latency                Low Latency
     Strong Guarantees           Weak Guarantees
     Complex Implementation     Simple Implementation
```

### Levels of Consistency

1. **Linearizability**: Strongest. Appears as if all operations happen atomically at a single point in time. High cost.
2. **Sequential Consistency**: Operations appear in some sequential order. Moderate cost.
3. **Causal Consistency**: Causally related operations seen in order. Lower cost.
4. **Eventual Consistency**: All replicas eventually converge. Lowest cost.

## Decision Matrix

| Factor | Choose Consistency | Choose Availability |
|--------|-------------------|---------------------|
| Data correctness critical | Yes | No |
| Downtime cost per minute | Low | High |
| User tolerance for stale data | Low | High |
| Regulatory requirements | Strict | Lenient |
| Partition frequency | Rare | Common |
| Read-to-write ratio | High write | High read |

## Interview Relevance

**Common questions**:
- "How would you design a distributed counter?" (consistency vs availability for increment operations)
- "Design a chat system" (message ordering consistency vs delivery availability)
- "How do you handle conflicts in a multi-region database?" (CRDTs, last-writer-wins)

**What interviewers want to hear**:
- You understand CAP is a spectrum, not binary
- You can discuss tunable consistency
- You know specific databases and their consistency models
- You can map business requirements to consistency needs

**Red flags**:
- "CAP theorem means you can only have two" without explaining PACELC
- Choosing consistency for everything or availability for everything
- Not considering the latency cost of consistency

## Key Takeaway

There is no universally correct choice. The right answer depends on what your application can tolerate: stale data or unavailable service. Document your choice, measure the consequences, and revisit when constraints change.
