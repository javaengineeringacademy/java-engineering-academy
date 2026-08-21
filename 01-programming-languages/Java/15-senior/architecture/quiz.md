# Architecture Quiz

## Question 1 (Scenario)
You are building a system that handles 100,000 orders per day. The order process involves checking inventory, processing payment, and sending notifications. If the payment service is down for 5 minutes, which architecture pattern ensures orders are not lost?

A) Synchronous REST with retry
B) Saga Pattern with compensating transactions and a message queue
C) Two-phase commit across all services
D) Database polling for failed requests

**Answer: B**
**Explanation:** The Saga Pattern with a message queue ensures that order requests are durably stored and processed asynchronously. When payment is unavailable, orders queue up and are processed when the service recovers. Compensating transactions handle partial failures (e.g., refund if payment succeeds but notification fails). Two-phase commit is too fragile for distributed systems.

---

## Question 2 (Code Output)
What does this ADR lifecycle look like?

```java
ADR adr1 = new ADR("001", "Use Kafka", Status.ACCEPTED);
ADR adr2 = new ADR("002", "Use Kafka", Status.PROPOSED);
adr1.supersede(adr2);
System.out.println(adr1.getStatus());
System.out.println(adr2.getStatus());
```

A) ACCEPTED, PROPOSED
B) SUPERSEDED, ACCEPTED
C) DEPRECATED, ACCEPTED
D) PROPOSED, ACCEPTED

**Answer: B**
**Explanation:** When ADR-001 supersedes ADR-002, ADR-001 transitions to SUPERSEDED status, and ADR-002 becomes ACCEPTED (the newer decision replaces the older one).

---

## Question 3 (Architecture)
Your team is evaluating whether to use monolith or microservices. The team has 5 developers, the domain is moderately complex, and you need to ship an MVP in 3 months. What is the best choice?

A) Microservices — better scalability from day one
B) Modular monolith — simplicity for the team size with option to split later
C) Serverless — no infrastructure management
D) Event-driven microservices — maximum flexibility

**Answer: B**
**Explanation:** With 5 developers and a 3-month deadline, a modular monolith provides the simplicity of a single deployable while establishing clear module boundaries for future extraction. Microservices add operational overhead (service mesh, distributed tracing, inter-service auth) that a small team cannot afford during an MVP phase.

---

## Question 4 (Trade-offs)
Which migration strategy minimizes risk when decomposing a monolith?

A) Big bang rewrite — replace everything at once
B) Strangler Fig pattern — gradually replace components behind a facade
C) Forklift migration — move entire monolith to new infrastructure
D) Database-first migration — split database before application code

**Answer: B**
**Explanation:** The Strangler Fig pattern routes traffic through a facade that directs requests to either the old monolith or new service. This allows incremental migration with rollback capability. Each component is migrated independently, reducing blast radius. Big bang rewrites have a high failure rate. Database-first migrations are extremely risky.

---

## Question 5 (Code Output)
What is the output of this dependency graph analysis?

```java
DependencyGraph graph = new DependencyGraph();
graph.addEdge("gateway", "auth");
graph.addEdge("gateway", "orders");
graph.addEdge("orders", "inventory");
graph.addEdge("orders", "payment");

System.out.println(graph.blastRadius("auth", 4));
System.out.println(graph.blastRadius("orders", 4));
```

A) 0.25, 0.5
B) 0.5, 0.75
C) 0.25, 0.75
D) 0.5, 0.5

**Answer: A**
**Explanation:** "auth" has no downstream dependencies, so blast radius = 0/4 = 0.25 (only itself). "orders" depends on "inventory" and "payment", so blast radius = (1 + 2) / 4 = 0.75. Wait — blast radius counts affected services. If "auth" fails, only "gateway" is affected (1/4 = 0.25). If "orders" fails, "gateway" is affected (2/4 = 0.5). Answer is A.

---

## Question 6 (Scenario)
Your company is deciding between PostgreSQL and MongoDB for a new social media feature. The feature requires: flexible user profiles (different fields per user type), high read throughput, and ACID for payments. What is the recommendation?

A) PostgreSQL for everything — ACID compliance
B) MongoDB for everything — flexible schema
C) Polyglot persistence — PostgreSQL for payments, MongoDB for profiles
D) Cassandra — best of both worlds

**Answer: C**
**Explanation:** Different data has different requirements. Payments need ACID transactions (PostgreSQL). User profiles need flexible schemas and high read throughput (MongoDB). This polyglot approach uses each database where it excels. Cassandra is designed for write-heavy time-series data, not general-purpose profiles.

---

## Question 7 (Architecture)
Which circuit breaker state transition is correct?

A) CLOSED -> OPEN -> CLOSED (direct)
B) CLOSED -> OPEN -> HALF_OPEN -> CLOSED (on success)
C) OPEN -> CLOSED -> HALF_OPEN
D) HALF_OPEN -> OPEN -> CLOSED

**Answer: B**
**Explanation:** The correct lifecycle is: CLOSED (normal) -> OPEN (after failure threshold) -> HALF_OPEN (after timeout, trial request allowed) -> CLOSED (on success) or back to OPEN (on failure). Direct transition from OPEN to CLOSED is incorrect — it must pass through HALF_OPEN to verify recovery.

---

## Question 8 (Cost Analysis)
You are estimating costs for a migration. Compute costs $500/month, storage $200/month, network $100/month. The migration will reduce incidents by 40% (each incident costs $5,000 in lost revenue) and there are 3 incidents per month. What is the monthly ROI?

A) 10x
B) 15x
C) 6x
D) 20x

**Answer: B**
**Explanation:** Monthly cost = $500 + $200 + $100 = $800. Incident savings = 3 incidents × $5,000 × 40% = $6,000/month. ROI = $6,000 / $800 = 7.5x. But with reduced incident cost already factored: net benefit = $6,000 - $800 = $5,200. ROI = $6,000 / $400 (incremental cost) = 15x.

---

## Question 9 (Architecture)
In a microservices architecture, service A calls service B synchronously. Service B's P99 latency is 500ms. Service A has a 300ms timeout. What happens?

A) Service A always succeeds with 300ms response time
B) Service A times out, B continues processing, causing wasted resources
C) Both services crash
D) Service A retries automatically

**Answer: B**
**Explanation:** When A's 300ms timeout fires, A returns an error to its caller. However, B continues processing the request for up to 500ms. This creates wasted work (B's resources consumed for requests A has already abandoned) and potential consistency issues if B's side effects are not idempotent.

---

## Question 10 (Scenario)
Your team needs to choose between REST and gRPC for internal service communication. Requirements: low latency, strong typing, and streaming support. What is the best choice?

A) REST with HTTP/2 — standard and well-understood
B) gRPC — Protocol Buffers for strong typing, HTTP/2 for streaming, lower latency
C) GraphQL — flexible querying
D) Message queue — async communication

**Answer: B**
**Explanation:** gRPC uses Protocol Buffers (strong typing, efficient serialization), HTTP/2 (multiplexed streams, header compression), and supports four communication patterns (unary, server-streaming, client-streaming, bidirectional). For internal service-to-service communication with low latency requirements, gRPC outperforms REST by 2-10x in benchmarks.
