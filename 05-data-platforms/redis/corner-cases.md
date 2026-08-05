# Redis Corner Cases

## Memory Limit and Eviction

Redis runs out of memory when `maxmemory` is reached and no eviction policy allows removal. The server rejects write commands with `OOM command not allowed`. Read commands continue to work.

Eviction policies: `noeviction` (reject writes), `allkeys-lru`, `allkeys-lfu`, `volatile-lru`, `volatile-lfu`, `allkeys-random`, `volatile-random`, `volatile-ttl`. Choose based on access patterns.

## Split Brain in Sentinel/Cluster

Network partitions can cause multiple masters for the same slot. Sentinel uses quorum to elect a new master, but if the old master is still accepting writes, data diverges.

Use `min-slaves-to-write` and `min-slaves-max-lag` to prevent the old master from accepting writes when disconnected from replicas.

## Key Expiration Accuracy

Redis does not use a timer for each key. Expiration is checked lazily (on access) and probabilistically (during periodic scans). Keys may live slightly past their TTL.

The 10 active expiry tests per cycle (100ms) examine a random set of keys. If no keys expire, the next cycle is skipped. Under memory pressure, keys may expire faster due to eviction.

## Pipeline and Transaction Semantics

Pipelining sends multiple commands without waiting for responses. It reduces round-trip latency but does not provide atomicity. Commands in a pipeline are not guaranteed to execute atomically.

Transactions (`MULTI`/`EXEC`) provide atomicity for a batch of commands. If a command fails (e.g., wrong type), other commands in the transaction still execute. There is no rollback.

## Lua Script Atomicity

Lua scripts execute atomically. No other client command runs during script execution. Long-running scripts block all other operations. Use `lua-time-limit` to kill scripts that exceed the timeout.

Scripts must be deterministic. Do not use random commands, time-based commands, or commands with side effects inside scripts.

## Cluster Slot Migration

Migrating slots between nodes is a two-phase process. The source node marks slots as migrating, and the target node marks them as importing. During migration, requests may be redirected with `-ASK`.

If migration is interrupted, slots may be in an inconsistent state. Use `CLUSTER SETSLOT` to manually fix slot ownership.

## Redis Sentinel Failover

Sentinel detects failure based on `down-after-milliseconds`. If the master does not respond within this time, it is marked as主观下线 (SDOWN). If enough Sentinels agree, it becomes客观下线 (ODOWN), triggering failover.

Failover takes time: detection + election + propagation. During this window, the system may be partially unavailable.

## RDB and AOF Persistence

RDB snapshots are point-in-time backups. Data between snapshots is lost if Redis crashes. AOF records every write operation but grows larger over time. `BGREWRITEAOF` compacts the AOF file.

Using both RDB and AOF is common. Redis loads from AOF first if both exist.

## Slowlog and Performance

`SLOWLOG` records commands that exceed `slowlog-log-slower-than`. It helps identify expensive operations. `SLOWLOG GET` returns the slowest commands.

Large keys, O(n) commands like `KEYS`, and Lua scripts can cause slowdowns. Monitor with `INFO COMMANDSTATS`.

## Redis Streams Consumer Groups

Streams consumer groups provide at-least-once delivery. If a consumer crashes before acknowledging a message, it is redelivered. Use `XACK` to acknowledge messages.

`XPENDING` shows pending messages. `XCLAIM` transfers ownership of pending messages from one consumer to another.

## Cluster Resharding and Replicas

Adding a new node to a cluster requires slot migration. This is online but can increase latency during migration. Use `redis-cli --cluster reshard` for guided resharding.

Replicas in a cluster follow the same slot ranges as their master. Replicas are read-only by default (`replica-read-only yes`).
