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
