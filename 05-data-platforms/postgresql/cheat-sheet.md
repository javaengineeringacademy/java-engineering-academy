# PostgreSQL Cheat Sheet

## Connection

```bash
# Connect to database
psql -h localhost -U myuser -d mydb

# Connect as postgres user
psql postgres

# Connect with password
PGPASSWORD=password psql -h localhost -U myuser -d mydb
```

## Database Operations

```sql
-- Create database
CREATE DATABASE mydb;

-- List databases
\l

-- Connect to database
\c mydb

-- Drop database
DROP DATABASE mydb;
```

## Table Operations

```sql
-- Create table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- List tables
\dt

-- Describe table
\d users

-- Drop table
DROP TABLE users;
```

## Index Operations

```sql
-- Create index
CREATE INDEX idx_users_email ON users(email);

-- Create unique index
CREATE UNIQUE INDEX idx_users_email_unique ON users(email);

-- List indexes
\di

-- Drop index
DROP INDEX idx_users_email;
```

## Data Manipulation

```sql
-- Insert data
INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');

-- Update data
UPDATE users SET name = 'Bob' WHERE id = 1;

-- Delete data
DELETE FROM users WHERE id = 1;

-- Select data
SELECT * FROM users WHERE email = 'alice@example.com';
```

## Joins

```sql
-- Inner join
SELECT * FROM users u
INNER JOIN orders o ON u.id = o.user_id;

-- Left join
SELECT * FROM users u
LEFT JOIN orders o ON u.id = o.user_id;

-- Full outer join
SELECT * FROM users u
FULL OUTER JOIN orders o ON u.id = o.user_id;
```

## Aggregation

```sql
-- Count
SELECT COUNT(*) FROM users;

-- Group by
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id;

-- Having
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id
HAVING COUNT(*) > 5;
```

## Subqueries

```sql
-- IN subquery
SELECT * FROM users WHERE id IN (SELECT user_id FROM orders);

-- EXISTS subquery
SELECT * FROM users u
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

## Views

```sql
-- Create view
CREATE VIEW active_users AS
SELECT * FROM users WHERE status = 'active';

-- Create materialized view
CREATE MATERIALIZED VIEW user_stats AS
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id;

-- Refresh materialized view
REFRESH MATERIALIZED VIEW user_stats;
```

## Functions

```sql
-- Create function
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Call function
SELECT update_timestamp();
```

## Triggers

```sql
-- Create trigger
CREATE TRIGGER set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();
```

## Transactions

```sql
-- Begin transaction
BEGIN;

-- Commit
COMMIT;

-- Rollback
ROLLBACK;

-- Savepoint
SAVEPOINT my_savepoint;
ROLLBACK TO my_savepoint;
```

## Export/Import

```bash
# Export to CSV
psql -c "\COPY users TO 'users.csv' CSV HEADER"

# Import from CSV
psql -c "\COPY users FROM 'users.csv' CSV HEADER"

# Dump database
pg_dump -U postgres -d mydb -f backup.sql

# Restore database
psql -d mydb -f backup.sql
```

## psql Commands

```sql
-- List tables
\dt

-- Describe table
\d tablename

-- List databases
\l

-- Connect to database
\c dbname

-- Execute SQL from file
\i filename.sql

-- Quit
\q
```

## Useful Queries

```sql
-- Database size
SELECT pg_size_pretty(pg_database_size('mydb'));

-- Table size
SELECT pg_size_pretty(pg_total_relation_size('users'));

-- Active connections
SELECT count(*) FROM pg_stat_activity;

-- Running queries
SELECT pid, query, state FROM pg_stat_activity;
```

## Best Practices

1. Use EXPLAIN ANALYZE for query optimization
2. Use connection pooling in production
3. Enable SSL for remote connections
4. Use transactions for data integrity
5. Regular backups and testing
