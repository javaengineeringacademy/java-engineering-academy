# Glossary - B

## Bean (Java Bean)

**Definition**: A reusable software component that follows specific naming conventions for properties, with getters and setters, and implements Serializable.

**Context**: Used in Java enterprise development, particularly within the Spring Framework for dependency injection and component management.

**Related Terms**: Spring Bean, Dependency Injection, IoC Container

**Example**: A `UserService` bean managed by Spring, injected into controllers and other services via `@Autowired`.

---

## Bean Definition

**Definition**: The metadata used by a Spring container to create and manage beans, including class type, scope, dependencies, and initialization methods.

**Context**: Defines how beans are instantiated, configured, and assembled within the Spring IoC container.

**Related Terms**: BeanFactory, ApplicationContext, Bean Scope

**Example**: A bean definition specifying that `OrderRepository` depends on `DataSource` and should be created as a singleton instance.

---

## Broker Pattern

**Definition**: A design pattern that mediates communication between clients and services, handling service location, activation, and request routing.

**Context**: Common in distributed systems and message-oriented middleware for decoupling service consumers from providers.

**Related Terms**: Service Locator, Message Broker, Load Balancer

**Example**: A message broker like RabbitMQ that receives messages from producers and routes them to appropriate consumers based on routing rules.

---

## Bulkhead Pattern

**Definition**: A resilience pattern that isolates elements of an application into pools so that if one fails, the others continue to function.

**Context**: Used in microservices to prevent cascading failures by limiting the impact of a failing service on the overall system.

**Related Terms**: Circuit Breaker, Rate Limiting, Resource Isolation

**Example**: Configuring separate thread pools for different external services so a slow payment service cannot exhaust resources needed for the user service.

---

## Builder Pattern

**Definition**: A creational design pattern that separates the construction of a complex object from its representation, allowing step-by-step construction.

**Context**: Used when constructing objects that require many configuration parameters or when construction involves multiple steps.

**Related Terms**: Factory Pattern, Prototype Pattern, Fluent Interface

**Example**: Building an HTTP request with `.setUrl()`, `.setMethod()`, `.addHeader()`, and `.build()` methods instead of a constructor with dozens of parameters.

---

## Bus Topology

**Definition**: A network configuration where all devices share a single communication line, with data transmitted to all devices but processed only by the intended recipient.

**Context**: Used in legacy network architectures and some industrial communication systems.

**Related Terms**: Network Topology, Star Topology, Ring Topology

**Example**: An Ethernet bus network where computers connect to a single coaxial cable, common in early local area networks.

---

## Byzantine Fault Tolerance

**Definition**: The ability of a distributed system to function correctly even when some nodes fail in arbitrary ways, including sending incorrect information.

**Context**: Critical for financial systems and blockchain networks where nodes may behave maliciously or unpredictably.

**Related Terms**: Consensus Algorithm, Fault Tolerance, Distributed Systems

**Example**: Bitcoin's proof-of-work consensus mechanism provides Byzantine fault tolerance by ensuring honest nodes can agree on the blockchain state despite malicious actors.
