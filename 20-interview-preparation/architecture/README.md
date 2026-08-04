# Architecture Interview Guide

Master architecture interviews with systematic approaches and real-world examples.

## Overview

Architecture interviews assess your ability to design scalable, maintainable, and reliable systems. They test both technical depth and breadth.

## Interview Structure

### Phase 1: Requirements Gathering (5-10 minutes)
- Understand functional requirements
- Define non-functional requirements
- Identify constraints and assumptions

### Phase 2: High-Level Design (10-15 minutes)
- Draw major components
- Define APIs and data models
- Discuss trade-offs

### Phase 3: Detailed Design (20-25 minutes)
- Deep dive into components
- Database schema
- API design
- Algorithms and data structures

### Phase 4: Scale and Optimize (10-15 minutes)
- Identify bottlenecks
- Discuss scaling strategies
- Address failure modes

### Phase 5: Wrap Up (5 minutes)
- Summarize design
- Discuss alternatives
- Address trade-offs

## Common Architecture Patterns

### 1. Microservices Architecture
**When to Use:**
- Large teams
- Complex domains
- Need for independent scaling
- Technology diversity

**Components:**
- Service discovery
- API gateway
- Load balancing
- Circuit breakers
- Event-driven communication

```
┌─────────┐  ┌─────────┐  ┌─────────┐
│Service A│  │Service B│  │Service C│
└────┬────┘  └────┬────┘  └────┬────┘
     │            │            │
┌────▼────┐  ┌────▼────┐  ┌────▼────┐
│Database │  │Database │  │Database │
└─────────┘  └─────────┘  └─────────┘
```

### 2. Event-Driven Architecture
**When to Use:**
- Real-time systems
- IoT applications
- Complex workflows
- Audit requirements

**Components:**
- Event producers
- Event bus (Kafka)
- Event consumers
- Event store

### 3. CQRS (Command Query Responsibility Segregation)
**When to Use:**
- Read-heavy systems
- Complex domains
- Performance requirements
- Audit needs

**Components:**
- Command model
- Query model
- Event store
- Read database

### 4. Strangler Fig Pattern
**When to Use:**
- Legacy system replacement
- Large codebases
- Risk-averse environments

**Components:**
- Proxy/facade
- New system
- Legacy system
- Migration logic

## Database Design

### SQL vs. NoSQL
| Factor | SQL | NoSQL |
|--------|-----|-------|
| Schema | Fixed | Dynamic |
| Consistency | Strong | Eventual |
| Scaling | Vertical | Horizontal |
| Use Case | Complex queries | Simple queries |

### Sharding Strategies
- **Hash-based**: Even distribution
- **Range-based**: Geographic or time-based
- **Directory-based**: Lookup table

### Replication
- **Master-Slave**: Read scaling
- **Master-Master**: Write scaling
- **Multi-region**: Global distribution

## API Design

### RESTful APIs
```
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### GraphQL
```graphql
type User {
  id: ID!
  name: String!
  email: String!
  posts: [Post!]
}

type Query {
  user(id: ID!): User
  users: [User!]
}
```

### gRPC
```protobuf
service UserService {
  rpc GetUser (GetUserRequest) returns (User);
  rpc CreateUser (CreateUserRequest) returns (User);
}
```

## Scaling Strategies

### Horizontal Scaling
- Add more machines
- Stateless services
- Load balancing
- Auto-scaling

### Vertical Scaling
- Upgrade hardware
- More CPU, memory, storage
- Limited by hardware

### Database Scaling
- Read replicas
- Sharding
- Caching
- Denormalization

## Reliability Patterns

### Circuit Breaker
- Prevent cascading failures
- Fallback mechanisms
- Graceful degradation

### Retry with Backoff
- Handle transient failures
- Exponential backoff
- Prevent thundering herd

### Bulkhead
- Isolate components
- Limit concurrent requests
- Protect critical paths

## Observability

### Metrics
- Latency
- Throughput
- Error rates
- Saturation

### Logging
- Structured logging
- Centralized logging
- Log levels

### Tracing
- Distributed tracing
- Request flow
- Latency analysis

### Alerting
- Threshold-based
- Anomaly detection
- PagerDuty integration

## Common Architecture Questions

### 1. Design a URL Shortener
**Key Considerations:**
- Hash generation
- Database design
- Caching strategy
- Analytics

### 2. Design a Chat System
**Key Considerations:**
- Real-time communication
- Message persistence
- Group messaging
- Online status

### 3. Design a News Feed
**Key Considerations:**
- Fan-out on write vs. read
- Timeline generation
- Real-time updates
- Content ranking

### 4. Design a Payment System
**Key Considerations:**
- Idempotency
- Exactly-once processing
- Audit trail
- Compliance

### 5. Design a Search Engine
**Key Considerations:**
- Indexing
- Ranking algorithms
- Auto-complete
- Personalization

## Interview Tips

### Do's
- Ask clarifying questions
- Discuss trade-offs
- Consider scalability
- Address failure modes
- Use diagrams
- Think out loud

### Don'ts
- Jump to implementation
- Ignore non-functional requirements
- Over-engineer solutions
- Forget about monitoring
- Ignore security
- Be dogmatic about technology

## Practice Problems

### Easy
- Design a URL shortener
- Design a rate limiter
- Design a key-value store

### Medium
- Design Twitter feed
- Design Instagram
- Design Uber

### Hard
- Design Google Search
- Design WhatsApp
- Design Netflix

## Resources

### Books
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "System Design Interview" by Alex Xu
- "Software Architecture: The Hard Parts" by Neal Ford

### Online
- System Design Interview (YouTube)
- High Scalability (blog)
- Architecture Notes (blog)

## Study Plan

### Week 1-2: Fundamentals
- Study common patterns
- Practice database design
- Learn API design

### Week 3-4: System Design
- Practice design problems
- Study scaling strategies
- Learn reliability patterns

### Week 5-6: Mock Interviews
- Practice with peers
- Time yourself
- Get feedback

### Week 7-8: Advanced Topics
- Study real-world architectures
- Learn about observability
- Prepare for follow-up questions