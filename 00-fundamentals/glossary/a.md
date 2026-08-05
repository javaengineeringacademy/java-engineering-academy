# Glossary - A

## Abstraction

**Definition**: The process of hiding implementation details while exposing only the essential features of an object or system.

**Context**: Fundamental concept in object-oriented programming and software design. Used to manage complexity by focusing on what an object does rather than how it does it.

**Related Terms**: Encapsulation, Interface, Polymorphism

**Example**: A `DatabaseConnection` class abstracts the details of connecting to MySQL, PostgreSQL, or MongoDB, providing a统一 `connect()` method regardless of the underlying implementation.

---

## Abstract Class

**Definition**: A class that cannot be instantiated directly and may contain both abstract methods (without implementation) and concrete methods (with implementation).

**Context**: Used in object-oriented design to provide a base template for derived classes while enforcing certain method implementations.

**Related Terms**: Interface, Inheritance, Polymorphism

**Example**: An abstract `Shape` class defines `calculateArea()` as abstract, requiring concrete implementations in `Circle` and `Rectangle`.

---

## Adapter Pattern

**Definition**: A structural design pattern that allows objects with incompatible interfaces to work together by wrapping one interface in another.

**Context**: Used when integrating third-party libraries or legacy systems with different APIs than your application expects.

**Related Terms**: Decorator, Facade, Proxy

**Example**: An `Adapter` class wraps a legacy payment processor to implement the modern `PaymentGateway` interface used by your application.

---

## ADO.NET

**Definition**: A data access technology from Microsoft that provides a bridge between front-end applications and data sources.

**Context**: Used in .NET applications for database connectivity, including SQL Server, Oracle, and other databases.

**Related Terms**: Entity Framework, ORM, Connection Pooling

**Example**: Using `SqlConnection` and `SqlCommand` to execute a query against a SQL Server database.

---

## AOP (Aspect-Oriented Programming)

**Definition**: A programming approach that separates cross-cutting concerns (logging, security, transactions) from business logic.

**Context**: Used to reduce code duplication and improve modularity by extracting concerns that span multiple modules.

**Related Terms**: Aspects, Pointcuts, Advice, Spring AOP

**Example**: Defining a logging aspect that automatically logs method entry and exit for all service layer methods without modifying each method.

---

## API (Application Programming Interface)

**Definition**: A set of rules and protocols that allows different software applications to communicate with each other.

**Context**: Defines how software components should interact, including data formats, authentication methods, and available operations.

**Related Terms**: REST API, GraphQL, gRPC, API Gateway

**Example**: A REST API endpoint `GET /users/{id}` that returns user data in JSON format when provided with a valid user ID.

---

## API Gateway

**Definition**: A server that acts as a single entry point for all client requests, routing them to appropriate microservices and handling cross-cutting concerns.

**Context**: Used in microservices architecture to provide unified API access, rate limiting, authentication, and request aggregation.

**Related Terms**: Microservices, Load Balancer, Reverse Proxy

**Example**: An API Gateway handles authentication, rate limiting, and routes requests to the user service, order service, or inventory service based on the URL path.

---

## Aggregation

**Definition**: A type of association where one object contains references to other objects, but the contained objects can exist independently.

**Context**: Used in object modeling to represent "has-a" relationships where the contained object has its own lifecycle.

**Related Terms**: Composition, Association, Dependency

**Example**: A `Department` class has a list of `Employee` objects; employees can exist independently of any specific department.

---

## Asynchronous Processing

**Definition**: A programming approach where operations are initiated and continue in the background without blocking the main thread.

**Context**: Used to improve application responsiveness and handle long-running operations without freezing the user interface or blocking other requests.

**Related Terms**: Callbacks, Promises, Async/Await, Message Queues

**Example**: Using `async/await` in JavaScript to make an API call without blocking the UI, allowing the page to remain interactive while data loads.
