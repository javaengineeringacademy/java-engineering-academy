# Architecture Patterns

Common architecture patterns and their use cases.

## Overview

Architecture patterns provide reusable solutions to common software design problems. Understanding these patterns helps you make informed decisions during architecture interviews.

## 1. Layered Architecture

**Description:**
- System organized in horizontal layers
- Each layer has specific responsibility
- Layers communicate through interfaces

**Components:**
- Presentation Layer
- Business Logic Layer
- Data Access Layer
- Database Layer

**Benefits:**
- Separation of concerns
- Easy to understand
- Reusable components
- Testable

**Drawbacks:**
- Performance overhead
- Tight coupling between layers
- Difficult to change layers
- Single point of failure

**When to Use:**
- Simple applications
- Traditional web applications
- Small teams
- Rapid development

```
┌─────────────────────────────────┐
│      Presentation Layer         │
├─────────────────────────────────┤
│      Business Logic Layer       │
├─────────────────────────────────┤
│      Data Access Layer          │
├─────────────────────────────────┤
│      Database Layer             │
└─────────────────────────────────┘
```

## 2. Microservices Architecture

**Description:**
- System decomposed into small, independent services
- Each service owns its data
- Services communicate via APIs

**Components:**
- Service Discovery
- API Gateway
- Load Balancer
- Circuit Breaker
- Message Queue

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

## 3. Event-Driven Architecture

**Description:**
- Services communicate via events
- Asynchronous message passing
- Loose coupling

**Components:**
- Event Producer
- Event Bus (Kafka)
- Event Consumer
- Event Store

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

## 4. CQRS (Command Query Responsibility Segregation)

**Description:**
- Separate read and write models
- Optimize each for its purpose
- Often combined with event sourcing

**Components:**
- Command Model
- Query Model
- Event Store
- Read Database

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

## 5. Event Sourcing

**Description:**
- Store events, not state
- Rebuild state from events
- Audit trail

**Components:**
- Event Store
- Event Projector
- Query Model
- Snapshot Store

**Benefits:**
- Complete audit trail
- Temporal queries
- Replay capability
- Debugging

**Drawbacks:**
- Complexity
- Eventual consistency
- Storage requirements
- Schema evolution

**When to Use:**
- Financial systems
- Audit-critical applications
- Complex domains
- Temporal data requirements

## 6. Saga Pattern

**Description:**
- Manage distributed transactions
- Compensating transactions
- Event-driven coordination

**Components:**
- Saga Coordinator
- Transaction Steps
- Compensating Actions
- Event Store

**Benefits:**
- Distributed transactions
- Partial failure handling
- Eventual consistency
- Audit trail

**Drawbacks:**
- Complexity
- Eventual consistency
- Debugging difficulty
- Error handling

**When to Use:**
- Multi-service transactions
- E-commerce orders
- Financial transfers
- Workflow orchestration

## 7. Strangler Fig Pattern

**Description:**
- Gradually replace legacy system
- New features built in new system
- Old features migrated over time

**Components:**
- Proxy/Facade
- New System
- Legacy System
- Migration Logic

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

## 8. API Gateway Pattern

**Description:**
- Single entry point for all clients
- Routes requests to appropriate services
- Handles cross-cutting concerns

**Components:**
- Request Router
- Authentication
- Rate Limiting
- Request Transformation

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

## 9. Service Mesh Pattern

**Description:**
- Dedicated infrastructure layer for service communication
- Sidecar proxies handle networking concerns
- Transparent to application code

**Components:**
- Sidecar Proxy
- Control Plane
- Data Plane
- Policy Engine

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

## 10. Sidecar Pattern

**Description:**
- Deploy helper service alongside main service
- Handles cross-cutting concerns
- Transparent to main service

**Components:**
- Main Service
- Sidecar Service
- Shared Network
- Shared Storage

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

## Pattern Selection Guide

| Problem Type | Recommended Pattern |
|--------------|---------------------|
| Simple application | Layered Architecture |
| Large teams | Microservices |
| Real-time systems | Event-Driven |
| Read-heavy systems | CQRS |
| Financial systems | Event Sourcing |
| Multi-service transactions | Saga |
| Legacy replacement | Strangler Fig |
| Multiple client types | API Gateway |
| Kubernetes environment | Service Mesh |
| Cross-cutting concerns | Sidecar |

## Interview Tips

1. **Understand Trade-offs**: Each pattern has pros and cons
2. **Consider Context**: Choose based on requirements
3. **Explain Reasoning**: Justify your pattern choice
4. **Discuss Alternatives**: Show awareness of other patterns
5. **Think About Scale**: Consider future growth
6. **Address Complexity**: Acknowledge implementation challenges

## Resources

### Books
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "Software Architecture: The Hard Parts" by Neal Ford
- "Building Microservices" by Sam Newman

### Online
- Martin Fowler's Blog
- High Scalability
- Architecture Notes