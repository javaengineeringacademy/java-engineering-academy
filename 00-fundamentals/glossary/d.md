# Glossary - D

## DDD (Domain-Driven Design)

**Definition**: A software design approach focusing on creating a rich model that reflects the business domain, with ubiquitous language shared between developers and domain experts.

**Context**: Used for complex business domains where understanding the domain is crucial for software success.

**Related Terms**: Bounded Context, Aggregate, Ubiquitous Language, Value Object

**Example**: In an e-commerce system, modeling `Order` as an aggregate with `OrderItem` entities, using terms like `checkout` and `fulfillment` that both developers and business stakeholders understand.

---

## DI (Dependency Injection)

**Definition**: A design pattern where an object receives its dependencies from an external source rather than creating them internally.

**Context**: Promotes loose coupling and testability by making dependencies explicit and injectable.

**Related Terms**: IoC Container, Service Locator, Spring Framework

**Example**: A controller receiving a `UserService` instance through constructor injection instead of instantiating it directly.

---

## Docker

**Definition**: A platform for developing, shipping, and running applications in lightweight, portable containers.

**Context**: Standard tool for packaging applications with all dependencies for consistent deployment across environments.

**Related Terms**: Container, Image, Dockerfile, Kubernetes

**Example**: A Dockerfile defining how to build an image containing a Python application, its dependencies, and runtime configuration.

---

## Docker Compose

**Definition**: A tool for defining and running multi-container Docker applications using a YAML configuration file.

**Context**: Simplifies development environments by defining all application services in a single file.

**Related Terms**: Docker, Container, Orchestration, Stack

**Example**: A `docker-compose.yml` file defining a web service, database, and cache that start together with a single command.

---

## Dockerfile

**Definition**: A text file containing instructions to build a Docker image, specifying the base image, dependencies, and configuration.

**Context**: Enables reproducible image builds and version control of container configurations.

**Related Terms**: Docker Image, Build Context, Multi-stage Build

**Example**: A Dockerfile starting FROM node:18, copying package.json, running npm install, and defining the start command.

---

## DTO (Data Transfer Object)

**Definition**: An object that carries data between processes or layers, typically containing only data with no business logic.

**Context**: Used to reduce network calls by sending multiple data items in a single object, or to decouple internal models from external APIs.

**Related Terms**: Value Object, Mapper, API Response

**Example**: A `UserDTO` containing user profile information returned by an API endpoint, separate from the internal `User` entity.

---

## Daemon

**Definition**: A background process that runs continuously without direct user interaction, handling periodic tasks or system services.

**Context**: Used for system services, background workers, and scheduled tasks in operating systems and applications.

**Related Terms**: Service, Background Process, Cron Job

**Example**: A logging daemon that continuously collects and forwards application logs to a central logging system.

---

## Data Sharding

**Definition**: A database partitioning strategy that distributes data across multiple database instances to improve scalability and performance.

**Context**: Used when a single database cannot handle the volume of data or traffic, requiring horizontal partitioning.

**Related Terms**: Partitioning, Horizontal Scaling, Database Cluster

**Example**: Sharding a user database by user ID ranges, with users 1-1000000 on shard 1 and 1000001-2000000 on shard 2.

---

## Database Index

**Definition**: A data structure that improves the speed of data retrieval operations on a database table at the cost of additional storage space and write overhead.

**Context**: Essential for query optimization in databases with large datasets or frequent queries.

**Related Terms**: B-Tree, Query Optimization, Primary Key

**Example**: Creating an index on the `email` column in a users table to speed up login queries that search by email.

---

## Dead Letter Queue

**Definition**: A message queue that receives messages that could not be processed successfully, allowing for later investigation and reprocessing.

**Context**: Used in message-driven systems to handle failed messages without blocking the main processing queue.

**Related Terms**: Message Queue, Retry, Error Handling

**Example**: A DLQ receiving messages that failed to process after 3 retry attempts, allowing administrators to investigate and manually reprocess them.
