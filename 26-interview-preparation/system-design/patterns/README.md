# System Design Patterns

Common patterns for designing scalable distributed systems.

## Overview

Design patterns are reusable solutions to common problems in software design. In system design interviews, knowing these patterns demonstrates architectural maturity.

## Architectural Patterns

### 1. Microservices Architecture

**Description:**
- Decompose system into small, independent services
- Each service owns its data
- Services communicate via APIs

**Benefits:**
- Independent deployment
- Technology flexibility
- Team autonomy
- Fault isolation

**Drawbacks:**
- Distributed system complexity
- Network latency
- Data consistency challenges
- Operational overhead

**When to Use:**
- Large teams
- Complex domains
- Need for independent scaling
- Technology diversity

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

**Description:**
- Services communicate via events
- Asynchronous message passing
- Loose coupling

**Benefits:**
- Scalability
- Flexibility
- Real-time processing
- Audit trail

**Drawbacks:**
- Eventual consistency
- Complexity
- Debugging difficulty
- Schema evolution

**When to Use:**
- Real-time systems
- IoT applications
- Complex workflows
- Audit requirements

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│Service A│────▶│  Event  │────▶│Service B│
└─────────┘     │  Bus    │     └─────────┘
                │ (Kafka) │
┌─────────┐     │         │     ┌─────────┐
│Service C│◀────│         │◀────│Service D│
└─────────┘     └─────────┘     └─────────┘
```

### 3. CQRS (Command Query Responsibility Segregation)

**Description:**
- Separate read and write models
- Optimize each for its purpose
- Often combined with event sourcing

**Benefits:**
- Read/write optimization
- Scalability
- Flexibility
- Audit trail

**Drawbacks:**
- Complexity
- Eventual consistency
- More infrastructure
- Learning curve

**When to Use:**
- Read-heavy systems
- Complex domains
- Performance requirements
- Audit needs

```
┌─────────┐     ┌─────────┐
│Commands │────▶│ Write DB│
└─────────┘     └─────────┘
                    │
                ┌───▼───┐
                │ Events │
                └───┬───┘
                    │
┌─────────┐     ┌───▼───┐
│ Queries │◀────│Read DB│
└─────────┘     └───────┘
```

### 4. API Gateway Pattern

**Description:**
- Single entry point for all clients
- Routes requests to appropriate services
- Handles cross-cutting concerns

**Benefits:**
- Simplified client code
- Centralized authentication
- Rate limiting
- Request transformation

**Drawbacks:**
- Single point of failure
- Performance bottleneck
- Complexity
- Versioning challenges

**When to Use:**
- Multiple client types
- Microservices architecture
- Need for centralized control
- API management

```
┌─────────┐
│ Client  │
└────┬────┘
     │
┌────▼────┐
│   API   │
│ Gateway │
└────┬────┘
     │
┌────┼────┐
│    │    │
▼    ▼    ▼
┌──┐┌──┐┌──┐
│S1││S2││S3│
└──┘└──┘└──┘
```

### 5. Service Mesh Pattern

**Description:**
- Dedicated infrastructure layer for service communication
- Sidecar proxies handle networking concerns
- Transparent to application code

**Benefits:**
- Observability
- Security (mTLS)
- Load balancing
- Circuit breaking

**Drawbacks:**
- Complexity
- Resource overhead
- Latency
- Debugging difficulty

**When to Use:**
- Kubernetes environments
- Microservices at scale
- Need for advanced networking
- Security requirements

```
┌─────────────────────────────────┐
│           Service A             │
│  ┌───────────────────────────┐  │
│  │       Sidecar Proxy       │  │
│  └─────────────┬─────────────┘  │
└────────────────┼────────────────┘
                 │
┌────────────────┼────────────────┐
│  ┌─────────────▼─────────────┐  │
│  │       Sidecar Proxy       │  │
│  └───────────────────────────┘  │
│           Service B             │
└─────────────────────────────────┘
```

## Data Patterns

### 6. Sharding

**Description:**
- Partition data across multiple databases
- Each shard handles a subset of data
- Enables horizontal scaling

**Benefits:**
- Horizontal scaling
- Improved performance
- Data locality
- Parallel processing

**Drawbacks:**
- Cross-shard queries
- Rebalancing complexity
- Increased operational overhead
- Data consistency

**When to Use:**
- Large datasets
- High write throughput
- Single database bottleneck
- Geographic distribution

```
┌─────────┐
│ Router  │
└────┬────┘
     │
