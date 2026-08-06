# Java Architecture Decision Records

## What is an ADR?

An Architecture Decision Record (ADR) is a lightweight document that captures a significant architectural decision along with its context and consequences. Unlike meeting notes or Slack threads, ADRs are **versioned, searchable, and permanent**.

ADRs exist because:
- Architectural decisions are expensive to reverse
- Context is forgotten within weeks
- "Why did we do this?" is the most common question new team members ask
- Oral history doesn't survive team turnover

## When to Write an ADR

Write an ADR when:
- Choosing between two or more viable approaches with no obvious winner
- The decision affects more than one team or service
- The decision is hard to reverse (e.g., database choice, protocol selection)
- You're adopting a new framework, tool, or pattern
- A stakeholder asked "why did we choose X?" more than once
- You're making a decision that will outlast the people in the room

Do NOT write an ADR for:
- Code formatting (use linters)
- Naming conventions (use style guides)
- Trivial implementation details
- Decisions that will be revisited in weeks anyway

## ADR Template for Java Decisions

```markdown
# ADR-{NUMBER}: {TITLE}

## Status
{Proposed | Accepted | Deprecated | Superseded by ADR-XXX}

## Date
{YYYY-MM-DD}

## Context
What is the issue that we're seeing that is motivating this decision or change?

## Decision
What is the change that we're proposing and/or doing?

## Consequences

### Positive
- What becomes easier or more supported to do?
- What new capabilities does this enable?

### Negative
- What becomes harder to do?
- What was previously possible that is now constrained?
- What are the risks of this decision?

## Alternatives Considered

### {Alternative A}
- Description
- Why rejected

### {Alternative B}
- Description
- Why rejected

## References
- Links to documentation, benchmarks, or prior discussions
```

---

## 10 Real ADR Examples

### ADR-1: Why Java 21 over Java 17

**Status:** Accepted

**Date:** 2024-01-15

**Context**
Our services run on Java 17 LTS. Java 21 LTS was released in September 2023 with significant performance and language improvements. We need to decide whether to upgrade for our next release cycle.

**Decision**
Adopt Java 21 as the standard JVM version for all services.

**Consequences**

Positive:
- Virtual threads (Project Loom) eliminate the need for reactive programming complexity
- Sequenced collections provide a more intuitive Collection API
- Pattern matching for switch reduces boilerplate in complex conditional logic
- ZGC now has sub-millisecond pause times regardless of heap size
- String templates reduce string concatenation errors
- Record patterns enable destructuring in type-safe ways

Negative:
- Some internal libraries compiled against Java 17 may need recompilation
- CI/CD pipelines need JDK 21 images
- Team needs training on virtual threads to avoid misuse
- A few third-party libraries may not yet have Java 21 compatible versions

Alternatives Considered

Java 17 (stay):
- Stable, battle-tested, all libraries compatible
- Rejected because we miss virtual threads which directly impact our reactive service performance

Java 22 (bleeding edge):
- Non-LTS, would require another migration in 6 months
- Rejected because the operational cost of non-LTS is not justified

GraalVM Native Image:
- Interesting for startup time, but our services are long-running
- Rejected as a primary runtime; considered as a secondary option for CLI tools

---

### ADR-2: Why Spring Boot over Quarkus

**Status:** Accepted

**Date:** 2024-02-01

**Context**
We're building a new set of microservices. The team has varying experience levels with Java frameworks. We need a framework that supports rapid development, has strong ecosystem support, and can run in both traditional and cloud-native deployments.

**Decision**
Use Spring Boot 3.x as the primary application framework for all new services.

**Consequences**

Positive:
- Most Java developers already know Spring; reduces onboarding time
- Massive ecosystem of starters, integrations, and community support
- Spring Boot Actuator provides production-ready observability out of the box
- Spring Cloud provides service discovery, config, circuit breakers
- Excellent documentation and books available
- Spring Security is the most mature Java security framework
- Virtual threads support added in Spring Boot 3.2

Negative:
- Spring Boot has a larger runtime footprint than Quarkus
- Cold start time is slower (though not an issue for our long-running services)
- Auto-configuration can be a "magic" black box for debugging
- Spring's approach to dependency injection is less compile-time safe than CDI

Alternatives Considered

Quarkus:
- Faster startup, lower memory, excellent for GraalVM native images
- Better developer experience with live reload
- Rejected because: smaller ecosystem, team lacks experience, most of our services are long-running where startup time doesn't matter

Micronaut:
- Compile-time DI is appealing, good GraalVM support
- Rejected because: even smaller ecosystem than Quarkus, limited enterprise adoption, hiring pool is smaller

