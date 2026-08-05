# Database Interview Questions

Comprehensive guide to database interview questions and answers.

## SQL Questions

### 1. What is the difference between WHERE and HAVING?

**Answer:**

| Aspect | WHERE | HAVING |
|--------|-------|--------|
| Purpose | Filter rows | Filter groups |
| Timing | Before GROUP BY | After GROUP BY |
| Aggregate Functions | Cannot use | Can use |
| Performance | Faster | Slower |

**Examples:**
```sql
-- WHERE: Filter before grouping
SELECT department, COUNT(*) as emp_count
FROM employees
WHERE salary > 50000
GROUP BY department;

-- HAVING: Filter after grouping
SELECT department, COUNT(*) as emp_count
FROM employees
GROUP BY department
HAVING COUNT(*) > 5;

-- Both together
SELECT department, AVG(salary) as avg_salary
FROM employees
WHERE hire_date > '2020-01-01'
GROUP BY department
HAVING AVG(salary) > 60000;
```

### 2. Explain different types of JOINs.

**Answer:**

```sql
-- INNER JOIN: Only matching rows
SELECT u.name, o.total
FROM users u
INNER JOIN orders o ON u.id = o.user_id;

-- LEFT JOIN: All rows from left table
SELECT u.name, COALESCE(SUM(o.total), 0) as total_spent
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;

-- RIGHT JOIN: All rows from right table
SELECT u.name, o.total
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;

-- FULL OUTER JOIN: All rows from both tables
SELECT u.name, o.total
FROM users u
FULL OUTER JOIN orders o ON u.id = o.user_id;

-- CROSS JOIN: Cartesian product
SELECT u.name, p.name
FROM users u
CROSS JOIN products p;
```

### 3. What are aggregate functions?

**Answer:** Aggregate functions perform calculations on multiple rows:

```sql
-- COUNT: Number of rows
SELECT COUNT(*) FROM users;

-- SUM: Total of numeric column
SELECT SUM(total) FROM orders WHERE user_id = 1;

-- AVG: Average value
SELECT AVG(salary) FROM employees;

-- MIN/MAX: Minimum/Maximum values
SELECT MIN(salary), MAX(salary) FROM employees;

-- GROUP BY: Aggregate per group
SELECT department, AVG(salary) as avg_salary
FROM employees
GROUP BY department;

-- DISTINCT with aggregate
SELECT COUNT(DISTINCT user_id) FROM orders;
```

### 4. What are window functions?

**Answer:** Window functions perform calculations across related rows:

```sql
-- ROW_NUMBER: Sequential numbering
SELECT name, salary,
       ROW_NUMBER() OVER (ORDER BY salary DESC) as rank
FROM employees;

-- RANK: Rank with gaps
SELECT name, department, salary,
       RANK() OVER (PARTITION BY department ORDER BY salary DESC) as dept_rank
FROM employees;

-- DENSE_RANK: Rank without gaps
SELECT name, department, salary,
       DENSE_RANK() OVER (PARTITION BY department ORDER BY salary DESC) as rank
FROM employees;

-- LAG/LEAD: Access previous/next rows
SELECT name, salary,
       LAG(salary) OVER (ORDER BY hire_date) as prev_salary,
       LEAD(salary) OVER (ORDER BY hire_date) as next_salary
FROM employees;

-- Running total
SELECT name, salary,
       SUM(salary) OVER (ORDER BY hire_date) as running_total
FROM employees;
```

### 5. What are indexes and when to use them?

**Answer:** Indexes improve query performance by allowing faster data retrieval:

```sql
-- Create index on single column
CREATE INDEX idx_users_email ON users(email);

-- Composite index
CREATE INDEX idx_orders_user_date ON orders(user_id, created_at);

-- Unique index
CREATE UNIQUE INDEX idx_users_email ON users(email);

-- Partial index
CREATE INDEX idx_orders_pending ON orders(created_at) 
WHERE status = 'pending';

-- Covering index
CREATE INDEX idx_orders_covering ON orders(user_id, status, total);

-- Drop index
DROP INDEX idx_users_email;
```

