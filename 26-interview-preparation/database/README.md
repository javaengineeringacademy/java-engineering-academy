# Database Interview Guide

Master database interviews with comprehensive coverage of SQL, NoSQL, and data modeling.

## Overview

Database interviews test your knowledge of data modeling, query optimization, transactions, and database design.

## Key Topics

### 1. SQL Fundamentals

**Basic Queries:**
```sql
-- SELECT with WHERE
SELECT * FROM users WHERE age > 25;

-- JOIN operations
SELECT u.name, o.total
FROM users u
JOIN orders o ON u.id = o.user_id;

-- Aggregate functions
SELECT department, AVG(salary) as avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 50000;

-- Subqueries
SELECT * FROM users
WHERE id IN (SELECT user_id FROM orders WHERE total > 100);
```

**Advanced SQL:**
```sql
-- Window functions
SELECT name, salary,
       RANK() OVER (ORDER BY salary DESC) as rank
FROM employees;

-- CTEs (Common Table Expressions)
WITH dept_stats AS (
    SELECT department, AVG(salary) as avg_salary
    FROM employees
    GROUP BY department
)
SELECT * FROM dept_stats
WHERE avg_salary > 50000;

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_date ON orders(user_id, created_at);
```

### 2. Database Design

**Normalization:**
- **1NF**: Atomic values, no repeating groups
- **2NF**: 1NF + no partial dependencies
- **3NF**: 2NF + no transitive dependencies
- **BCNF**: Boyce-Codd Normal Form

**Denormalization:**
- Reduces joins
- Improves read performance
- Increases storage
- Data redundancy

**Example Schema:**
```sql
-- Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
```

### 3. Indexing

**Index Types:**
- **B-Tree**: Default, good for equality and range queries
- **Hash**: Good for equality queries only
- **GIN**: Good for full-text search
- **GiST**: Good for geometric data

**Index Best Practices:**
```sql
-- Create index on frequently queried columns
CREATE INDEX idx_users_email ON users(email);

-- Composite index for multi-column queries
CREATE INDEX idx_orders_user_status ON orders(user_id, status);

-- Partial index for filtered queries
CREATE INDEX idx_orders_pending ON orders(created_at) 
WHERE status = 'pending';

-- Covering index for index-only scans
CREATE INDEX idx_orders_covering ON orders(user_id, status, total);
```

**Index Monitoring:**
```sql
-- Check index usage
SELECT * FROM pg_stat_user_indexes;

-- Identify unused indexes
SELECT schemaname, relname, indexrelname
FROM pg_stat_user_indexes
WHERE idx_scan = 0;

-- Check index size
SELECT pg_size_pretty(pg_relation_size(indexrelid))
FROM pg_stat_user_indexes;
```

### 4. Transactions and ACID

**ACID Properties:**
- **Atomicity**: All or nothing
- **Consistency**: Valid state transitions
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed data persists

**Isolation Levels:**
```sql
-- Read Uncommitted (lowest isolation)
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

-- Read Committed (default in PostgreSQL)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Repeatable Read
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- Serializable (highest isolation)
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

**Example Transaction:**
```sql
BEGIN;

-- Debit account
UPDATE accounts SET balance = balance - 100 WHERE id = 1;

-- Credit account
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

-- Check for overdraft
IF (SELECT balance FROM accounts WHERE id = 1) < 0 THEN
    ROLLBACK;
ELSE
    COMMIT;
END IF;
```

### 5. NoSQL Databases

**Types:**
- **Document**: MongoDB, CouchDB
- **Key-Value**: Redis, DynamoDB
- **Column-Family**: Cassandra, HBase
- **Graph**: Neo4j, ArangoDB

**When to Use:**
| Use Case | Recommended Type |
|----------|------------------|
| User profiles | Document |
| Session storage | Key-Value |
| Time-series data | Column-Family |
| Social networks | Graph |

**MongoDB Example:**
```javascript
// Create collection with schema validation
db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["name", "email"],
            properties: {
                name: { bsonType: "string" },
                email: { bsonType: "string" },
                age: { bsonType: "int", minimum: 0 }
            }
        }
    }
});

// Query with indexing
db.users.createIndex({ email: 1 }, { unique: true });
db.users.find({ age: { $gte: 25 } }).sort({ name: 1 });
```

### 6. Query Optimization

**EXPLAIN Analysis:**
```sql
-- PostgreSQL
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';

-- MySQL
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';
```

**Common Optimizations:**
1. **Add Indexes**: For frequently queried columns
2. **Avoid SELECT ***: Only select needed columns
3. **Use JOINs Properly**: Avoid cartesian products
4. **Limit Results**: Use LIMIT for large result sets
5. **Optimize Subqueries**: Use JOINs or CTEs instead

**Example Optimization:**
```sql
-- Slow query
SELECT * FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.total > 100;