┌────┼────┐
│    │    │
▼    ▼    ▼
┌──┐┌──┐┌──┐
│S1││S2││S3│
└──┘└──┘└──┘
```

### 7. Caching

**Description:**
- Store frequently accessed data in fast storage
- Reduce database load
- Improve response time

**Benefits:**
- Reduced latency
- Lower database load
- Cost savings
- Better user experience

**Drawbacks:**
- Cache invalidation complexity
- Consistency issues
- Memory costs
- Stale data

**When to Use:**
- Read-heavy workloads
- Expensive queries
- Static content
- Session data

```
┌─────────┐     ┌─────────┐
│ Client  │────▶│  Cache  │
└─────────┘     │ (Redis) │
                └────┬────┘
                     │ (miss)
                ┌────▼────┐
                │Database │
                └─────────┘
```

### 8. Message Queue

**Description:**
- Asynchronous communication between services
- Decouples producers and consumers
- Enables reliable message delivery

**Benefits:**
- Scalability
- Reliability
- Decoupling
- Buffering

**Drawbacks:**
- Complexity
- Eventual consistency
- Message ordering
- Dead letter handling

**When to Use:**
- Async processing
- Workload leveling
- Cross-service communication
- Event-driven architectures

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│Producer │────▶│  Queue  │────▶│Consumer │
└─────────┘     │ (Kafka) │     └─────────┘
                └─────────┘
```

## Resilience Patterns

### 9. Circuit Breaker

**Description:**
- Prevent cascading failures
- Stop calling failing services
- Enable recovery

**Benefits:**
- Fault isolation
- Graceful degradation
- System stability
- Fast failure

**Drawbacks:**
- False positives
- Complex state management
- Recovery logic
- Monitoring requirements

**When to Use:**
- External service calls
- Network dependencies
- Unreliable services
- Critical paths

```
┌─────────┐     ┌─────────┐
│ Client  │────▶│Circuit  │
└─────────┘     │Breaker  │
                └────┬────┘
                     │
                ┌────▼────┐
                │Service B│
                └─────────┘
```

### 10. Retry with Backoff

**Description:**
- Automatically retry failed operations
- Exponential backoff delay
- Prevent thundering herd

**Benefits:**
- Handles transient failures
- Reduces load on failing services
- Improves reliability
- Automatic recovery

**Drawbacks:**
- Increased latency
- Resource consumption
- Complex logic
- Potential for infinite loops

**When to Use:**
- Network calls
- External services
- Transient failures
- Critical operations

### 11. Bulkhead

**Description:**
- Isolate components to prevent failure propagation
- Limit concurrent requests
- Protect critical paths

**Benefits:**
- Fault isolation
- Resource protection
- Priority handling
- Stability

**Drawbacks:**
- Resource underutilization
- Complex configuration
- Latency
- Debugging difficulty

**When to Use:**
- Critical services
- Resource-intensive operations
- Multiple dependencies
- Priority-based access

### 12. Timeout

**Description:**
- Limit wait time for operations
- Prevent resource exhaustion
- Enable fast failure

**Benefits:**
- Prevents hanging
- Resource protection
- Predictable behavior
- System stability

**Drawbacks:**
- False failures
- Complex configuration
- Cascading timeouts
- Debugging difficulty

**When to Use:**
- External calls
- Network operations
- Critical paths
- Resource-constrained systems

## Organizational Patterns

### 13. Strangler Fig

**Description:**
- Gradually replace legacy system
- New features built in new system
- Old features migrated over time

**Benefits:**
- Low risk migration
- Continuous delivery
- Gradual learning
- Business continuity

**Drawbacks:**
- Extended timeline
- Dual systems
- Complexity
- Testing burden

**When to Use:**
- Legacy system replacement
- Large codebases
- Risk-averse environments
- Long-term projects

```
┌─────────────────────────────────┐
│         Strangler Fig           │
│                                 │
│  ┌─────────┐    ┌─────────┐    │
│  │ Legacy  │    │  New    │    │
│  │ System  │    │ System  │    │
│  └─────────┘    └─────────┘    │
│       │              │         │
│       └──────┬───────┘         │
│              │                 │
│         ┌────▼────┐            │
│         │  Proxy  │            │
│         └─────────┘            │
└─────────────────────────────────┘
```

### 14. Sidecar

**Description:**
- Deploy helper service alongside main service
- Handles cross-cutting concerns
- Transparent to main service

**Benefits:**
- Separation of concerns
- Language independence
- Reusability
- Easy updates

**Drawbacks:**
- Resource overhead
- Complexity
- Debugging difficulty
- Network latency

**When to Use:**
- Service mesh
- Logging/monitoring
- Security
- Protocol translation

### 15. Ambassador

**Description:**
- Helper service that proxies network traffic
- Offloads common tasks
- Provides consistent interface

**Benefits:**
- Simplified client code
- Centralized concerns
- Language independence
- Reusability

**Drawbacks:**
- Single point of failure
- Performance overhead
- Complexity
- Debugging difficulty

**When to Use:**
- Service discovery
- Load balancing
- Circuit breaking
- Monitoring
