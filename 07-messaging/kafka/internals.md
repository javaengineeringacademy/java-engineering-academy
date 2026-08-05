# Kafka Internals

## Partition Architecture

Kafka divides topics into partitions, each an ordered, immutable sequence of records. Partitions are the fundamental unit of parallelism and scalability. Each partition is assigned to a single broker called the leader; followers replicate the partition for fault tolerance. The leader handles all read and write requests. Followers passively replicate from the leader and do not serve client reads by default.

Partition assignment is deterministic: a partition is placed on a broker based on the replica assignment algorithm. When a new broker joins the cluster, partitions can be rebalanced. Each partition is identified by a topic name and partition index (e.g., `orders-3`). Partitions can be colocated across brokers using replica assignment rules.

The number of partitions determines the maximum parallelism for consumers within a consumer group. Adding partitions after creation is possible but requires careful consideration of key-based routing. Keys are hashed to determine partition assignment, so changing the partition count affects key distribution. Partition leaders are elected from the ISR when the current leader fails.

Each partition maintains an in-memory replica manager that tracks replication state. The replica manager monitors follower lag and manages ISR membership. Partition leaders replicate to followers using a pull-based model. The replication lag is measured in bytes, not time, providing precise replication state tracking.

## Segment Storage

Each partition is divided into segments. A segment is a file on disk containing a contiguous range of offset records. Only the active segment accepts new writes. Once a segment reaches the configured size threshold (default 1GB), it is closed and a new one opens. Older segments are retained based on time or size policies and then deleted or compacted.

Each segment maintains an index file mapping offsets to physical file positions, enabling O(1) lookups. The index is sparse and approximate, so Kafka performs a binary search within the segment file for exact offset resolution. Log compaction runs per-segment, retaining only the latest value per key. Compaction ensures that consumers can recover the latest state by replaying from the beginning.

The segment file naming convention uses the base offset of the segment. For example, segment `00000000000000000000.log` contains records starting at offset 0. The index file (`00000000000000000000.index`) maps relative offsets to file positions. Time-based indexes (`00000000000000000000.timeindex`) enable timestamp-based lookups for retention management.

Segment deletion follows a two-phase process. First, the broker checks retention policies (time-based or size-based). Then, eligible segments are marked for deletion. The `log.cleanup.policy` setting controls whether segments are deleted or compacted. The `log.retention.hours` and `log.retention.bytes` settings define retention boundaries.

## In-Sync Replicas (ISR)

The ISR is the set of replicas that are fully caught up with the leader. A replica is removed from the ISR if it falls behind by more than the configured `replica.lag.time.max.ms` (default 30 seconds). When a producer requires `acks=all`, the broker waits for all ISR members to acknowledge the write before responding.

ISR management is critical for data durability. If a leader fails, the new leader is elected exclusively from the ISR. The `min.insync.replicas` setting controls the minimum ISR size for writes to succeed; if the ISR drops below this threshold, the broker rejects writes with a `NotEnoughReplicasException`. This prevents data loss when insufficient replicas are available.

The ISR shrinks and expands dynamically as replicas fall behind or catch up. The controller tracks ISR membership and propagates changes via metadata updates. Producers receive ISR information in ProduceResponse, allowing them to detect under-replicated conditions. The `unclean.leader.election.enable` setting (default false) prevents non-ISR replicas from becoming leader, trading availability for consistency.

The ISR state is tracked per partition in the controller's metadata cache. When a broker fails, the controller removes all its partitions from the ISR. The controller then initiates leader election for affected partitions. The `replica.lag.time.max.ms` setting determines how long a replica can be lagging before being removed from the ISR.

## Raft Protocol (KRaft)

Kafka 3.0+ introduced KRaft mode, replacing ZooKeeper. KRaft uses the Raft consensus protocol for metadata management. A quorum of controller nodes maintains the cluster metadata log. One controller is elected as the active controller (leader); others replicate the log.

Raft ensures strong consistency for metadata operations such as partition assignments, broker registrations, and configuration changes. The metadata log is partitioned into segments, each with a monotonically increasing epoch. Snapshots are periodically taken to compact the log and accelerate recovery. KRaft eliminates the ZooKeeper dependency, simplifying deployment and reducing operational complexity.

The Raft implementation in Kafka uses a dedicated topic `__cluster_metadata` for storing metadata events. Controllers replicate events sequentially and acknowledge once a quorum confirms. The active controller handles all metadata writes; followers serve read-only requests. Leader election uses term-based voting to prevent split-brain scenarios.

