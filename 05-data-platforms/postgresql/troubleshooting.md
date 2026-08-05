# PostgreSQL Troubleshooting

## Connection Issues

### Connection Refused

```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Check port
netstat -tlnp | grep 5432

# Check pg_hba.conf
cat /etc/postgresql/*/main/pg_hba.conf
```

### Authentication Failed

```bash
# Check pg_hba.conf
grep -v "^#" /etc/postgresql/*/main/pg_hba.conf | grep -v "^$"

# Test connection
psql -h localhost -U myuser -d mydb
```

## Performance Issues

### Slow Queries

```sql
-- Find slow queries
SELECT pid, now() - query_start AS duration, query
FROM pg_stat_activity
WHERE state = 'active'
AND now() - query_start > interval '5 minutes';

-- Check query plan
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'alice@example.com';
```

### Table Bloat

```sql
-- Check table bloat
SELECT schemaname, relname, n_live_tup, n_dead_tup,
       round(n_dead_tup / nullif(n_live_tup, 0) * 100, 2) as dead_pct
FROM pg_stat_user_tables
WHERE n_dead_tup > 1000;
```

### Index Usage

```sql
-- Unused indexes
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0;
```

## Replication Issues

### Replication Lag

```sql
-- Check replication status
SELECT client_addr, state, sent_lsn, replay_lsn,
       replay_lag
FROM pg_stat_replication;
```

### WAL Generation

```sql
-- Check WAL size
SELECT pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), '0/0'));
```

## Disk Issues

### Disk Space

```bash
# Check disk usage
df -h

# Check database size
psql -c "SELECT pg_size_pretty(pg_database_size('mydb'));"
```

### Table Size

```sql
-- Top 10 largest tables
SELECT schemaname, relname,
       pg_size_pretty(pg_total_relation_size(relid)) as size
FROM pg_catalog.pg_statio_user_tables
ORDER BY pg_total_relation_size(relid) DESC
LIMIT 10;
```

## Lock Issues

### Blocking Queries

```sql
-- Find blocking queries
SELECT blocked_locks.pid AS blocked_pid,
       blocking_locks.pid AS blocking_pid,
       blocked_activity.usename AS blocked_user,
       blocking_activity.usename AS blocking_user,
       blocked_activity.query AS blocked_query,
       blocking_activity.query AS blocking_query
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

### Deadlocks

```sql
-- Check for deadlocks in logs
grep -i "deadlock" /var/log/postgresql/*.log
```

## Memory Issues

### Out of Memory

```bash
# Check memory usage
free -h

# Check PostgreSQL memory settings
psql -c "SHOW shared_buffers;"
psql -c "SHOW work_mem;"
```

### Connection Limits

```sql
-- Check connection count
SELECT count(*) FROM pg_stat_activity;

-- Check max connections
SHOW max_connections;
```

## Backup Issues

### Backup Failures

```bash
# Check backup logs
cat /var/log/pgbackup/*.log

# Test backup
pg_dump -U postgres -d mydb -f test_backup.sql
```

### Restore Failures

```bash
# Check restore logs
cat /var/log/pgrestore/*.log

# Test restore
psql -d mydb_test -f test_backup.sql
```

## Logging Issues

### Enable Debug Logging

```sql
-- In postgresql.conf
log_statement = 'all'
log_duration = on
log_min_duration_statement = 0

-- Reload
SELECT pg_reload_conf();
```

### Check Logs

```bash
# Tail logs
tail -f /var/log/postgresql/postgresql.log

# Search for errors
grep -i "error" /var/log/postgresql/*.log
```

## Best Practices

1. Check logs first
2. Monitor disk space
3. Test backups regularly
4. Use connection pooling
5. Monitor replication lag
