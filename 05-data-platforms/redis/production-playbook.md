# Redis Production Playbook

## Twitter

Twitter uses Redis extensively for real-time features: timelines, trending topics, and user sessions. Their deployment runs thousands of Redis instances across multiple datacenters. Twitter uses Redis Cluster for horizontal scaling, distributing data across hundreds of shards.

Twitter's caching strategy employs tiered caching: L1 (process-local cache), L2 (Redis), L3 (memcached). Cache invalidation uses a publish-subscribe mechanism where writes to the primary trigger invalidation messages to cache replicas. Their session management stores OAuth tokens and user preferences with TTL-based expiration. Twitter built a custom Redis proxy (Twemproxy predecessor) that handles consistent hashing and connection pooling across their fleet.

Twitter's Redis usage includes: user timeline caching (recent tweets), trending topic calculation (sorted sets with time-decaying scores), and notification delivery (pub/sub for real-time updates). Their memory management strategy uses maxmemory with allkeys-lru eviction policy. Twitter monitors Redis latency at sub-millisecond granularity using custom instrumentation.

Twitter's Redis deployment includes: session storage for authentication, rate limiting for API endpoints, and real-time feature caching. Their Sentinel configuration uses 3 sentinels with quorum-based failover. Twitter uses Redis Cluster for high-throughput workloads and Sentinel for smaller deployments. Their capacity planning includes seasonal traffic patterns and growth projections.

Twitter monitors Redis cluster health with custom dashboards. Their alerting system considers business impact: real-time features have higher priority than caching. Twitter uses Redis persistence with RDB and AOF for durability. Their disaster recovery includes cross-region replication and backup restoration procedures. Twitter practices chaos engineering to validate resilience.

## GitHub

GitHub uses Redis for background job queues (Sidekiq), caching (fragment and action caching), and rate limiting. Their deployment processes millions of background jobs daily for webhook delivery, email notifications, and repository maintenance. GitHub uses Redis Sentinel for high availability with automatic failover.

GitHub's rate limiting uses Redis sorted sets with sliding window counters. Each API request increments a counter with a timestamp score; expired entries are removed via ZREMRANGEBYSCORE. Their caching layer stores computed data (repository file trees, user profiles) with semantic versioning-based invalidation keys. GitHub monitors Redis memory usage aggressively, using maxmemory policies to evict least-recently-used data rather than risking OOM conditions.

GitHub's Redis deployment includes: Sidekiq queues for background jobs, fragment caching for Rails views, rate limiting for API endpoints, and session storage for user authentication. Their Sentinel configuration uses 3 sentinels with quorum-based failover. GitHub uses Redis Cluster for high-throughput workloads and Sentinel for smaller deployments.

GitHub monitors Redis cluster health with custom dashboards. Their alerting system considers business impact: background job queues have higher priority than caching. GitHub uses Redis persistence with AOF for durability. Their capacity planning includes growth projections and seasonal traffic patterns.

GitHub's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. GitHub uses Redis persistence with AOF for durability. Their operational runbooks document recovery procedures for common failure scenarios. GitHub practices chaos engineering to validate resilience.

## Stack Overflow

Stack Overflow uses Redis for caching frequently accessed data: question pages, user profiles, and badge calculations. Their Redis deployment is relatively small but critical. Stack Overflow uses Redis as a write-through cache backed by SQL Server, ensuring cache coherence.

Their caching strategy caches entire rendered HTML fragments keyed by URL and user-specific variations. Badge calculations (reputation, badges) are cached in Redis and invalidated when users earn new achievements. Stack Overflow uses Redis Lists for job queues that handle search index updates and email delivery. Their monitoring tracks hit rates and eviction rates to tune cache sizing.

Stack Overflow's Redis usage prioritizes simplicity and reliability over scale. They use Redis as a cache-aside layer with explicit invalidation. Their deployment uses a single primary with replicas for read scaling. Stack Overflow monitors cache hit rates to optimize key expiration policies. Their backup strategy includes RDB snapshots and AOF persistence.

Stack Overflow's Redis deployment includes: page caching, user session storage, and badge calculation caching. Their memory management strategy uses maxmemory with allkeys-lru eviction policy. Stack Overflow monitors Redis latency at sub-millisecond granularity. Their capacity planning includes traffic patterns and growth projections.

Stack Overflow's disaster recovery strategy includes regular backups and restoration testing. They use Redis persistence with RDB snapshots and AOF for durability. Stack Overflow monitors cache hit rates to optimize key expiration policies. Their operational runbooks document recovery procedures for common failure scenarios.

## Stripe

Stripe uses Redis for distributed locking, session management, and real-time rate limiting across their payment platform. Their deployment requires high availability for payment processing workflows. Stripe uses Redis with AOF persistence for durability guarantees on critical data.

