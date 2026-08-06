# Transaction Concepts

## ACID Properties
- **Atomicity**: All operations complete or none
- **Consistency**: Data remains valid after transaction
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed changes survive failures

## Isolation Levels
- **READ_UNCOMMITTED**: Fastest, least safe
- **READ_COMMITTED**: Default for most databases
- **REPEATABLE_READ**: Consistent reads
- **SERIALIZABLE**: Full isolation, slowest

## Propagation (Spring)
- **REQUIRED**: Join existing transaction
- **REQUIRES_NEW**: Create new transaction
- **SUPPORTS**: Join or run non-tx
- **MANDATORY**: Must have existing tx
- **NEVER**: Must not have tx
- **NOT_SUPPORTED**: Pause current tx

## Rollback Strategies
- Automatic rollback on exception
- Manual rollback with savepoints
- Partial rollback to savepoint
- Exception-based rollback triggers

## Distributed Transactions
- Two-phase commit (2PC)
- XA transactions
- Saga pattern
- Eventually consistent patterns

## Common Issues
- Deadlocks
- Long-running transactions
- Connection leaks
- Lost updates
- Phantom reads

## Best Practices
- Keep transactions short
- Use appropriate isolation level
- Implement proper error handling
- Monitor transaction duration
- Use connection pooling
- Avoid distributed transactions when possible

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
