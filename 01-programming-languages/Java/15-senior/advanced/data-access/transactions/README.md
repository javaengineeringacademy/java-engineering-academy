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

1. **Explain the difference between READ_COMMITTED and REPEATABLE_READ isolation levels.**
   READ_COMMITTED: each query sees only committed data at query start time. Problem: non-repeatable reads (same query returns different rows within a transaction). REPEATABLE_READ: entire transaction sees a consistent snapshot from transaction start. Prevents non-repeatable reads but allows phantom reads. MySQL InnoDB REPEATABLE_READ also prevents phantoms via gap locking.

2. **What is a phantom read and which isolation level prevents it?**
   Phantom read occurs when a transaction queries a range of rows, another transaction inserts a row in that range, and the first transaction re-queries to find the new "phantom" row. Only SERIALIZABLE prevents phantoms. InnoDB REPEATABLE_READ uses next-key locking to partially prevent phantoms, but pure phantom reads can still occur with certain query patterns.

3. **How does Spring's @Transactional propagation work?**
   REQUIRED (default): joins existing transaction or creates new. REQUIRES_NEW: always creates new, suspends existing. NESTED: creates savepoint within existing. SUPPORTS: joins if exists, runs non-transactional otherwise. MANDATORY: must have existing transaction, throws exception otherwise. NEVER: must not have transaction, throws exception otherwise.

4. **What causes deadlocks and how do you prevent them?**
   Deadlock: two transactions each hold a lock the other needs. Prevention: (1) Always acquire locks in same order; (2) Keep transactions short; (3) Use lower isolation level; (4) Add proper indexes to reduce lock scope; (5) Use `SELECT ... FOR UPDATE SKIP LOCKED` for queue patterns. Detection: MySQL InnoDB automatically detects deadlocks and rolls back one transaction.

5. **When should you use distributed transactions vs Saga pattern?**
   Distributed transactions (2PC/XA): strong consistency, single database vendor, acceptable latency. Saga pattern: eventual consistency, microservices across databases, better performance. 2PC locks resources for entire duration; Sagas use compensating transactions. In microservices, Sagas are preferred because 2PC doesn't work across service boundaries.

6. **What is the lost update problem and how do you solve it?**
   Lost update: two transactions read same row, both compute updates, one overwrites the other. Solutions: (1) Optimistic locking with version column: `UPDATE SET val=x, version=version+1 WHERE version=old_version`; (2) Pessimistic locking: `SELECT ... FOR UPDATE`; (3) SERIALIZABLE isolation; (4) Atomic SQL: `UPDATE account SET balance = balance + 100`.

## Performance

### Isolation Level Impact
| Isolation | Lock Scope | Concurrency | Use Case |
|-----------|------------|-------------|----------|
| READ_UNCOMMITTED | None | Highest | Analytics, dirty reads OK |
| READ_COMMITTED | Statement-level | High | Most web apps (default) |
| REPEATABLE_READ | Transaction-level | Medium | Financial reads |
| SERIALIZABLE | Range locks | Lowest | Critical consistency |

### Transaction Duration Impact
```
Transaction holding lock for 100ms: 10,000 transactions/day blocked
Transaction holding lock for 1s:     100,000 transactions/day blocked
Transaction holding lock for 10s:    1,000,000 transactions/day blocked
```

## Examples

```java
// Spring declarative transaction
@Service
public class OrderService {
    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = orderRepository.save(new Order(request));
        inventoryService.reserve(order.getItems());
        paymentService.charge(order.getId(), order.getTotal());
        return order;
    }
}

// Optimistic locking for concurrent updates
@Entity
public class Account {
    @Id private Long id;
    private BigDecimal balance;
    @Version private Long version;
}

// Deadlock prevention: consistent lock ordering
@Transactional
public void transfer(Long fromId, Long toId, BigDecimal amount) {
    Long first = Math.min(fromId, toId);
    Long second = Math.max(fromId, toId);
    Account a1 = accountRepository.findByIdForUpdate(first);
    Account a2 = accountRepository.findByIdForUpdate(second);
    // Apply transfer logic...
}

// Programmatic transaction for complex flows
TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
Order order = txTemplate.execute(status -> {
    try {
        Order o = orderRepo.save(new Order(req));
        inventoryService.reserve(o.getItems());
        return o;
    } catch (Exception e) {
        status.setRollbackOnly();
        throw e;
    }
});

// Saga pattern (compensating transactions)
public class OrderSaga {
    @SagaStep compensation = () -> orderService.cancel(orderId);
    paymentService.charge(orderId, amount);
    inventoryService.reserve(items);
    shippingService.schedule(orderId);
    // On failure at any step, run compensations in reverse order
}
```

## Internal Working

### Transaction Manager Flow
1. `@Transactional` intercepted by `TransactionAspectSupport`
2. Transaction manager calls `dataSource.getConnection()`
3. Connection auto-commit set to false
4. Savepoint created (if nested)
5. Method executes with connection bound to thread
6. On success → `connection.commit()`
7. On exception → `connection.rollback()` (or to savepoint)
8. Connection returned to pool

### Lock Acquisition in InnoDB
```
Row lock → index entry lock → gap lock → next-key lock
```
- Record lock: locks the index record itself
- Gap lock: locks the gap before the index record
- Next-key lock: record lock + gap lock (prevents phantoms)
- Insert intention lock: special gap lock for INSERT operations

## Why This Concept Exists

Transactions exist because databases are shared resources accessed by concurrent processes. Without transactions: (1) Partial updates leave data in inconsistent states; (2) Concurrent writes cause lost updates; (3) System failures leave data corrupted. ACID properties guarantee that concurrent operations produce correct results and system failures don't corrupt data. Transactions abstract complex concurrency control into simple begin/commit/rollback semantics.

## Overview

Transactions group multiple database operations into a single atomic unit that either completely succeeds or completely fails. Key properties: Atomicity (all or nothing), Consistency (valid state transitions), Isolation (concurrent transactions don't interfere), Durability (committed data survives crashes). In Java, Spring's `@Transactional` provides declarative transaction management, while JDBC provides programmatic control via `Connection.setAutoCommit(false)` / `commit()` / `rollback()`.

## Pitfalls

```java
// PITFALL 1: Self-invocation bypasses proxy
@Service
public class UserService {
    public void process() {
        this.updateUser(); // Internal call — @Transactional ignored!
    }
    @Transactional
    public void updateUser() { }
}

// PITFALL 2: Checked exceptions don't trigger rollback (Spring default)
@Transactional // Won't rollback on checked exception!
public void process() throws IOException {
    throw new IOException("file error");
}
// Fix: @Transactional(rollbackFor = Exception.class)

// PITFALL 3: Long-running transactions
@Transactional
public void exportLargeDataset() {
    // Processing 1M rows while holding transaction open
    // Locks held for minutes → blocks other transactions
}

// PITFALL 4: Not setting isolation level for concurrent reads
// Two users read balance=100, both withdraw 80, both succeed
// Result: balance = -60 (lost update)

// PITFALL 5: Swallowing exceptions prevents rollback
@Transactional
public void process() {
    try {
        riskyOperation();
    } catch (Exception e) {
        log.error("error", e); // Exception swallowed — no rollback!
    }
}
```

## References

- [Spring Transaction Documentation](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)
- [MySQL InnoDB Isolation Levels](https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-isolation-levels.html)
- "High Performance Java Persistence" by Vlad Mihalcea
- "Database Internals" by Alex Petrov
