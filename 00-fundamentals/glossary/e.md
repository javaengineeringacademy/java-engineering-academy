# Glossary - E

## ELK Stack

**Definition**: A collection of three open-source tools: Elasticsearch, Logstash, and Kibana, used for log aggregation, search, and visualization.

**Context**: Widely used for centralized logging, monitoring, and analytics in distributed systems.

**Related Terms**: Elasticsearch, Logstash, Kibana, Observability

**Example**: Using Logstash to collect logs from multiple servers, Elasticsearch to index and search them, and Kibana to create dashboards for monitoring.

---

## Event

**Definition**: A significant change in state or an occurrence that is recorded and can trigger actions in other parts of a system.

**Context**: Central concept in event-driven architectures, enabling loose coupling between components.

**Related Terms**: Event Sourcing, Message Queue, Pub/Sub

**Example**: An `OrderPlaced` event emitted when a customer completes a purchase, triggering inventory updates and notification services.

---

## Event-Driven Architecture

**Definition**: A software design pattern where the flow of the program is determined by events, which can be user actions, sensor outputs, or messages from other programs.

**Context**: Used for building loosely coupled, scalable systems that respond to changes in real-time.

**Related Terms**: Event Sourcing, CQRS, Pub/Sub, Message Broker

**Example**: A microservices system where services communicate by publishing and subscribing to events through a message broker like Kafka.

---

## Event Sourcing

**Definition**: A pattern where state changes are stored as an immutable sequence of events, allowing reconstruction of current state by replaying events.

**Context**: Used for audit trails, temporal queries, and systems requiring complete history of state changes.

**Related Terms**: CQRS, Event Store, Aggregate

**Example**: A banking system storing every transaction as an event, allowing reconstruction of account balance at any point in time.

---

## Eventual Consistency

**Definition**: A consistency model where, given enough time without new updates, all replicas in a distributed system will converge to the same value.

**Context**: A trade-off in distributed systems prioritizing availability over immediate consistency.

**Related Terms**: CAP Theorem, Strong Consistency, Replication

**Example**: DNS propagation where a domain change may take time to propagate to all servers worldwide, but eventually all servers return the same IP.

---

## Expression Language (SpEL)

**Definition**: A powerful expression language supported by the Spring Framework for runtime evaluation and manipulation of object graphs.

**Context**: Used in Spring configuration, security expressions, and dynamic value resolution.

**Related Terms**: Spring Framework, Dynamic Configuration, Security Expressions

**Example**: Using `@Value('${app.feature.enabled}')` to inject configuration values or `@PreAuthorize('#user.id == authentication.principal.id')` for security.

---

## EJB (Enterprise JavaBeans)

**Definition**: A managed server-side component model for developing and deploying enterprise applications in Java EE.

**Context**: Historically used for enterprise business logic, though largely replaced by lighter frameworks like Spring.

**Related Terms**: Spring Framework, IoC Container, Session Bean

**Example**: A Stateless Session Bean handling order processing business logic in a Java EE application server.
