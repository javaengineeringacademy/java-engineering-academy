# System Design Interview Quiz

## Question 1
What are the key steps to approach a system design interview question?
- A) Jump straight into designing the database
- B) Clarify requirements, define scope, design high-level architecture, detail components, discuss trade-offs, and estimate scale
- C) Only draw diagrams
- D) List all possible technologies

**Answer: B**
**Explanation:** A structured approach shows systematic thinking: clarify functional/non-functional requirements, estimate scale, design the high-level architecture, deep-dive into components, and discuss trade-offs and alternatives.

## Question 2
What is the purpose of a reverse proxy in a web application architecture?
- A) It proxies requests from the client to the database
- B) It sits between clients and backend servers, handling SSL termination, caching, load balancing, and request routing
- C) It replaces the application server
- D) It only handles static files

**Answer: B**
**Explanation:** A reverse proxy (like Nginx) handles cross-cutting concerns: SSL termination, compression, caching, rate limiting, and load balancing. It hides backend server details and provides a single entry point.

## Question 3
Why would you choose a NoSQL database over a relational database?
- A) NoSQL is always better
- B) For flexible schemas, horizontal scalability, high write throughput, or when data relationships are not complex
- C) When you need ACID transactions
- D) When you have very little data

**Answer: B**
**Explanation:** NoSQL databases (like MongoDB, Cassandra) excel when you need flexible schemas, horizontal scaling, high write throughput, or eventual consistency. Relational databases are better for complex relationships and ACID transactions.

## Question 4
What is caching and where should you place a cache in a web architecture?
- A) Caching is only for databases
- B) Storing frequently accessed data in fast storage; common locations include CDN, application-level, database query cache, and session store
- C) Caching is only for static files
- D) Caching should only be at the database level

**Answer: B**
**Explanation:** Caching can be applied at multiple levels: CDN (static assets), reverse proxy (full responses), application (computed results), database (query results), and browser. Each level serves different purposes and has different invalidation strategies.

## Question 5
What is the trade-off between consistency and availability in a distributed system?
- A) There is no trade-off
- B) Strong consistency requires synchronous replication (higher latency, lower availability), while high availability allows asynchronous replication (eventual consistency)
- C) Availability is always more important
- D) Consistency only matters for writes

**Answer: B**
**Explanation:** To achieve strong consistency, all nodes must agree before responding, which increases latency and reduces availability during partitions. High availability allows the system to respond even if some nodes are inconsistent, accepting eventual consistency.