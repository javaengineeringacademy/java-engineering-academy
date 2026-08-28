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

## Interview Questions

1. **What is an Architecture Decision Record (ADR)?**
   An ADR is a lightweight document that captures a significant architectural decision along with its context, rationale, and consequences. Unlike meeting notes, ADRs are versioned, searchable, and permanent. They follow a standard template with status, context, decision, consequences, and alternatives sections.

2. **When should you write an ADR versus having a Slack discussion?**
   Write an ADR when the decision affects multiple teams, is hard to reverse, or will outlast the people making it. Slack discussions are fine for tactical, reversible decisions. If someone asks "why did we do this?" more than once, that's a signal an ADR is needed.

3. **What is the difference between an ADR and a design document?**
   ADRs capture a single decision and its rationale in 1-2 pages. Design documents are longer, describe how to implement something, and may contain multiple decisions. ADRs are decision records; design documents are implementation plans.

4. **What are common mistakes when writing ADRs?**
   Writing ADRs too late (after the decision is made and forgotten), making them too long (should be 1-2 pages), not including alternatives considered, and failing to update the status when decisions are superseded. ADRs should be written within 1-2 weeks of the decision.

5. **How do ADRs differ across organizations?**
   Some teams use a simple prose format, others use the MADR (Markdown Any Decision Record) template. The key is consistency within an organization. Formats include: Nygard's original format, the AWS ADR template, and the Joel on Software format. Choose one and standardize.

6. **How should ADRs be stored and versioned?**
   Store ADRs in a version control system (Git) alongside the code they describe. Use a numbered naming convention (ADR-001, ADR-002). Tools like adr-tools automate creation and management. The `docs/adr/` directory in a repository is a common location.

7. **How do you handle superseded ADRs?**
   Mark the old ADR as "Superseded by ADR-XXX" and never delete it. The superseded ADR provides valuable historical context about why the previous approach was chosen and why it was replaced. This creates an audit trail of architectural evolution.

8. **Who should be involved in ADR decisions?**
   The decision maker (tech lead or architect), stakeholders affected by the decision, and domain experts. For high-impact decisions, involve the CTO or VP Engineering. The ADR author doesn't have to be the decision maker but should facilitate the process.

## Pitfalls

**ADR too late or never written:**
```java
// BAD: Decision made in a meeting, nobody documented it
// 6 months later, new team member asks "why Kafka?"
// Nobody remembers the alternatives considered

// GOOD: ADR written within 1 week of decision
// ADR-007: Why Kafka over RabbitMQ
// Status: Accepted
// Context: [clear problem statement]
// Alternatives: [RabbitMQ, SQS, Pulsar — with rejection reasons]
```

**ADR as a design document:**
```markdown
<!-- BAD: ADR that's 15 pages long with implementation details -->
## Decision
We will use Spring Boot with Redis caching, implement cache-aside pattern
with Caffeine L1 cache, configure HikariCP with 50 connections...

<!-- GOOD: ADR that focuses on the WHY, not the HOW -->
## Decision
Use Redis 7 as the distributed cache layer for read-heavy data.
## Alternatives Considered
- Caffeine (local cache): Rejected because doesn't work across instances
- Memcached: Rejected because no data structures or persistence
```

**Not updating ADR status:**
```markdown
<!-- BAD: ADR still says "Accepted" after being replaced -->
# ADR-003: Use Java 17
## Status: Accepted
(Actually superseded by ADR-008: Use Java 21)

<!-- GOOD: Status reflects current reality -->
# ADR-003: Use Java 17
## Status: Superseded by ADR-008
```

**Ignoring "do nothing" as an alternative:**
Always include the status quo as an explicit alternative with reasons for rejection. This forces you to justify the cost of change against doing nothing.

## Performance

ADR creation has negligible runtime performance impact — it's a documentation process. However, good ADRs improve organizational performance:

- **Decision speed**: Teams with ADR templates make decisions 30-40% faster because the format forces structured thinking
- **Onboarding time**: New developers understand architectural context 2-3x faster with ADRs vs. tribal knowledge
- **Incident post-mortems**: Root cause analysis for architecture-related incidents is 50% faster with ADR history
- **Reversal cost**: Decisions documented with alternatives are easier to reverse when needed, reducing wasted effort

The time investment per ADR is typically 2-4 hours for writing and review. Over a year, a team making 20 significant decisions spends 40-80 hours on ADRs, which pays for itself through reduced rework and faster onboarding.