Stripe's distributed locking uses the Redlock algorithm for coordinating distributed transactions. Rate limiting uses token bucket algorithms implemented with Redis Lua scripts for atomicity. Their session store handles authentication tokens with automatic expiration. Stripe monitors Redis latency at sub-millisecond granularity, as payment processing cannot tolerate cache misses.

Stripe's Redis deployment includes: distributed locks for payment processing coordination, session storage for merchant authentication, rate limiting for API endpoints, and real-time feature flags. Their AOF configuration uses `appendfsync always` for maximum durability. Stripe uses Redis Cluster for horizontal scaling and Sentinel for failover.

Stripe monitors Redis cluster health with custom dashboards. Their alerting system considers business impact: payment processing events have higher priority than analytics events. Stripe uses Redis persistence with AOF for durability. Their capacity planning includes transaction volumes and growth projections.

Stripe's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. Stripe uses encryption at rest and in transit for PCI compliance. Their operational runbooks document recovery procedures for common failure scenarios. Stripe practices chaos engineering to validate resilience.

## Discord

Discord uses Redis for presence tracking (online/offline status), rate limiting, and pub/sub messaging. Their deployment handles millions of concurrent users. Discord uses Redis Cluster with 50+ shards for horizontal scaling of their presence system.

Discord's presence tracking uses Redis sets to track online users per guild. Pub/sub broadcasts presence updates to relevant shards. Rate limiting uses sliding window counters stored in Redis. Discord built a custom Redis client that handles cluster topology changes transparently. Their monitoring tracks per-shard memory usage and connection counts to prevent hotspots.

Discord's Redis usage includes: presence tracking for online status, rate limiting for API endpoints, pub/sub for real-time notifications, and session storage for user authentication. Their cluster configuration uses hash tags to colocate related keys. Discord monitors Redis memory fragmentation and connection counts per shard.

Discord monitors Redis cluster health with custom dashboards. Their alerting system considers business impact: presence tracking has higher priority than rate limiting. Discord uses Redis Cluster for horizontal scaling. Their capacity planning includes user growth projections and seasonal traffic patterns.

Discord's disaster recovery strategy includes cross-region replication with automated failover. They regularly test failover procedures to ensure data durability. Discord uses Redis persistence with RDB snapshots for durability. Their operational runbooks document recovery procedures for common failure scenarios. Discord practices chaos engineering to validate resilience.

## Common Production Patterns

Redis production deployments consistently emphasize the following. Sentinel provides automatic failover for primary-replica topologies. Cluster mode distributes data across nodes for horizontal scaling. Memory management requires careful monitoring of used_memory_rss vs maxmemory. Persistence tuning balances durability (AOF fsync) with performance.

Connection pooling is critical: each Redis connection consumes memory and file descriptors. Client libraries should maintain connection pools sized appropriately for the workload. Pipeline batching reduces round-trip overhead for bulk operations. Lua scripts provide atomic multi-command operations without explicit locking.

Operational monitoring tracks: operations per second, hit rate, memory fragmentation ratio, connected clients, replication lag, and evicted keys. Alerting thresholds are based on percentage of maxmemory rather than absolute values. Capacity planning accounts for peak loads and replication overhead (typically 2x for replicas).

Common failure scenarios include: OOM kills (increase maxmemory or optimize memory usage), replication lag (check network, increase replica buffers), split-brain (ensure quorum in Sentinel), and hot keys (distribute across shards, use local caching). Runbooks should document recovery procedures for each scenario.

Production Redis deployments use dedicated hardware with sufficient RAM. Memory optimization includes: using appropriate data structure encodings, compressing values, setting TTLs on ephemeral data, and monitoring memory fragmentation. Backup strategies include RDB snapshots and AOF persistence. Disaster recovery includes cross-region replication and backup restoration procedures.

Redis disaster recovery strategies include: cross-region replication with automated failover, regular backup testing, and capacity planning for peak loads. Production runbooks document recovery procedures for OOM kills, replication lag, split-brain, and hot keys. Chaos engineering practices regularly test failure scenarios to validate resilience and data durability.

Redis production clusters require careful memory management. Memory sizing accounts for data structures, replication overhead, and fragmentation. The `maxmemory` setting enforces memory limits with configurable eviction policies. Monitoring tracks memory usage, fragmentation ratio, and eviction rates. Capacity planning accounts for peak loads and replication overhead (typically 2x for replicas).

Connection pooling is critical for Redis performance. Each Redis connection consumes memory and file descriptors. Client libraries should maintain connection pools sized appropriately for the workload. Pipeline batching reduces round-trip overhead for bulk operations. Lua scripts provide atomic multi-command operations without explicit locking. Regular operational reviews ensure connection pool sizing remains appropriate.