**When to Use:**
- Columns in WHERE clauses
- Columns in JOIN conditions
- Columns in ORDER BY clauses
- Columns with high cardinality

**When NOT to Use:**
- Small tables
- Columns with low cardinality
- Frequently updated columns
- Tables with heavy write operations

## Database Design Questions

### 6. How do you design a database schema?

**Answer:** I follow these steps:

1. **Identify Entities:**
```sql
-- Users, Products, Orders
```

2. **Define Relationships:**
```sql
-- Users 1:N Orders
-- Orders N:M Products (via order_items)
```

3. **Choose Data Types:**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

4. **Create Indexes:**
```sql
CREATE INDEX idx_users_email ON users(email);
```

5. **Normalize:**
```sql
-- Separate tables for different entities
CREATE TABLE orders (...);
CREATE TABLE order_items (...);
```

### 7. What is normalization?

**Answer:** Normalization reduces data redundancy:

**1NF (First Normal Form):**
```sql
-- Bad: Repeating groups
CREATE TABLE users (
    id INT,
    phone1 VARCHAR(20),
    phone2 VARCHAR(20),
    phone3 VARCHAR(20)
);

-- Good: 1NF
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE user_phones (
    user_id INT,
    phone VARCHAR(20),
    PRIMARY KEY (user_id, phone)
);
```

**2NF (Second Normal Form):**
```sql
-- Bad: Partial dependency
CREATE TABLE orders (
    order_id INT,
    product_id INT,
    product_name VARCHAR(100),
    quantity INT
);

-- Good: 2NF
CREATE TABLE products (
    id INT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    product_id INT,
    quantity INT,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

**3NF (Third Normal Form):**
```sql
-- Bad: Transitive dependency
CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    department_id INT,
    department_name VARCHAR(100)
);

-- Good: 3NF
CREATE TABLE departments (
    id INT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);
```

### 8. What is denormalization?

**Answer:** Denormalization adds redundancy to improve read performance:

```sql
-- Normalized (multiple queries)
SELECT u.name, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;

-- Denormalized (single query)
SELECT name, order_count FROM users;
```

**Trade-offs:**
- Faster reads
- Slower writes
- More storage
- Data inconsistency risk

**When to Use:**
- Read-heavy workloads
- Reporting systems
- Data warehousing
- Caching layers

## Transaction Questions

### 9. What are ACID properties?

**Answer:** ACID ensures data integrity:

```sql
-- Atomicity: All or nothing
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;

-- Consistency: Valid state
-- (Enforced by constraints, triggers)

-- Isolation: Concurrent transactions don't interfere
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- Durability: Committed data persists
-- (Enforced by write-ahead logging)
```

### 10. What are isolation levels?

**Answer:** Isolation levels control transaction visibility:

```sql
-- Read Uncommitted (lowest)
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
-- Can read uncommitted changes (dirty reads)

-- Read Committed (default)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
-- Can only read committed data

-- Repeatable Read
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
-- Same query returns same results

-- Serializable (highest)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
-- Full isolation, like sequential execution
```

### 11. How do you handle deadlocks?

**Answer:** Deadlocks occur when transactions wait for each other:

```sql
-- Detection (PostgreSQL)
SELECT * FROM pg_stat_activity WHERE wait_event_type = 'Lock';

-- Prevention
-- 1. Lock tables in same order
-- 2. Use shorter transactions
-- 3. Add appropriate indexes
-- 4. Use SELECT FOR UPDATE SKIP LOCKED

-- Example: Skip locked rows
SELECT * FROM orders
WHERE status = 'pending'
ORDER BY created_at
LIMIT 1
FOR UPDATE SKIP LOCKED;
```

## Performance Questions

### 12. How do you optimize slow queries?

**Answer:** I follow this process:

1. **Identify Slow Queries:**
```sql
-- PostgreSQL
SELECT query, mean_exec_time, calls
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

