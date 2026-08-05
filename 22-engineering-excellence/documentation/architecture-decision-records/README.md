# Architecture Decision Records (ADRs)

## Overview

ADRs capture important architectural decisions with context and consequences.

## Template

```markdown
# ADR-001: Use Event Sourcing for Order Service

## Status: Accepted

## Context
The order service needs complete history for audit.

## Decision
Implement event sourcing with Kafka.

## Consequences
+ Complete audit trail
+ Event replay capability
- Increased complexity
- Event schema evolution challenges
```

## Best Practices

1. One ADR per decision
2. Write when decision is made
3. Keep ADRs forever
4. Review before accepting
