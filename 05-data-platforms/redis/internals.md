# Redis Internals

## Event Loop Architecture

Redis operates on a single-threaded event loop using the ae library. The event loop multiplexes I/O operations across multiple connections without threads. The ae library abstracts platform-specific I/O multiplexing: epoll on Linux, kqueue on macOS, and select as fallback. Each iteration of the event loop processes timed events (expired keys, background tasks) and file events (client connections, disk I/O).

The single-threaded model eliminates locking overhead and context switching. Redis achieves high throughput by keeping operations in-memory and using non-blocking I/O. The event loop processes one command at a time, ensuring atomicity without explicit locking. Redis 6.0+ introduced threaded I/O for network operations, allowing multiple threads to read/write while the main thread executes commands.

The event loop uses a beforeSleep and afterSleep hook. beforeSleep processes pending commands from clients. afterSleep handles file events. The aeMain function runs the loop continuously. Timer events are processed with millisecond precision. The event loop integrates with the BIO (background I/O) threads for asynchronous operations like AOF writes and lazyfree.

The event loop tracks file descriptors using the aeFileEvent structure. Each file event has read and write handlers. The aeTimeEvent structure tracks timer events with absolute or relative timeouts. The event loop processes events in order: file events, time events, and beforeSleep hooks. The aeProcessEvents function orchestrates event processing.

## Data Structures

Redis implements specialized data structures for each type. Strings use a simple SDS (Simple Dynamic String) structure with O(1) length retrieval and binary safety. SDS preallocates memory to avoid frequent reallocations. Strings can hold integers, floats, and binary data up to 512MB.

Lists use a quicklist encoding: a doubly-linked list of ziplists, balancing memory efficiency with O(1) push/pop at both ends. The quicklist combines the compression benefits of ziplists with the O(1) operations of linked lists. For small lists, Redis uses ziplist (flat, compressed) encoding. Quicklist nodes are compressed using LZF compression to reduce memory usage.

Hashes use listpack (replacing ziplink in Redis 7.0) for small hashes and hashtable encoding for large ones. Listpack stores elements in a single contiguous memory block. Sets use intset for small integer-only sets (sorted, binary search) and hashtable otherwise. Sorted sets use skiplists with a hashtable for O(log N) range queries and O(1) membership checks.

HyperLogLogs use a probabilistic algorithm with 12KB memory footprint regardless of cardinality. Streams (Redis 5.0+) use a radix tree of listpacks for efficient append-only logging. The stream data structure supports consumer groups, pending entries lists, and acknowledgment tracking. Bitmaps use SDS strings for bitwise operations on byte arrays.

## Persistence Mechanisms

Redis supports two persistence approaches. RDB creates point-in-time snapshots by forking the process and writing the dataset to disk. The child process writes the snapshot while the parent handles commands. RDB produces compact files but risks data loss since writes between snapshots are untracked. RDB uses copy-on-write semantics during the fork.

AOF (Append Only File) logs every write operation. The log is rewritten periodically to remove redundant commands. AOF fsync policies control durability: `always` (fsync every write), `everysec` (fsync once per second, default), or `no` (OS decides). AOF rewrite uses a background process that reads the current dataset and writes a compact AOF file, then atomically replaces the old one.

AOF rewrite creates a child process that iterates all keys and writes their current values. The parent continues appending to the old AOF during rewrite. When the rewrite completes, the new AOF is swapped in. Redis 7.0 introduced multi-part AOF, splitting the AOF into base and incremental files for faster recovery. The base file is the result of the last rewrite; incremental files append new writes.

RDB and AOF can be used together. When both are enabled, Redis loads the AOF file on startup since it is guaranteed to be more complete. The `aof-use-rdb-preamble` setting allows the AOF to start with an RDB preamble for faster loading. The `aof-load-truncated` setting handles truncated AOF files gracefully.

## Replication Architecture

Redis uses asynchronous replication. A replica connects to a primary and issues `PSYNC` to begin partial or full synchronization. Full synchronization transfers the entire RDB file. After the RDB transfer, the replica replays any buffered commands from the primary. Full sync is expensive and should be avoided when possible.

Partial synchronization uses an in-memory replication backlog (circular buffer). If the replica's offset falls within the backlog, only the missing commands are sent. Replication is non-blocking on the primary: the primary continues accepting writes while replicating. Replicas can be configured with `replica-read-only yes` to serve read traffic.

Replication offsets track the byte position in the replication stream. The primary maintains a replication backlog sized by `repl-backlog-size` (default 1MB). If the replica falls behind the backlog, a full sync is required. The `repl-diskless-sync` option sends the RDB directly over the network without writing to disk first, reducing replication latency.

Redis supports cascading replication: replicas can replicate from other replicas. This reduces the primary's replication load. The `replicaof` command configures replication. The `INFO replication` command shows replication status including master link status, replication lag, and offset. The `WAIT` command blocks until the specified number of replicas acknowledge the write.

## Cluster Architecture

Redis Cluster partitions data across 16,384 hash slots. Each node owns a subset of slots. Keys are mapped to slots using `CRC16(key) mod 16384`. Hash tags allow related keys to live in the same slot by using `{tag}` syntax. Cluster mode requires at least 3 master nodes for fault tolerance.

