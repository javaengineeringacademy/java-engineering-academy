# PostgreSQL Scaling

## Read Replicas

### Setup Streaming Replication

```bash
# Primary configuration
CREATE USER replicator WITH REPLICATION PASSWORD 'password';

# In postgresql.conf
wal_level = replica
max_wal_senders = 5
wal_keep_size = 1GB

# Create replica
pg_basebackup -h primary -U replicator -D /replica/data -P
```

### Load Balancing

```bash
# PgBouncer for read replicas
[databases]
mydb_write = host=primary port=5432 dbname=mydb
mydb_read = host=replica1,replica2 port=5432 dbname=mydb

[pgbouncer]
pool_mode = transaction
```

## Partitioning

### Range Partitioning

```sql
-- Create partitioned table
CREATE TABLE orders (
    id SERIAL,
    created_at TIMESTAMP,
    amount DECIMAL
) PARTITION BY RANGE (created_at);

-- Create partitions
CREATE TABLE orders_2024 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE orders_2025 PARTITION OF orders
FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
```

### Hash Partitioning

```sql
-- Hash partitioning
CREATE TABLE users (
    id SERIAL,
    email VARCHAR(255)
) PARTITION BY HASH (id);

-- Create partitions
CREATE TABLE users_p0 PARTITION OF users
FOR VALUES WITH (MODULUS 4, REMAINDER 0);

CREATE TABLE users_p1 PARTITION OF users
FOR VALUES WITH (MODULUS 4, REMAINDER 1);
```

### List Partitioning

```sql
-- List partitioning
CREATE TABLE orders (
    id SERIAL,
    region VARCHAR(20)
) PARTITION BY LIST (region);

-- Create partitions
CREATE TABLE orders_us PARTITION OF orders
FOR VALUES IN ('US');

CREATE TABLE orders_eu PARTITION OF orders
FOR VALUES IN ('EU');
```

## Connection Pooling

### PgBouncer Configuration

```ini
[databases]
mydb = host=localhost port=5432 dbname=mydb

[pgbouncer]
listen_port = 6432
listen_addr = 0.0.0.0
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 25
```

### Connection Pool Sizing

```
total_connections = max_connections
pool_size = total_connections / num_servers
```

## Vertical Scaling

### Memory Tuning

```
shared_buffers = 25% of RAM
effective_cache_size = 75% of RAM
work_mem = RAM / max_connections
maintenance_work_mem = RAM / 16
```

### CPU Tuning

```
max_worker_processes = CPU cores
max_parallel_workers = CPU cores
max_parallel_workers_per_gather = CPU cores / 2
```

## Horizontal Sharding

### Citus

```sql
-- Install extension
CREATE EXTENSION citus;

-- Add worker node
SELECT * FROM master_add_node('worker1', 5432);

-- Create distributed table
SELECT create_distributed_table('orders', 'customer_id');
```

### Manual Sharding

```sql
-- Shard by customer_id
CREATE TABLE orders_shard_0 AS
SELECT * FROM orders WHERE customer_id % 4 = 0;

CREATE TABLE orders_shard_1 AS
SELECT * FROM orders WHERE customer_id % 4 = 1;
```

## Query Optimization

### Materialized Views

```sql
-- Create materialized view
CREATE MATERIALIZED VIEW user_stats AS
SELECT user_id, COUNT(*) as order_count
FROM orders
GROUP BY user_id;

-- Refresh periodically
REFRESH MATERIALIZED VIEW CONCURRENTLY user_stats;
```

### Indexing Strategy

```sql
-- Composite index
CREATE INDEX idx_orders_customer_date ON orders(customer_id, created_at);

-- Partial index
CREATE INDEX idx_active_users ON users(email) WHERE status = 'active';
```

## Monitoring

### Replication Lag

```sql
-- Check replication status
SELECT client_addr, state, sent_lsn, replay_lsn,
       replay_lag
FROM pg_stat_replication;
```

### Connection Monitoring

```sql
-- Active connections
SELECT count(*), state
FROM pg_stat_activity
GROUP BY state;
```

## Best Practices

1. Use partitioning for large tables
2. Implement read replicas for read scaling
3. Use connection pooling
4. Monitor replication lag
5. Plan for growth