KRaft mode supports dynamic controller quorum changes. The `controller.quorum.voters` setting specifies the controller nodes. Controller IDs are assigned based on node IDs. The metadata log uses a separate log directory from data partitions. Controller snapshots are taken periodically to speed up recovery.

## Consumer Group Protocol

Consumer groups enable load-balanced consumption. Each group has a coordinator broker that manages group state. Consumers send heartbeats at regular intervals to indicate liveness. The group protocol uses a two-phase commit process: first, consumers join the group and receive partition assignments; then, they sync their assignments.

Partition assignment strategies include range, round-robin, and sticky. The sticky strategy minimizes partition reassignment when consumers join or leave. Consumer offsets are stored in an internal topic (`__consumer_offsets`). When a consumer commits offsets, the coordinator persists them, enabling exactly-once semantics when combined with idempotent producers.

The group coordinator uses a session timeout to detect consumer failures. If a consumer fails to heartbeat within the timeout, it is removed from the group and its partitions are reassigned. The `max.poll.interval.ms` setting controls how long a consumer can be idle before being removed. Rebalancing can be triggered by consumer join, leave, or failure.

The group protocol supports cooperative rebalancing (incremental cooperative rebalancing). This approach minimizes partition movement during rebalances. The `partition.assignment.strategy` setting configures the assignment algorithm. The consumer group coordinator manages the rebalance process, tracking partition assignments and consumer liveness.

## Controller Election

In ZooKeeper-based Kafka, the controller is elected via an ephemeral node in ZooKeeper. The first broker to successfully create the node becomes the controller. In KRaft mode, controller election follows Raft leader election semantics. The controller manages partition leader election, topic creation/deletion, and broker failure recovery.

When a broker fails, the controller detects the failure via session timeouts and initiates leader election for all partitions hosted on the failed broker. The election prefers ISR members to maintain data consistency. Unclean leader election (electing non-ISR replicas) is disabled by default for safety. The controller propagates metadata updates to all brokers after leader election.

The controller maintains an in-memory cache of all partition and broker metadata. This cache is rebuilt from the metadata log on startup. Controllers handle thousands of partition leadership changes during broker failures. The controller channel protocol communicates metadata changes between the controller and brokers.

Controller failover is automatic in both ZooKeeper and KRaft modes. In ZooKeeper mode, the first broker to create the ephemeral node becomes the controller. In KRaft mode, the Raft protocol elects a new leader from the controller quorum. The controller manages partition leader election, topic operations, and broker lifecycle.

## Message Format

Kafka messages consist of a key, value, timestamp, headers, and offset. The offset is a 64-bit integer assigned by the broker. Messages are batched into record batches for efficiency. Each batch includes a base offset, first and last timestamps, and a CRC for integrity verification.

The message format evolved across versions. Version 2 (introduced with Kafka 0.11) supports headers and transactions. Compression is applied at the batch level (GZIP, Snappy, LZ4, ZSTD). The broker stores messages as-is; the consumer deserializes the value using a configurable deserializer. Batch-level compression improves throughput by reducing I/O and network overhead.

Record batches include attributes (compression type, timestamp type), producer ID, and producer epoch. Transactional markers are special records that indicate commit or abort. The base offset enables relative offset encoding within the batch, reducing storage overhead. The CRC32C checksum ensures data integrity during storage and retrieval.

The message format supports both log append time andCreateTime timestamp types. Log append time uses the broker's timestamp; create time uses the producer's timestamp. The `message.timestamp.type` setting controls which timestamp is used. Message headers are key-value pairs that support arbitrary metadata.

## Network Layer

Kafka uses a custom NIO-based network layer. A single acceptor thread distributes connections to a pool of network threads. Each network thread reads requests from multiple connections and writes responses. Request handlers process messages on dedicated threads. The reactor pattern decouples I/O from request processing.

The request types include `Produce`, `Fetch`, `Metadata`, `OffsetCommit`, and others. Each request type has a versioned protocol for backward compatibility. The broker tracks request metrics including queue time, local time, remote time, and total time to facilitate performance monitoring. Request throttling limits the rate of specific request types.

The network layer uses zero-copy transfer for Fetch requests via `sendfile()` system call. This transfers data directly from the page cache to the socket buffer, avoiding user-space copies. The broker uses a request queue to buffer incoming requests. The response is sent asynchronously after processing. The socket server handles connection lifecycle, including TLS termination and protocol negotiation.

The network layer implements connection throttling to prevent resource exhaustion. The `max.connections.per.ip` setting limits connections per IP address. The `max.connections.per.ip.overrides` setting allows IP-specific overrides. The network layer tracks connection metrics including active connections, request rates, and response times.
