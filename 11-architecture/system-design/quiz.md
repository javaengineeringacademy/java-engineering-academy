# System Design Quiz

## Question 1
What does the CAP theorem state about distributed systems?
- A) A system can have Consistency, Availability, and Partition tolerance simultaneously
- B) A distributed system can only guarantee two out of three: Consistency, Availability, and Partition tolerance
- C) Partition tolerance is always optional
- D) Consistency is less important than availability

**Answer: B**
**Explanation:** The CAP theorem states that a distributed system can provide at most two of three guarantees: Consistency (all nodes see the same data), Availability (every request gets a response), and Partition Tolerance (system works despite network failures).

## Question 2
What is the purpose of a load balancer in a system architecture?
- A) To encrypt data
- B) To distribute incoming network traffic across multiple servers to ensure no single server is overwhelmed
- C) To store user data
- D) To compile source code

**Answer: B**
**Explanation:** A load balancer distributes client requests across multiple backend servers, improving availability, reliability, and scalability by preventing any single server from becoming a bottleneck.

## Question 3
What is the difference between horizontal and vertical scaling?
- A) Horizontal adds more power to existing servers, vertical adds more servers
- B) Horizontal adds more servers, vertical adds more power (CPU/RAM) to existing servers
- C) They are the same thing
- D) Horizontal is only for databases

**Answer: B**
**Explanation:** Horizontal scaling (scaling out) adds more machines to handle load. Vertical scaling (scaling up) increases resources (CPU, RAM, disk) on existing machines. Horizontal provides better fault tolerance.

## Question 4
What is the primary benefit of using a CDN (Content Delivery Network)?
- A) It provides database hosting
- B) It caches static content at edge locations closer to users, reducing latency and server load
- C) It encrypts all traffic
- D) It manages user authentication

**Answer: B**
**Explanation:** A CDN caches static assets (images, CSS, JS) at geographically distributed edge servers. Users get content from the nearest location, reducing latency and offloading traffic from the origin server.

## Question 5
What is eventual consistency?
- A) Data is always immediately consistent across all nodes
- B) Data will become consistent across all nodes after a period of time, but reads may return stale data temporarily
- C) Consistency is never achieved
- D) Only write operations are eventually consistent

**Answer: B**
**Explanation:** Eventual consistency is a consistency model where, after a write operation, all replicas will eventually have the same data. There may be a window where different nodes return different values, but the system converges to consistency.