## Examples

**ADR-101: Adopt Virtual Threads for I/O-Bound Services**
```markdown
# ADR-101: Adopt Virtual Threads for I/O-Bound Services

## Status
Accepted

## Date
2024-06-15

## Context
Our order processing service uses traditional platform threads with a pool
of 200 threads. During peak traffic, all 200 threads are occupied by
database and HTTP calls, causing request queuing. We've been considering
reactive programming (WebFlux) but the team lacks experience.

## Decision
Adopt virtual threads (Project Loom) for all I/O-bound services, starting
with the order processing service.

## Consequences

### Positive
- Eliminates thread pool tuning for I/O-bound workloads
- Existing blocking code works without modification
- Team can use familiar imperative programming model
- 10x more concurrent connections with same memory

### Negative
- Requires Java 21 LTS
- Must not use synchronized blocks (pinning risk)
- Need to migrate to ReentrantLock for fine-grained locking

## Alternatives Considered

### Reactive Programming (WebFlux)
- Non-blocking, high throughput
- Rejected because: steep learning curve, harder to debug, reactive streams are complex

### Thread Pool Tuning
- Increase pool to 2000 threads
- Rejected because: context switching overhead, memory usage, doesn't scale

## Interview Questions
(see section above)
```

**ADR-102: API Versioning Strategy**
```java
// ADR-102 Decision: URI-based versioning for REST APIs

// Implementation example
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    @GetMapping("/{id}")
    public UserV1 getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 {
    @GetMapping("/{id}")
    public UserV2 getUser(@PathVariable Long id) {
        return userService.getUserV2(id);
    }
}

// Why not header-based versioning:
// - Harder to test with curl/browser
// - Documentation tools don't handle it well
// - OpenAPI/Swagger struggles with header-based routing
```

## Internal Working

ADR workflow in practice:

1. **Trigger**: A significant decision needs to be made (new technology, architectural change, pattern adoption)
2. **Draft**: Author creates an ADR using the standard template, documenting context and alternatives
3. **Review**: Stakeholders review and provide feedback (1-2 week window for high-impact decisions)
4. **Accept**: Decision maker formally accepts the ADR
5. **Implement**: Team implements the decision
6. **Supersede**: If the decision is later replaced, update status to "Superseded by ADR-XXX"
7. **Archive**: Superseded ADRs remain for historical context

Tools that support ADR workflows:
- `adr-tools` (CLI): Automates creation, linking, and management
- GitHub/GitLab: Store ADRs in `docs/adr/` directory, review via pull requests
- Confluence/Notion: For non-code-centric teams, but loses versioning benefits

## Why This Concept Exists

ADRs solve three critical organizational problems:

1. **Context loss**: Without ADRs, the "why" behind decisions is lost within weeks as team members forget discussions, move to other projects, or leave the company. New team members ask "why did we do this?" and nobody can answer definitively.

2. **Repeated debates**: Without documented decisions, teams revisit the same decisions when new members join or when the original decision makers are unavailable. ADRs prevent the "let's re-evaluate our database choice" discussion that happens every 6 months.

3. **Accountability gaps**: Without documented decisions, there's no clear ownership of architectural choices. When something breaks, it's unclear who decided on the approach and what alternatives were considered. ADRs create a clear audit trail.

## Overview

An Architecture Decision Record (ADR) is a lightweight, versioned document that captures a significant architectural decision along with its context, alternatives considered, and consequences. ADRs are a key practice in modern software architecture, providing a permanent, searchable record of why decisions were made. They are typically 1-2 pages long and follow a standardized template. ADRs originated in the Agile and Lean communities and have been adopted by organizations like Amazon, Microsoft, and Spotify.

## References
- Michael Nygard, "Documenting Architecture Decisions" (original ADR article): https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions
- adr-tools GitHub repository: https://github.com/npryce/adr-tools
- MADR (Markdown Any Decision Records): https://adr.github.io/madr/
- Joel on Software — "Choosing a Technology": https://www.joelonsoftware.com/
- Sam Newman, "Building Microservices" — Chapter on Architecture Decision Records
- Michael feathers, "Documenting Architecture Decisions" (video): https://www.youtube.com/watch?v=4p5RrrqTKQI
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
- Sequenced collections provide a more clear Collection API
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