# Glossary - F through M

## Factory Pattern

**Definition**: A creational design pattern that provides an interface for creating objects without specifying their concrete classes.

**Context**: Used when the system needs to be independent from how its objects are created and when there are multiple possible implementations.

**Related Terms**: Abstract Factory, Builder, Singleton

**Example**: A `PaymentFactory` that returns the appropriate `PaymentProcessor` (CreditCard, PayPal, Bitcoin) based on the payment method string.

---

## Facade Pattern

**Definition**: A structural design pattern that provides a simplified interface to a complex subsystem.

**Context**: Used to reduce complexity for clients by hiding subsystem details behind a unified interface.

**Related Terms**: Adapter, Wrapper, Interface

**Example**: A `ComputerFacade` class that simplifies the startup process by coordinating the CPU, memory, and hard drive operations behind a single `start()` method.

---

## Fault Tolerance

**Definition**: The ability of a system to continue operating properly in the event of the failure of some of its components.

**Context**: Critical for distributed systems and high-availability applications where component failures are expected.

**Related Terms**: Redundancy, Circuit Breaker, Retry

**Example**: A web application running on multiple servers so if one fails, load balancers redirect traffic to healthy instances.

---

## gRPC

**Definition**: A high-performance RPC framework developed by Google using Protocol Buffers for serialization and HTTP/2 for transport.

**Context**: Used for efficient communication between microservices, especially when performance is critical.

**Related Terms**: Protocol Buffers, HTTP/2, Microservices, REST API

**Example**: A gRPC service definition for `UserService` with methods like `GetUser` and `CreateUser`, generating client and server code from Protocol Buffer definitions.

---

## GitOps

**Definition**: An operational framework where Git repositories serve as the single source of truth for declarative infrastructure and application configuration.

**Context**: Used for managing cloud-native infrastructure and applications through pull requests and version control.

**Related Terms**: Infrastructure as Code, CI/CD, ArgoCD, Flux

**Example**: Using ArgoCD to automatically deploy Kubernetes resources when changes are pushed to a Git repository, ensuring the cluster state matches the desired configuration.

---

## Goroutine

**Definition**: A lightweight thread managed by the Go runtime, enabling concurrent execution with minimal overhead.

**Context**: Core concurrency primitive in Go programming language for building scalable concurrent applications.

**Related Terms**: Channel, Concurrency, Parallelism

**Example**: Spawning thousands of goroutines to handle concurrent HTTP requests, with each goroutine processing a request independently.

---

## GraphQL

**Definition**: A query language and runtime for APIs developed by Facebook that allows clients to request exactly the data they need.

**Context**: Used to reduce over-fetching and under-fetching problems in REST APIs by allowing flexible queries.

**Related Terms**: REST API, Schema, Resolver, Apollo

**Example**: A GraphQL query requesting only `user.name` and `user.email` fields instead of getting the entire user object.

---

## HATEOAS

**Definition**: Hypermedia As The Engine Of Application State, a constraint of REST API design where responses include links to related resources.

**Context**: Enables API discoverability by providing navigation links in responses.

**Related Terms**: REST API, Hypermedia, Resource Links

**Example**: A user API response including links like `{"self": "/users/123", "orders": "/users/123/orders"}` for navigation.

---

## Hibernate

**Definition**: An object-relational mapping framework for Java that simplifies database interactions by mapping Java objects to database tables.

**Context**: Widely used in Java applications to reduce boilerplate JDBC code and provide object-oriented data access.

**Related Terms**: ORM, JPA, Entity, Session

**Example**: A `@Entity` annotated `User` class mapped to a `users` table, with Hibernate handling SQL generation and result mapping.

---

## Horizontal Scaling

**Definition**: Adding more machines to a system to handle increased load, as opposed to vertical scaling which adds more resources to existing machines.

**Context**: Preferred approach for distributed systems to achieve better availability and performance.

**Related Terms**: Load Balancer, Auto-scaling, Distributed System

**Example**: Adding more web server instances behind a load balancer to handle increased traffic during a sale event.

---

## Idempotency

**Definition**: The property of an operation where performing it multiple times has the same effect as performing it once.

**Context**: Critical for API design, especially for payment processing and operations that may be retried.

**Related Terms**: REST API, HTTP Methods, Retry

**Example**: A PUT request to update a user's email is idempotent, while a POST request to create a new order is not.

---

## IoC (Inversion of Control)

**Definition**: A design principle where the control of object creation and flow is transferred from the application code to an external container or framework.

**Context**: Fundamental to dependency injection and modern application frameworks.

**Related Terms**: Dependency Injection, Spring Framework, Container

**Example**: Spring IoC container managing bean creation and dependency injection rather than the application code instantiating objects directly.

---

## JWT (JSON Web Token)

**Definition**: A compact, URL-safe means of representing claims to be transferred between two parties, commonly used for authentication and authorization.

**Context**: Standard for stateless authentication in web applications, especially in microservices.

**Related Terms**: OAuth, Token, Authorization, HMAC

**Example**: A JWT containing user ID and roles, signed with a secret key, sent in the Authorization header for each API request.

---

## Kafka

**Definition**: A distributed event streaming platform developed by LinkedIn, designed for high-throughput, fault-tolerant, durable messaging.

**Context**: Used for building real-time data pipelines, event-driven architectures, and streaming applications.

**Related Terms**: Event Streaming, Pub/Sub, Message Queue, Event Sourcing

**Example**: Using Kafka topics to handle millions of clickstream events per second for real-time analytics and logging.

---

## Kubernetes (K8s)

**Definition**: An open-source container orchestration platform that automates deployment, scaling, and management of containerized applications.

**Context**: Industry standard for managing containerized applications in production environments.

**Related Terms**: Container, Docker, Pod, Service, Deployment

**Example**: A Kubernetes Deployment defining 3 replicas of a web application, with automatic scaling based on CPU usage.

---

## Load Balancer

**Definition**: A device or software that distributes network traffic across multiple servers to ensure no single server bears too much demand.

**Context**: Essential for high-availability and scalable web applications.

**Related Terms**: Horizontal Scaling, Reverse Proxy, Health Check

**Example**: An AWS Application Load Balancer distributing incoming HTTP traffic across multiple EC2 instances running the same application.

---

## Microservices

**Definition**: An architectural style structuring an application as a collection of small, autonomous services modeled around business capabilities.

**Context**: Used for building large, complex applications that need to scale independently and be developed by multiple teams.

**Related Terms**: Domain-Driven Design, API Gateway, Service Mesh, Container

**Example**: An e-commerce platform with separate services for user management, product catalog, shopping cart, and payment processing.

---

## Message Queue

**Definition**: A communication mechanism that allows processes to exchange messages asynchronously, decoupling message producers from consumers.

**Context**: Used for reliable asynchronous communication between services or components.

**Related Terms**: Pub/Sub, Event-Driven, RabbitMQ, Kafka

**Example**: A message queue where order events are published by the checkout service and consumed by inventory and notification services independently.
