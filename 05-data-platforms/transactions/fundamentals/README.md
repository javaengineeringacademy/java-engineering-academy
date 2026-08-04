# Transaction Fundamentals

## Comprehensive Guide to Database Transactions

Transactions ensure data consistency and integrity. This guide covers ACID properties, isolation levels, and concurrency control.

---

## Table of Contents

1. [ACID Properties](#acid-properties)
2. [Transaction States](#transaction-states)
3. [Isolation Levels](#isolation-levels)
4. [Concurrency Control](#concurrency-control)
5. [Best Practices](#best-practices)

---

## ACID Properties

### Atomicity

```
- All operations succeed or all fail
- No partial updates
- Rollback on failure
```

```sql
-- Start transaction
BEGIN;

-- Atomic operations
INSERT INTO accounts (id, balance) VALUES (1, 1000);
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

-- Commit or rollback
COMMIT;
-- or
ROLLBACK;
```

### Consistency

```
- Database moves from one valid state to another
- Constraints are always satisfied
- Data integrity is maintained
```

```sql
-- Consistency through constraints
CREATE TABLE accounts (
  id INT PRIMARY KEY,
  balance DECIMAL(10,2) CHECK (balance >= 0)
);
```

### Isolation

```
- Concurrent transactions don't interfere
- Each transaction appears isolated
- Intermediate states are invisible
```

### Durability

```
- Committed changes are permanent
- Survive system failures
- Written to non-volatile storage
```

---

## Transaction States

### States

```
Active → Partially Committed → Committed
Active → Failed → Aborted
Partially Committed → Failed → Aborted
```

### SQL Commands

```sql
-- Start transaction
BEGIN;

-- Or
START TRANSACTION;

-- Commit
COMMIT;

-- Rollback
ROLLBACK;

-- Savepoint
SAVEPOINT my_savepoint;
ROLLBACK TO my_savepoint;
```

---

## Isolation Levels

### Read Uncommitted

```sql
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
```

```
- Can read uncommitted data
- Dirty reads possible
- Lowest isolation
- Highest concurrency
```

### Read Committed

```sql
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

```
- Can only read committed data
- No dirty reads
- Non-repeatable reads possible
- Default in PostgreSQL
```

### Repeatable Read

```sql
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
```

```
- Consistent reads within transaction
- No dirty reads
- No non-repeatable reads
- Phantom reads possible
- Default in MySQL
```

### Serializable

```sql
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

```
- Full isolation
- No dirty reads
- No non-repeatable reads
- No phantom reads
- Lowest concurrency
```

---

## Concurrency Control

### Locking

```sql
-- Shared lock (read)
SELECT * FROM accounts WHERE id = 1 LOCK IN SHARE MODE;

-- Exclusive lock (write)
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;

-- Skip locked
SELECT * FROM accounts WHERE id = 1 FOR UPDATE SKIP LOCKED;
```

### Deadlock

```sql
-- Prevention
SET lock_timeout = 5000;

-- Detection
SHOW ENGINE INNODB STATUS;

-- Resolution
KILL <process_id>;
```

### MVCC

```
- Multi-Version Concurrency Control
- Each transaction sees snapshot
- No read locks needed
- Used by PostgreSQL, MySQL InnoDB
```

---

## Best Practices

### 1. Keep Transactions Short

```sql
-- Good - Short transaction
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT;

-- Bad - Long transaction
BEGIN;
-- ... lots of operations ...
COMMIT;
```

### 2. Use Appropriate Isolation Level

```sql
-- Good - Read committed for most cases
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Good - Serializable for critical operations
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

### 3. Handle Deadlocks

```sql
-- Good - Retry logic
BEGIN;
-- ... operations ...
COMMIT;
-- On deadlock, retry
```

### 4. Use Savepoints

```sql
-- Good - Savepoints
BEGIN;
INSERT INTO orders VALUES (1, 100);
SAVEPOINT order_inserted;
INSERT INTO order_items VALUES (1, 1, 2);
-- If error, rollback to savepoint
ROLLBACK TO order_inserted;
COMMIT;
```

### 5. Monitor Transactions

```sql
-- Check active transactions
SELECT * FROM information_schema.innodb_trx;

-- Check locks
SELECT * FROM information_schema.innodb_locks;

-- Check deadlocks
SHOW ENGINE INNODB STATUS;
```

---

## Further Reading

- [Transaction Processing](https://en.wikipedia.org/wiki/Transaction_processing)
- [Isolation Levels](https://en.wikipedia.org/wiki/Isolation_(database_systems))
- [Concurrency Control](https://en.wikipedia.org/wiki/Concurrency_control)
