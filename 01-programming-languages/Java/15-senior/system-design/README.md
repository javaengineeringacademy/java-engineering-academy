# Java System Design Patterns

## Not GoF Patterns — Architectural Patterns

This document covers patterns for building production systems, not design patterns for writing classes. These patterns solve problems at the system level: migration, consistency, scalability, resilience, and observability.

---

## 1. Strangler Fig Pattern

### Problem
You have a large monolithic application that needs to be migrated to a new architecture, but you can't afford downtime or a risky "big bang" rewrite.

### Solution
Gradually replace parts of the monolith with new services, routing traffic through a facade that directs requests to either the old or new system. Over time, the old system shrinks (is "strangled") until it can be decommissioned.

### When to Use
- Legacy monolith that's too risky to rewrite at once
- Team wants to migrate incrementally while maintaining feature delivery
- Business cannot tolerate any downtime during migration
- You need to prove the new architecture works before committing fully

### Java Implementation Approach

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   Client    │────▶│   Facade     │────▶│  New Service │
└─────────────┘     │  (Gateway)   │     └──────────────┘
                    │              │
                    │              │────▶┌──────────────┐
                    └──────────────┘     │   Monolith   │
                                         └──────────────┘
```

- Implement a facade using Spring Cloud Gateway or Nginx
- Route by URL path, header, or feature flag
- Start with low-risk endpoints (read-only, non-critical)
- Gradually shift traffic: 10% → 50% → 100%
- Use feature flags (LaunchDarkly, Unleash) for fine-grained control
- Shadow traffic: send to both old and new, compare results
- Monitor error rates and latency before each traffic shift

---

## 2. Saga Pattern

### Problem
In a microservices architecture, a business transaction spans multiple services. Traditional ACID transactions don't work across service boundaries. How do you ensure consistency without distributed locks?

### Solution
Model the business transaction as a sequence of local transactions. Each service performs its local transaction and publishes events. If any step fails, compensating transactions undo the previous steps.

### When to Use
- Business transactions span 2+ microservices
- You need eventual consistency without distributed transactions
- Each service owns its data (database-per-service pattern)
- You need audit trails of business transactions

### Java Implementation Approach

**Choreography-based Saga:**
```
Order Service ──▶ OrderCreated ──▶ Payment Service
                                        │
                                        ▼
                                   PaymentProcessed
                                        │
                                        ▼
                                   Inventory Service
                                        │
                                        ▼
                                   InventoryReserved
```

- Each service listens for events and publishes its own events
- Compensating events are published on failure (PaymentFailed → OrderCancelled)
- Use Kafka for reliable event delivery
- Implement idempotency at every consumer (idempotency key in database)

**Orchestration-based Saga:**
- A central orchestrator service coordinates the steps
- Orchestrator calls each service and handles failures
- Easier to reason about but introduces a single point of coordination

```java
// Pseudocode for orchestration
public class OrderSaga {
    public void execute(CreateOrderCommand cmd) {
        try {
            paymentService.charge(cmd.payment);
            inventoryService.reserve(cmd.items);
            orderService.confirm(cmd.orderId);
        } catch (PaymentFailedException e) {
            inventoryService.release(cmd.items); // compensate
            throw new SagaFailedException(e);
        } catch (InventoryException e) {
            paymentService.refund(cmd.payment); // compensate
            throw new SagaFailedException(e);
        }
    }
}
```

---

## 3. CQRS (Command Query Responsibility Segregation)

### Problem
Your application has very different read and write patterns. Writes are simple CRUD, but reads require complex joins, aggregations, and denormalization. Optimizing for both in the same model creates conflict.

### Solution
Separate the read model from the write model. The write side uses a normalized model optimized for consistency. The read side uses a denormalized model optimized for query performance. Synchronize them via events.

### When to Use
- Read and write workloads have different scaling needs
- Complex queries on the write model are slow
- You need different data shapes for different consumers
- Read-heavy systems (100:1 read-to-write ratio)

### Java Implementation Approach

```
┌─────────────┐                    ┌─────────────┐
│  Commands   │──▶ Write DB ──▶ Events ──▶ Read DB │
│  (writes)   │   (normalized)         (denormalized) │
└─────────────┘                    └─────────────┘
                                         │
                                         ▼
                                   ┌─────────────┐
                                   │   Queries   │
                                   │   (reads)   │
                                   └─────────────┘