Jakarta EE (Payara/WildFly):
- Standards-based, good for vendor portability
- Rejected because: verbose configuration, declining community, slower innovation cycle

---

### ADR-3: Why PostgreSQL over MongoDB

**Status:** Accepted

**Date:** 2024-02-15

**Context**
Our new user activity service needs to store structured event data that is queried in complex ways (aggregations, joins with user data, time-range queries). The data has a semi-structured component but is predominantly relational.

**Decision**
Use PostgreSQL 16 as the primary database for the user activity service.

**Consequences**

Positive:
- ACID transactions ensure data consistency for financial-adjacent activity data
- JSONB columns allow us to store semi-structured event payloads without losing queryability
- Window functions and CTEs support the complex analytical queries we need
- Materialized views pre-compute expensive aggregations
- PostgreSQL's EXPLAIN ANALYZE makes query optimization transparent
- Rich indexing: B-tree, GIN, GiST, BRIN for different access patterns
- pg_partman enables automatic partitioning for time-series data

Negative:
- Schema migrations require careful planning (column renames are not atomic in all cases)
- Vertical scaling limits reached before horizontal (though Citus extension helps)
- Connection pooling (PgBouncer/HikariCP) adds operational complexity
- JSONB queries are slower than native document queries in MongoDB for deeply nested access

Alternatives Considered

MongoDB:
- Natural fit for semi-structured event data
- Horizontal scaling via sharding is simpler
- Rejected because: our queries involve joins with relational user data, ACID requirements, team expertise in SQL

Apache Cassandra:
- Excellent write throughput for event data
- Rejected because: limited query flexibility, no joins, eventual consistency model doesn't fit our use case

Google BigQuery / Snowflake:
- Perfect for analytical workloads
- Rejected because: this is an operational service, not purely analytical; latency requirements are sub-100ms

---

### ADR-4: Why Kafka over RabbitMQ

**Status:** Accepted

**Date:** 2024-03-01

**Context**
We need a message broker for event-driven communication between 15 microservices. Current volume is 50K messages/second with projected growth to 500K within 18 months. We need replay capability, event sourcing support, and strict ordering within partitions.

**Decision**
Use Apache Kafka (Confluent Platform) as the central event backbone.

**Consequences**

Positive:
- Log-based retention allows event replay (critical for event sourcing and debugging)
- Partitioning provides ordering guarantees within a key
- Horizontal scaling is proven at LinkedIn-scale (trillions of messages/day)
- Kafka Streams and ksqlDB enable real-time stream processing
- Consumer groups allow parallel processing without message loss
- Exactly-once semantics (with transactional producers)

Negative:
- Operational complexity: ZooKeeper/KRaft, topic management, partition rebalancing
- Higher latency than RabbitMQ for simple task queue patterns
- Ordered delivery requires careful partition key design
- Schema evolution requires careful Avro/Protobuf schema registry management
- Smaller message overhead per message (batching required for efficiency)

Alternatives Considered

RabbitMQ:
- Simpler to operate, better for task queue patterns
- Rejected because: no replay capability, doesn't scale to 500K msg/sec comfortably, no log-based retention

Amazon SQS/SNS:
- Fully managed, zero operations
- Rejected because: vendor lock-in, limited ordering guarantees, no replay, cost at scale

Apache Pulsar:
- Similar capabilities to Kafka with better multi-tenancy
- Rejected because: smaller community, fewer production references at our scale, team familiarity with Kafka

NATS JetStream:
- Lightweight, fast, simpler than Kafka
- Rejected because: ecosystem maturity, fewer integrations, smaller community for support

---

### ADR-5: Why Microservices over Monolith

**Status:** Accepted

**Date:** 2024-03-15

**Context**
Our monolith has grown to 2M LOC with 45 developers across 6 teams. Deployments take 2 hours, a single bug can take down the entire system, and teams block each other on releases. We need to enable independent team velocity.

**Decision**
Decompose the monolith into domain-aligned microservices over the next 12 months, starting with the highest-change-rate bounded contexts.

**Consequences**

Positive:
- Teams can deploy independently without coordinating releases
- A bug in one service doesn't cascade to others (when properly isolated)
- Technology heterogeneity: teams can choose the best tool for their domain
- Scaling is granular: scale the hot services independently
- Easier to reason about and onboard new developers to a single service

Negative:
- Distributed system complexity: network failures, eventual consistency, distributed tracing
- Data consistency across services requires saga patterns (not simple ACID)
- Operational overhead multiplies: more deployments, more monitoring, more runbooks
- Debugging requires correlation IDs and distributed tracing infrastructure
- Inter-service communication adds latency
- Need for API versioning and backward compatibility

