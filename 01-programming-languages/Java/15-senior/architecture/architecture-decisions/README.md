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


---

**Continue to Part 2**: README-part2.md