```

- Write side: PostgreSQL with normalized schema, ACID transactions
- Read side: Elasticsearch, Redis, or materialized views
- Synchronize via Kafka events (Change Data Capture with Debezium)
- Read models are eventually consistent (acceptable for most use cases)
- Each read model can be optimized for its specific query pattern

---

## 4. Event Sourcing

### Problem
Traditional CRUD systems lose history. When a record is updated, the previous state is gone. You need a complete audit trail, the ability to time-travel, and the ability to rebuild state from scratch.

### Solution
Instead of storing current state, store the sequence of events that led to the current state. Current state is derived by replaying events.

### When to Use
- Audit trail is a business requirement (finance, healthcare, legal)
- You need to reconstruct state at any point in time
- Event-driven architecture is already in place
- Debugging requires understanding what happened, not just what is

### Java Implementation Approach

```
Event Store (append-only log):
─────────────────────────────────────────
│ OrderCreated │ PaymentReceived │ ItemShipped │
─────────────────────────────────────────

Current State = replay all events for this aggregate
```

- Event store: PostgreSQL with append-only table, or Kafka topics
- Events are immutable once written
- Aggregate root is rebuilt by replaying events
- Snapshot every N events to avoid replaying entire history
- Use Jackson for event serialization with schema versioning
- Implement event upcasting for backward compatibility

```java
public class OrderAggregate {
    private List<Event> events = new ArrayList<>();

    public void apply(Event event) {
        events.add(event);
        // update state based on event type
    }

