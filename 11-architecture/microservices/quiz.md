# Microservices Quiz

## Question 1
What is the main benefit of using an API Gateway in a microservices architecture?
- A) It replaces all microservices
- B) It provides a single entry point that handles routing, authentication, rate limiting, and load balancing
- C) It eliminates the need for service discovery
- D) It only handles HTTP requests

**Answer: B**
**Explanation:** An API Gateway acts as a single entry point for all client requests, providing cross-cutting concerns like authentication, rate limiting, SSL termination, and request routing to appropriate microservices.

## Question 2
What is the Circuit Breaker pattern used for?
- A) Breaking circuits during power outages
- B) Preventing cascading failures by stopping calls to a failing service and providing fallback behavior
- C) Encrypting service communication
- D) Load balancing between services

**Answer: B**
**Explanation:** The Circuit Breaker pattern monitors for failures and "opens" the circuit when failures exceed a threshold, preventing further calls and returning fallback responses. This prevents cascading failures across services.

## Question 3
What is the Saga pattern used for?
- A) Database replication
- B) Managing distributed transactions by breaking them into a sequence of local transactions with compensating actions
- C) Logging across services
- D) Service authentication

**Answer: B**
**Explanation:** The Saga pattern coordinates distributed transactions across multiple services. Each service performs its local transaction and publishes events. If a step fails, compensating transactions undo the previous steps.

## Question 4
What is the difference between synchronous and asynchronous communication in microservices?
- A) Synchronous is faster
- B) Synchronous waits for a response (e.g., REST), asynchronous doesn't wait (e.g., message queues)
- C) Asynchronous requires more memory
- D) Synchronous only works with databases

**Answer: B**
**Explanation:** Synchronous communication (like REST) requires the caller to wait for a response. Asynchronous communication (like message queues or event streams) allows the caller to continue processing without waiting.

## Question 5
What is CQRS (Command Query Responsibility Segregation)?
- A) A security pattern for authentication
- B) Separating read and write operations into different models or services
- C) A caching strategy
- D) A database optimization technique

**Answer: B**
**Explanation:** CQRS separates the read model (Query) from the write model (Command). This allows independent optimization, scaling, and technology choices for read-heavy vs write-heavy operations.