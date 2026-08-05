# Architecture Decision Records (ADR)

## Overview

Architecture Decision Records (ADRs) are documents that capture important architectural decisions along with their context and consequences. They provide a lightweight way to track significant decisions without heavy documentation overhead.

## Why ADRs

- **Preserve context** - Future teams understand why decisions were made
- **Track evolution** - Architecture changes over time
- **Onboard faster** - New developers understand historical context
- **Avoid revisiting** - Prevent re-litigating settled decisions
- **Communication** - Align team on architectural direction

## ADR Format

### Simple Template

```markdown
# [Number]. [Title]

## Status
[Proposed | Accepted | Deprecated | Superseded by ADR-XXX]

## Context
[What is the issue that we're seeing that is motivating this decision?]

## Decision
[What is the change that we're proposing and/or doing?]

## Consequences
[What becomes easier or more difficult because of this change?]
```

### MADR Template

```markdown
# [ADR Title]

## Metadata
- ADR: [number]
- Date: [YYYY-MM-DD]
- Status: [Proposed | Accepted | Deprecated | Superseded]
- Deciders: [list of people]
- Technical Story: [ticket/issue link]

## Context and Problem Statement
[Describe the context and problem statement.]

## Decision Drivers
* [factor 1]
* [factor 2]

## Considered Options
* [option 1]
* [option 2]
* [option 3]

## Decision Outcome
Chosen option: [option X]

### Positive Consequences
* [consequence 1]

### Negative Consequences
* [consequence 1]

## Links
* [related ADRs]
```

## Example ADRs

### ADR-001: Database Choice

```markdown
# ADR-001: Use PostgreSQL as Primary Database

## Status
Accepted

## Context
We need a relational database for our new microservices platform. Requirements include ACID transactions, JSON support, full-text search, and strong community support.

## Decision
We will use PostgreSQL 15 as our primary relational database.

## Consequences
### Positive
- Excellent JSON/JSONB support reduces need for separate document store
- Strong ACID compliance
- Rich extension ecosystem (PostGIS, pg_trgm, pg_partman)
- Active open-source community

### Negative
- Higher memory usage than MySQL
- Requires DBA expertise for optimization
- Scaling requires careful planning (read replicas, partitioning)
```

### ADR-002: Message Broker

```markdown
# ADR-002: Use Apache Kafka as Message Broker

## Status
Accepted

## Context
Our system requires event-driven communication between microservices. We need to handle 100K+ events/second with ordering guarantees and replay capability.

## Decision
We will use Apache Kafka as our primary message broker.

## Consequences
### Positive
- High throughput with ordering guarantees
- Built-in replay and retention
- Kafka Streams for stream processing
- Mature ecosystem (Connect, Schema Registry)

### Negative
- Operational complexity
- Requires ZooKeeper (or KRaft)
- Learning curve for team
- Harder to operate than RabbitMQ for simple use cases
```

## ADR Lifecycle

```
Draft → Proposed → Accepted → Deprecated/Superseded
```

## Tools

| Tool | Description |
|------|-------------|
| adr-tools | CLI for managing ADRs |
| MADR | Markdown Architectural Decision Records |
| Log4brains | ADR management UI |
| adr-manager | VS Code extension |

## Best Practices

1. **Write early** - Capture decisions when they're fresh
2. **Keep it short** - One page maximum
3. **Focus on why** - Context matters more than what
4. **Review with team** - ADRs are collaborative
5. **Link to code** - Reference implementation PRs
6. **Don't over-document** - Only significant decisions need ADRs

## Key Takeaways

- ADRs capture architectural decisions with context
- Use simple templates (MADR or simple format)
- Track status through lifecycle
- Focus on the "why" not just the "what"
- Keep records short and focused