    public static OrderAggregate reconstitute(List<Event> events) {
        OrderAggregate aggregate = new OrderAggregate();
        events.forEach(aggregate::apply);
        return aggregate;
    }
}
```

---

## 5. API Gateway

### Problem
Clients need to talk to multiple backend services. Without a gateway, clients must know about every service, handle authentication for each, and manage multiple network calls.

### Solution**
Implement a single entry point that handles routing, authentication, rate limiting, and request aggregation. Clients talk only to the gateway.

### When to Use
- Multiple backend services with different protocols
- Need centralized authentication/authorization
- Rate limiting and throttling required
- Request aggregation for mobile clients (reduce round trips)
- API versioning management

### Java Implementation Approach

```
┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  Mobile  │────▶│     API      │────▶│ User Service │
│  Client  │     │   Gateway    │     └──────────────┘
└──────────┘     │              │     ┌──────────────┐
                 │  (Spring     │────▶│ Order Service│
┌──────────┐     │   Cloud      │     └──────────────┐
│  Web     │────▶│   Gateway)   │     ┌──────────────┐
│  Client  │     │              │────▶│Payment Svc   │
└──────────┘     └──────────────┘     └──────────────┘
```

- Use Spring Cloud Gateway (reactive, non-blocking)
- Route configuration via YAML or service discovery
- Implement filters for auth, rate limiting, logging
- JWT validation at the gateway (don't pass to every service)
- Request aggregation: combine multiple service calls into one response
- Circuit breaker integration (Resilience4j) for fault tolerance

---

## 6. Service Mesh

### Problem
Cross-cutting concerns (mTLS, retries, observability, traffic management) are duplicated across every service. Each service implements its own retry logic, its own logging, its own auth.

### Solution
Move cross-cutting concerns into the infrastructure layer (sidecar proxies). Services focus on business logic; the mesh handles communication concerns.

### When to Use
- Large number of microservices (>20)
- Need consistent mTLS across all services
- Traffic management (canary, A/B, blue-green) is complex
- Observability requirements are uniform across services
- Team wants to reduce code duplication for infrastructure concerns

### Java Implementation Approach

```
┌─────────────────────────────────────────┐
│ Service Pod                             │
│  ┌─────────────┐  ┌─────────────────┐  │
│  │ Java Service│◀─▶│ Envoy Proxy     │  │
│  │ (Business)  │  │ (Sidecar)       │  │
│  └─────────────┘  └─────────────────┘  │
└─────────────────────────────────────────┘
```

- Use Istio or Linkerd as the service mesh platform
- Envoy proxy handles mTLS, retries, circuit breaking, observability
- Java service only handles business logic (no retry/resilience code)
- Traffic management via Istio VirtualService (canary, routing)
- Distributed tracing via Jaeger (auto-injected by mesh)
- Service-to-service auth is automatic (mTLS)

---

## 7. Sidecar Pattern

### Problem
Cross-cutting concerns (logging, monitoring, security, configuration) need to be added to every service without modifying service code.

### Solution
Deploy a companion process (sidecar) alongside each service instance. The sidecar handles the cross-cutting concern and the service focuses on business logic.

### When to Use
- Need consistent logging/monitoring across polyglot services
- Security concerns (mTLS, certificate rotation) need centralization
- Configuration management across services
- Protocol translation (HTTP to gRPC)
- Want to add capabilities without code changes

### Java Implementation Approach

- Sidecar runs as a separate container in the same pod (Kubernetes)
- Communicates with the main service via localhost
- Common sidecars: Envoy, Fluentd, Consul agent, Vault agent
- Java service remains clean: no infrastructure libraries needed
- Sidecar handles: log shipping, metrics collection, certificate management, config polling

```
┌──────────────────────────────────────────┐
│ Kubernetes Pod                           │
│  ┌──────────────┐  ┌──────────────────┐  │
│  │ Java Service │  │ Fluentd Sidecar  │  │
│  │ (port 8080)  │──▶│ (collects logs)  │  │
│  └──────────────┘  └──────────────────┘  │
│  ┌──────────────┐  ┌──────────────────┐  │
│  │ Java Service │  │ Vault Agent      │  │
│  │              │◀─▶│ (secrets)        │  │
│  └──────────────┘  └──────────────────┘  │
└──────────────────────────────────────────┘
```

---

## 8. Bulkhead Pattern

### Problem
One failing dependency brings down the entire system. A slow database query consumes all threads, preventing other requests from being processed.

### Solution
Isolate resources for each dependency into separate pools. If one dependency fails or slows down, it only affects its own pool, not the entire system.

### When to Use
- Multiple downstream dependencies with different reliability profiles
- One slow dependency shouldn't affect others
- Need graceful degradation under partial failure
- High availability requirements

### Java Implementation Approach

```java
// Bulkhead using Resilience4j
Bulkhead bulkhead = Bulkhead.of("paymentService",
    BulkheadConfig.custom()
        .maxConcurrentCalls(10)
        .maxWaitDuration(Duration.ofMillis(500))
        .build());

// Separate bulkhead for each dependency
Bulkhead inventoryBulkhead = Bulkhead.of("inventoryService", ...);
Bulkhead userBulkhead = Bulkhead.of("userService", ...);