Alternatives Considered

Modular Monolith:
- Better organization without distribution overhead
- Considered but: our deployment coupling problem requires true independence, not just logical separation

Stay Monolith + Improve CI/CD:
- Faster pipelines, feature flags, blue-green deployments
- Rejected because: deployment speed was only one symptom; team coupling and blast radius were the core issues

Serverless:
- No infrastructure management
- Rejected because: cold start latency, vendor lock-in, complexity of state management

---

### ADR-6: Why Event-Driven over Request-Response

**Status:** Accepted

**Date:** 2024-04-01

**Context**
Services currently communicate via synchronous REST calls. When the Order Service calls the Inventory Service, which calls the Payment Service, a single request creates a chain of synchronous calls. Failures cascade, and services are tightly coupled.

**Decision**
Adopt event-driven architecture for cross-service communication where immediate response is not required.

**Consequences**

Positive:
- Temporal decoupling: producers and consumers don't need to be available simultaneously
- Natural audit trail: events are the source of truth
- New consumers can be added without modifying producers
- System resilience: a downstream failure doesn't block the upstream

Negative:
- Debugging is harder: you can't follow a simple call stack
- Eventual consistency requires UX design that tolerates it
- Idempotency must be enforced at every consumer
- Schema evolution requires a schema registry and versioning strategy
- Monitoring requires event flow visualization, not just request/response traces

Alternatives Considered

Synchronous REST everywhere:
- Simpler mental model, easier to debug
- Rejected because: cascade failures, tight coupling, scaling bottleneck

GraphQL Federation:
- Good for client-facing APIs
- Rejected because: doesn't solve service-to-service coupling, still synchronous

gRPC (bidirectional streaming):
- More efficient than REST, supports streaming
- Rejected because: still synchronous from a business logic perspective, doesn't provide temporal decoupling

---

### ADR-7: Why Redis for Caching

**Status:** Accepted

**Date:** 2024-04-15

**Context**
Our product catalog API serves 10K requests/second with 95% hitting the same 5K product pages. Database queries for these pages average 45ms. We need sub-10ms response times.

**Decision**
Use Redis 7 as a distributed cache layer for read-heavy data, with cache-aside pattern.

**Consequences**

Positive:
- Sub-millisecond read latency for cached data
- Pub/Sub for cache invalidation across instances
- Data structures (sorted sets, hashes) enable complex caching patterns
- Redis Cluster provides horizontal scaling and high availability
- TTL-based expiration reduces manual cache management
- Lua scripts enable atomic cache operations

Negative:
- Cache invalidation remains one of the two hard problems in CS
- Memory cost: Redis holds data in RAM, which is 10x more expensive than disk
- Cache stampede risk: when cache expires, all requests hit the database simultaneously
- Data inconsistency window: cached data may be stale
- Operational overhead: Redis Cluster, persistence configuration, memory management

Alternatives Considered

Guava/Caffeine (local cache):
- No network overhead, simplest to implement
- Rejected because: doesn't work across multiple application instances, memory pressure on JVM heap

Memcached:
- Simpler than Redis, good for pure key-value caching
- Rejected because: no data structures, no persistence, no pub/sub for invalidation

Hazelcast:
- In-memory data grid with caching capabilities
- Rejected because: heavier than needed for our use case, JVM dependency for all nodes

---

### ADR-8: Why gRPC for Internal Services

**Status:** Accepted

**Date:** 2024-05-01

**Context**
Internal service-to-service communication currently uses REST/JSON. With 15 services making hundreds of inter-service calls per second, JSON serialization overhead and HTTP/1.1 connection limitations are measurable.

**Decision**
Use gRPC (Protocol Buffers) for all synchronous inter-service communication. REST remains for external-facing APIs.

**Consequences**

Positive:
- Binary serialization: 5-10x smaller payloads than JSON
- HTTP/2 multiplexing: one connection handles multiple concurrent requests
- Streaming support for real-time data flows
- Strongly-typed contracts via .proto files (auto-generated code)
- 20-40% lower latency for high-throughput internal calls
- Backward/forward compatibility via proto field numbering

Negative:
- Not human-readable in wire format (harder to debug without tools)
- Browser support requires gRPC-Web proxy
- Team needs to learn Protocol Buffers and gRPC tooling
- Load balancing is more nuanced (need client-side or L7 load balancing)
- Not suitable for public APIs where curl/browser testing is expected

Alternatives Considered

REST/JSON:
- Universally understood, human-readable
- Rejected because: performance overhead at our scale, no streaming, no contract enforcement