-- Optimized query
SELECT u.id, u.name, o.total
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.total > 100
AND o.created_at > '2024-01-01';
```

## Common Interview Questions

### 1. What is the difference between SQL and NoSQL?

**Answer:**

| Aspect | SQL | NoSQL |
|--------|-----|-------|
| Schema | Fixed | Dynamic |
| Scaling | Vertical | Horizontal |
| Consistency | Strong | Eventual |
| Query Language | SQL | Varies |
| Relationships | Complex joins | Embedded/referenced |
| Use Cases | Complex queries | Simple queries |

**SQL Example:**
```sql
-- Relational database
SELECT u.name, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id;
```

**NoSQL Example:**
```javascript
// MongoDB embedded documents
db.users.aggregate([
    {
        $lookup: {
            from: "orders",
            localField: "_id",
            foreignField: "user_id",
            as: "orders"
        }
    },
    {
        $project: {
            name: 1,
            order_count: { $size: "$orders" }
        }
    }
]);
```

### 2. How do you optimize slow queries?

**Answer:** I follow a systematic approach:

1. **Identify Slow Queries:**
```sql
-- PostgreSQL
SELECT * FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

2. **Analyze Execution Plan:**
```sql
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';
```

3. **Optimize:**
- Add appropriate indexes
- Rewrite queries
- Update statistics
- Consider denormalization

4. **Monitor:**
```sql
-- Check query performance
SELECT query, mean_exec_time, calls
FROM pg_stat_statements
ORDER BY mean_exec_time DESC;
```

### 3. Explain database normalization.

**Answer:** Normalization organizes data to reduce redundancy:

**1NF (First Normal Form):**
- Atomic values
- No repeating groups
- Each row is unique

**2NF (Second Normal Form):**
- 1NF satisfied
- No partial dependencies
- All non-key attributes depend on entire primary key

**3NF (Third Normal Form):**
- 2NF satisfied
- No transitive dependencies
- Non-key attributes don't depend on other non-key attributes

**Example:**
```sql
-- Unnormalized
CREATE TABLE orders (
    id INT,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    product_name VARCHAR(100),
    product_price DECIMAL(10,2)
);

-- 3NF Normalized
CREATE TABLE customers (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE products (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10,2)
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    customer_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### 4. What are database transactions?

**Answer:** Transactions ensure data integrity:

**ACID Properties:**
- **Atomicity**: All operations succeed or all fail
- **Consistency**: Database remains in valid state
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed data persists

**Example:**
```sql
BEGIN TRANSACTION;

-- Transfer money
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

-- Verify success
IF @@ERROR <> 0
    ROLLBACK;
ELSE
    COMMIT;
```

### 5. How do you design a database schema?

**Answer:** I follow these steps:

1. **Identify Entities**: Users, Orders, Products
2. **Define Relationships**: One-to-many, many-to-many
3. **Choose Data Types**: Appropriate types for each field
4. **Create Indexes**: For frequently queried columns
5. **Normalize**: Reduce redundancy
6. **Consider Scaling**: Sharding, replication

**Example:**
```sql
-- E-commerce schema
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## Advanced Topics

### 1. Database Sharding

**Sharding Strategies:**
- **Hash-based**: Even distribution
- **Range-based**: Geographic or time-based
- **Directory-based**: Lookup table

**Example:**
```python
def get_shard(user_id, num_shards):
    return user_id % num_shards

def get_database(user_id):
    shard = get_shard(user_id, 4)
    return f"database_{shard}"
```

### 2. Replication

**Types:**
- **Master-Slave**: Read scaling
- **Master-Master**: Write scaling
- **Multi-region**: Global distribution

**Example:**
```sql
-- PostgreSQL replication
CREATE SUBSCRIPTION my_sub
    CONNECTION 'host=master dbname=mydb'
    PUBLICATION my_pub;
```

### 3. Connection Pooling

**Benefits:**
- Reduces connection overhead
- Limits concurrent connections
- Improves performance

**Example:**
```python
# Using SQLAlchemy with connection pooling
from sqlalchemy import create_engine

engine = create_engine(
    'postgresql://user:pass@localhost/db',
    pool_size=20,
    max_overflow=30,
    pool_timeout=30
)
```

## Study Plan

### Week 1-2: SQL Fundamentals
- Basic queries
- JOIN operations
- Aggregate functions
- Subqueries

### Week 3-4: Database Design
- Normalization
- Schema design
- Indexing strategies
- Data modeling

### Week 5-6: Advanced Topics
- Transactions
- Query optimization
- Sharding and replication
- NoSQL databases

### Week 7-8: Practice
- Design database schemas
- Optimize slow queries
- Implement sharding
- Practice interview questions

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