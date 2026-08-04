# System Design Interview Framework

Master the art of designing distributed systems at scale.

## Overview

System design interviews test your ability to design complex systems, discuss trade-offs, and communicate technical decisions.

## Interview Structure

### Phase 1: Requirements Gathering (5-10 min)

**Functional Requirements:**
- What should the system do?
- Who are the users?
- What are the main features?

**Non-Functional Requirements:**
- Scalability (users, data, traffic)
- Latency (response time)
- Availability (uptime)
- Consistency (data accuracy)
- Durability (data persistence)

**Constraints:**
- Budget
- Time
- Team size
- Existing infrastructure

**Example: Design Twitter**
```
Functional:
- Post tweets (text, images, videos)
- Follow users
- View timeline (home, user)
- Search tweets
- Like/Retweet

Non-Functional:
- 300M daily active users
- 500M tweets/day
- 99.99% availability
- < 200ms latency
- Data retention: 5 years
```

### Phase 2: High-Level Design (10-15 min)

**Components:**
- API Gateway
- Application servers
- Databases
- Caches
- Message queues
- CDNs

**API Design:**
```
POST /api/tweets
GET /api/timeline/{userId}
GET /api/search?q={query}
POST /api/follow/{userId}
POST /api/like/{tweetId}
```

**Data Model:**
```sql
Users: {id, name, email, created_at}
Tweets: {id, user_id, content, created_at}
Follows: {follower_id, following_id}
Likes: {user_id, tweet_id}
```

**Architecture Diagram:**
```
Client → API Gateway → App Servers → Database
                     → Cache (Redis)
                     → Message Queue (Kafka)
                     → CDN (Static assets)
```

### Phase 3: Detailed Design (20-25 min)

**Database Design:**
- Schema design
- Indexing strategy
- Sharding approach
- Replication

**Caching Strategy:**
- What to cache
- Cache invalidation
- TTL strategy
- Cache hierarchy

**Message Queue:**
- Event-driven patterns
- Async processing
- Retry mechanisms
- Dead letter queues

**Load Balancing:**
- Algorithm (round-robin, least connections)
- Health checks
- Session affinity
- Geographic routing

### Phase 4: Scale and Optimize (10-15 min)

**Horizontal Scaling:**
- Stateless services
- Database sharding
- Read replicas
- CDN optimization

**Vertical Scaling:**
- Hardware upgrades
- Performance tuning
- Memory optimization

**Bottleneck Analysis:**
- Identify hotspots
- Profile performance
- Optimize queries
- Reduce latency

**Failure Handling:**
- Circuit breakers
- Retry mechanisms
- Fallbacks
- Graceful degradation

### Phase 5: Wrap Up (5 min)

**Summary:**
- Recap key decisions
- Discuss alternatives
- Address trade-offs

**Follow-up Questions:**
- How would you monitor this system?
- How would you handle disaster recovery?
- What are the security considerations?

## Common System Design Patterns

### 1. CQRS (Command Query Responsibility Segregation)
- Separate read and write models
- Optimize for each use case
- Event sourcing for consistency

### 2. Event Sourcing
- Store events, not state
- Rebuild state from events
- Audit trail

### 3. Saga Pattern
- Distributed transactions
- Compensating transactions
- Event-driven coordination

### 4. API Gateway
- Single entry point
- Authentication/Authorization
- Rate limiting
- Request routing

### 5. Service Mesh
- Sidecar proxies
- Service discovery
- Load balancing
- Observability

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

### Cache Scaling
- In-memory caching
- Distributed caching
- Cache hierarchy
- Cache invalidation

## Consistency Models

### Strong Consistency
- All reads return latest write
- Linearizability
- Use cases: Financial transactions

### Eventual Consistency
- Reads may return stale data
- High availability
- Use cases: Social media feeds

### Causal Consistency
- Causal order preserved
- Middle ground
- Use cases: Collaborative editing

## Availability Patterns

### Redundancy
- Multiple instances
- Geographic distribution
- Data replication

### Failover
- Automatic failover
- Manual failover
- Disaster recovery

### Circuit Breaking
- Prevent cascading failures
- Fallback mechanisms
- Graceful degradation

## Monitoring and Observability

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

## Practice Problems

### Easy
- Design URL shortener
- Design Rate Limiter
- Design Key-Value Store

### Medium
- Design Twitter Feed
- Design Instagram
- Design Uber

### Hard
- Design Google Search
- Design WhatsApp
- Design Netflix
