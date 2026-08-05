# PostgreSQL Core Concepts

## SQL Fundamentals

### Data Types

- Numeric: INTEGER, BIGINT, DECIMAL, NUMERIC
- Character: VARCHAR, CHAR, TEXT
- Date/Time: DATE, TIMESTAMP, INTERVAL
- Boolean: BOOLEAN
- JSON: JSON, JSONB (indexed)
- Arrays: Any type can be array

### DDL Commands

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT NOW()
);

ALTER TABLE users ADD COLUMN phone VARCHAR(20);
DROP TABLE users;
```

### DML Commands

```sql
INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');
UPDATE users SET name = 'Bob' WHERE id = 1;
DELETE FROM users WHERE id = 1;
```

## Joins

### Types of Joins

```sql
-- Inner Join
SELECT * FROM orders o
INNER JOIN customers c ON o.customer_id = c.id;

-- Left Join
SELECT * FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id;

-- Full Outer Join
SELECT * FROM table1 t1
FULL OUTER JOIN table2 t2 ON t1.id = t2.id;
```

### Self Join

```sql
SELECT e.name, m.name AS manager
FROM employees e
JOIN employees m ON e.manager_id = m.id;
```

## Indexes

### Index Types

- B-tree: Default, equality and range queries
- Hash: Equality only
- GiST: Geometric, full-text search
- GIN: Composite types, arrays, JSONB
- BRIN: Large tables with natural ordering

### Creating Indexes

```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_products_gin ON products USING GIN(tags);
```

### Index Usage

- Check with EXPLAIN
- Avoid over-indexing
- Consider partial indexes
- Monitor index usage statistics

## Constraints

### Types

```sql
PRIMARY KEY: Unique identifier
FOREIGN KEY: Referential integrity
UNIQUE: No duplicate values
NOT NULL: Required field
CHECK: Custom validation
```

### Foreign Keys

```sql
ALTER TABLE orders
ADD CONSTRAINT fk_customer
FOREIGN KEY (customer_id)
REFERENCES customers(id)
ON DELETE CASCADE;
```

## Views

### Creating Views

```sql
CREATE VIEW active_users AS
SELECT * FROM users WHERE status = 'active';

SELECT * FROM active_users;
```

### Materialized Views

```sql
CREATE MATERIALIZED VIEW user_stats AS
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id;

-- Refresh
REFRESH MATERIALIZED VIEW user_stats;
```

## Triggers

### Trigger Functions

```sql
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
```

## Common Table Expressions (CTEs)

```sql
WITH active_orders AS (
    SELECT * FROM orders WHERE status = 'active'
)
SELECT * FROM active_orders
WHERE created_at > NOW() - INTERVAL '7 days';
```

## Window Functions

```sql
SELECT name, department,
    ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as rank
FROM employees;
```

## Best Practices

1. Use appropriate data types
2. Add indexes on frequently queried columns
3. Use constraints for data integrity
4. Prefer CTEs for complex queries
5. Use EXPLAIN to analyze queries
