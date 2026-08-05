# Redis Architecture

## Event Loop Model

Redis uses a single-threaded, event-driven architecture based on `epoll` (Linux), `kqueue` (macOS/BSD), or `IOCP` (Windows). The event loop runs in a single thread, handling all client requests sequentially.

```
Client A ──┐
Client B ──┤──→ Event Loop ──→ Execute Command ──→ Send Response
Client C ──┘
```

### Why Single-Threaded?

- No context switching overhead
- No locking contention
- Predictable latency (no mutexes)
- Simple to reason about correctness

### Event Loop Internals

1. **I/O Multiplexing**: Waits for events on file descriptors
2. **Event Dispatch**: Routes ready events to handlers
3. **Command Execution**: Processes commands synchronously
4. **Response Writing**: Sends replies back to clients

## Data Structure Server

Redis is not a simple key-value store. It is a data structure server where keys map to values of specific types.

| Structure | Engine | Use Case |
|-----------|--------|----------|
| Strings | `embstr`/`raw`/`int` | Cache, counters, locks |
| Lists | `quicklist` | Message queues, feeds |
| Sets | `intset`/`hashtable` | Tags, intersections |
| Sorted Sets | `ziplist`/`skiplist` | Leaderboards, ranges |
| Hashes | `ziplist`/`hashtable` | Object storage |
| Streams | `stream` | Event sourcing, log aggregation |
| HyperLogLog | `sparse`/`dense` | Cardinality estimation |
| Bitmaps | `String` ops | Feature flags, analytics |

## Persistence

### RDB (Redis Database Backup)

- Periodic point-in-time snapshots
- Compact binary format (`dump.rdb`)
- Faster restart, smaller files
- Potential data loss between snapshots

### AOF (Append-Only File)

- Logs every write operation
- Three `fsync` policies: `always`, `everysec`, `no`
- Better durability guarantees
- Larger files, slower restart

### Hybrid Persistence (Redis 4.0+)

- AOF rewrite uses RDB + incremental AOF
- Best of both worlds
- Enabled with `aof-use-rdb-preamble yes`

## Replication

### Primary-Replica Model

```
Primary (read/write) ──→ Replica 1 (read-only)
                      ──→ Replica 2 (read-only)
                      ──→ Replica 3 (read-only)
```

- Asynchronous replication by default
- Full resync: sends RDB + buffer
- Partial resync: uses replication backlog (offset-based)
- Replicas serve stale reads

### Replication Handshake

1. Replica sends `PSYNC` with replication ID and offset
2. Primary decides full or partial resync
3. For full resync: sends RDB, then replates backlog
4. For partial resync: replays missing commands

## Clustering

### Redis Cluster

- Automatic sharding across 16384 hash slots
- Each node owns a subset of slots
- Client-side routing (MOVED/ASK redirections)
- No single point of failure (with replicas)

### Slot Assignment

```
Node A: slots 0-5460
Node B: slots 5461-10922
Node C: slots 10923-16383
```

### Cluster Operations

- **MOVED**: Slot permanently moved to another node
- **ASK**: Slot being migrated, temporary redirect
- **CLUSTER NODES**: Lists all nodes and slots
- **CLUSTER INFO**: Cluster state and health

## Memory Management

- **jemalloc** allocator (default)
- **Active defragmentation**: Reclaims fragmented memory
- **Memory policy**: Eviction when `maxmemory` reached
- **Memory reporting**: `INFO memory` for usage stats

## I/O Threads (Redis 6.0+)

- Network I/O can use multiple threads
- Command execution remains single-threaded
- Configurable via `io-threads` and `io-threads-do-reads`
- Significant improvement for high-throughput scenarios

## Modules

- Loadable modules extend Redis functionality
- Written in C, loaded at runtime via `MODULE LOAD`
- Examples: RediSearch, RedisJSON, RedisGraph
- Module API provides access to all Redis internals

## Lua Scripting

- Scripts execute atomically (no interleaving)
- Evaluates on the main thread
- Access to all Redis commands
- Useful for complex multi-step operations
- Script cache managed with `SCRIPT LOAD` and `EVALSHA`

## Key Design Patterns

- **Namespaces**: Use `:` separator (e.g., `user:1234:profile`)
- **Binary safety**: Keys and values are binary-safe strings
- **Key expiration**: TTL on keys, lazy + active deletion
- **SCAN**: Cursor-based iteration over keyspace (avoids blocking)