Apache Thrift:
- Similar binary protocol benefits
- Rejected because: smaller ecosystem, less language support, declining community

Cap'n Proto:
- Zero-copy deserialization, extremely fast
- Rejected because: very small community, limited Java support, maturity concerns

GraphQL:
- Flexible query language
- Rejected because: still HTTP/JSON based, doesn't solve the serialization overhead

---

### ADR-9: Why Caffeine over Guava Cache

**Status:** Accepted

**Date:** 2024-05-15

**Context**
Several services use Guava's CacheBuilder for in-process caching. Performance profiling shows that cache-related GC pressure is measurable under high load. We need to evaluate if a more modern cache implementation would help.

**Decision**
Migrate all in-process caching from Guava Cache to Caffeine.

**Consequences**

Positive:
- W-TinyLFU eviction algorithm provides near-optimal hit rates (vs. LRU in Guava)
- Up to 40% higher hit rates in benchmarks with realistic access patterns
- Asynchronous loading cache reduces thread blocking
- Built-in statistics (hit rate, eviction rate, load times) without extra code
- Java 9+ module system support
- Active development and maintenance (Guava Cache is maintenance-mode)

Negative:
- Migration effort: API is similar but not identical (LoadingCache vs AsyncLoadingCache)
- One more dependency to manage
- Caffeine's advanced features (expiration, refresh, listener) have a learning curve
- Some edge cases in eviction behavior differ from Guava

Alternatives Considered

Guava Cache (no change):
- Already in use, team knows it
- Rejected because: suboptimal eviction algorithm, maintenance-mode, worse performance at high load

Ehcache 3:
- Distributed caching support, JSR-107 compliant
- Rejected because: heavier than needed for in-process caching, distributed caching is handled by Redis

Redis (remove local caching entirely):
- Single source of truth
- Rejected because: network round-trip latency (1-5ms vs. <1ns local), Redis can't replace local caching for hot paths

---

### ADR-10: Why Virtual Threads over Thread Pools

**Status:** Accepted

**Date:** 2024-06-01

**Context**
Our API gateway handles 20K concurrent connections. Each connection needs to make 3-5 downstream service calls (30-100ms each). With platform threads, we need 20K threads minimum, consuming ~200MB of stack memory. Thread pool tuning is a constant source of production issues.

**Decision**
Migrate from platform thread pools (ExecutorService) to virtual threads (Executors.newVirtualThreadPerTaskExecutor) for all I/O-bound workloads.

**Consequences**

Positive:
- Can create millions of virtual threads without memory concern (each starts at ~1KB vs. 1MB)
- No thread pool sizing tuning: virtual threads scale automatically
- Thread-per-request programming model returns (simpler than reactive/async)
- Blocked virtual threads release the platform thread underneath (no thread starvation)
- Reduced context switching overhead (virtual threads are scheduled by the JVM, not OS)
- Eliminates the need for reactive programming (CompletableFuture chains, Mono/Flux)

Negative:
- Synchronized blocks pin virtual threads to platform threads (must migrate to ReentrantLock)
- Thread-local state (e.g., security context, MDC) must migrate to ScopedValue
- Monitoring tools need updates: jstack shows virtual threads differently
- Some libraries assume platform thread behavior and may not work correctly
- Cannot use synchronized in code that runs on virtual threads (subtle bug source)

Alternatives Considered

Platform threads + larger pool:
- Simpler, well-understood
- Rejected because: memory cost is prohibitive at 20K+ concurrent connections, thread pool tuning is error-prone

Reactive (Project Reactor/Mono/Flux):
- Handles high concurrency with fixed thread pools
- Rejected because: code complexity is extreme, debugging is painful, virtual threads make this unnecessary

WebFlux:
- Non-blocking I/O built into Spring
- Rejected because: same complexity concerns as reactive, virtual threads provide the benefits with simpler code

---

## How to Use This ADR Repository

1. Copy the template above
2. Number sequentially (ADR-11, ADR-12, etc.)
3. Start with status "Proposed"
4. Discuss with the team
5. Update to "Accepted" or "Rejected" with rationale
6. Store in version control alongside the code it affects
7. Reference ADRs in PRs and design docs

## Anti-Patterns

- **Post-decision ADRs**: Writing the ADR after the decision is already made defeats the purpose
- **ADR by committee**: One person writes, others review; don't design by consensus
- **ADR graveyard**: If you write 50 ADRs and none are ever referenced, you're doing it wrong
- **Too granular**: "Why we use tabs over spaces" is not an ADR
- **Too vague**: "We chose X because it's better" is not a useful ADR
