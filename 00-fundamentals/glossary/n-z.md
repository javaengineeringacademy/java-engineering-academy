# Glossary - N through Z

## OAuth

**Definition**: An open standard for token-based authentication and authorization, allowing third-party applications to access resources on behalf of a user.

**Context**: Used for delegated authorization in web and mobile applications, enabling secure access without sharing credentials.

**Related Terms**: JWT, Access Token, Authorization Server, OpenID Connect

**Example**: A user authorizing a third-party application to access their Google contacts without sharing their Google password.

---

## Observability

**Definition**: The ability to measure the internal state of a system by examining its outputs, typically through logs, metrics, and traces.

**Context**: Essential for debugging and monitoring distributed systems where direct inspection is not possible.

**Related Terms**: Logging, Metrics, Tracing, OpenTelemetry

**Example**: Using distributed tracing to follow a request through multiple microservices to identify performance bottlenecks.

---

## ORM (Object-Relational Mapping)

**Definition**: A technique for converting data between incompatible type systems in object-oriented programming languages and relational databases.

**Context**: Simplifies database interactions by allowing developers to work with objects instead of SQL queries.

**Related Terms**: Hibernate, Entity Framework, JPA

**Example**: Using JPA annotations to map a Java class to a database table, with Hibernate generating the SQL automatically.

---

## PostgreSQL

**Definition**: An open-source object-relational database system known for its reliability, feature robustness, and performance.

**Context**: Widely used as a primary database for web applications, known for standards compliance and extensibility.

**Related Terms**: SQL, ACID, Database, Relational Database

**Example**: Using PostgreSQL's JSONB type to store and query semi-structured data alongside traditional relational data.

---

## Proxy Pattern

**Definition**: A structural design pattern that provides a surrogate or placeholder for another object to control access to it.

**Context**: Used for lazy initialization, access control, logging, or remote resource proxying.

**Related Terms**: Facade, Adapter, Remote Proxy

**Example**: A virtual proxy that delays loading a large image until it is actually needed, improving initial load time.

---

## Query Language (SQL)

**Definition**: A domain-specific language for managing and manipulating relational databases, standardized as SQL.

**Context**: Fundamental for data retrieval, manipulation, and definition in relational database systems.

**Related Terms**: Database, ORM, Relational Model

**Example**: `SELECT name, email FROM users WHERE active = true ORDER BY created_at DESC LIMIT 10`

---

## Queue

**Definition**: A data structure that follows First-In-First-Out (FIFO) principle, where elements are added at the end and removed from the front.

**Context**: Used in message passing, task scheduling, and buffering in software systems.

**Related Terms**: Message Queue, Stack, FIFO

**Example**: A print queue where print jobs are processed in the order they are submitted.

---

## Rate Limiting

**Definition**: A technique to control the number of requests a client can make to an API within a given time period.

**Context**: Protects APIs from abuse, ensures fair usage, and prevents service overload.

**Related Terms**: Throttling, API Gateway, Token Bucket

**Example**: An API allowing 100 requests per minute per user, returning HTTP 429 Too Many Requests when exceeded.

---

## Redis

**Definition**: An open-source, in-memory data structure store used as a database, cache, message broker, and streaming engine.

**Context**: Widely used for caching, session storage, real-time analytics, and message queuing.

**Related Terms**: Cache, In-Memory Database, Pub/Sub, NoSQL

**Example**: Using Redis to cache user session data with automatic expiration after 30 minutes of inactivity.

---

## Repository Pattern

**Definition**: A design pattern that abstracts data layer logic, providing a collection-like interface for accessing domain objects.

**Context**: Separates business logic from data access, enabling easier testing and maintenance.

**Related Terms**: DDD, Data Access, Unit of Work

**Example**: A `UserRepository` interface with methods like `findById()`, `save()`, and `findAll()`, hiding the underlying database implementation.

---

## REST (Representational State Transfer)

**Definition**: An architectural style for designing networked applications, using standard HTTP methods and stateless communication.

**Context**: Dominant approach for designing web APIs, emphasizing simplicity and scalability.

