# Cassandra Queries

## Comprehensive Guide to CQL Queries

CQL (Cassandra Query Language) is similar to SQL but with limitations. This guide covers CQL syntax, secondary indexes, and query patterns.

---

## Table of Contents

1. [CQL Syntax](#cql-syntax)
2. [SELECT Queries](#select-queries)
3. [INSERT/UPDATE/DELETE](#insert-update-delete)
4. [Secondary Indexes](#secondary-indexes)
5. [Best Practices](#best-practices)

---

## CQL Syntax

### Basic CQL

```sql
-- Create keyspace
CREATE KEYSPACE myapp
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

-- Use keyspace
USE myapp;

-- Create table
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name TEXT,
    email TEXT,
    created_at TIMESTAMP
);
```

### Pagination

```sql
-- Basic pagination
SELECT * FROM users LIMIT 10;

-- Paging state (for driver-level pagination)
SELECT * FROM users LIMIT 10;
-- Driver returns paging state for next page
```

---

## SELECT Queries

### Where Clauses

```sql
-- Equality
SELECT * FROM users WHERE user_id = ?;

-- IN clause (limited usage)
SELECT * FROM users WHERE user_id IN (?, ?, ?);

-- Range on clustering key
SELECT * FROM user_events
WHERE user_id = ?
AND event_time >= '2024-01-01'
AND event_time < '2024-02-01'
ORDER BY event_time DESC;
```

### ALLOW FILTERING

```sql
-- Not recommended (full scan)
SELECT * FROM users WHERE status = 'active' ALLOW FILTERING;

-- Better: Create table for this query
CREATE TABLE active_users (
    status TEXT,
    user_id UUID,
    name TEXT,
    PRIMARY KEY (status, user_id)
);
```

### Aggregation

```sql
-- COUNT
SELECT COUNT(*) FROM users;

-- COUNT with WHERE
SELECT COUNT(*) FROM users WHERE status = 'active';

-- MIN/MAX
SELECT MIN(created_at), MAX(created_at) FROM users;

-- SUM/AVG
SELECT SUM(amount), AVG(amount) FROM orders WHERE user_id = ?;
```

---

## INSERT/UPDATE/DELETE

### INSERT

```sql
-- Basic insert
INSERT INTO users (user_id, name, email, created_at)
VALUES (uuid(), 'John', 'john@example.com', toTimestamp(now()));

-- Insert with TTL (expires in 1 hour)
INSERT INTO sessions (session_id, data)
VALUES (uuid(), 'session data')
USING TTL 3600;

-- Insert if not exists (LWT)
INSERT INTO users (user_id, name)
VALUES (uuid(), 'John')
IF NOT EXISTS;
```

### UPDATE

```sql
-- Basic update
UPDATE users
SET email = 'new@example.com'
WHERE user_id = ?;

-- Update with TTL
UPDATE users
SET session_data = 'data'
USING TTL 3600
WHERE user_id = ?;

-- Conditional update (LWT)
UPDATE users
SET email = 'new@example.com'
WHERE user_id = ?
IF email = 'old@example.com';
```

### DELETE

```sql
-- Delete row
DELETE FROM users WHERE user_id = ?;

-- Delete column
DELETE email FROM users WHERE user_id = ?;

-- Delete with timestamp
DELETE FROM users
USING TIMESTAMP 1234567890
WHERE user_id = ?;

-- Conditional delete (LWT)
DELETE FROM users
WHERE user_id = ?
IF email = 'john@example.com';
```

---

## Secondary Indexes

### Create Index

```sql
-- Simple index
CREATE INDEX ON users (email);

-- Index on collection values
CREATE INDEX ON user_activity (activity_type);

-- Custom SASI index
CREATE CUSTOM INDEX ON users (name)
USING 'org.apache.cassandra.index.sASI.SASIIndex'
WITH OPTIONS = {
    'mode': 'CONTAINS',
    'analyzer_class': 'org.apache.cassandra.index.sASI.StandardAnalyzer'
};
```

### Query with Index

```sql
-- Query using index
SELECT * FROM users WHERE email = 'john@example.com';

-- Query on collection
SELECT * FROM user_activity WHERE activity_type = 'login';
```

---

## Best Practices

### 1. Use Partition Key in WHERE

```sql
-- Good - Uses partition key
SELECT * FROM users WHERE user_id = ?;

-- Bad - Full scan
SELECT * WHERE email = 'john@example.com';
```

### 2. Use Clustering Key for Range Queries

```sql
-- Good - Uses clustering key
SELECT * FROM user_events
WHERE user_id = ?
AND event_time >= '2024-01-01'
ORDER BY event_time DESC;
```

### 3. Use LIMIT

```sql
-- Good - Limits results
SELECT * FROM users LIMIT 100;

-- Bad - Returns all rows
SELECT * FROM users;
```

### 4. Use Consistent Where Clauses

```sql
-- Good - Same partition key
SELECT * FROM user_events
WHERE user_id = ?;

-- Bad - Different partition keys
SELECT * FROM user_events
WHERE user_id IN (?, ?, ?);
```

### 5. Use Prepared Statements

```java
// Good - Prepared statement
PreparedStatement ps = session.prepare(
    "SELECT * FROM users WHERE user_id = ?");
BoundStatement bs = ps.bind(userId);

// Bad - Simple statement
session.execute("SELECT * FROM users WHERE user_id = " + userId);
```

---

## Further Reading

- [CQL Reference](https://cassandra.apache.org/doc/latest/cql/)
- [CQL Data Manipulation](https://cassandra.apache.org/doc/latest/cql/dml.html)
- [CQL Indexes](https://cassandra.apache.org/doc/latest/cql/indexes.html)