2. **Analyze Execution Plan:**
```sql
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';
```

3. **Optimize:**
```sql
-- Add index
CREATE INDEX idx_users_email ON users(email);

-- Rewrite query
SELECT id, name FROM users WHERE email = 'test@example.com';

-- Update statistics
ANALYZE users;
```

### 13. What is query execution plan?

**Answer:** Execution plan shows how database executes a query:

```sql
-- PostgreSQL
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';

-- Output:
-- Seq Scan on users  (cost=0.00..35.50 rows=1 width=100)
--   Filter: (email = 'test@example.com'::text)
--   Rows Removed by Filter: 99
-- Planning Time: 0.078 ms
-- Execution Time: 0.125 ms
```

**Key Operations:**
- Seq Scan: Full table scan
- Index Scan: Using index
- Hash Join: Join using hash
- Nested Loop: Nested loop join
- Sort: Sorting results

### 14. How do you design for scalability?

**Answer:** I use multiple strategies:

1. **Read Replicas:**
```sql
-- PostgreSQL streaming replication
CREATE SUBSCRIPTION my_sub
    CONNECTION 'host=master dbname=mydb'
    PUBLICATION my_pub;
```

2. **Sharding:**
```python
def get_shard(user_id, num_shards):
    return user_id % num_shards
```

3. **Connection Pooling:**
```python
engine = create_engine(
    'postgresql://user:pass@localhost/db',
    pool_size=20,
    max_overflow=30
)
```

4. **Caching:**
```python
import redis

r = redis.Redis()
def get_user(user_id):
    cache_key = f"user:{user_id}"
    user = r.get(cache_key)
    if not user:
        user = db.query(User).get(user_id)
        r.setex(cache_key, 3600, user.to_json())
    return user
```

## NoSQL Questions

### 15. When would you use NoSQL?

**Answer:** NoSQL is preferred for:

1. **Flexible Schema:**
```javascript
// MongoDB
db.users.insert({
    name: "John",
    email: "john@example.com",
    preferences: { theme: "dark", language: "en" }
});
```

2. **Horizontal Scaling:**
```javascript
// Cassandra
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name text,
    email text
) WITH CLUSTERING ORDER BY (name ASC);
```

3. **High Write Throughput:**
```python
# Redis
r.set("session:123", user_data, ex=3600)
```

4. **Simple Queries:**
```javascript
// MongoDB
db.users.find({ age: { $gte: 25 } });
```

### 16. What is the CAP theorem?

**Answer:** CAP theorem states distributed systems can only guarantee 2 of 3:

- **Consistency**: All nodes see same data
- **Availability**: Every request gets response
- **Partition Tolerance**: System works despite network failures

**Examples:**
- **CP Systems**: MongoDB, HBase (consistent but may be unavailable)
- **AP Systems**: Cassandra, DynamoDB (available but may be inconsistent)
- **CA Systems**: Traditional RDBMS (consistent and available but no partition tolerance)

## Best Practices

### 1. Schema Design
- Use appropriate data types
- Create indexes strategically
- Normalize to 3NF
- Denormalize for performance

### 2. Query Optimization
- Use EXPLAIN to analyze
- Avoid SELECT *
- Use JOINs instead of subqueries
- Limit result sets

### 3. Indexing
- Index frequently queried columns
- Use composite indexes
- Avoid over-indexing
- Monitor index usage

### 4. Transactions
- Keep transactions short
- Use appropriate isolation levels
- Handle deadlocks gracefully
- Use optimistic locking

### 5. Scalability
- Use read replicas
- Implement connection pooling
- Cache frequently accessed data
- Consider sharding for large datasets

## Resources

### Books
- "Database System Concepts" by Silberschatz
- "SQL Performance Explained" by Markus Winand
- "Designing Data-Intensive Applications" by Martin Kleppmann

### Online
- PostgreSQL Documentation
- MySQL Documentation
- MongoDB University
- LeetCode Database Problems