**Related Terms**: HTTP, API, Stateless, Resource

**Example**: A RESTful API with endpoints: GET /users, POST /users, GET /users/{id}, PUT /users/{id}, DELETE /users/{id}

---

## Resilience

**Definition**: The ability of a system to handle failures gracefully and continue operating, often degraded, rather than failing completely.

**Context**: Critical for distributed systems where component failures are inevitable.

**Related Terms**: Circuit Breaker, Bulkhead, Retry, Fallback

**Example**: A system that continues serving cached data when the primary database becomes unavailable.

---

## Saga Pattern

**Definition**: A pattern for managing distributed transactions across multiple services by defining a sequence of local transactions with compensating actions for rollback.

**Context**: Used in microservices where traditional ACID transactions across services are not feasible.

**Related Terms**: Event Sourcing, CQRS, Distributed Transactions

**Example**: An order saga that reserves inventory, processes payment, and ships the order, with compensation steps to undo each if a later step fails.

---

## Service Mesh

**Definition**: An infrastructure layer for handling service-to-service communication, providing features like load balancing, encryption, and observability.

**Context**: Used in microservices architectures to offload networking concerns from application code.

**Related Terms**: Istio, Linkerd, Envoy, Sidecar

**Example**: Istio automatically adding mTLS encryption between all microservices and providing distributed tracing without code changes.

---

## Singleton Pattern

**Definition**: A creational design pattern that ensures a class has only one instance and provides a global point of access to it.

**Context**: Used when exactly one object is needed to coordinate actions across the system.

**Related Terms**: Factory, Global State, Static Instance

**Example**: A database connection pool manager implemented as a singleton to ensure all application parts use the same pool.

---

## TDD (Test-Driven Development)

**Definition**: A software development approach where tests are written before the actual code, following a red-green-refactor cycle.

**Context**: Promotes better code design, comprehensive testing, and confidence in code changes.

**Related Terms**: Unit Testing, Refactoring, Red-Green-Refactor

**Example**: Writing a failing test for a login function, writing minimal code to pass it, then refactoring for better design.

---

## Unit Testing

**Definition**: Testing individual components or functions in isolation to verify they work correctly.

**Context**: Foundation of testing strategy, ensuring each unit of code behaves as expected.

**Related Terms**: TDD, Mocking, Test Coverage

**Example**: Testing a `calculateDiscount()` function with various inputs to verify correct discount calculation.

---

## Value Object

**Definition**: An immutable object that represents a descriptive aspect of the domain with no conceptual identity, defined by its attributes.

**Context**: Used in Domain-Driven Design for concepts like money, dates, or addresses that are defined by their values.

**Related Terms**: DDD, Entity, Immutable

**Example**: A `Money` value object containing amount and currency, where two Money objects with the same values are considered equal.

---

## Virtual Machine

**Definition**: An emulation of a computer system that runs on top of physical hardware, providing isolated computing environments.

**Context**: Used for server consolidation, testing environments, and cloud computing infrastructure.

**Related Terms**: Hypervisor, Container, Cloud Computing

**Example**: Running multiple virtual machines on a single physical server, each with its own operating system and applications.

---

## Webhook

**Definition**: A user-defined HTTP callback that is triggered by specific events, sending data to a specified URL.

**Context**: Used for real-time notifications and integration between systems when events occur.

**Related Terms**: Callback, API, Event-Driven

**Example**: A GitHub webhook sending a POST request to a deployment server when code is pushed to the main branch.

---

## WebSocket

**Definition**: A communication protocol providing full-duplex communication channels over a single TCP connection.

**Context**: Used for real-time applications requiring low-latency bidirectional communication.

**Related Terms**: HTTP, Real-time, Bidirectional

**Example**: A chat application using WebSocket to instantly deliver messages between users without polling the server.

---

## YAML

**Definition**: A human-readable data serialization language commonly used for configuration files.

**Context**: Widely used in DevOps tools, CI/CD pipelines, and application configuration.

**Related Terms**: Configuration, Docker Compose, Kubernetes

**Example**: A Kubernetes YAML file defining a Deployment with container images, replicas, and resource limits.