// Each dependency gets its own thread pool
ExecutorService paymentPool = Executors.newFixedThreadPool(10);
ExecutorService inventoryPool = Executors.newFixedThreadPool(5);
```

- Use Resilience4j Bulkhead for thread pool isolation
- Configure max concurrent calls per dependency
- Separate bulkheads for separate dependencies
- Monitor bulkhead metrics (available calls, wait duration)
- Combine with circuit breaker for comprehensive fault tolerance

---

## 9. Retry with Circuit Breaker

### Problem
Transient failures (network blips, temporary overloads) cause cascading failures. Retrying immediately overwhelms the already-struggling service. But not retrying at all loses recoverable requests.

### Solution
Implement retry with exponential backoff for transient failures, and circuit breaker to stop calling a failing service entirely. The circuit breaker trips after N failures, allowing the service to recover.

### When to Use
- Network calls to external services
- Database connections that may temporarily fail
- Any dependency that may have transient failures
- Need to prevent cascade failures

### Java Implementation Approach

```java
// Circuit Breaker + Retry using Resilience4j
CircuitBreaker circuitBreaker = CircuitBreaker.of("paymentService",
    CircuitBreakerConfig.custom()
        .failureRateThreshold(50)
        .waitDurationInOpenState(Duration.ofSeconds(30))
        .slidingWindowSize(10)
        .build());

Retry retry = Retry.of("paymentService",
    RetryConfig.custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofMillis(500))
        .retryExceptions(TimeoutException.class, IOException.class)
        .build());

// Compose them
Supplier<Payment> decoratedSupplier = Decorator.ofSupplier(() -> paymentService.charge(order))
    .withCircuitBreaker(circuitBreaker)
    .withRetry(retry)
    .decorate();

// Circuit breaker states: CLOSED → OPEN → HALF_OPEN → CLOSED
```

- Use Resilience4j for both circuit breaker and retry
- Configure failure rate threshold (e.g., 50% in sliding window)
- Wait duration in open state before trying again (e.g., 30 seconds)
- Retry with exponential backoff: 100ms → 200ms → 400ms
- Monitor circuit breaker state transitions via metrics
- Fallback method when circuit is open (return cached data, default response)

---

## 10. Leader Election

### Problem
In a distributed system, you need exactly one instance to perform a task (scheduled job, coordination, cleanup). How do you ensure only one instance runs the task?

### Solution
Implement leader election where instances compete to become leader. Only the leader performs the task. If the leader fails, another instance takes over.

### When to Use
- Scheduled jobs that must run exactly once (cleanup, aggregation)
- Coordination tasks in distributed systems
- Need to avoid duplicate processing
- High availability with single-active processing

### Java Implementation Approach

```
┌──────────────┐
│ Instance 1   │────▶ Leader (active)
└──────────────┘

┌──────────────┐
│ Instance 2   │────▶ Follower (standby)
└──────────────┘

┌──────────────┐
│ Instance 3   │────▶ Follower (standby)
└──────────────┘
```

- Use Kubernetes leader election (Lease API)
- Or use ZooKeeper/Curator for leader election
- Or use database-based locking (SELECT FOR UPDATE)
- Leader heartbeat: if leader doesn't renew lease, new election occurs
- Follower instances remain ready to take over
- Implement lease renewal with timeout detection

```java
// ZooKeeper-based leader election using Curator
LeaderSelector selector = new LeaderSelector(client, "/leader/election", new LeaderSelectorListenerAdapter() {
    public void takeLeadership(CuratorFramework client) {
        // This instance is now leader
        runScheduledTask();
    }
});
selector.autoRequeue();
selector.start();
```

---

## Pattern Selection Guide

| Problem | Pattern |
|---------|---------|
| Migrating from monolith | Strangler Fig |
| Distributed transactions | Saga |
| Different read/write models | CQRS |
| Audit trail needed | Event Sourcing |
| Multiple backend services | API Gateway |
| Cross-cutting concerns | Service Mesh / Sidecard |
| Resource isolation | Bulkhead |
| Transient failures | Retry + Circuit Breaker |
| Exactly-once scheduled task | Leader Election |

## Anti-Patterns in System Design

1. **Pattern over-engineering**: Don't use CQRS if simple CRUD works
2. **Copy-paste architecture**: What works at Netflix may not work for your 5-person team
3. **Ignoring failure modes**: Every pattern needs a failure mode analysis
4. **Premature decomposition**: Don't create microservices until you have the team size and domain clarity
5. **Over-reliance on infrastructure**: If your team can't debug it, the pattern isn't helping
