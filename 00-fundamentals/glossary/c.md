# Glossary - C

## CAP Theorem

**Definition**: A distributed system can only provide two of three guarantees simultaneously: Consistency, Availability, and Partition Tolerance.

**Context**: Fundamental concept in distributed systems design, guiding trade-off decisions between data consistency and system availability.

**Related Terms**: Eventual Consistency, ACID, BASE

**Example**: Amazon DynamoDB chooses availability and partition tolerance, providing eventual consistency for better uptime during network issues.

---

## Circuit Breaker Pattern

**Definition**: A resilience pattern that detects failures and prevents an application from repeatedly trying to execute an operation that is likely to fail, allowing it to recover.

**Context**: Used in distributed systems to prevent cascading failures when downstream services are unavailable or slow.

**Related Terms**: Bulkhead, Retry, Fallback

**Example**: A circuit breaker that opens after 5 consecutive failures to the payment service, returning a cached response until the service recovers.

---

## CQRS (Command Query Responsibility Segregation)

**Definition**: A pattern that separates read and write operations into different models, optimizing each for its specific use case.

**Context**: Used when read and write requirements differ significantly, such as in systems with heavy read loads or complex write operations.

**Related Terms**: Event Sourcing, DDD, Read Model, Write Model

**Example**: A command side that processes order creation with validation, and a separate query side optimized for displaying order history with denormalized views.

---

## CRUD

**Definition**: An acronym for Create, Read, Update, Delete, the four basic operations for persistent storage.

**Context**: Describes fundamental data manipulation operations in databases, APIs, and user interfaces.

**Related Terms**: REST API, Data Access, Persistence

**Example**: REST endpoints: POST (Create), GET (Read), PUT (Update), DELETE (Delete) for managing user records.

---

## Container

**Definition**: A lightweight, standalone, executable package of software that includes everything needed to run an application, including code, runtime, system tools, and libraries.

**Context**: Used to ensure consistent deployment across environments and enable microservices architecture.

**Related Terms**: Docker, Kubernetes, Virtual Machine, Image

**Example**: A Docker container running a Node.js application with all dependencies, deployable to any environment supporting Docker.

---

## Container Orchestration

**Definition**: The automated arrangement, coordination, and management of software containers, typically across multiple hosts.

**Context**: Essential for managing large-scale container deployments in production environments.

**Related Terms**: Kubernetes, Docker Swarm, Service Discovery

**Example**: Kubernetes automatically scaling a web application from 3 to 10 pods during high traffic and back to 3 during low traffic periods.

---

## Consistency

**Definition**: In distributed systems, the guarantee that all nodes in a system have the same data at the same time.

**Context**: One of the three CAP theorem properties, often traded off for availability or partition tolerance.

**Related Terms**: ACID, Eventual Consistency, CAP Theorem

**Example**: After updating a user's email in a consistent system, all subsequent reads from any node return the new email address.

---

## Concurrency

**Definition**: The ability of a system to handle multiple tasks simultaneously, either through parallel execution or time-slicing on a single processor.

**Context**: Fundamental concept in software development for improving performance and responsiveness.

**Related Terms**: Parallelism, Thread, Goroutine, Actor Model

**Example**: A web server handling multiple HTTP requests simultaneously using thread pools or async I/O.

---

## Coupling

**Definition**: The degree of interdependence between software modules, ranging from tight coupling (high dependency) to loose coupling (minimal dependency).

**Context**: A key design consideration affecting maintainability, testability, and flexibility of software systems.

**Related Terms**: Cohesion, Dependency Injection, Interface

**Example**: Loose coupling: A service depends only on an interface, not concrete implementations, allowing the implementation to change without affecting the service.

---

## CORS (Cross-Origin Resource Sharing)

**Definition**: A security mechanism that allows or restricts web pages from one origin to access resources from another origin.

**Context**: Essential for web applications that need to access APIs or resources from different domains.

**Related Terms**: Same-Origin Policy, Preflight Request, Access-Control-Allow-Origin

**Example**: A frontend at `example.com` making an API call to `api.example.com` requires CORS headers to allow the cross-origin request.

---

## Cache

**Definition**: A temporary storage area that stores frequently accessed data to reduce retrieval time from slower storage or network.

**Context**: Used to improve application performance by reducing database queries and network calls.

**Related Terms**: CDN, Redis, Memoization, Cache Invalidation

**Example**: Storing user session data in Redis for quick access instead of querying the database on every request.
