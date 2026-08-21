# Architecture: Decision Guide

## When to Apply Architecture Patterns

### Microservices vs Monolith

| Criteria | Monolith | Microservices |
|----------|----------|---------------|
| Team size | < 10 | > 10 |
| Domain complexity | Low-Medium | High |
| Deployment | Single artifact | Multiple artifacts |
| Scaling | Vertical | Horizontal per service |
| Debugging | Easy (single process) | Hard (distributed tracing) |
| Tech stack | One stack | Polyglot possible |

**Choose monolith when:** Small team, simple domain, MVP phase, limited DevOps capability.

**Choose microservices when:** Large team, complex bounded contexts, independent scaling needs, mature DevOps.

### Synchronous vs Asynchronous Communication

| Criteria | Synchronous (REST/gRPC) | Asynchronous (Events/Queues) |
|----------|------------------------|------------------------------|
| Latency | Request-response delay | Non-blocking |
| Coupling | Tight (caller waits) | Loose (event-driven) |
| Consistency | Strong (immediate) | Eventual |
| Complexity | Lower | Higher (eventual consistency) |
| Failure handling | Simple (timeout/retry) | Complex (dead letter queues) |

**Choose synchronous when:** Simple request-response, strong consistency needed, low latency acceptable.

**Choose asynchronous when:** High throughput, independent services, eventual consistency acceptable.

### Database Selection Matrix

| Database | Best For | Trade-offs |
|----------|----------|------------|
| PostgreSQL | ACID, complex queries, JSON | Write scaling limited |
| MongoDB | Flexible schema, rapid dev | No multi-doc ACID transactions |
| Cassandra | Write-heavy, time-series | No JOINs, eventual consistency |
| Redis | Caching, sessions, queues | Limited data size, in-memory cost |
| DynamoDB | Serverless, auto-scaling | Vendor lock-in, query limitations |

## Architecture Decision Record (ADR) Process

1. **Identify** the decision that needs to be made
2. **Context** — document the forces at play
3. **Options** — list all viable alternatives
4. **Decision** — choose one with rationale
5. **Consequences** — document what changes
6. **Review** — revisit after N months

## Migration Strategies

| Strategy | Risk | Speed | Best For |
|----------|------|-------|----------|
| Big Bang | Very High | Fast | Small systems |
| Strangler Fig | Low | Medium | Large monoliths |
| Branch by Abstraction | Medium | Medium | Feature-level changes |
| Database per Service | High | Slow | Data-heavy migrations |

## Cost-Benefit Framework

```
Monthly Cost = Compute + Storage + Network + Operations
Monthly Benefit = Incident Reduction + Developer Productivity + Revenue Impact

ROI = Monthly Benefit / Monthly Cost
Payback Period = Migration Cost / Monthly Net Benefit
```

## Further Reading

- *Building Microservices* by Sam Newman
- *Designing Data-Intensive Applications* by Martin Kleppmann
- *Monolith to Microservices* by Sam Newman
- [Architecture Decision Records](https://adr.github.io/)
- [Microservices.io](https://microservices.io/patterns/)
