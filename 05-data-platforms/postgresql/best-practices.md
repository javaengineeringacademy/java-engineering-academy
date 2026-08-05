# PostgreSQL Best Practices

## Schema Design

### Use Appropriate Data Types

```sql
-- Use INTEGER for IDs
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Use TEXT for variable length strings
-- Use VARCHAR only when needed
```

### Normalize Appropriately

```sql
-- 3NF for most cases
-- Denormalize for performance when needed
-- Use materialized views for complex queries
```

## Indexing

### Index Strategy

```sql
-- Index foreign keys
CREATE INDEX idx_orders_customer ON orders(customer_id);

-- Index frequently queried columns
CREATE INDEX idx_users_email ON users(email);

-- Use composite indexes
CREATE INDEX idx_orders_customer_date ON orders(customer_id, created_at);
```

### Avoid Over-Indexing

```sql
-- Check unused indexes
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0;
```

## Query Writing

### Use EXPLAIN ANALYZE

```sql
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'alice@example.com';
```

### Avoid SELECT *

```sql
-- Bad
SELECT * FROM users;

-- Good
SELECT id, name, email FROM users;
```

### Use EXISTS vs IN

```sql
-- EXISTS for large datasets
SELECT * FROM users u
WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id);
```

## Transactions

### Keep Transactions Short

```sql
-- Bad
BEGIN;
-- Long running operations
COMMIT;

-- Good
BEGIN;
-- Quick operations
COMMIT;
```

### Use Appropriate Isolation Level

```sql
-- Read Committed (default)
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Serializable when needed
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```

## Security

### Use Role-Based Access

```sql
-- Create roles
CREATE ROLE readonly;
CREATE ROLE readwrite;

-- Grant privileges
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES TO readwrite;
```

### Enable Row-Level Security

```sql
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;

CREATE POLICY user_orders ON orders
  FOR ALL
  TO app_user
  USING (user_id = current_setting('app.user_id')::int);
```

## Performance

### Connection Pooling

```ini
# PgBouncer configuration
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 25
```

### Tune Memory Settings

```
shared_buffers = 25% of RAM
effective_cache_size = 75% of RAM
work_mem = RAM / max_connections
```

## Monitoring

### Enable pg_stat_statements

```sql
CREATE EXTENSION pg_stat_statements;
```

### Monitor Slow Queries

```sql
SELECT query, calls, mean_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

## Backup

### Regular Backups

```bash
# Daily backup
pg_dump -U postgres -d mydb -F c -f backup_$(date +%Y%m%d).dump
```

### Test Restores

```bash
# Test restore procedure
pg_restore -D /test/restore backup.dump
```

## Maintenance

### Regular Vacuum

```sql
VACUUM VERBOSE users;
ANALYZE users;
```

### Monitor Disk Usage

```sql
SELECT pg_size_pretty(pg_database_size('mydb'));
```

## Best Practices Summary

1. Use appropriate data types
2. Index strategically
3. Write efficient queries
4. Use connection pooling
5. Monitor performance
6. Test backups regularly
7. Enable security features
8. Keep transactions short
9. Use EXPLAIN ANALYZE
10. Monitor disk usage
11. Regular maintenance
12. Document procedures
13. Plan for growth
14. Use version control
15. Review logs regularly