Cluster nodes communicate via a gossip protocol. Each node periodically pings random peers, sharing its view of the cluster topology. When a node detects a failure, it marks the node as potentially failing and propagates this via gossip. After a configurable timeout, the node is considered failing and a failover occurs.

Cluster buses use a separate TCP channel for inter-node communication. Gossip messages include the sender's view of cluster state. The cluster uses epoch numbers to order configuration changes. When a master fails, replicas vote using a Raft-like mechanism. The replica with the highest offset wins the election.

Cluster nodes track slot ownership using a bitmap. Each node knows which slots it owns and which slots other nodes own. When a client sends a command for a key in a slot not owned by the receiving node, it returns a MOVED redirect. The ASK redirect handles slot migration between nodes. The `CLUSTER NODES` command shows cluster topology.

## Memory Management

Redis uses jemalloc as its memory allocator by default. jemalloc provides efficient allocation for varied object sizes and reduces fragmentation. Redis tracks memory usage per key and can enforce limits via `maxmemory`. When the limit is reached, eviction policies remove keys: LRU, LFU, random, or volatile-TTL.

Internally, Redis uses object sharing for small integers (0-9999) and string caching. Objects are reference-counted and lazily freed. The `OBJECT ENCODING` command reveals the internal encoding of a key, useful for debugging memory usage. The `MEMORY USAGE` command returns the memory footprint of a key.

Memory optimization strategies include: using appropriate data structure encodings, leveraging compression (ziplist/listpack), sharing small integers, and using hash tags to colocate related keys. The `DEBUG SLEEP` command tests eviction behavior. Memory defragmentation can be enabled with `activedefrag yes`.

The `INFO memory` command provides memory statistics including used_memory, used_memory_rss, and mem_fragmentation_ratio. The `MEMORY DOCTOR` command provides memory optimization recommendations. The `MEMORY PURGE` command releases unused memory from jemalloc. Redis tracks memory allocation per database and per data type.

## Lua Scripting

Redis embeds a Lua 5.1 interpreter for server-side scripting. Scripts execute atomically: the event loop is blocked during script execution. The `EVAL` command sends a Lua script and arguments to the server. Scripts can call Redis commands via the `redis.call()` function. Scripts must be deterministic for cluster compatibility.

Lua scripts are cached using SHA1 hashes. Subsequent calls use `EVALSHA` with the hash instead of the full script. Script debugging is supported via `DEBUG SLEEP` and step-through capabilities. Redis Cluster supports cross-slot scripting with `EVAL` routed to the slot of the first key argument.

Script caching is per-instance. The `SCRIPT FLUSH` command clears the cache. Scripts can use `redis.pcall()` for non-fatal error handling. The `EVALSHA` command returns a NOSCRIPT error if the script is not cached. Scripts can return Lua tables that are converted to Redis arrays.

Lua scripts have access to Redis commands via `redis.call()` and `redis.pcall()`. The `redis.call()` function raises errors; `redis.pcall()` returns errors as Lua tables. Scripts can access KEYS and ARGV arrays for input parameters. The `redis.log()` function writes to the Redis log. Script debugging supports breakpoints and step-through execution.

## Pub/Sub Implementation

Redis Pub/Sub uses a simple dictionary of channels mapped to subscriber lists. When a message is published, Redis iterates the channel's subscriber list and sends the message to each connection. Messages are fire-and-forget: no persistence, no acknowledgment. Subscribers receive messages in real-time.

The `SUBSCRIBE` command adds a client to channel subscribers. Clients receive all messages on subscribed channels. Pattern matching via `PSUBSCRIBE` uses glob-style patterns. Messages are not queued for offline subscribers; if a subscriber is slow, messages may be dropped.

Pub/Sub has no backpressure mechanism. Slow consumers can cause message loss. Redis Streams (introduced in 5.0) provide a more robust alternative with persistence, consumer groups, and acknowledgment tracking. The `SUBSCRIBE` command transforms the client connection into a subscriber connection; the client can only issue subscription commands.

Pub/Sub channels support pattern matching with `PSUBSCRIBE` and `PUNSUBSCRIBE`. The `PUBSUB CHANNELS` command lists active channels. The `PUBSUB NUMSUB` command returns subscriber counts. The `PUBSUB NUMPAT` command returns pattern subscription counts. Pub/Sub messages include the channel name and message payload.

## Streams (Redis 5.0+)

Redis Streams provide an append-only log data structure. Streams support consumer groups for distributed consumption. Each entry in a stream has an ID (timestamp-sequence format) and key-value pairs. Streams support trimming by length or time range.

Consumer groups track the last delivered ID per consumer. Pending entries lists (PEL) store unacknowledged messages. The `XACK` command acknowledges consumption. Streams support block-based reads for real-time consumption. The `XCLAIM` command transfers pending entries between consumers for load balancing.

Stream entries are stored in a radix tree of listpacks. The listpacks are compressed to reduce memory usage. The `XLEN` command returns the number of entries. The `XRANGE` and `XREVRANGE` commands return entries in a range. The `XINFO` command provides stream and consumer group information. Streams support automatic trimming with `MAXLEN` and `MINID` options.
