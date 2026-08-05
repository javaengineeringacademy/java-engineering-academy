# PostgreSQL Monitoring

## pg_stat_statements

### Installation

```sql
-- Enable extension
CREATE EXTENSION pg_stat_statements;

-- In postgresql.conf
shared_preload_libraries = 'pg_stat_statements'
pg_stat_statements.max = 10000
pg_stat_statements.track = all
```

### Useful Queries

```sql
-- Top 10 slowest queries
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;

-- Most frequent queries
SELECT query, calls, rows
FROM pg_stat_statements
ORDER BY calls DESC
LIMIT 10;
```

## pg_stat Views

### Database Statistics

```sql
-- Database activity
SELECT datname, numbackends, xact_commit, xact_rollback,
       blks_read, blks_hit
FROM pg_stat_database;

-- Table statistics
SELECT schemaname, relname, seq_scan, seq_tup_read,
       idx_scan, idx_tup_fetch
FROM pg_stat_user_tables;
```

### Index Statistics

```sql
-- Index usage
SELECT schemaname, tablename, indexname, idx_scan, idx_tup_read
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- Unused indexes
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0;
```

## pgBadger

### Installation

```bash
# Install pgBadger
brew install pgbadger

# Or via apt
sudo apt install pgbadger
```

### Generate Report

```bash
# Parse log file
pgbadger /var/log/postgresql/postgresql.log

# With HTML output
pgbadger -o report.html /var/log/postgresql/postgresql.log
```

### Log Configuration

```
# In postgresql.conf
log_statement = 'all'
log_duration = on
log_line_prefix = '%t [%p-%l] %q%u@%d '
```

## Prometheus Exporter

### Installation

```bash
# Install postgres_exporter
brew install postgres_exporter

# Run exporter
POSTGRES_EXPORTER_DATA_SOURCE_NAME="postgresql://user:pass@localhost:5432/db?sslmode=disable" \
postgres_exporter
```

### Docker

```yaml
version: '3.8'
services:
  postgres-exporter:
    image: prometheuscommunity/postgres-exporter
    environment:
      DATA_SOURCE_NAME: "postgresql://user:pass@postgres:5432/db?sslmode=disable"
    ports:
      - "9187:9187"
```

### Prometheus Config

```yaml
scrape_configs:
  - job_name: 'postgresql'
    static_configs:
      - targets: ['localhost:9187']
```

## Grafana Dashboards

### PostgreSQL Dashboard

```json
{
  "panels": [
    {
      "title": "Connections",
      "targets": [{
        "expr": "pg_stat_activity_count"
      }]
    }
  ]
}
```

## pg_stat_activity

### Active Queries

```sql
-- Current activity
SELECT pid, usename, datname, state, query, query_start
FROM pg_stat_activity
WHERE state = 'active';

-- Long running queries
SELECT pid, now() - pg_stat_activity.query_start AS duration, query
FROM pg_stat_activity
WHERE (now() - pg_stat_activity.query_start) > interval '5 minutes';
```

### Blocking Queries

```sql
-- Find blocking queries
SELECT blocked_locks.pid AS blocked_pid,
       blocking_locks.pid AS blocking_pid,
       blocked_activity.usename AS blocked_user,
       blocking_activity.usename AS blocking_user
FROM pg_catalog.pg_locks blocked_locks
JOIN pg_catalog.pg_stat_activity blocked_activity
  ON blocked_activity.pid = blocked_locks.pid
JOIN pg_catalog.pg_locks blocking_locks
  ON blocking_locks.locktype = blocked_locks.locktype
  AND blocking_locks.relation = blocked_locks.relation
  AND blocking_locks.pid != blocked_locks.pid
JOIN pg_catalog.pg_stat_activity blocking_activity
  ON blocking_activity.pid = blocking_locks.pid
WHERE NOT blocked_locks.granted;
```

## Disk Usage

```sql
-- Database size
SELECT pg_size_pretty(pg_database_size('mydb'));

-- Table size
SELECT pg_size_pretty(pg_total_relation_size('users'));

-- Index size
SELECT pg_size_pretty(pg_indexes_size('users'));
```

## Best Practices

1. Enable pg_stat_statements
2. Set up Prometheus and Grafana
3. Monitor disk usage
4. Alert on long-running queries
5. Review pgBadger reports regularly
